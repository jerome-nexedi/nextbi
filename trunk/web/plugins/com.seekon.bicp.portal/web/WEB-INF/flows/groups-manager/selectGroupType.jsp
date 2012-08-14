<%@ page contentType="text/html; charset=UTF-8"%>

<%@ include file="/WEB-INF/jsp/include.jsp" %>
<portlet:renderURL var="formUrl">
    <portlet:param name="execution" value="${flowExecutionKey}" />
</portlet:renderURL>
<portlet:renderURL var="peopleUrl">
    <portlet:param name="execution" value="${flowExecutionKey}"/>
    <portlet:param name="groupType" value="group"/>
    <portlet:param name="_eventId" value="next"/>
</portlet:renderURL>
<portlet:renderURL var="portletUrl">
    <portlet:param name="execution" value="${flowExecutionKey}"/>
    <portlet:param name="groupType" value="category"/>
    <portlet:param name="_eventId" value="next"/>
</portlet:renderURL>
<c:set var="n"><portlet:namespace/></c:set>

<!-- Portlet -->
<div class="fl-widget portlet grp-mgr view-selectgroup" role="section">
    
    <!-- Portlet Titlebar -->
    <div class="fl-widget-titlebar titlebar portlet-titlebar" role="sectionhead">
        <h2 class="title" role="heading">
            <spring:message code="groups.by.type" />
        </h2>
    </div> <!-- end: portlet-titlebar -->
    
    <!-- Portlet Content -->
    <div class="fl-widget-content content portlet-content" role="main">
    
    	<div class="panel-list icon-large group-types">
        	<div class="panel type-categories">
            	<div class="titlebar">
                	<h2 class="title">
                    	<a href="${ portletUrl }"><spring:message code="portlet.categories"/></a>
                    </h2>
                    <h3 class="subtitle"><spring:message code="portlet.categories.description"/></h3>
                </div>
                <div class="content">
                	<span class="link-list">
                    	<c:forEach items="${ groups.categories }">
                        	<a href="${ groupUrl }">${ fn:escapeXml(group.name )}</a>${ fn:escapeXml(status.last ? "" : ", " )}
                        </c:forEach>
                    </span>
                </div>
            </div>
            <div class="panel type-people">
            	<div class="titlebar">
                	<h2 class="title">
                    	<a href="${ peopleUrl }"><spring:message code="person.groups"/></a>
                    </h2>
                    <h3 class="subtitle"><spring:message code="person.groups.description"/></h3>
                </div>
                <div class="content">
                	<span class="link-list">
                    	<c:forEach items="${ groups.people }">
                        	<a href="${ groupUrl }">${ fn:escapeXml(group.name )}</a>${ fn:escapeXml(status.last ? "" : ", " )}
                        </c:forEach>
                    </span>
                </div>
            </div>
        </div>
        
    </div>
</div>