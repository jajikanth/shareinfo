<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<div class="row-fluid">
			<div class="span12">
				<div class="row-fluid">
					<div class="span4">
						<h2 style="margin-left:20px">shareinfo</h2>
					</div>
					<div class="span8">s
						<div class="row-fluid">
							<ul class="nav pull-right">
<!-- TODO: go to users posts on click -->
								<li><p style="padding: 10px 15px;  color: #777;"><i class="icon-user"></i> <sec:authentication
											property="principal.firstName" /> </p></li>
								<li><a href="<spring:url value="/logout" htmlEscape="true" />"><i class="icon-off"></i> Logout</a></li>
							</ul>
						</div>
						<div class="row-fluid">
							<ul class="nav pull-right">
								<li><a href="<spring:url value="/users" htmlEscape="true" />"><i class="icon-home"></i> Home</a></li>
								<li><a href="<spring:url value="/quiz" htmlEscape="true" />"><i class="icon-edit"></i> Quiz</a></li>
								<li><a href="<spring:url value="/users/${user.id}/blogs/all" htmlEscape="true" />"><i class="icon-book"></i> Blog</a></li>
							</ul>
						</div>
					</div>

				</div>
			</div>
		</div>


	</div>
</div>