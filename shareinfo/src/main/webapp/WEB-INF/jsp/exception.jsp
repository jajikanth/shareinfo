<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<jsp:include page="fragments/headTag.jsp" />
<body>
	<div class="container">
		<jsp:include page="fragments/bodyHeader.jsp" />
		<h2>Something happened...</h2>
		<p>${exception.message}</p>
		<jsp:include page="fragments/footer.jsp" />
	</div>
</body>
</html>
