<%@ page contentType="text/html; charset=UTF-8"%>

<%@ include file="/WEB-INF/jsp/include.jsp" %>

<!-- Portlet -->
<div class="fl-widget portlet imp-exp view-delete" role="section">

    <!-- Portlet Titlebar -->
    <div class="fl-widget-titlebar titlebar portlet-titlebar" role="sectionhead">
        <h2 class="title" role="heading"><spring:message code="delete.portlet.entities"/></h2>
        <div class="toolbar" role="toolbar">
            <ul>
                <li><a class="button" href="<portlet:renderURL/>"><spring:message code="import"/></a></li>
                <li><a class="button" href="<portlet:renderURL><portlet:param name="action" value="export"/></portlet:renderURL>"><spring:message code="export"/></a></li>
            </ul>
        </div>
    </div>
    
    <!-- Portlet Content -->
    <div class="fl-widget-content content portlet-content" role="main">   
        
        <!-- Messages -->
        <div class="portlet-msg-error portlet-msg error" role="alert">
            <div class="titlebar">
                <h3 class="title">警告</h3>
            </div>
            <div class="content">
                <p>删除一些对象将对门户产生很严重的影响。所有删除操作默认是被禁止的 ；谨慎使用此特性。 </p>
            </div>
        </div>
        
        <!-- Note -->
        <div class="note" role="note">
            <p>请选择删除的对象.</p>
        </div>
        
        <div class="portlet-form">
            <form id="${n}form" method="POST">
                <table class="purpose-layout">
                    <tr>
                        <td class="label">
                            <label class="portlet-form-label" for="entityType"><spring:message code="type"/>:</label>
                        </td>
                        <td>
                            <select id="entityType" name="entityType">
                                <option>[<spring:message code="select.type"/>]</option>
                                <c:forEach items="${supportedTypes}" var="type">
                                    <option value="${fn:escapeXml(type.typeId)}"><spring:message code="${type.titleCode}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <label class="portlet-form-label" for="sysid"><spring:message code="id"/>:</label>
                        </td>
                        <td>
                            <input type="text" id="sysid" name="sysid"/>
                        </td>
                    </tr>
                </table>
                <div class="buttons">
                    <input class="button primary" type="submit" value="删除"/>
                </div>
            </form>
        </div>
    
    </div> <!-- end: portlet-content -->
</div> <!-- end:portlet -->

<script type="text/javascript">
    up.jQuery(document).ready(function () {
        var $ = up.jQuery;
        
        $("#${n}form").submit(function () {
           var form, entityType, sysId, href;
           
           form = this;
           entityType = form.entityType.value;
           sysId = form.sysid.value;
           $.ajax({
               url: "<c:url value="/api/entity/"/>" + entityType + "/" + sysId,
               type: "DELETE"
           });
           
           return false;
        });
    });
</script>
