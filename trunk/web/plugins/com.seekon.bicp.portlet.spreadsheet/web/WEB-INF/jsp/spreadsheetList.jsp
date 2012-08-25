<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.security.Principal" %>
<%@ page import="org.jasig.cas.client.validation.Assertion" %>
<%@ page import="org.jasig.cas.client.authentication.AttributePrincipal" %>
<%
	String locale = "zh_CN";
	String user = "";
	String pass = "";

	Assertion assertion = (Assertion)request.getSession().getAttribute("_const_cas_assertion_");
	if(assertion != null){
		AttributePrincipal principal = assertion.getPrincipal();
		if(principal != null){
			user = principal.getName();
			java.util.Map attributes = principal.getAttributes();
			if(attributes != null){
				pass = (String)attributes.get("password");
			}
		}
	}
%>
<div>
	<input type="button" name="new" value="新建" onClick="openNewSpreadSheetWindow();">
</div>
<script>
	function openNewSpreadSheetWindow(){		
		window.open("/spreadsheet/main.jsp?username=<%=user%>&password=<%=pass%>");		
	}
</script>