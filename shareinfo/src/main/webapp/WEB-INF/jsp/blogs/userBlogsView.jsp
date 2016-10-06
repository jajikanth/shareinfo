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
@media ( min-width : 992px) {
	.modal-lg {
		width: 1200px;
		left: 22%;
		margin-left: -140px;
		margin-top: -30px;
	}
	.modal-body {
		max-height: 60vh;
	}
}
</style>
<body>


	<sec:authentication var="loggedInUserId" property="principal.id" />
	<spring:url value="/users/${userId}/blogs/new" htmlEscape="true" var="newBlogUrl" />
	<spring:url value="/users/${userId}/blogs" htmlEscape="true" var="userBlogUrl" />

	<jsp:include page="../fragments/bodyHeader.jsp" />
	<script type="text/javascript">
	
 		$(document).ready(function() {
 			var allUsers = window.location.pathname.search("all");
 			if(allUsers != -1){
 				$( ".panel" ).removeClass( "panel-info" ).addClass("panel-success");
 			}
 		 	$("iframe").hide();
 		 	$("video").remove();
 				
		});
 
		var page = 1;
		var requestStatus = true;

			function element_in_scroll(elem) {
				var docViewTop = $(window).scrollTop();
				var docViewBottom = docViewTop + $(window).height();

				var elemTop = $(elem).offset().top;
				var elemBottom = elemTop + $(elem).height();
				return ((elemBottom <= docViewBottom) && (elemTop >= docViewTop));
			}
			var lastScrollTop = 0;
			$(document).scroll(function(e) {
			   var currentPosition = $(this).scrollTop();
				   if ((currentPosition > lastScrollTop) && element_in_scroll("ul#blogs-container li:last") && requestStatus ){
					   
					   loadContent();
				   } 
				   lastScrollTop = currentPosition;
				
		//		if ( element_in_scroll("#load ul li:last")) {
		//			 	 $( "#load ul" ).append( "<li> <div class='panel panel-info'><div class='panel-heading'><h3 class='panel-title'>Panel title</h3></div>	<div class='panel-body'>Panel content</div>	<div class='panel-footer'>footer</div>	</div></li>" );
				 
		 //		 if(requestStatus){
					// timeout = setTimeout(function() {
		//				 loadContent();
					//}, 5000);
		//			 }
		//		}
				
			});
			

			function loadContent() {
				var userBlogUrl = "<c:out value='${userBlogUrl}'/>";
				var topicId = "<c:out value='${topicId}'/>";
				var topic  = (topicId.length > 0 ? topicId : 0);
				var allUsers = window.location.pathname.search("all");
				var authenticatedUserId = "<c:out value='${loggedInUserId}'/>";
				console.log("request status: " + requestStatus);
				console.log("page number: " + page);
				console.log("Topic id: " + topic);
				console.log("Auth User id: " + authenticatedUserId);
				if (requestStatus) {
					requestStatus = false;
					$.ajax({
						url : userBlogUrl +"/load/" + page,
						type : "GET",
						cache : false,
						data : {topicId: topic, allUsers: allUsers },
						dataType : "json",
						async : false,
						success : function(blogs, status) {
							page++;
							console.log(" incremented page number: " + page);
							console.log("request status: " + requestStatus);
							blogs.forEach(function(blog) {
								console.log(" data: " + blog.title);
							
										var result = "<li id = 'blog-"+blog.id+"'> <div class='panel panel-info'><div class='panel-heading'><h3 class='panel-title'>"
											result += blog.title
											result += "</h3></div>	<div class='panel-body'>"
											result += blog.content
											result += "</div>	<div class='panel-footer clearfix'><ol class='unstyled inline'> "
											result += '<li><i class="icon-user"></i>  	<span class="label label-warning">'+blog.user.firstName+'</span>	</li>';
											result += '<li><i class="icon-time"></i>'+ new Date(blog.creationTime).toLocaleString()+'</li>';
											result += '<li><i class="icon-tag"></i> <span class="label label-info">'+ blog.blogTopic.content+'</span></li>';
											result += '<li><i class="icon-comment"></i> Comments <span id= "comment-count-'+blog.id+'" class="badge badge-info">'+blog.commentCount+'</span></li>';
											result += '<li> <i class="icon-film"></i> <span id="video-'+ blog.id +'" class="label '+ (blog.containsVideo ? "label-info" : "muted" )+'"> Videos  </span></li>';
											result += '<li> <i class="icon-file"></i> <span id="file-view-'+ blog.id +'" class="label '+ (blog.externalDocument ? "label-info" : "muted" )+'"> Files  </span></li>';
											if (blog.user.id == authenticatedUserId) {
												result += '<li><a class = "btn btn-mini" href="javascript:deleteBlog('+blog.id+')"><i class="icon-trash"></i> Delete</a></li>';
												var editUrl = '/shareinfo/users/'+blog.user.id+'/blogs/'+blog.id+'/edit';
												result += '<li><a class = "btn btn-mini" href="'+editUrl+'"> <i class="icon-edit"></i> Edit</a></li>';
											}
											result += '<li class = "pull-right" ><a  class = "btn btn-mini btn-success" href="javascript:void(0)" onclick="loadModal('+blog.id+')" > <i class="icon-fullscreen icon-white"></i> Read More</a></li>';
											result += "</ol></div>	</div></li>"
											$("ul#blogs-container").append(result);
										});
							
							var allUsers = window.location.pathname.search("all");
				 			if(allUsers != -1){
				 				$( ".panel" ).removeClass( "panel-info" ).addClass("panel-success");
				 			}
							},
							error : function(e) {
								console.log("Error: " + e);
								alert(" Session Expired");

							},
							complete : function() {
								console.log("Completed request");
								requestStatus = true;
								$("iframe").hide();
								$("#modal-body-content").find("iframe").show();
							}
						});
			}
			//clearTimeout(timeout);
		}
			

		function deleteBlog(blogId) {
			var userBlogUrl = "<c:out value='${userBlogUrl}'/>";
			//var blog = blogId;
			bootbox.confirm("Are you sure?",function(result) {
								if (result) {
									$.ajax({
												url : userBlogUrl + "/"	+ blogId + "/delete",
												type : "DELETE",
												cache : false,
												contentType : "application/text",
												dataType : "text",
												success : function(result,	status) {
													console.log("Data : "+ result);
													$("#blog-" + blogId + "").slideUp("slow");
													console.log("Blog Id : "+ blogId);
												},
												error : function(e) {
													console.log("Error: " + e);
													alert("Please check session and try again.");
												}
											});
								}
							});
		}
		

	</script>
	<div class="container-fluid">
		<div class="row-fluid">
			<div id="ajaxData" class="span9" style="height: 2000px">
				<c:choose>
					<c:when test="${empty blogs}">
						<div class=" alert ">
							<h3 class="text-center">No Blogs found.</h3>
						</div>
					</c:when>
					<c:otherwise>
						<ul id="blogs-container" class="unstyled">
							<c:forEach items="${blogs}" var="blog">
								<li id="blog-${blog.id}">
									<div class="panel panel-info">
										<div class="panel-heading">
											<h3 class="panel-title">
												<c:out value="${blog.title}"></c:out>
											</h3>
										</div>
										<div class="panel-body">
											${blog.content}

										</div>
										<div class="panel-footer clearfix">
											<ol class="unstyled inline">
												<li><i class="icon-user"></i> <span class="label label-warning"><c:out value="${blog.user.firstName}"></c:out></span></li>
												<li><i class="icon-time"></i> <joda:format value="${blog.creationTime}" style="SM" /></li>
												<li><i class="icon-tag"></i> <span class="label label-info"><c:out value="${blog.blogTopic.content}"></c:out></span></li>
												<li><i class="icon-comment"></i> Comments <span id="comment-count-${blog.id}" class="badge badge-info"> <c:out value="${blog.commentCount}"></c:out></span></li>
												<li><i class="icon-film"></i> <span id="video-${blog.id}" class="label ${blog.containsVideo ? 'label-info' : 'muted' }"> Videos </span></li>
												<li><i class="icon-file"></i> <span id="file-view-${blog.id}" class="label ${blog.externalDocument ? 'label-info' : 'muted' }"> Files </span></li>
												<c:if test="${blog.user.id == loggedInUserId}">
													<li><a class="btn btn-mini" href="javascript:deleteBlog(${blog.id})"><i class="icon-trash"></i> Delete</a></li>
													<li><spring:url value="/users/${blog.user.id}/blogs/${blog.id}/edit" htmlEscape="true" var="editUrl" /> <a class="btn btn-mini" href="${editUrl}">
															<i class="icon-edit"></i> Edit
													</a></li>
												</c:if>
												<li class="pull-right"><a class="btn btn-mini btn-success" href="javascript:void(0)" onclick="loadModal(${blog.id})"> <i
														class="icon-fullscreen icon-white"></i>Read More
												</a></li>
											</ol>
										</div>
									</div>
								</li>
							</c:forEach>
						</ul>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="span3">
				<div class="well clearfix">
					<div class="media">
						<div class="pull-left">
							<spring:url value="/download/users/images/${blogUser.id}" htmlEscape="true" var="imageUrl" />
							<img style="width: 80px;" src="${!empty blogUser.imageSize ? imageUrl : '/shareinfo/resources/css/images/user-icon.gif'}" alt="user Name">
							<h3 class="text-center">
								<c:out value="${blogUser.firstName}" />
							</h3>
						</div>
						<div class="media-body">
							<h2 class="media-heading">
								Welcome <small class="inline-block "><sec:authentication property="principal.firstName" /> </small>
							</h2>
							<span id="user-status">${blogUser.statusMessage} </span>
						</div>
					</div>
					<c:if test="${userId == loggedInUserId}">
						<a class="btn btn-primary" href="${newBlogUrl}"> <i class="icon-pencil"></i> <c:out value="New Blog" /></a>
					</c:if>
					<spring:url value="/users/${userId}/posts" htmlEscape="true" var="postsUrl" />
					<a class="btn btn-warning " href="${postsUrl}">My Post »</a>
				</div>
				<div class="page-header">
					<h3 class="text-center text-info">Blog Topics</h3>
				</div>
				<c:choose>
					<c:when test="${viewAllBlogs}">
						<ol class="nav nav-list bs-docs-sidenav">
							<c:forEach items="${blogTopics}" var="blogTopic">
								<spring:url value="/users/${userId}/blogs/all/category/${blogTopic.id}" htmlEscape="true" var="userBlogTopicUrl" />
								<li class="${(currentTopicId == blogTopic.id) ? 'active' : '' }"><a href="${userBlogTopicUrl}"> <i class="pull-left icon-chevron-left"></i> <c:out
											value="${blogTopic.content}"></c:out></a></li>
							</c:forEach>
						</ol>
					</c:when>
					<c:otherwise>
						<ol class="nav nav-list bs-docs-sidenav">
							<c:forEach items="${blogTopics}" var="blogTopic">
								<spring:url value="/users/${userId}/blogs/category/${blogTopic.id}" htmlEscape="true" var="userBlogTopicUrl" />
								<li class="${(currentTopicId == blogTopic.id) ? 'active' : '' }"><a href="${userBlogTopicUrl}"> <i
										class="pull-left icon-chevron-left"></i> <c:out value="${blogTopic.content}"></c:out></a></li>
							</c:forEach>
						</ol>
					</c:otherwise>
				</c:choose>



				<!-- Modal -->



			</div>
		</div>
	</div>



	<jsp:include page="readBlogFrame.jsp" />
</body>
</html>