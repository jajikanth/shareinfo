<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>
<jsp:include page="../fragments/headTag.jsp" />

<!-- <script src="//tinymce.cachefly.net/4.1/tinymce.min.js"></script> -->
<script src="<spring:url value="/webjars/tinymce-jquery/4.0.16/tinymce.min.js"/>"></script>

<body>
	<jsp:include page="../fragments/bodyHeader.jsp" />
	<spring:url value="/users/${userId}/blogs/file/delete" htmlEscape="true" var="deleteBlogFileUrl" />
	
<script>
$(function () { 
	$.fn.placeholder();
	  $('input#input-title').maxlength({
	      alwaysShow: true,
	      placement: 'bottom-right'
	     
	  });
	  
		$("form").submit(
				function(event) {
					var blogTitleLength = $("#input-title").val().length;
					console.log("blogTitleLength" + blogTitleLength);
					var file = $("#file").get(0);
					if (blogTitleLength > 0  && validateFile(file)) {
						
						return;
					}
					$(window).scrollTop(0);
					$("#message").addClass("text-error").text(" Title Cannot be empty ").show().fadeOut(10000);
					event.preventDefault();
				});
	  
		$("#file").change(function(event) {
			validateFile(this);
			var ele = document.getElementById("file");
			ele.setAttribute("title",ele.value);
			});
		
	  });
	  
		tinymce.init({
			selector:'textarea',
			height : 500,
			plugins: [
                      "media, link, preview, code"
                ],
                toolbar: "preview, media, link, code",
             
		});
		
		function deleteFile(blogId , fileId) {
			var deleteBlogFileUrl = "<c:out value='${deleteBlogFileUrl}'/>";
			console.log("deleteBlogFileUrl: " + deleteBlogFileUrl);
			
			console.log("blogId:" + blogId);
			bootbox.confirm("Are you sure?",function(result) {
								if (result) {
									$.ajax({
												url : deleteBlogFileUrl + "/"+ blogId + "/"	+ fileId ,
												type : "DELETE",
												cache : false,
												dataType : "text",
												success : function(result,	status) {
												$("#file-" + fileId + "").slideUp("slow");
													console.log("Deleted File Id : "+ fileId);
												},
												error : function(e) {
													console.log("Error: " + e);
													alert("Please check session and try again.");
												}
											});
								}
							});
		}
		
		
		
		
		
		function validateFile(file) {
			if ((file.value == null) || (file.value == "")) {
			
				return true;
			}
	
	
			if (typeof file.files === "undefined") {
			     var myFSO = new ActiveXObject("Scripting.FileSystemObject");
			     var filepath = file.value;
			     var thefile = myFSO.getFile(filepath);
			     var size = thefile.size;
			     console.log("file size : "+ size);
					if (size > (1024 * 1024 * 1024)) {
						$("#file-control-group").addClass("error");
						$(file).parent("div").children(".help-inline").text("max size: 1 GB");
						return false;
					}else {
						$("#file-control-group").removeClass("error");
						$(file).parent("div").children(".help-inline").text("");
						return true;
					}
			}

			if (file.files[0].size > (1024 * 1024 * 1024)) {
				$("#file-control-group").addClass("error");
				$(file).parent("div").children(".help-inline").text("max size: 1 GB");
				
				return false;
			}else {
				$("#file-control-group").removeClass("error");
				$(file).parent("div").children(".help-inline").text("");
				return true;
			}
			
		
		}
		
		
</script>


	<div class="container-fluid">
		<h2>Edit Blog</h2>
		<div class="row-fluid">
			<div class="span12">
				<div class="span9 offset1">
					<form:form modelAttribute="blog" method="post" class="form-horizontal" enctype="multipart/form-data">
						<div class="control-group">
							<label class="control-label"> Blog Title </label>
							<div class="controls">
								<form:input path="title" class="input-xxlarge" id="input-title"  maxlength="128" type="text"  />
								<span id="message" class=""></span>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label"> Topic </label>
							<div class="controls">
								<form:input path="blogTopic.content" class="input-xxlarge" type="text" disabled="true" />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label"> Categoty </label>
							<div class="controls">
								<form:textarea path="content" class="input-xxlarge" />
							</div>
						</div>
							<div class="control-group">
							<div class="controls">
								<form:checkbox path="sendEmail"  id="input-send-email" checked="checked"/>
								<span class="help-inline">  Send Email to all users </span>
							</div>
						</div>
						<div class="control-group " id="file-control-group">
							<label class="control-label">File</label>
							<div class="controls">
								<form:input type="file" path="file" id="file" /> <span class="help-inline">  max size: 1 GB</span>

							</div>
						</div>
						
						
						
				

						<div class="control-group">
							<div class="controls">
								<button class="btn btn-primary" type="submit">Save</button>
								<spring:url value="/users/${user.id}/blogs" htmlEscape="true" var="userBlogUrl" />
								<a class="btn" href="${userBlogUrl}">Cancel</a>
							</div>
						</div>

					</form:form>
					
											<div class="offset4 control-group" id="file-control-group">
							<label class="control-label">Existing Files</label>
							<div class="controls">
								<c:choose>
						<c:when test="${!empty blogFiles}">
							<ul class="unstyled">
								<c:forEach items="${blogFiles}" var="blogFile">
									<li>
										<div class="alert alert-info span7" id = "file-${blogFile.id}">
											<button class="close pull-right" onclick="deleteFile(${blog.id},${blogFile.id})">&times;</button>
											<spring:url value="/download/blogFiles/${blogFile.id}" htmlEscape="true" var="downloadFileUrl" />
											<a href="${downloadFileUrl}">
											<c:out value="${blogFile.name}"></c:out></a>
										</div>
									</li>
								</c:forEach>
							</ul>

						</c:when>
						<c:otherwise>
							<div class="alert alert-danger span6">
								<button class="close pull-right">&times;</button>
								<c:out value="No Files Attached."></c:out>
							</div>
						</c:otherwise>
					</c:choose>

							</div>
						</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>