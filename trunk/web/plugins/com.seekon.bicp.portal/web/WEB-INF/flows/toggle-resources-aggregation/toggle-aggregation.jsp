<%@ page contentType="text/html; charset=UTF-8"%>

<%@ include file="/WEB-INF/jsp/include.jsp" %>
<portlet:actionURL var="actionUrl">
    <portlet:param name="execution" value="${flowExecutionKey}" />
</portlet:actionURL>
    
<!-- Portlet -->
<div class="fl-widget portlet toggle-aggr view-main" role="section">

    <!-- Portlet Titlebar -->
    <div class="fl-widget-titlebar titlebar portlet-titlebar" role="sectionhead">
    	<h2 class="title" role="heading">压缩</h2>
    </div>
    
    <!-- Portlet Body -->
    <div class="fl-widget-content content portlet-content" role="main">
	
        <div class="portlet-form">
            <form action="${actionUrl}" method="POST">
                <p>
                    CSS/JavaScript压缩当前 
                    <strong>
                        <c:choose>
                        	<c:when test="${aggregationEnabled}">启用</c:when>
                        	<c:otherwise>未启用</c:otherwise>
                        </c:choose>
                    </strong>
                    .
                <p> 
                <c:choose>
                    <c:when test="${aggregationEnabled}">
                        <input type="hidden" name="newAggregationValue" value="false"/> 
                        <input class="button" type="submit" value="不启用压缩" name="_eventId_disableAggregation"/>
                    </c:when>
                    <c:otherwise>
                    	<input type="hidden" name="newAggregationValue" value="true"/> 
                    	<input class="button" type="submit" value="启用压缩" name="_eventId_enableAggregation"/>
                    </c:otherwise>
                </c:choose> 
            </form>
        </div>
    
	</div> <!-- end: portlet-content -->
</div> <!-- end: portlet -->