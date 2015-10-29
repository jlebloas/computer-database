<%@ tag language="java" pageEncoding="UTF-8"
	description="Pagination menu" body-content="empty"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags/pagination" prefix="p" %>

<%@ attribute name="page" type="fr.jonathanlebloas.computerdatabase.model.Page" required="true" rtexprvalue="true"%>

<ul class="pagination">
	
	<li><a href="<p:link target="dashboard" index="1" size="${page.size}" search="${page.search}"/>" aria-label="First"> <span aria-hidden="true">&laquo;</span></a></li>

	<li><a href="<p:link target="dashboard" index="${page.index == 1 ? 1 : page.index - 1}" size="${page.size}" search="${page.search}"/>" aria-label="Previous"> <span aria-hidden="true">&lsaquo;</span></a></li>

	<c:forEach items="${page.range}" var="index">
		<li <c:if test="${page.index == index}">class="active"</c:if>><a href="<p:link target="dashboard" index="${index}" size="${page.size}" search="${page.search}"/>">${index}</a></li>
	</c:forEach>
	
	<li><a href="<p:link target="dashboard" index="${page.index == page.nbTotalPages ? page.nbTotalPages : page.index + 1}" size="${page.size}" search="${page.search}"/>" aria-label="Next"> <span aria-hidden="true">&rsaquo;</span></a></li>
	
	<li><a href="<p:link target="dashboard" index="${page.nbTotalPages}" size="${page.size}" search="${page.search}"/>" aria-label="Last"> <span aria-hidden="true">&raquo;</span></a></li>
</ul>


<div class="btn-group btn-group-sm pull-right" role="group">
	<a href="<p:link target="dashboard" index="1" search="${page.search}" size="10"/>"  type="button" class="${page.size == 10 ?  'btn btn-primary' : 'btn btn-default'}">10</a>
	<a href="<p:link target="dashboard" index="1" search="${page.search}" size="50"/>"  type="button" class="${page.size == 50 ?  'btn btn-primary' : 'btn btn-default'}">50</a>
	<a href="<p:link target="dashboard" index="1" search="${page.search}" size="100"/>" type="button" class="${page.size == 100 ? 'btn btn-primary' : 'btn btn-default'}">100</a>
</div>