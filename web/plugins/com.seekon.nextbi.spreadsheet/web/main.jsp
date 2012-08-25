<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
%>
<script type="text/javascript" src="/spreadsheet/root/cc/login.php?stub=Login&client=Util,Main,Request,HttpClient,Dispatcher,Behavior,Loading,JSON,iframe,orderedQueue"></script>
	<script type="text/javascript">

		HTML_AJAX.defaultServerUrl = '/spreadsheet/root/cc/login.php';

		function login ()
		{

			var login = new Login(),
					res = login.log_in('<%=username%>', '<%=password%>');

			if (res[0] !== true)
			{
				alert(res[1]);
				userField.focus();
				return;
			}

			//var app = document.cookie.match(/(^|; ?)app=([^;]+)/),
			//		url = '/spreadsheet/root/ui/'.concat(app ? unescape(app[2]) : 'studio/index.php', window.location.search);
			url = '/spreadsheet/root/ui/app/main.php';
//			if (document.getElementById('newwin').checked)
//				window.open(url, 'app_win', 'directories=no,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=no');
//			else
				window.location.href = url;
		}
	</script>
	</head>
	<body onload="login();">
	</body>
	</html>