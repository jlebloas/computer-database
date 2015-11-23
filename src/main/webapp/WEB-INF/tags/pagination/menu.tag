<%@ tag language="java" pageEncoding="UTF-8"
	description="Pagination menu" body-content="empty"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/pagination" prefix="p" %>

<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true" rtexprvalue="true"%>

<ul class="pagination">
	<li><a href="<p:link target="dashboard" index="0" />" aria-label="First"> <span aria-hidden="true">&laquo;</span></a></li>

	<li><a href="<p:link target="dashboard" index="${page.number == 0 ? 0 : page.number -1}" />" aria-label="Previous"> <span aria-hidden="true">&lsaquo;</span></a></li>

	<c:forEach items="${pageRange}" var="index">
		<li <c:if test="${page.number == index}">class="active"</c:if>><a href="<p:link target="dashboard" index="${index}"/>">${index}</a></li>
	</c:forEach>

	<li><a href="<p:link target="dashboard" index="${page.number == page.totalPages - 1 ? page.totalPages -1 : page.number + 1}" />" aria-label="Next"> <span aria-hidden="true">&rsaquo;</span></a></li>

	<li><a href="<p:link target="dashboard" index="${page.totalPages - 1}" />" aria-label="Last"> <span aria-hidden="true">&raquo;</span></a></li>
</ul>

<div class="btn-group btn-group-sm pull-right" role="group">
	<a href="<p:link target="dashboard" index="0" size="10"/>"  type="button" class="${page.size == 10 ?  'btn btn-primary' : 'btn btn-default'}">10</a>
	<a href="<p:link target="dashboard" index="0" size="50"/>"  type="button" class="${page.size == 50 ?  'btn btn-primary' : 'btn btn-default'}">50</a>
	<a href="<p:link target="dashboard" index="0" size="100"/>" type="button" class="${page.size == 100 ? 'btn btn-primary' : 'btn btn-default'}">100</a>
</div>
