<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/pagination" prefix="p"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

<jsp:include page="template/header.jsp" />

<section id="main">
	<div class="container">
		<h1 id="homeTitle">
			<c:out value="${page.totalElements}" />
			<spring:message code="dashboard.title" />
		</h1>
		<div id="actions" class="form-horizontal">
			<div class="pull-left">
				<form id="searchForm" action="" method="GET" class="form-inline">
					<input type="hidden" name="page" value="0"> <input
						type="search" id="searchbox" name="search" class="form-control"
						placeholder="<spring:message code="dashboard.search.field" />"
						value="${search}" /> &nbsp; <input type="submit"
						id="searchsubmit"
						value="<spring:message code="dashboard.search.button" />"
						class="btn btn-primary" />
				</form>
			</div>
			<sec:authorize access="hasRole('ADMIN')">
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer"
						href="<c:url value="/computer/add" />"><spring:message
							code="dashboard.button.add" /></a> <a class="btn btn-default"
						id="editComputer" href="#" onclick="$.fn.toggleEditMode();"><spring:message
							code="dashboard.button.edit" /></a>
				</div>
			</sec:authorize>
		</div>
	</div>

	<sec:authorize access="hasRole('ADMIN')">
		<form:form id="deleteForm" servletRelativeAction="/computer/delete"
			method="POST">
			<input type="hidden" name="selection" value="" />
		</form:form>
	</sec:authorize>

	<div class="container" style="margin-top: 10px;">
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<!-- Variable declarations for passing labels as parameters -->
					<!-- Table header for Computer Name -->

					<sec:authorize access="hasRole('ADMIN')">
						<th class="editMode" style="width: 60px; height: 22px;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> - <a href="#"
								id="deleteSelected"> <i class="fa fa-trash-o fa-lg"></i></a>
						</span></th>
					</sec:authorize>
					<c:forEach items="${columns}" var="column">
						<p:column page="${page}" currentOrder="${order}"
							column="${column}"></p:column>
					</c:forEach>
				</tr>
			</thead>
			<!-- Browse attribute computers -->
			<tbody id="results">

				<c:forEach items="${computers}" var="computer">
					<tr>

						<sec:authorize access="hasRole('ADMIN')">
							<td class="editMode"><input type="checkbox" name="cb"
								class="cb" value="${computer.id}"></td>
						</sec:authorize>

						<td><sec:authorize access="hasRole('ADMIN')">
								<a href="<c:url value="/computer/edit/${computer.id}" />"><c:out
										value="${computer.name}" /></a>
							</sec:authorize> <sec:authorize access="!hasRole('ADMIN')">
								<c:out value="${computer.name}" />
							</sec:authorize></td>
						<td><c:out value="${computer.introduced}" /></td>
						<td><c:out value="${computer.discontinued}" /></td>
						<td><c:out value="${computer.companyName}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</section>

<footer class="navbar-fixed-bottom">
	<div class="container text-center">
		<p:menu page="${page}" />
	</div>
</footer>

<sec:authorize access="hasRole('ADMIN')">
	<script src="<c:url value="/js/dashboard.js"/>"></script>
</sec:authorize>
<jsp:include page="template/footer.jsp" />
