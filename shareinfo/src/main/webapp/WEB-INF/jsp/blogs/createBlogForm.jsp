<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>
<jsp:include page="../fragments/headTag.jsp" />
<script src="<spring:url value="/webjars/tinymce-jquery/4.0.16/tinymce.min.js"/>"></script>
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
					$("#message").addClass("text-error").text(" Title Cannot be empty.  ").show().fadeOut(6000);
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
        	height : 400,
            plugins: [
                      "media, link, preview, code, table"
                ],
                toolbar: "preview, media, link, code",
     

        });
        
        
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

<body>
	<jsp:include page="../fragments/bodyHeader.jsp" />

	<div class="container-fluid">
		<h2>Create Blog</h2>
		<div class="row-fluid">
			<div class="span12">
				<div class="span9 offset1">
					<form:form modelAttribute="blog" method="post" class="form-horizontal" enctype="multipart/form-data">
						<div class="control-group">
							<label class="control-label"> Blog Title </label>
							<div class="controls">
								<form:input path="title" class="input-xxlarge" id="input-title"  maxlength="128" type="text" />
								<span id="message" class=""></span>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label"> Topic </label>
							<div class="controls">
								<form:select path="blogTopic.id" items="${blogTopics}" itemLabel="content" itemValue="id" />
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<form:textarea path="content" class="input-xxlarge" />
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<form:checkbox path="sendEmail"  id="input-send-email" checked="checked" /> 	
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
				</div>
			</div>
		</div>
	</div>
</body>
</html>