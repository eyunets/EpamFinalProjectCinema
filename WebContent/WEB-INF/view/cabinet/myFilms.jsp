<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>


<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages" var="i18n"/>
<script>
    var returned = '<fmt:message bundle="${i18n}" key="film.returned"/>';
</script>
<div class="container">
    <div class="card-columns">

        <c:forEach var="film" items="${films}">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title">${film.name}</h4>
                    <p class="card-text">
                        <b>
                            <fmt:message bundle="${i18n}" key="catalog.genre"/>:
                        </b>
                            ${film.genre}
                    </p>
                </div>
            </div>
        </c:forEach>
    </div>
</div>