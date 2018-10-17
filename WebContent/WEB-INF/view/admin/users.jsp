<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<jsp:useBean id="now" class="java.util.Date" scope="page"/>

<fmt:formatDate value='${now}' pattern='yyyy-MM-dd' var="searchFormated"/>
<c:set var="strDate" value="${searchFormated}"/>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages" var="i18n"/>

<script>
    var ban = '<fmt:message bundle="${i18n}" key="user.ban"></fmt:message>';
    var unban = '<fmt:message bundle="${i18n}" key="user.unban"></fmt:message>';
</script>

<div class="container">
    <div class="card-columns">
        <c:forEach var="user" items="${users}">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title">${user.surname} ${user.name}</h4>
                </div>
               
                <div class="card-body">
                    <c:choose>
                        <c:when test="${user.type eq 'banned'}">
                            <button id="${user.id}" class="btn btn-success ban-user">
                                <fmt:message bundle="${i18n}" key="user.unban"/>
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button id="${user.id}" class="btn btn-danger ban-user">
                                <fmt:message bundle="${i18n}" key="user.ban"/>
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:forEach>
    </div>
</div>