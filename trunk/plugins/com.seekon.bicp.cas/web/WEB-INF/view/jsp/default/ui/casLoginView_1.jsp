<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />

 
  <div class="box fl-panel" id="login" style="padding: 0px; background: background: url('/cas/images/loginBg.jpg') repeat-x scroll 0 -1px transparent;">
	<form:form method="post" id="fm1" cssClass="fm-v clearfix" commandName="${commandName}" htmlEscape="true"
		style="margin-top: 0px; margin-right: 0px; margin-bottom: 0px;">
      <table class="mainTableborder" width="100%" height="100%" cellspacing="0" cellpadding="0" 
      		align="center" border="0" style="font-size:12px; margin-right: 0px; margin-bottom: 0px; margin-top: 0px;">
			<tr>
				<td>
					<table style="background: url('/cas/images/login.jpg') no-repeat;" width="1000" height="600" border="0" align="center" cellpadding="0" valign="middle" 
						cellspacing="0">
						<tr>
							<td colspan="3" height="205" valign="top">&nbsp;</td>
						</tr>
						<tr>
							<td width="480">&nbsp;</td>
							<td width="265" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="3">
									<tr>
										<td style="color:white;height:18px">账&nbsp;&nbsp;&nbsp;&nbsp;户：&nbsp;
											<form:input cssClass="required" cssErrorClass="error" id="username" size="18" maxlength="20" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="false" htmlEscape="true" />
										</td>
									</tr>
									<tr><td style="line-height: 6px;">&nbsp;</td></tr>
									<tr>
										<td style="color:white;height:18px">密&nbsp;&nbsp;&nbsp;&nbsp;码：&nbsp;
											<form:password cssClass="required" cssErrorClass="error" id="password" size="18" maxlength="20" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off"/>
										</td>
									</tr>
									<tr>
										<td height="50" align="left" valign="middle">&nbsp;&nbsp;
											<input class="btn-submit" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="4" type="submit" />
            								<input class="btn-reset" name="reset" accesskey="c" value="<spring:message code="screen.welcome.button.clear" />" tabindex="5" type="reset" />										</td>
									</tr>
								</table>
							</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>
			</table>            
			<input type="hidden" name="lt" value="${loginTicket}" />
			<input type="hidden" name="execution" value="${flowExecutionKey}" />
			<input type="hidden" name="_eventId" value="submit" />

    </form:form>
  </div>
            
<jsp:directive.include file="includes/bottom.jsp" />
