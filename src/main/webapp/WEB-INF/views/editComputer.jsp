<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="template/header.jsp" />

<section id="main">
	<div class="container">
		<div class="row">
			<div class="col-xs-8 col-xs-offset-2 box">
				<div class="label label-default pull-right">id: <c:out value="${computerDTO.id}" /></div>
				<h1>Edit Computer</h1>
				<form:form servletRelativeAction="/computer/edit" commandName="computerDTO" onsubmit="return checkForm()" method="POST">
					<form:hidden path="id"/>
					<fieldset>
						<jsp:include page="template/computerForm.jsp" />
					</fieldset>
					<div class="actions pull-right">
						<input type="submit" value="Edit" class="btn btn-primary"> or <a href="<c:url value="/" />" class="btn btn-default">Cancel</a>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</section>

<script src="<c:url value="/js/computer.js"/>"></script>

<jsp:include page="template/footer.jsp" />
