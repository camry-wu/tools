<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>login cooking</title>
</head>
<body>
<h2>Hello Cooking!</h2>

<br/>
<h3>Login</h3>
<h4>${message}</h4>
<%
	//org.springframework.security.authentication.encoding.Md5PasswordEncoder enc = new org.springframework.security.authentication.encoding.Md5PasswordEncoder();
	//System.out.println(enc.encodePassword("123", null));
%>

<c:url var="loginUrl" value="/auth/login"/>
<form:form action="${loginUrl}" method="POST" commandName="loginUser">
	Username: <form:input path="username"/><br/>
	Password: <form:input path="password"/><br/>
	<input type="submit" value="Login"/>
</form:form>

</body>
</html>
