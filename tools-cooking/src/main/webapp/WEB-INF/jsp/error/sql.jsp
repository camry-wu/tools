<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%--
 * ===========================================================
 * file name  : error/sql.jsp
 * authors    : camry(camry_camry@sina.com)
 * created    : Thu 11 Jun 2015 02:13:21 PM CST
 * @version   : 1.0
 *
 * exception page
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
<title>sql error</title>
</head>

<body>
<% Exception ex = (Exception) request.getAttribute("exception"); %>
<h3><%= ex.getMessage(); %></h3>
<% ex.printStackTrace(new java.io.PrintWriter(out)); %>
<br/>
<a href ="${pageContext.request.contextPath}/index.html">index</a>
</body>
</html>
