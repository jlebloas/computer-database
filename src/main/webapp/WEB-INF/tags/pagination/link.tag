<%@ tag language="java" pageEncoding="UTF-8" description="Page link"
	body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="target" required="true" rtexprvalue="fasle"%>
<%@ attribute name="index" required="true" rtexprvalue="true"%>
<%@ attribute name="size" required="true" rtexprvalue="true"%>
<c:url value="${target}?page=${index}&size=${size}" />