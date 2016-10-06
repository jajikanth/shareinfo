<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<html>
<jsp:include page="../fragments/headTag.jsp" />
<body>
	<script type="text/javascript">
		$(function() {
			$.fn.placeholder();
			$('textarea.validate').maxlength({
				alwaysShow : true,
				placement : 'bottom-right'
			});

			$('input.validate').maxlength({
				alwaysShow : true,
				placement : 'bottom-right'

			});

			$("form").submit(
							function(event) {
								var quizTopicLength = $("#quiz-topic-input").val().length;
								var firstQuizLength = $('textarea :first').val().length;
								console.log("quizTopicLength" + quizTopicLength);
								var file = $("#file").get(0);
								
								if (quizTopicLength > 0 && firstQuizLength > 0 && validateFile(file)) {
									return;
								}else{
									$(window).scrollTop(0);
									$("#messages").addClass("text-error").text(" Topic cannot be empty and Min 1 Question Should be posted. ").show().fadeOut(7000);
									event.preventDefault();
								}
								
				/* 				if (validateFile(file)) {
									return;
								} else {
									$(window).scrollTop(0);
									event.preventDefault();
								} */
	
							});
			
			$("#file").change(function(event) {
				validateFile(this);
				var ele = document.getElementById("file");
				ele.setAttribute("title",ele.value);
				});
			
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

	<jsp:include page="../fragments/bodyHeader.jsp" />
	<div class="container-fluid">
		<h2>Edit Quiz</h2>

		<div class="row-fluid">

			<div class="span9 offset1">


				<form:form modelAttribute="quizForm" method="post" class="form-horizontal" enctype="multipart/form-data">


					<div class="control-group ">
						<label class="control-label"> Topic </label>
						<div class="controls">
							<form:input id="quiz-topic-input" path="quizTopic.content" placeholder="Title of the Quiz Topic" class="input-xxlarge validate" type="text" maxlength="64" />
							<span id="messages" class=""></span>

						</div>
					</div>

					<div class="control-group ">


						<label class="control-label">Existing Files</label>
						<div class="controls">
							<c:choose>
								<c:when test="${empty quizForm.quizTopic.quizFiles}">
									<div class="alert alert-danger span7">
										
										<c:out value="No Files Attached."></c:out>
									</div>
								</c:when>
								<c:otherwise>
									<ul class="unstyled">
										<c:forEach items="${quizForm.quizTopic.quizFiles}" var="quizFile">
											<li>
												<div class="alert alert-info span7">
												<c:out value="${quizFile.name}"></c:out>
												</div> 
											</li>
										</c:forEach>
									</ul>
								</c:otherwise>
							</c:choose>
						</div>
					</div>

					<div class="control-group " id="file-control-group">
						<label class="control-label">Add File</label>
						<div class="controls">
							<form:input type="file" path="file" id="file" />
							<span class="help-inline"> max size: 1 GB</span>

						</div>
					</div>


					<table class="table table-striped ">
						<thead>

							<tr>
								<th>No.</th>
								<th>Question</th>
								<th>Answer</th>

							</tr>
						</thead>
						<tbody>

							<c:forEach items="${quizForm.quizTopic.quizs}" var="existingForm" varStatus="status">
								<tr>
									<td align="center">${status.count}</td>
									<td><form:textarea path="quizTopic.quizs[${status.index}].question" class="input-xxlarge validate" maxlength="512" /></td>
									<td><form:textarea path="quizTopic.quizs[${status.index}].answer" class="input-xxlarge validate" maxlength="512" /></td>

								</tr>
							</c:forEach>
							<tr>
								<td></td>
								<td><h4>Update More Questions.</h4></td>
								<td></td>
							</tr>

							<c:forEach items="${quizForm.quizs}" var="newQuiz" varStatus="status">
								<tr>
									<td align="center">${status.count}</td>
									<td><form:textarea path="quizs[${status.index}].question" class="input-xxlarge validate" maxlength="512" /></td>
									<td><form:textarea path="quizs[${status.index}].answer" class="input-xxlarge validate" maxlength="512" /></td>

								</tr>
							</c:forEach>
						</tbody>
					</table>
					<br />

					<button class="btn btn-primary" type="submit">Save</button>
					<spring:url value="/quiz" htmlEscape="true" var="quizUrl" />
					<a class="btn" href="${quizUrl}">Cancel</a>


				</form:form>








			</div>

		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>