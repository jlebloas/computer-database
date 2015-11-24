<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Computer Database</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link href="<c:url value="/css/bootstrap.min.css" />" rel="stylesheet"
	media="screen">
<link href="<c:url value="/css/font-awesome.css" />" rel="stylesheet"
	media="screen">
<link href="<c:url value="/css/main.css" />" rel="stylesheet"
	media="screen">
	
<script src="<c:url value="/js/jquery.min.js"/>"></script>
<script src="<c:url value="/js/global.js"/>"></script>
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="<c:url value="/" />">Application - Database</a>
			</div>
	        <ul class="nav navbar-nav navbar-right">
	            <li class="dropdown">
	                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><spring:message code="header.lang_dropdown" /><span class="caret"></span></a>
	                <ul class="dropdown-menu">
	                    <li><a href="javascript:$.fn.setParam('lang', 'en')" ><img src="<c:url value="/png/gb.png" />" /> English</a></li>
	                    <li><a href="javascript:$.fn.setParam('lang', 'fr')" ><img src="<c:url value="/png/fr.png" />" /> Français</a></li>
	                </ul>
	            </li>
	        </ul>
		</div>
	</nav>
