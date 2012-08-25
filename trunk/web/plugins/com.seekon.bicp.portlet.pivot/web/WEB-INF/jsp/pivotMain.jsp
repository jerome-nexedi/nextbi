<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="org.springframework.security.core.context.SecurityContext" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.core.userdetails.UserDetails" %>
<%@ page import="java.security.Principal" %>
<%@ page import="org.jasig.cas.client.validation.Assertion" %>
<%@ page import="org.jasig.cas.client.authentication.AttributePrincipal" %>
<%
	String locale = "zh_CN";
	String user = "";
	String pass = "";
	/*
	Object context = request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
	if(context instanceof SecurityContext){
		Authentication auth = ((SecurityContext)context).getAuthentication();
		Object principal = auth.getPrincipal();
		if(principal instanceof UserDetails){
			UserDetails userInfo = (UserDetails)principal;
			user = userInfo.getUsername();
			pass = userInfo.getPassword();
		}
	}*/
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
	<input type="button" name="open" value="打开" onClick="openPivotMainWindow();">
</div>
<script>
	function openPivotMainWindow(){
		var url = '/pivot/com.tensegrity.wpalo.Palo_Pivot/Palo_Pivot.html?locale=<%=locale%>' 
			+ '&options=(user="<%=user%>",pass="<%=pass%>")';		
		window.open(url);		
	}
</script>