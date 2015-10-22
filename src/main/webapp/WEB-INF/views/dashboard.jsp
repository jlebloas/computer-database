<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jonathanlebloas.fr/computerdatabase/tag/pagination" prefix="p"%>

<jsp:include page="template/header.jsp" />

<section id="main">
	<div class="container">
		<h1 id="homeTitle">121 Computers found</h1>
		<div id="actions" class="form-horizontal">
			<div class="pull-left">
				<form id="searchForm" action="#" method="GET" class="form-inline">

					<input type="search" id="searchbox" name="search"
						class="form-control" placeholder="Search name" /> <input
						type="submit" id="searchsubmit" value="Filter by name"
						class="btn btn-primary" />
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

	<form id="deleteForm" action="#" method="POST">
		<input type="hidden" name="selection" value="">
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
							id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
								class="fa fa-trash-o fa-lg"></i></a>
					</span></th>
					<th>Computer name</th>
					<th>Introduced date</th>
					<th>Discontinued date</th>
					<th>Company</th>
				</tr>
			</thead>
			<!-- Browse attribute computers -->
			<tbody id="results">

				<c:forEach items="${computers}" var="computer">
					<tr>
						<td class="editMode"><input type="checkbox" name="cb"
							class="cb" value="0"></td>
						<td><a href="editComputer.html" onclick=""><c:out
									value="${computer.name}" /></a></td>
						<td><c:out value="${computer.introduced}" /></td>
						<td><c:out value="${computer.discontinued}" /></td>
						<td><c:out value="${computer.manufacturerName}" /></td>

					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</section>

<footer class="navbar-fixed-bottom">
	<div class="container text-center">
		<p:menu page="${page}" pageCount="${pageCount}"/>
	
<!-- 		<ul class="pagination"> -->
<!-- 			<li><a href="#" aria-label="Previous"> <span -->
<!-- 					aria-hidden="true">&laquo;</span> -->
<!-- 			</a></li> -->
<!-- 			<li><a href="#">1</a></li> -->
<!-- 			<li><a href="#">2</a></li> -->
<!-- 			<li><a href="#">3</a></li> -->
<!-- 			<li><a href="#">4</a></li> -->
<!-- 			<li><a href="#">5</a></li> -->
<!-- 			<li><a href="#" aria-label="Next"> <span aria-hidden="true">&raquo;</span> -->
<!-- 			</a></li> -->
<!-- 		</ul> -->

		<div class="btn-group btn-group-sm pull-right" role="group">
			<button type="button" class="btn btn-default">10</button>
			<button type="button" class="btn btn-default">50</button>
			<button type="button" class="btn btn-default">100</button>
		</div>
	</div>
</footer>

<jsp:include page="template/footer.jsp" />
