<%@ page language="java" pageEncoding="utf-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
		<title>绑定</title>  
	</head> 

	<body bgcolor="white">
		<h3>绑定</h3>  
		<hr>  
		<form action="BindServlet" method="post">  
			SN：&emsp;&emsp;&emsp;
			<input type="text" name="sn"/><br>  
			萌宠昵称：
			<input type="text" name="name"/><br> 
			主人电话：
			<input type="text" name="mobile"/><br>
			地址：&emsp;&emsp;
			<input type="text" name="address"/><br>		
			<br>    
			<input type="submit" value="绑定"/>  
		</form>  
	</body>

</html>
