<%@ tag language="java" pageEncoding="UTF-8" description="Page link"
	body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/pagination" prefix="p" %>
<%@ attribute name="page" required="true" type="fr.jonathanlebloas.computerdatabase.model.Page" rtexprvalue="true"%>
<%@ attribute name="column" required="true" type="fr.jonathanlebloas.computerdatabase.controller.DashboardServlet.OrderColumn" rtexprvalue="true"%>
<%@ attribute name="orderIndex" required="true" rtexprvalue="true"%>

<th>
	<a href="<p:link target="dashboard" index="1" search="${page.search}" size="${page.size}" order="${column.index}" direction="${column.direction}"/>">${column.name}</a>
	<c:if test="${orderIndex == column.index}">

		<c:if test="${page.sort.direction == 'ASC'}">
			&nbsp;
			<i class="fa fa-sort-asc fa-lg"></i>
		</c:if>
		<c:if test="${page.sort.direction == 'DESC'}">
			&nbsp;
			<i class="fa fa-sort-desc fa-lg"></i>
		</c:if>
	</c:if>
</th>
