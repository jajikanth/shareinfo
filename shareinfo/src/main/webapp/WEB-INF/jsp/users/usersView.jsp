<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>

<html>
<jsp:include page="../fragments/headTag.jsp" />
<style>
#quiz-container,#blog-container {
	position: relative;
}

@media ( min-width : 992px) {
	.modal-lg {
		width: 60%;
		margin-left: -480px;
	}
	.modal-body {
		max-height: 60vh;
	}
}
</style>

<body>
	<spring:url value="/users/principals" htmlEscape="true" var="authUsersUrl" />
	<jsp:include page="../fragments/bodyHeader.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span8" style="height: 80%; overflow-y: auto;">
				<h2>OMADM</h2>
				
				<c:if test="${!empty usersList}">
				<spring:url value="/download/users/images" htmlEscape="true" var="imageUrl" />
												<%-- <div class="thumbnail text-center">
									<div style="width: 160px; height: 165px; overflow-y: hidden;">
										<a href="#"><img style="width: 160px;" src="${imageUrl}"
											alt="test"> </a>
									</div>

							

								</div> --%>
								
								
					<ul class="thumbnails">
					
						<c:forEach items="${usersList}" var="user">
							<spring:url value="/users/${user.id}/posts" htmlEscape="true" var="postsUrl" />
							<li>
								<div class="thumbnail text-center">
									<div style="width: 160px; height: 165px; overflow-y: hidden;">
									<spring:url value="/download/users/images/${user.id}" htmlEscape="true" var="imageUrl" />
										<a href="${postsUrl}"><img style="width: 160px;" src="${!empty user.imageSize ?  imageUrl : '/shareinfo/resources/css/images/user-icon.gif'}"> </a>
									</div>
									<span id="user-name-container-${user.firstName}" class ="inline">
										<c:out value="${user.firstName}">    </c:out> 
									
									</span>
								

									<div id="user-${user.id}">
										<%-- <button id="call-user-${user.id}" class="btn-mini" disabled > <i class="icon-eye-open icon-white"></i> Call</button> --%>

										<%--  <button id="text-user-${user.id}" class= "inline btn-mini" disabled onclick = "openChatSession('${user.firstName}')" > Chat </button> --%>
									</div>
								</div>

							</li>
						</c:forEach>
					</ul>
				</c:if>
			</div>

			<div class="span4">

			<button id="text-user" class="btn-large btn-success" onclick="openConferenceRoom()" style="margin-left: 25%; margin-right:25%; margin-bottom: 10px" title="Please open in Chrome browser only.">Open Conference Room</button>
	
				<div class="panel panel-success">
					<div class="panel-heading">
						<h1 class="panel-title text-center">
							<c:out value="Quiz"></c:out>
						</h1>
					</div>
					<div id="quiz-container" class="panel-body" style="height: 270px">
						<div id="quiz-list">

							<ol class="unstyled">
								<c:forEach items="${recentQuizs}" var="quiz">
									<li style="border-bottom: 1px solid #ddd; width: 480px; margin: 10px; max-height: 80px; overflow-y: hidden;">
									<a style="white-space: pre-wrap; width: 100%;" href="#load-modal-${quiz.id}" data-toggle="modal"><c:out value="${quiz.question}"></c:out></a></li>
									
										<div id="load-modal-${quiz.id}" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">

				
									
									<%-- <div id="load-modal-${quiz.id}" class="modal hide fade modal-lg" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> --%>
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
											<h4 class="modal-title">Question</h4>
										</div>
										<div class="modal-body">
											<p style="white-space: pre-wrap; width: 100%;">${quiz.question}</p>


											<div id="answer-alert-${quiz.id}" class="hide alert alert-success">
												<button onclick="$('#answer-alert-${quiz.id}').toggle();" class="close pull-right">&times;</button>
												<p style="white-space: pre-wrap; width: 100%;"><c:out value="${quiz.answer}"></c:out></p>
											</div>

										</div>
										<div class="modal-footer">
											<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
											<a class="btn btn-warning pull-right" onclick="$('#answer-alert-${quiz.id}').toggle();">Answer »</a>
										</div>
									</div>
						</c:forEach>
							</ol>
						</div>
					</div>
				</div>

				<div class="clearfix">

					<div class="panel panel-success">
						<div class="panel-heading">
							<h1 class="panel-title text-center">
								<c:out value="Recent Feeds"></c:out>
							</h1>
						</div>
						<div id="blog-container" class="panel-body" style="height: 270px">
							<div id="blog-list">
								<ol class="unstyled">

									<c:forEach items="${recentBlogs}" var="blog">
										<li id="blog-${blog.id}" style="border-bottom: 1px solid #ddd; width: 480px; margin: 10px">
											<div class="media">
												<div class=" pull-left media-object" style="width: 30px; height: 50px; overflow-y: hidden;">
													<spring:url value="/download/users/images/${blog.user.id}" htmlEscape="true" var="imageUrl" />
													<img width=30px src="${!empty blog.user.imageSize ?  imageUrl : '/shareinfo/resources/css/images/user-icon.gif'}" alt="User Images">
												</div>
												<div class="media-body">
													<h5 class="media-heading">
														<c:out value="${blog.user.firstName}"></c:out>
														<small class="inline-block "><joda:format value="${blog.creationTime}" style="SM" /></small>
													</h5>

													<a href="javascript:void(0)" onclick="loadModal(${blog.id})"> <c:out value="${blog.title}"></c:out></a>

												</div>
											</div> <!-- For read more jsp page -->
											<div style="height: 1px; display: none;">

												<div class="panel-heading">

													<c:out value="${blog.title}"></c:out>

												</div>
												<div class="panel-body">${blog.content}</div>



											</div>

										</li>

									</c:forEach>



								</ol>
							</div>
						</div>
					</div>
				</div>







			</div>

		</div>
		<jsp:include page="../blogs/readBlogFrame.jsp" />
		<script src="<spring:url value="/resources/js/jscroller-0.4.js" />"></script>
		<script>
		$(function() {
			  // Add Scroller Object
			  $jScroller.add("#quiz-container","#quiz-list","up",1);
			  $jScroller.add("#blog-container","#blog-list","up",1);
			  // Start Autoscroller
			  $jScroller.start();
			  
			  
		});
		
	</script>

		<script type="text/javascript">
 	getAuthenticatedUsers();

