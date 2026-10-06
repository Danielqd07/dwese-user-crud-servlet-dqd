<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="../../partials/header.jsp" %>

<h1>
    <c:choose>
        <c:when test="${user != null}">
            <fmt:message key="msg.user-form.edit"/>
        </c:when>
        <c:otherwise>
            <fmt:message key="msg.user-form.add"/>
        </c:otherwise>
    </c:choose>
</h1>

<c:if test="${errors != null}">
    <ul>
        <c:forEach var="error" items="${errors}">
            <li>${error}</li>
        </c:forEach>
    </ul>
</c:if>

<form action="${pageContext.request.contextPath}/users" method="post">

    <input type="hidden"
           name="action"
           value="${user != null ? 'update' : 'insert'}">

    <c:if test="${user != null}">
        <input type="hidden"
               name="id"
               value="${user.id}">
    </c:if>

    <label for="username">
        <fmt:message key="msg.user-form.username"/>
    </label>

    <input type="text"
           name="username"
           id="username"
           maxlength="40"
           required
           value="${user != null ? user.username : ''}">

    <br><br>

    <label for="passwordHash">
        <fmt:message key="msg.user-form.passwordHash"/>
    </label>

    <input type="text"
           name="passwordHash"
           id="passwordHash"
           maxlength="500"
           required
           value="${user != null ? user.passwordHash : ''}">

    <br><br>

    <label for="active">
        <fmt:message key="msg.user-form.active"/>
    </label>

    <input type="checkbox"
           name="active"
           id="active"
           value="true"
           <c:if test="${user ne null and user.active}">
               checked="checked"
           </c:if>>

    <br><br>

    <label for="accountNonLocked">
        <fmt:message key="msg.user-form.accountNonLocked"/>
    </label>

    <input type="checkbox"
           name="accountNonLocked"
           id="accountNonLocked"
           value="true"
           <c:if test="${user ne null and user.accountNonLocked}">
               checked="checked"
           </c:if>>

    <br><br>

    <label for="lastPasswordChange">
        <fmt:message key="msg.user-form.lastPasswordChange"/>
    </label>

    <input type="datetime-local"
           name="lastPasswordChange"
           id="lastPasswordChange"
           required
           value="${user != null && user.lastPasswordChange != null
                   ? user.lastPasswordChange
                   : ''}">

    <br><br>

    <label for="failedLoginAttempts">
        <fmt:message key="msg.user-form.failedLoginAttempts"/>
    </label>

    <input type="number"
           name="failedLoginAttempts"
           id="failedLoginAttempts"
           min="0"
           required
           value="${user != null ? user.failedLoginAttempts : '0'}">

    <br><br>

    <label for="emailVerified">
        <fmt:message key="msg.user-form.emailVerified"/>
    </label>

    <input type="checkbox"
           name="emailVerified"
           id="emailVerified"
           value="true"
           <c:if test="${user ne null and user.emailVerified}">
               checked="checked"
           </c:if>>

    <br><br>

    <label for="mustChangePassword">
        <fmt:message key="msg.user-form.mustChangePassword"/>
    </label>

    <input type="checkbox"
           name="mustChangePassword"
           id="mustChangePassword"
           value="true"
           <c:if test="${user ne null and user.mustChangePassword}">
               checked="checked"
           </c:if>>

    <br><br>

    <button type="submit">
        <c:choose>
            <c:when test="${user != null}">
                <fmt:message key="msg.user-form.update"/>
            </c:when>
            <c:otherwise>
                <fmt:message key="msg.user-form.create"/>
            </c:otherwise>
        </c:choose>
    </button>

</form>

<br>

<a href="${pageContext.request.contextPath}/users">
    <fmt:message key="msg.user-form.returnback"/>
</a>

<%@ include file="../../partials/footer.jsp" %>