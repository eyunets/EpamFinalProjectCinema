<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages" var="i18n"/>

<div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
    <ol class="carousel-indicators">
        <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
    </ol>
    <div class="carousel-inner">
        <div class="carousel-item active">
            <div class="carousel-caption d-none d-md-block">
                <h1 class="display-1"><fmt:message bundle="${i18n}" key="menu.name"/></h1>
                <h3 class="display-3"><fmt:message bundle="${i18n}" key="menu.welcome"/></h3>
            </div>
            <img class="d-block w-100" src="${pageContext.request.contextPath}/assests/img/cinema1.jpg"
                 alt="First slide">
        </div>
        <div class="carousel-item">
            <div class="carousel-caption d-none d-md-block">
                <h1 class="display-1"><fmt:message bundle="${i18n}" key="menu.name"/></h1>
                <h3 class="display-3"><fmt:message bundle="${i18n}" key="menu.welcome"/></h3>
            </div>
            <img class="d-block w-100" src="${pageContext.request.contextPath}/assests/img/cinema2.jpg"
                 alt="Second slide">
        </div>
        <div class="carousel-item">
            <div class="carousel-caption d-none d-md-block">
                <h1 class="display-1"><fmt:message bundle="${i18n}" key="menu.name"/></h1>
                <h3 class="display-3"><fmt:message bundle="${i18n}" key="menu.welcome"/></h3>
            </div>
            <img class="d-block w-100" src="${pageContext.request.contextPath}/assests/img/cinema3.jpg"
                 alt="Third slide">
        </div>
    </div>
    <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
    </a>
    <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
    </a>
</div>
