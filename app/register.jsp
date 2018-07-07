<%@ page language="java" pageEncoding="utf-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
		<title>注册</title>  
	</head> 

	<body bgcolor="white">
		<h3>注册</h3>  
		<hr>  
		<form action="RegisterServlet" method="post">  
			用户名：
			<input type="text"     name="username"/><br>  
			密&emsp;码：
			<input type="password" name="password"/><br><br>    
			<input type="submit" value="注册"/>  
		</form>  
	</body>

</html>
