<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${empty sessionScope.locale ? 'es' : sessionScope.locale.language}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="msg.title"/></title>
</head>
<body>

<form action="${pageContext.request.contextPath}/language" method="get">

    <select name="lang" onchange="this.form.submit()">

        <option value="es"
                ${empty sessionScope.locale || sessionScope.locale.language == 'es'
                        ? 'selected' : ''}>
            Español
        </option>

        <option value="en"
                ${sessionScope.locale.language == 'en'
                        ? 'selected' : ''}>
            English
        </option>

    </select>

</form>

<hr>

<nav>
    <a href="${pageContext.request.contextPath}/users">
        <fmt:message key="msg.menu.users"/>
    </a>
</nav>

<hr>