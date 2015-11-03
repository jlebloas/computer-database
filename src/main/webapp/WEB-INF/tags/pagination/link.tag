<%@ tag language="java" pageEncoding="UTF-8" description="Page link"
	body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="target" required="true" rtexprvalue="fasle"%>
<%@ attribute name="index" required="true" rtexprvalue="true"%>
<%@ attribute name="size" required="true" rtexprvalue="true"%>
<%@ attribute name="search" required="true" rtexprvalue="true"%>
<%@ attribute name="order" required="true" rtexprvalue="true"%>
<%@ attribute name="direction" required="true" rtexprvalue="true"%>
<c:url value="${target}?page=${index}" />
<c:if test="${size != null}">&size=${size}</c:if>
<c:if test="${search != null}">&search=${search}</c:if>
<c:if test="${order != null}">&order=${order}</c:if>
<c:if test="${direction != null}">&direction=${direction}</c:if>
