<!DOCTYPE html>
<%@page import="org.springframework.security.core.AuthenticationException"%>
<%@page import="org.springframework.security.web.WebAttributes"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<html>
<jsp:include page="../fragments/headTag.jsp" />
<script type="text/javascript">
$(function () { 

	  $('input.length').maxlength({
	      alwaysShow: true,
	      placement: 'bottom-right'
	     
	  });
	  
		$("#input-password").keyup(function(){

	
			$('#input-confirm-password').val("");
			 $('#input-confirm-password').css("background-image", "url(/shareinfo/resources/css/images/invalid.png)");
	    	 $('#input-confirm-password').css("background-position", "right top");
	    	 $('#input-confirm-password').css("background-repeat", "no-repeat");
		});
		$("#input-confirm-password").keyup(function(){
			    if($('#input-password').val() === $(this).val()){
			    	 console.log("Matches  ");
			    	 $(this).removeAttr("style");
			    	 $(this).css("background-image", "url(/shareinfo/resources/css/images/valid.png)");
			    	 $(this).css("background-position", "right top");
			    	 $(this).css("background-repeat", "no-repeat");
			    }else{
	
			    	 $(this).css("border-color", "#e9322d");
			    	 $(this).css("box-shadow", "0 0 6px #f8b9b7");
			    	 $(this).css("background-image", "url(/shareinfo/resources/css/images/invalid.png)");
			    	 $(this).css("background-position", "right top");
			    	 $(this).css("background-repeat", "no-repeat");
			    	 $(this).prop("title", "Password Missmatched");
			    	 

			    	 
			    	 
			    }
		  

			});
		
		$("form").submit(
				function(event) {
					//for ie8/9
				    if ($("<input />").prop("required") === undefined) {
					      
			            $(this).find("input, select, textarea").filter("[required]").filter(function() { return this.value == ''; })
			                    .each(function() {
			                    	event.preventDefault();
			                        $(this).css({ "border-color":"red" });
			                        $("#user_message").addClass("text-error").text(" Please check the mandatory fields.").show().fadeOut(7000);
			                        $("#username_message").text(" ");
			                    });
			   

			    }
					var userName = $("#input-username").val();
					var firstName = $('#input-firstName').val();
					var lastName = $('#input-lastName').val();
					
					var password = $('#input-password').val();
					var confirmPassword = $('#input-confirm-password').val();
		

					 if(password != confirmPassword){
						event.preventDefault();
						$("#user_message").addClass("text-error").text(" Password Missmatched. ").show().fadeOut(7000);
					}
				});
		
			
		
});



			
			$( document ).ready(function() {
				$('#input-password').val("");
				$('#input-confirm-password').val("");
				var user_message = '<c:out value="${username_message}"></c:out>';
				if(user_message.length > 0){
					$('#input-username').val("");
				}
				
				});
</script>
<style type="text/css">
input:required:invalid,input:focus:invalid {
	background-image: url(/shareinfo/resources/css/images/invalid.png);
	background-position: right top;
	background-repeat: no-repeat;
}

input:required:valid {
	background-image: url(/shareinfo/resources/css/images/valid.png);
	background-position: right top;
	background-repeat: no-repeat;
}
</style>

<body>

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span6 offset2">
				<!--Body content-->



				<!--   enctype="multipart/form-data" -->
				<form:form modelAttribute="registerUser" method="post" class="form-horizontal" id="register-user-form">
					<div class="page-header">
						<h1 class="text-info">Employer Registration at shareinfo</h1>
					</div>
					<div class="control-group">
						<label class="control-label"> E-Mail</label>
						<div class="controls">
							<form:input path="username" class="length" id="input-username" placeholder="shareinfo userID" title=" minlength = 3 . only alphabets." maxlength="16" type="text"
								required="true" pattern="^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" />
							<span class="muted"> (Only Alphabets '_' , '@' and '.' )</span>
						</div>

					</div>
					<div class="control-group">
						<label class="control-label"> First Name</label>
						<div class="controls">
							<form:input path="firstName" class="length" id="input-firstName" placeholder="First Name" maxlength="16" type="text" required="required" pattern="^[a-zA-Z]{3,16}$" />
							<span class="muted">(Only Alphabets. Minimum length is 3)</span>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"> Last Name</label>
						<div class="controls">
							<form:input path="lastName" class="length" id="input-lastName" placeholder="Last Name" type="text" required="required" pattern="^[a-zA-Z]{1,16}$" maxlength="16" />
							<span class="muted">(Only Alphabets.)</span>
						</div>
					</div>


					<div class="control-group">
						<label class="control-label"> Password </label>
						<div class="controls">
							<form:input path="passwordUnEncrypted" class="length" id="input-password" placeholder="Password" maxlength="16" type="password" required="required"
								pattern="^.{8,}$" title="minlength = 8" />
							<span class="muted">(Minimum length is 8)</span>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">Confirm Password </label>
						<div class="controls">
							<form:input path="password" class="length" id="input-confirm-password" placeholder="Confirm Password" maxlength="16" type="password" required="required"
								pattern="^.{8,}$" />
							<span class="muted">(Minimum length is 8)</span>
						</div>
					</div>

					<div class="control-group">
						<div class="controls">
							<button class="btn btn-primary btn-large" type="submit">Sign Up »</button>
							<spring:url value="/login" htmlEscape="true" var="loginUrl" />
							<a class="btn btn-large " href="${loginUrl}"> Cancel </a>

						</div>
					</div>

					<span id="user_message" class="text-error"><c:out value="${user_message}"></c:out></span>
					<span id="username_message" class="text-error"><c:out value="${username_message}"></c:out></span>
				</form:form>


				<div style="border-bottom: 1px solid #eee;"></div>

			</div>
		</div>
	</div>






</body>
</html>

