<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<spring:bind path="name">
	<div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
		<form:label path="name" cssClass="control-label"><spring:message code="computer.name" /></form:label>
		<spring:message code="computer.name" var="namePlaceHolder"/>
		<form:input type="text" cssClass="form-control" id="computerName"
			path="name" placeholder="${namePlaceHolder}" value="${computer.name}"
			aria-describedby="computerNameErrorStatus" />
		<span class="glyphicon glyphicon-remove form-control-feedback ${!status.error ? 'sr-only' : ''}"></span>
		<span id="computerNameErrorStatus"	class="help-block ${!status.error ? 'sr-only' : ''}"><spring:message code="computer.error.emptyName" /></span>
	</div>
</spring:bind>

<spring:bind path="introduced">
	<div class="form-group has-feedback ${status.error || globalError.code == 'IsFirstNotEmptyIfSecondNotEmpty' ? 'has-error' : ''}">
		<form:label path="introduced" cssClass="control-label"><spring:message code="computer.introduced" /></form:label>
		<form:input type="date" placeholder="2015-10-01"
			cssClass="form-control" id="introduced" path="introduced"
			value="${computer.introduced}"
			aria-describedby="introducedErrorStatus introducedEmptyErrorStatus" />
		<span class="glyphicon glyphicon-remove form-control-feedback ${!status.error && globalError.code != 'IsFirstNotEmptyIfSecondNotEmpty' ? 'sr-only' : ''}"></span>
		<span id="introducedErrorStatus"
			class="help-block ${!status.error ? 'sr-only' : ''}"><spring:message code="computer.error.invalidIntroduced" /></span> <span id="introducedEmptyErrorStatus"
			class="help-block ${globalError.code != 'IsFirstNotEmptyIfSecondNotEmpty' ? 'sr-only' : ''}"><spring:message code="computer.error.emptyIntroduced" /></span>
	</div>
</spring:bind>

<spring:bind path="discontinued">
	<div class="form-group has-feedback ${status.error || globalError.code == 'DateAfter' ? 'has-error' : ''}">
		<form:label path="discontinued" cssClass="control-label"><spring:message code="computer.discontinued" /></form:label>
		<form:input type="date" placeholder="2015-12-25"
			cssClass="form-control" id="discontinued" path="discontinued"
			value="${computer.discontinued}"
			aria-describedby="discontinuedErrorStatus orderErrorStatus" />
		<span class="glyphicon glyphicon-remove form-control-feedback ${!status.error && globalError.code != 'DateAfter' ? 'sr-only' : ''}"></span>
		<span id="discontinuedErrorStatus"
			class="help-block ${!status.error ? 'sr-only' : ''}"><spring:message code="computer.error.invalidDiscontinueded" /></span> <span id="orderErrorStatus"
			class="help-block ${globalError.code != 'DateAfter' ? 'sr-only' : ''}"><spring:message code="computer.error.invalidDateOrder" /></span>
	</div>
</spring:bind>

<div class="form-group has-feedback">
	<form:label path="companyId" cssClass="control-label"><spring:message code="computer.company" /></form:label>
	<form:select path="companyId" items="${companies}" itemLabel="name"
		itemValue="id" cssClass="form-control" id="companyId" />
</div>
