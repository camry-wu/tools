<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%--
 * ===========================================================
 * file name  : jsp/denied.jsp
 * authors    : camry(camry_camry@sina.com)
 * created    : Thu 11 Jun 2015 02:13:21 PM CST
 * @version   : 1.0
 *
 * denied page
 *
 * @param message
 *
 * modifications:
 *
 * ===========================================================
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>cooking denied page</title>
</head>

<body>
<h2>Denny!</h2>
<h3>${message}</h3>
<h4>*<sec:authentication property="name"/>*<h4>

<a href ="${pageContext.request.contextPath}/index.html">index</a>
<br/>
<br/>
<form:form action="${pageContext.request.contextPath}/logout" method="post">
<input type="submit" value="Logout"/>
</form:form>
</body>
</html>
