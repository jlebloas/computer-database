<%@ tag language="java" pageEncoding="UTF-8" description="Generate column header"
	body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags/pagination" prefix="p" %>
<%@ attribute name="page" required="true" type="org.springframework.data.domain.Page" rtexprvalue="true"%>
<%@ attribute name="column" required="true" type="org.springframework.data.domain.Sort.Order" rtexprvalue="true"%>
<%@ attribute name="currentOrder" required="true" type="org.springframework.data.domain.Sort.Order" rtexprvalue="true"%>
<c:set var="columnName" value="computer.${column.property}" />
<th>
	<a href="<p:link target="dashboard" index="0" orderField="${column.property}" orderDirection="${column.direction}"/>"><spring:message code="${columnName}" /></a>
	<c:if test="${currentOrder.property == column.property}">
		<c:if test="${currentOrder.direction == 'ASC'}">
			&nbsp;
			<i class="fa fa-sort-asc fa-lg"></i>
		</c:if>
		<c:if test="${currentOrder.direction == 'DESC'}">
			&nbsp;
			<i class="fa fa-sort-desc fa-lg"></i>
		</c:if>
	</c:if>
</th>
