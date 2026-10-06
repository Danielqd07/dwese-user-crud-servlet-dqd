<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="../../partials/header.jsp" %>

<h1><fmt:message key="msg.user-list.title"/></h1>

<a href="${pageContext.request.contextPath}/users?action=new">
    <fmt:message key="msg.user-list.add"/>
</a>

<br><br>

<table border="1">

    <thead>
    <tr>
        <th><fmt:message key="msg.user-list.id"/></th>
        <th><fmt:message key="msg.user-list.username"/></th>
        <th><fmt:message key="msg.user-list.active"/></th>
        <th><fmt:message key="msg.user-list.accountNonLocked"/></th>
        <th><fmt:message key="msg.user-list.lastPasswordChange"/></th>
        <th><fmt:message key="msg.user-list.passwordExpiresAt"/></th>
        <th><fmt:message key="msg.user-list.failedLoginAttempts"/></th>
        <th><fmt:message key="msg.user-list.emailVerified"/></th>
        <th><fmt:message key="msg.user-list.mustChangePassword"/></th>
        <th><fmt:message key="msg.user-list.actions"/></th>
    </tr>
    </thead>

    <tbody>

    <c:forEach var="user" items="${listUsers}">

        <tr>

            <td>${user.id}</td>

            <td>${user.username}</td>

            <td>${user.active}</td>

            <td>${user.accountNonLocked}</td>

            <td>${user.lastPasswordChange}</td>

            <td>${user.passwordExpiresAt}</td>

            <td>${user.failedLoginAttempts}</td>

            <td>${user.emailVerified}</td>

            <td>${user.mustChangePassword}</td>

            <td>

                <a href="${pageContext.request.contextPath}/users?action=edit&id=${user.id}">
                    <fmt:message key="msg.user-list.edit"/>
                </a>

                <form action="${pageContext.request.contextPath}/users"
                      method="post"
                      style="display:inline;"
                      onsubmit="return confirm('<fmt:message key="msg.user-list.confirm"/>');">

                    <input type="hidden"
                           name="action"
                           value="delete">

                    <input type="hidden"
                           name="id"
                           value="${user.id}">

                    <button type="submit">
                        <fmt:message key="msg.user-list.delete"/>
                    </button>

                </form>

            </td>

        </tr>

    </c:forEach>

    </tbody>

</table>

<%@ include file="../../partials/footer.jsp" %>