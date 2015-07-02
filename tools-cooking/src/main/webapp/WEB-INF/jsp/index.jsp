<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>cooking</title>
</head>
<body>
<h2>Hello <sec:authentication property="name"/>!</h2>

<br/>
<h3>${message}</h3>

<a href ="${pageContext.request.contextPath}/auth/login">login</a><br/>
<a href ="${pageContext.request.contextPath}/main/dashboard">dashboard</a><br/>
<a href ="${pageContext.request.contextPath}/main/admin">admin</a><br/>

<br/>
<a href ="${pageContext.request.contextPath}/auth/logout">logout</a><br/>

</body>
</html>
