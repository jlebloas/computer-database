<%@ tag language="java" pageEncoding="UTF-8" description="Page link"
	body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="target" required="true" rtexprvalue="fasle"%>
<%@ attribute name="index" required="true" rtexprvalue="true"%>
<%@ attribute name="size" required="false" rtexprvalue="true"%>
<%@ attribute name="orderField" required="false" rtexprvalue="true"%>
<%@ attribute name="orderDirection" required="false" rtexprvalue="true"%>
<c:url value="${target}?page=${index}" />
<c:if test="${size != null}">&size=${size}</c:if>
<c:if test="${orderField != null}">&order=${orderField}</c:if>
<c:if test="${orderDirection != null}">&direction=${orderDirection}</c:if>