//var intervalID;
//clearInterval(intervalID);
//intervalID = window.setInterval(getAuthenticatedUsers, 30000);

function getAuthenticatedUsers(){
	var wth = $(".panel-body").width();
	$(".panel-body").find("li").width(wth);
	console.log("get users called");
//	$(":button").removeClass('btn-success').prop("disabled", true);
	var authUsersUrl = "<c:out value='${authUsersUrl}'/>";
$.ajax({
	url : authUsersUrl,
	type : "GET",
	cache : false,
	dataType : "json",
	async : false,
	success : function(authenticatedUsers, status) {
		authenticatedUsers.forEach(function(user) {
			console.log("Authenticate User : " + user);
			
		var activeUser = ''+ user +' &nbsp; <img src="/shareinfo/resources/css/images/active-icon.png" alt="*">'
			
			//todo: check for current user to avoid caling himself
			
			
			$('#user-name-container-'+user+'').html(activeUser);
			
				});
	
	},
	error : function(e) {
		console.log("Error: " + e);
		clearInterval(intervalID);
		bootbox.alert("Running Out of Session.");
		
	},
	complete : function() {
		console.log("Completed request");
	}
});


} 



/* window.onbeforeunload = function() {

	var logoutUrl = "<c:out value='${logoutUrl}'/>";
	$.post(logoutUrl);
	console.log("logout url ");
	
}
 */
 
 function openConferenceRoom(){
	 var url = "<c:out value='${chatRoomUrl}'/>";
	 console.log("chat url ..........."+ url);
var chatUrl = window.location +"/chatRoom";
	 window.open(chatUrl , "chatWindow", "scrollbars=yes, resizable=yes, top=500, left=500, width=1000, height=400");
 }
  
 
 
 
</script>

<spring:url value="/users/chatRoom" htmlEscape="true" var="chatRoomUrl" />
	</div>

	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>

<%-- 	<jsp:include page="webRTCmultiConnect.jsp" /> --%>