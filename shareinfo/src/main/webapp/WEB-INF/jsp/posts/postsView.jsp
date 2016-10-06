<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>
<jsp:include page="../fragments/headTag.jsp" />
<style>
div.mouseScrollOnPosts {
	max-height: 500px;
	overflow-y: hidden;
}

div.mouseScrollOnPosts:hover {
	overflow-y: auto;

}
</style>
<body>
	<sec:authentication var="loggedInUseId" property="principal.id" />
	<spring:url value="/users/${userId}/edit" htmlEscape="true" var="editUserUrl" />
	<spring:url value="/users/${userId}/posts/new" htmlEscape="true" var="newPostUrl" />
	<spring:url value="/users/${userId}/posts/delete" htmlEscape="true" var="deletePostUrl" />
	<spring:url value="/users/${userId}/posts/newComment" htmlEscape="true" var="newCommentUrl" />
	<spring:url value="/users/${userId}/blogs" htmlEscape="true" var="userBlogUrl" />

	<script>
		$(function() {
	
		//	var userStatusModalBody = $("#user-status-form").html();
			
			
/* 			$("#update-status-button").click(function(e) {
				
				
				
				
			}); */

		    $('#statusModal').on('shown', function () {
		        console.log("show modal event triggered.");
		        $("#user-status-input").val("");
		        
		      	$("#file-control-group").removeClass("error");
				$("#file").val("").parent("div").children(".help-inline").text("max size: 1 MB");
		        
		        
		        
		        })
					//validate max-length of input
			  $('textarea.max-length , input.max-length').maxlength({
			      alwaysShow: true,
			      placement: 'bottom-right'
			     
			  });
			
			
			
			$(".modal").draggable({
			    handle: ".modal-header"
			}); 
			var editUserUrl = "<c:out value='${editUserUrl}'/>";
			var newPostUrl = "<c:out value='${newPostUrl}'/>";
			$("#save-status-button").click(function(e) {
	
				$(e.currentTarget).prop('disabled', true);
				
				var file = $("#file").get(0);
				if (validateFile(file)) {
					$("#user-status-form").submit();
				} else {
					$(this).prop("disabled", false);
				}
				
				/* validation not required.
				var userStatusInput = $("#user-status-input").val().length;
				if(userStatusInput > 0){
					$("#user-status-form").submit();
				} else{
					$("#user-status-message").addClass("text-error").text(" Cannot be empty.").show().fadeOut(6000);
					$(e.currentTarget).prop('disabled', false);
				} */
				
				
				
			});
			
			
			$("#file").change(function(event) {
				validateFile(this);
				var ele = document.getElementById("file");
				ele.setAttribute("title",ele.value);
				});
			
			

		$("#new-post-button").click(function(e) {
				console.log("new post clicked");
				$(e.currentTarget).prop('disabled', true);
				
				var newPostLength =  $("#new-post-input").val().length ;
				if(newPostLength === 0){
					$("#new-post-message").addClass("text-error").text(" Cannot be empty.").show().fadeOut(6000);
					$(e.currentTarget).prop('disabled', false);
					return;
					
				}
				
				$.ajax({
					url : newPostUrl,
					type : "POST",
					cache : false,
					data : $("#new-post-input").val(),
					contentType : "application/text",
					dataType : "text",
					success : function(data, status) {
						console.log("Data received " + data);
						window.location.href = "posts";
						//$('#user-status ').text(data);
					},
					error : function(e) {
						console.log("Error: " + e);
						alert("Please check session and try again.");
					},
					complete : function() {
						console.log("Completed request");
						$('#postModal').modal('hide');
						$('#new-post-button').prop('disabled', false);

					}
				});

			});
		

		
		
		
		
		

		
		
		
		
		

		});

		
		function deletePost(postId)
		{
			var deletePostUrl = "<c:out value='${deletePostUrl}'/>";
			var deletePostId = postId;
			console.log("delete post requested for id:"+postId + "url : " + deletePostUrl);
			if (confirm("Please confirm to delete.")){
			$.ajax({
				type : "POST",
				cache : false,
				url : deletePostUrl,
				data : JSON.stringify(deletePostId),
				contentType : "application/json",
				dataType : "text",
				success : function(data, status) {
					console.log("Success: Data received " + data);
					/* hide the deleted instead of reloading */
					window.location.href = "posts";
					
				},
				error : function(e) {
					console.log("Error: " + e);
					alert("Unauthorised ! Please check session and try again.");
				}
			
			});
			}	
		}
		
		function newComment(postId)
		{
			var newCommentUrl = "<c:out value='${newCommentUrl}'/>";
			var userComment = $("#post-"+ postId +"-comment-input").val();
			$("#post-container-"+postId+"").append('<div id = "loading-container-'+postId+'"> <img src="/sdm-admin/resources/css/images/loading-icon.gif"/> <p>Posting your Comment. . .</p> </div>');
			
			if(userComment.length === 0){
				$("#new-post-"+postId+"-comment-message").addClass("text-error").text(" Cannot be empty.").show().fadeOut(9000);
				
				return;
				
			}
			
			
			var obj = {postId : postId, comment : userComment };
			console.log("add comment for :"+ obj);
		
			$.ajax({
				type : "POST",
				cache : false,
				url : newCommentUrl,
				data : JSON.stringify(obj),
				contentType : "application/json",
				dataType : "json",
				success : function(postComment, status) {
					var results = '<div class="media">';
					results += '<div class=" pull-left media-object" style="width: 30px; height: 50px; overflow-y: hidden;">';
				
					var image = '/shareinfo/resources/css/images/user-icon.gif';
					if(postComment.user.imageSize != null){
						image = '/shareinfo/download/users/images/'+ postComment.user.id ; 
						
					}
					results += '<img style = "width: 30px;" src="'+image+'" alt="'+postComment.user.firstName+'"> </div>';
					results += '<div class="media-body">';
					results += 	'<h5 class="media-heading"> '+postComment.user.firstName+' <small class="inline-block ">'+ new Date(postComment.creationTime).toLocaleString()+'</small></h5>';
					results += '<span class="text-error"> '+postComment.comment+'</span>	</div></div>';

					
					$("#loading-container-"+postComment.post.id+"").remove();
					$("#post-container-"+postComment.post.id+"").append(results);
					$("#post-"+ postComment.post.id +"-comment-input").val("");
					
					
					//window.location.href = "posts";
					
				},
				error : function(e) {
										
					console.log("Error: " + e);
					alert("Unauthorised ! Please check session and try again.");
				}
			
			});
			
					
		}
		
		
		
		
		
		
		function validateFile(file) {
			if ((file.value == null) || (file.value == "")) {
			
				return true;
			}
	
			var extension = file.value.split('.').pop().toUpperCase();
	     	
	        if (extension!="PNG" && extension!="JPG" && extension!="GIF" && extension!="JPEG"){
	        	$("#file-control-group").addClass("error");
				$(file).parent("div").children(".help-inline").text("Accepted Extensions, png | jpg | gif | jpeg");
				return false;
	        }
			
			
			
			
			if (typeof file.files === "undefined") {
			     var myFSO = new ActiveXObject("Scripting.FileSystemObject");
			     var filepath = file.value;
			     var thefile = myFSO.getFile(filepath);
			     var size = thefile.size;
			     console.log("file size : "+ size);
					if (size/1024 > (1024)) {
						$("#file-control-group").addClass("error");
						$(file).parent("div").children(".help-inline").text("max size: 1 MB");
						return false;
					}else {
						$("#file-control-group").removeClass("error");
						$(file).parent("div").children(".help-inline").text("");
						return true;
					}
			}

			if (file.files[0].size/1024 > (1024)) {
				$("#file-control-group").addClass("error");
				$(file).parent("div").children(".help-inline").text("max size: 1 MB");
				
				return false;
			}else {
				$("#file-control-group").removeClass("error");
				$(file).parent("div").children(".help-inline").text("");
				return true;
			}
			
		
		}
		
	</script>

	<jsp:include page="../fragments/bodyHeader.jsp" />
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="well well-small clearfix">
					<div class="media">
						<div class="pull-left">
						<spring:url value="/download/users/images/${postUser.id}" htmlEscape="true" var="imageUrl" />
							<img style="width: 100px;" src="${!empty postUser.imageSize ?  imageUrl : '/shareinfo/resources/css/images/user-icon.gif'}" alt="user Name">

							<h3 class="text-center">
								<c:out value="${postUser.firstName}" />
							</h3>
						</div>
						<div class="media-body">
							<h2 class="media-heading">
								Welcome <small class="inline-block "><sec:authentication property="principal.firstName" /> </small>
							</h2>
							<span id="user-status">${postUser.statusMessage} </span>
						</div>
					</div>
					<div class="pull-right">
						<c:if test="${userId == loggedInUseId}">
							<a id="update-status-button" class="btn btn-info" data-target="#statusModal" data-toggle="modal"><c:out value="Update Status" /></a>
							<a class="btn btn-primary" href="#postModal" data-toggle="modal"> <i class="icon-pencil"></i> <c:out value="New Post" /></a>
						</c:if>
						<a class="btn btn-warning " href="${userBlogUrl}">My Blog »</a>
					</div>
				</div>
			</div>
		</div>
		<!-- End of User details container -->

		<div class="row-fluid">
			<!-- style="height: 80%; overflow-y: auto;" -->
			<div class="span12">
				<h2>My Posts</h2>

				<div class="row-fluid">

					<c:if test="${empty posts1}">
						<div class=" alert ">
							<h3 class="text-center">No Posts found.</h3>
						</div>

					</c:if>
					<div class="span4">
						<c:if test="${!empty posts1}">
							<ul class="unstyled">
								<c:forEach items="${posts1}" var="post">
									<li>
									
									
										<div class="well mouseScrollOnPosts">
										
										<div  id = "post-container-${post.id}">
									
								
											<div class="media">
												<div class=" pull-left media-object" style="width: 30pxpx; height: 50px; overflow-y: hidden;">
													<spring:url value="/download/users/images/${post.user.id}" htmlEscape="true" var="imageUrl" />
													<img style="width: 30px" src="${!empty post.user.imageSize ?  imageUrl : '/shareinfo/resources/css/images/user-icon.gif'}"
														alt="User Images">
												</div>
												<div class="media-body">
													<h5 class="media-heading">
														<c:out value="${post.user.firstName}"></c:out>
														<small class="inline-block "><joda:format value="${post.creationTime}" style="SM" /></small>
														<button onclick="deletePost(${post.id})" class="close pull-right">&times;</button>

													</h5>
													<span class="text-error"> <c:out value="${post.content}"></c:out>
													</span>
												</div>
											</div>

											<c:forEach items="${post.postComments}" var="postComment">
												<div class="media">
													<div class=" pull-left media-object" style="width: 30px; height: 50px; overflow-y: hidden;">
														<spring:url value="/download/users/images/${postComment.user.id}" htmlEscape="true" var="imageUrl" />
														<img width=30px src="${!empty postComment.user.imageSize ?  imageUrl : '/shareinfo/resources/css/images/user-icon.gif'}"
															alt="User Images">
													</div>
													<div class="media-body">
														<h5 class="media-heading">
															<c:out value="${postComment.user.firstName}"></c:out>
															<small class="inline-block "><joda:format value="${postComment.creationTime}" style="SM" /></small>
														</h5>
														<span class="text-error"> <c:out value="${postComment.comment}"></c:out>
														</span>
													</div>
												</div>



											</c:forEach>
											</div>
											<div class="media">
												<div class=" pull-left media-object" style="width: 30px; height: 50px; overflow-y: hidden;">
												<spring:url value="/download/users/images/${user.id}" htmlEscape="true" var="imageUrl" />
													<img width=30px src="${!empty user.imageSize ?  imageUrl : '/shareinfo/resources/css/images/user-icon.gif'}" alt="User Images">
												</div>
												<div class="media-body">
													<h5 class="media-heading">
														<sec:authentication property="principal.firstName" />
													</h5>

													<textarea class="input-xlarge max-length" id="post-${post.id}-comment-input" maxlength="512"></textarea>
													<button onclick="newComment(${post.id})" class="inline btn btn-info pull-right">
														<i class="icon-pencil"></i> Write
													</button>
												</div>
												<span id="new-post-${post.id}-comment-message"></span>
											</div>
										</div>
									</li>
								</c:forEach>
							</ul>
						</c:if>
					</div>
					<div class="span4">
						<c:if test="${!empty posts2}">
							<ul class="unstyled">
								<c:forEach items="${posts2}" var="post">
									<li>
										<div class="well mouseScrollOnPosts">
										<div  id = "post-container-${post.id}">
											<div class="media">
												<div class=" pull-left media-object" style="width: 30px; height: 50px; overflow-y: hidden;">
												<spring:url value="/download/users/images/${post.user.id}" htmlEscape="true" var="imageUrl" />
													<img width=30px src="${!empty post.user.imageSize ?  imageUrl : '/shareinfo/resources/css/images/user-icon.gif'}" alt="User Images">
												</div>
												<div class="media-body">
													<h5 class="media-heading">
														<c:out value="${post.user.firstName}"></c:out>
														<small class="inline-block "><joda:format value="${post.creationTime}" style="SM" /></small>
														<button onclick="deletePost(${post.id})" class="close pull-right">&times;</button>

													</h5>
													<span class="text-error"> <c:out value="${post.content}"></c:out>
													</span>
												</div>
											</div>

											<c:forEach items="${post.postComments}" var="postComment">
												<div class="media">
													<div class=" pull-left media-object" style="width: 30px; height: 50px; overflow-y: hidden;">
													<spring:url value="/download/users/images/${postComment.user.id}" htmlEscape="true" var="imageUrl" />
														<img width=30px src="${!empty postComment.user.imageSize ?  imageUrl : '/shareinfo/resources/css/images/user-icon.gif'}"
															alt="User Images">
													</div>
													<div class="media-body">
														<h5 class="media-heading">
															<c:out value="${postComment.user.firstName}"></c:out>
															<small class="inline-block "><joda:format value="${postComment.creationTime}" style="SM" /></small>
														</h5>
														<span class="text-error"> <c:out value="${postComment.comment}"></c:out>
														</span>
													</div>
												</div>
											</c:forEach>
											
											</div>
											<div class="media">
												<div class=" pull-left media-object" style="width: 30px; height: 50px; overflow-y: hidden;">
												<spring:url value="/download/users/images/${user.id}" htmlEscape="true" var="imageUrl" />
													<img width=30px src="${!empty user.imageSize ?  imageUrl : '/shareinfo/resources/css/images/user-icon.gif'}" alt="User Images">
												</div>
												<div class="media-body">
													<h5 class="media-heading">
														<sec:authentication property="principal.firstName" />
													</h5>

													<textarea class="input-xlarge max-length" id="post-${post.id}-comment-input" maxlength="512"></textarea>
													<button onclick="newComment(${post.id})" class="inline btn btn-info pull-right">
														<i class="icon-pencil"></i> Write
													</button>

												</div>
												<span id="new-post-${post.id}-comment-message"></span>
											</div>
										</div>
									</li>
								</c:forEach>
							</ul>
						</c:if>
					</div>
					<div class="span4">
						<c:if test="${!empty posts3}">
							<ul class="unstyled">
								<c:forEach items="${posts3}" var="post">
									<li>
										<div class="well mouseScrollOnPosts">
										<div  id = "post-container-${post.id}">
											<div class="media">
												<div class=" pull-left media-object" style="width: 30px; height: 50px; overflow-y: hidden;">
												<spring:url value="/download/users/images/${post.user.id}" htmlEscape="true" var="imageUrl" />
													<img width=30px src="${!empty post.user.imageSize ?  imageUrl : '/shareinfo/resources/css/images/user-icon.gif'}" alt="User Images">
												</div>
												<div class="media-body">
													<h5 class="media-heading">
														<c:out value="${post.user.firstName}"></c:out>
														<small class="inline-block "><joda:format value="${post.creationTime}" style="SM" /></small>
														<button onclick="deletePost(${post.id})" class="close pull-right">&times;</button>

													</h5>
													<span class="text-error"> <c:out value="${post.content}"></c:out>
													</span>
												</div>
											</div>

											<c:forEach items="${post.postComments}" var="postComment">
												<div class="media">
													<div class=" pull-left media-object" style="width: 30px; height: 50px; overflow-y: hidden;">
													<spring:url value="/download/users/images/${postComment.user.id}" htmlEscape="true" var="imageUrl" />
														<img width=30px src="${!empty postComment.user.imageSize ?  imageUrl : '/shareinfo/resources/css/images/user-icon.gif'}"
															alt="User Images">
													</div>
													<div class="media-body">
														<h5 class="media-heading">
															<c:out value="${postComment.user.firstName}"></c:out>
															<small class="inline-block "><joda:format value="${postComment.creationTime}" style="SM" /></small>
														</h5>
														<span class="text-error"> <c:out value="${postComment.comment}"></c:out>
														</span>
													</div>
												</div>



											</c:forEach>
											
											</div>
											<div class="media">
												<div class=" pull-left media-object" style="width: 30px; height: 50px; overflow-y: hidden;">
													<spring:url value="/download/users/images/${user.id}" htmlEscape="true" var="imageUrl" />
													<img width=30px src="${!empty user.imageSize ?  imageUrl : '/shareinfo/resources/css/images/user-icon.gif'}" alt="User Images">
												</div>
												<div class="media-body">
													<h5 class="media-heading">
														<sec:authentication property="principal.firstName" />
													</h5>
													<span id="new-post-${post.id}-comment-message"></span>
													<textarea class="input-xlarge max-length" id="post-${post.id}-comment-input" maxlength="512"></textarea>
													<button onclick="newComment(${post.id})" class="inline btn btn-info pull-right">
														<i class="icon-pencil"></i> Write
													</button>

												</div>

											</div>
										</div>
									</li>
								</c:forEach>
							</ul>
						</c:if>
					</div>
				</div>
			</div>
		</div>

		<div id="statusModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title">
					Hello
					<sec:authentication property="principal.firstName" />
					, Update your status.
				</h4>
			</div>
			<div class="modal-body">
				<form id="user-status-form" action="${editUserUrl}" method="post" enctype="multipart/form-data">
					<fieldset>
						<label for="status">Status</label>

						<textarea id="user-status-input" name="status" class="input-xlarge text ui-widget-content ui-corner-all max-length" maxlength="256"></textarea>
						<span class="inline" id="user-status-message"></span>

						<div class="control-group " id="file-control-group">
							<label class="control-label">File</label>
							<div class="controls">
								<input type="file" name="file" id="file" /> <span class="help-inline">  max size: 1 MB</span>

							</div>
						</div>
					</fieldset>
				</form>

			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
				<button id="save-status-button" type="button" class="btn btn-primary">Save changes</button>
			</div>
		</div>


		<%-- 
		<div id="imgModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title">
					Hello
					<sec:authentication property="principal.firstName" />
					, Update your Image
				</h4>
			</div>
			<div class="modal-body">
				<form id="user-img-form" action="${userImgUrl}" method="post"	enctype="multipart/form-data">
			<fieldset>
				<label for="file">File</label> <input type="file" name="file"
					id="file" class="text ui-widget-content ui-corner-all" />
			</fieldset>
		</form>
				
				
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
				<button id="update-image-button" type="button" class="btn btn-primary">Save changes</button>
			</div>
		</div> --%>

		<div id="postModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title">
					Hello
					<sec:authentication property="principal.firstName" />
					, type your new Post. . .
				</h4>
			</div>
			<div class="modal-body">
				<!-- <input type="text" name="status" class="input-xxlarge" placeholder="Continue typing . . . "> -->
				<textarea class="input-xlarge max-length" id="new-post-input" maxlength="512"></textarea>
				<span class="inline" id="new-post-message"></span>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
				<button id="new-post-button" type="button" class="btn btn-primary">Save changes</button>
			</div>
		</div>





	</div>
</body>
</html>
