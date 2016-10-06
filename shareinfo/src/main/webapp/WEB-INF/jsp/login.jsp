<!DOCTYPE html>
<%@page import="org.springframework.security.core.AuthenticationException"%>
<%@page import="org.springframework.security.web.WebAttributes"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<html>
<jsp:include page="fragments/headTag.jsp" />
<body>

<spring:url value="/j_spring_security_check" htmlEscape="true" var="loginUrl" />

    <div class="container-fluid">
    <div class="row-fluid">
    <div class="span6 offset2">
	<div class="">

		<form id="loginform" class="form-horizontal" action="${loginUrl}" method="post">
			<div class="page-header">
						<h1 class="text-info">Sec IT Services</h1>
					</div>

			<div class="control-group">
				<label class="control-label">Username</label>
				<div class="controls">
					<input type="text" name="username" value="${username}" placeholder="Username">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Password</label>
				<div class="controls">
					<input type="password" name="password" placeholder="Password">
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn btn-success btn-large">Sign in</button>
					<spring:url value="/signup" htmlEscape="true" var="signUpUrl" />
					<a class="btn btn-primary btn-large " href="${signUpUrl}">Sign Up »</a>
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<a href="#forgot-password-modal" data-toggle="modal"> Forgot your password ? </a>
				</div>
			</div>
			<%
			if (request.getParameter("login_error") != null) {
			%>
						<div class="control-group">
							<div class="alert alert-error">The username or password is incorrect. Please retype the username and password.</div>
							
							
						</div>
			<%
			}
			%>
		</form>
		<span class="text-success"> <c:out value="${message}"></c:out> </span>
		<span class="text-error"> <c:out value="${messageError}"></c:out> </span>
		
		<div style="border-bottom: 1px solid #eee;"></div>
		
	</div>
    </div>
    <div class="span6">
    <!--Body content-->
    
    
    
  <!--   enctype="multipart/form-data" -->
<%--     					<form:form modelAttribute="registerUser" method="post" class="form-horizontal" >
						<div class="control-group">
							<label class="control-label"> E-Mail</label>
							<div class="controls">
								<form:input path="email" class="input-xxlarge" id="input-title"  placeholder="example@gmail.com"   maxlength="128" type="text" />
								
							</div>
						</div>
						<div class="control-group">
							<label class="control-label"> First Name</label>
							<div class="controls">
								<form:input path="email" class="input-xxlarge" id="input-title"  placeholder="First Name"   maxlength="128" type="text" />
								
							</div>
						</div>
						<div class="control-group">
							<label class="control-label"> Last Name</label>
							<div class="controls">
								<form:input path="email" class="input-xxlarge" id="input-title"  placeholder="Last Name"   maxlength="128" type="text" />
								
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label">User Group </label>
							<div class="controls">
								<form:select path="blogTopic.id" items="${userGroups}" itemLabel="content" itemValue="id" />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label"> Password </label>
							<div class="controls">
								<form:input path="password" class="input-xxlarge" id="input-title"  placeholder="Password"   maxlength="128" type="password" />
								
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
								<button class="btn btn-primary" type="submit">Sign Up</button>
									
								
							</div>
						</div>

					</form:form>
					 --%>
					
    
    
    </div>
    </div>
    </div>





		<div id="forgot-password-modal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title">
					Hello					
				</h4>
			</div>
			<div class="modal-body">
			<spring:url value="/signup/recoverPassword" htmlEscape="true" var="recoverPasswordUrl" />
				<form id="recover-password-form" action="${recoverPasswordUrl}" method="post" >
					<fieldset>
						<div class="control-group">
						<label class="control-label" for="inputIcon">Email address</label>
						<div class="controls">
							<div class="input-prepend input-append">
								<span class="add-on"><i class="icon-envelope"></i></span> <input name="emailId" class="span2" id="input-email" type="text" placeholder="Email" required="required"> <span class="add-on muted">@gmail.com</span>
									
							</div>
						</div><span id="email-error-message"></span>
					</div>
				</fieldset>
				</form>

			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
				<button id="send-email-button" type="button" class="btn btn-primary" > <i class="icon-envelope icon-white"></i> Send Email</button>
			</div>
		</div>
		
		
	<script type="text/javascript">
	

		$(function() {
			$("#send-email-button").click(
					function(e) {
						$(e.currentTarget).prop('disabled', true);
					
					
						var userInput = $("#input-email").val().length;
						if (userInput > 3 && userInput < 16) {
							$("#recover-password-form").submit();
						} else {
							$("#email-error-message").addClass("text-error").text(" Cannot be empty. Range is 4 to 16 ").show().fadeOut(6000);
							$(e.currentTarget).prop('disabled', false);
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

		});
	</script>
</body>
</html>

