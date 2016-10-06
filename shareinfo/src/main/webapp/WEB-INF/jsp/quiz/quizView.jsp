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
<body>
	<spring:url value="/quiz/new" htmlEscape="true" var="newQuizTopicUrl" />
	<spring:url value="/quiz/${selectedQuizTopic.id}/delete" htmlEscape="true" var="deleteQuizTopicUrl" />
	<spring:url value="/quiz/file/delete" htmlEscape="true" var="deleteQuizFileUrl" />
	<spring:url value="/quiz/${selectedQuizTopic.id}/edit" htmlEscape="true" var="editQuizTopicUrl" />

	<jsp:include page="../fragments/bodyHeader.jsp" />

	<script type="text/javascript">
		$(document).ready(function() {

			//To store stick-notes in browser session.	
			if (window.sessionStorage) {

				var notes = document.getElementById("sticky-notes");
				if (sessionStorage.feedbackdata) {
					notes.value = sessionStorage.feedbackdata
				}
				notes.onkeyup = function(e) {
					sessionStorage.feedbackdata = this.value
				}

			}
		});
		
	
		function deleteFile(fileId) {
			var deleteQuizFileUrl = "<c:out value='${deleteQuizFileUrl}'/>";
			bootbox.confirm("Are you sure?",function(result) {
								if (result) {
									$.ajax({
												url : deleteQuizFileUrl + "/"	+ fileId ,
												type : "DELETE",
												cache : false,
												contentType : "application/text",
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
		
		
	</script>


	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3 mousescroll">
				<c:if test="${!empty quizTopics}">
					<div class="page-header">
						<h3 class="text-center text-info">Quiz Topics</h3>
					</div>
					<ul class="nav nav-list bs-docs-sidenav">
						<c:forEach items="${quizTopics}" var="quizTopic">
							<spring:url value="/quiz/${quizTopic.id}" htmlEscape="true" var="quizUrl" />
							<li class="${(quizTopic.id == selectedQuizTopic.id) ? 'active' : '' }"><a href="${quizUrl}"> <c:out value="${quizTopic.content}"></c:out> <i
									class="pull-right icon-chevron-right"></i></a></li>
						</c:forEach>
					</ul>
				</c:if>

			</div>

			<div class="span6">

				<c:if test="${Deleted}">
					<div class="alert alert-success">
						<button type="button" class="close" data-dismiss="alert">&times;</button>
						Successfully deleted.
					</div>
				</c:if>
				<div class="well well-small">
					<a class="pull-right btn btn-mini btn-warning " href="${newQuizTopicUrl}">Create Quiz »</a>
					<c:if test="${!empty selectedQuizTopic}">
						<h4 class="text-info">${selectedQuizTopic.content}</h4>
						<ul class="inline">
							<li><i class="icon-user"></i> <c:out value="${selectedQuizTopic.user.firstName}"></c:out></li>
							<li><i class="icon-time"></i> <joda:format value="${selectedQuizTopic.creationTime}" style="SM" /></li>
							<li><a class=" " href="${deleteQuizTopicUrl}" onclick="return confirm('Are you sure?') ? true : false;"><i class="icon-trash"></i> Delete</a></li>
							<li><a class=" " href="${editQuizTopicUrl}"> <i class="icon-edit"></i> Edit
							</a></li>
						</ul>
					</c:if>
				</div>


				<div id="quiz-container">
					<c:choose>
						<c:when test="${!empty quizDetails}">
							<ul class="unstyled">

								<c:forEach items="${quizDetails}" var="quiz" varStatus="count">
									<li>

										<div class="panel panel-success">
											<div class="panel-heading">
												<h2 class="panel-title">
													<c:out value="Question ${count.count}"></c:out>
												</h2>
											</div>
											<div id="quiz-container" class="panel-body" style="height: 25vh; overflow-y: auto">
												<p>${quiz.question}</p>
											</div>
											<div class="panel-footer clearfix">
												<a class="btn btn-warning pull-right" onclick="$('#answer-alert-${quiz.id}').toggle();">Get Key »</a>
											</div>
										</div> <%-- 									<div class="modal" style="margin: 0px auto 20px; left: auto; position: relative; width: auto">
										<div class="modal-header clearfix">
											<h3>Question ${count.count}</h3>
										</div>
										<div class="modal-body">
											<p>${quiz.question}</p>
										</div>
										<div class="modal-footer" style="background-color: white;">
											<a class="btn btn-warning " onclick="$('#answer-alert-${quiz.id}').toggle();">Get Key »</a>
										</div>
									</div> --%>
										<div id="answer-alert-${quiz.id}" class="hide alert alert-success">
											<button onclick="$('#answer-alert-${quiz.id}').toggle();" class="close pull-right">&times;</button>
											<p>${quiz.answer}</p>
										</div>
									</li>
								</c:forEach>
							</ul>
						</c:when>

						<c:otherwise>
							<span> No Questions Available.</span>
						</c:otherwise>
					</c:choose>
				</div>

				<div id="files-container">
				
					<c:choose>
						<c:when test="${!empty quizFiles}">
							<ul class="unstyled">
								<c:forEach items="${quizFiles}" var="quizFile">
									<li>
										<div class="alert alert-info span7" id = "file-${quizFile.id}">
											<button class="close pull-right" onclick="deleteFile(${quizFile.id})">&times;</button>
											<spring:url value="/download/quizFiles/${quizFile.id}" htmlEscape="true" var="downloadFileUrl" />
											<a href="${downloadFileUrl}">
											<c:out value="${quizFile.name}"></c:out></a>
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
			<div class="span3">
				<div style="width: 22%" data-spy="affix">
					<div class="page-header">
						<h3 class="text-center text-warning">Sticky notes</h3>
					</div>
					<div>
						<textarea id="sticky-notes" style="background-color: #f5f5f5; width: 100%; height: 65vh" placeholder="Continue typing. . ."></textarea>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>