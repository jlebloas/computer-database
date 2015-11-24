<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="template/header.jsp" />

<section id="main">
	<div class="container">
		<div class="row">
			<div class="col-xs-8 col-xs-offset-2 box">
				<h1><spring:message code="computer.form.add" /></h1>
				<form:form servletRelativeAction="/computer/add"
					commandName="computerDTO" onsubmit="return checkForm()"
					method="POST">
					<fieldset>
						<jsp:include page="template/computerForm.jsp" />
					</fieldset>
					<div class="actions pull-right">
						<input type="submit"
							value="<spring:message code="computer.form.button.add" />"
							class="btn btn-primary">
						<spring:message code="computer.form.button.or" />
						<a href="<c:url value="/" />" class="btn btn-default"><spring:message
								code="computer.form.button.cancel" /></a>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</section>

<script src="<c:url value="/js/computer.js"/>"></script>

<jsp:include page="template/footer.jsp" />
