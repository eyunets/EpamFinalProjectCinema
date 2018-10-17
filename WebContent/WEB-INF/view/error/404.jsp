<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>


<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages" var="i18n"/>
<div class="container text-center">
    <div class="page-header center-block">
        <h1>404
            <small><fmt:message bundle="${i18n}" key="error.page_not_found"/></small>
        </h1>
    </div>
    <img src="assests/img/404.png" class="rounded w-25">
</div>