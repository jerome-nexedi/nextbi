<%@ page session="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<spring:theme code="mobile.custom.css.file" var="mobileCss" text="" />
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<head>
	    <title>用户登录</title>
        <c:choose>
           <c:when test="${not empty requestScope['isMobile'] and not empty mobileCss}">
                <meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;" />
                <meta name="apple-mobile-web-app-capable" content="yes" />
                <meta name="apple-mobile-web-app-status-bar-style" content="black" />
				<link href="/resource/rs/jquery-mobile/1.0a3/themes/default/jquery.mobile-1.0a3-default.min.css" media="" rel="stylesheet" type="text/css"/>
           </c:when>
           <c:otherwise>
                <spring:theme code="standard.custom.css.file" var="customCssFile" />
                <link type="text/css" rel="stylesheet" href="<c:url value="/css/cas.css" />" />
           </c:otherwise>
        </c:choose>
		<script src="/resource/rs/jquery/1.5/jquery-1.5.min.js" type="text/javascript"></script>
		<script src="/resource/rs/jqueryui/1.8/jquery-ui-1.8.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<c:url value="/js/cas.js" />"></script>
		<c:if test="${not empty requestScope['isMobile']}">
			<script type="text/javascript">
				$(document).bind("mobileinit", function(){
				  $.extend(  $.mobile , {
				    ajaxEnabled: false,
				    ajaxFormsEnabled: false
				  });
				});
			</script>
			<script src="/resource/rs/jquery-mobile/1.0a4.1/jquery.mobile-1.0a4.1.min.js" type="text/javascript"></script>
		</c:if>
	    <link rel="icon" href="<c:url value="/favicon.ico" />" type="image/x-icon" />
	</head>
	<body id="cas" class="fl-theme-iphone">
    <div class="flc-screenNavigator-view-container">
        <div class="fl-screenNavigator-view" data-role="page">
        <!-- 
            <div id="header" class="flc-screenNavigator-navbar fl-navbar fl-table" data-role="header" data-backbtn="false" data-position="inline">
                <h1 id="app-name" class="fl-table-cell">Central Authentication Service (CAS)</h1>
            </div>
         -->		
            <div id="content" class="fl-screenNavigator-scroll-container" data-role="content"
            	style="margin-left: 0px; margin-right: 0px; margin-bottom: 0px;">
