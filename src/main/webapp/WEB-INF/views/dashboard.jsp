<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/pagination" prefix="p"%>

<jsp:include page="template/header.jsp" />

<section id="main">
	<div class="container">
		<h1 id="homeTitle">
			<c:out value="${page.nbTotalElement}" />
			Computers found
		</h1>
		<div id="actions" class="form-horizontal">
			<div class="pull-left">
				<form id="searchForm" action="" method="GET" class="form-inline">
					<input type="hidden" name="page" value="1">
					<input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" value="${page.search}" />
					&nbsp;
					<input type="submit" id="searchsubmit" value="Filter by name" class="btn btn-primary" />
				</form>
			</div>
			<div class="pull-right">
				<a class="btn btn-success" id="addComputer"
					href="<c:url value="/computer/add" />">Add Computer</a> <a
					class="btn btn-default" id="editComputer" href="#"
					onclick="$.fn.toggleEditMode();">Edit</a>
			</div>
		</div>
	</div>

	<form id="deleteForm" action="<c:url value="/computer/delete" />"
		method="POST">
		<input type="hidden" name="selection" value="">
		<input type="hidden" name="size" value="${page.size}">
	</form>

	<div class="container" style="margin-top: 10px;">
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<!-- Variable declarations for passing labels as parameters -->
					<!-- Table header for Computer Name -->

					<th class="editMode" style="width: 60px; height: 22px;"><input
						type="checkbox" id="selectall" /> <span
						style="vertical-align: top;"> - <a href="#"
							id="deleteSelected"> <i class="fa fa-trash-o fa-lg"></i></a>
					</span></th>
					<c:forEach items="${columns}" var="column">
						<p:column page="${page}" orderIndex="${orderIndex}" column="${column}" ></p:column>
					</c:forEach>
				</tr>
			</thead>
			<!-- Browse attribute computers -->
			<tbody id="results">

				<c:forEach items="${computers}" var="computer">
					<tr>
						<td class="editMode"><input type="checkbox" name="cb"
							class="cb" value="${computer.id}"></td>
						<td><a
							href="<c:url value="/computer/edit?id=${computer.id}" />"><c:out
									value="${computer.name}" /></a></td>
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

<jsp:include page="template/footer.jsp" />
