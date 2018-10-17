<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>


<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages" var="i18n"/>

<div class="container text-center">
    <div class="page-header center-block">
        <h1>
            <small><fmt:message bundle="${i18n}" key="error.page_under_construction"/></small>
        </h1>
    </div>
    <img src="assests/img/build.gif" class="rounded w-50">
</div>

