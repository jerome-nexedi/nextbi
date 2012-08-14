<%@ page contentType="text/html; charset=UTF-8"%>

<%@ include file="/WEB-INF/jsp/include.jsp" %>
    
<!-- Portlet -->
<div class="fl-widget portlet portal-adm view-links" role="section">
  
  <!-- Portlet Titlebar 
  <div class="fl-widget-titlebar titlebar portlet-titlebar" role="sectionhead"> 	
    <h2 class="title" role="heading">门户管理工具</h2>    
  </div>
  -->
  <!-- Portlet Content -->
  <div class="fl-widget-content content portlet-content" role="main">
  
    <!-- Portlet Section -->
    <div class="portlet-section" role="region">
    	<div class="titlebar">
      		<h3 class="title" role="heading">门户对象管理</h3>
        </div>
        <div class="content">
            <ul>
                <li>
                    <a href="<c:url value="/portal/p/user-administration"/>">用户管理</a>
                </li>
                <li>
                    <a href="<c:url value="/portal/p/portlet-admin"/>">portlets管理</a>
                </li>
                <li>
                    <a href="<c:url value="/portal/p/groupsmanager"/>">组管理</a>
                </li>
                <li>
                    <a href="<c:url value="/portal/p/permissionsmanager"/>">权限管理</a>
                </li>
                <li>
                    <a href="<c:url value="/portal/p/fragment-admin"/>">DLM片段管理</a>
                </li>
            </ul>
        </div>
    </div>
    
    <!-- Portlet Section -->
    <div class="portlet-section" role="region">
    	<div class="titlebar">
      		<h3 class="title" role="heading">门户管理</h3>
        </div>
        <div class="content">
            <ul>
                <li>
                    <a href="<c:url value="/portal/p/cache-manager"/>">缓存管理</a>
                </li>
                <li>
                    <a href="<c:url value="/portal/p/toggle-resources-aggregation"/>">JS/CSS压缩</a>
                </li>
                <li>
                    <a href="<c:url value="/portal/p/fragment-audit"/>">DLM片段审核</a>
                </li>
            </ul>
        </div>
    </div>
    
    <!-- Portlet Section -->
    <div class="portlet-section" role="region">
    	<div class="titlebar">
      		<h3 class="title" role="heading">导入/导出</h3>
        </div>
        <div class="content">
            <ul>
                <li>
                    <a href="<c:url value="/portal/p/ImportExportPortlet"/>">导入/导出/删除对象</a>
                </li>
            </ul>
        </div>  
    </div>
    
  </div> <!-- end: portlet-content -->
</div> <!-- end: portlet -->
