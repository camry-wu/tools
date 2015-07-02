<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--
<%@ page isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>

<%@ taglib uri="/WEB-INF/framework-html.tld" prefix="ihtml" %>
<%@ taglib uri="/WEB-INF/framework-nested.tld" prefix="inested" %>
--%>

<%--
 * ===========================================================
 * file name  : main/dashboard.jsp
 * authors    : camry(camry_camry@sina.com)
 * created    : Thu 11 Jun 2015 02:13:21 PM CST
 * @version   : 1.0
 *
 * dashboard page
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
<title>cooking dashboard page</title>
</head>

<body>
<h2>Dashboard</h2>
<h3>${message}</h3>

<a href ="${pageContext.request.contextPath}/index">index</a>
</body>
</html>
