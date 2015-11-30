<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="template/header.jsp" />


<section id="main">
	<div class="container">
		<div class="alert alert-danger">
			<spring:message code="error_401"/><br />
		</div>
	</div>
</section>

<jsp:include page="template/footer.jsp" />
