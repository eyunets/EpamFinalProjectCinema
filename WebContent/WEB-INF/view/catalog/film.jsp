<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" var="i18n" />

<c:if test="${not empty errorMsg and not (errorMsg eq '')}">
	<div class="alert alert-warning alert-dismissible fade show"
		role="alert">
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<i class="fa fa-times-circle-o"></i>
		</button>
		<c:choose>
			<c:when test="${errorMsg eq 'Invalid Login or Password'}">
				<fmt:message bundle="${i18n}" key="data.lp" />
			</c:when>
			<c:otherwise>
				<fmt:message bundle="${i18n}" key="data.invalid-rerty" />
			</c:otherwise>
		</c:choose>

	</div>
</c:if>
<script>
	var reserved = '<fmt:message bundle="${i18n}" key="film.reserved"/>';
</script>
<div class="col-sm-12">
	<div class="card">
		<div class="card-header p-b-0">
			<h5 class="card-title">
				<i class="fa fa-film"></i> ${film.name}
			</h5>
		</div>

		<ul class="list-group list-group-flush">
			<li class="list-group-item"><b><fmt:message bundle="${i18n}"
						key="catalog.genre" />: </b> ${film.genre}</li>

			<li class="list-group-item"><b><fmt:message bundle="${i18n}"
						key="catalog.year" />: </b> ${film.year}</li>

			<li class="list-group-item"><b><fmt:message bundle="${i18n}"
						key="catalog.description" />: </b> <span id="quantity">${film.description}</span>
			</li>

			<li class="list-group-item"><b><fmt:message bundle="${i18n}"
						key="catalog.price"></fmt:message>: </b> ${film.price}</li>
			<li class="list-group-item"><b><fmt:message bundle="${i18n}"
						key="catalog.rating"></fmt:message>: </b> ${film.rating}</li>

		</ul>
		<!-- logic -->
		<c:set var="flag" value="false" />
		<c:if test="${not empty suser}">
			<c:forEach var="user" items="${users}">
				<c:if test="${user.id eq suser.id}">
					<c:set var="flag" value="true" />
				</c:if>
			</c:forEach>
		</c:if>

		<c:set var="flagReview" value="false" />
		<c:if test="${not empty suser}">
			<c:forEach var="review" items="${reviews}">
				<c:if test="${review.userId eq suser.id}">
					<c:set var="flagReview" value="true" />
				</c:if>
			</c:forEach>
		</c:if>
		<!-- logic -->

		<c:choose>
			<c:when test="${not empty sadmin}">
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${flag eq \"true\"}">
						<button class="btn disabled">
							<fmt:message bundle="${i18n}" key="film.reserved" />
						</button>
						<c:choose>
							<c:when test="${flagReview eq \"false\"}">
								<a class="btn btn-primary"
									href="${pageContext.request.contextPath}/frontController?command=addreview&id=${film.id}">
									<fmt:message bundle="${i18n}" key="review.add" />
								</a>
							</c:when>
						</c:choose>
					</c:when>
					<c:otherwise>
						<form class="card-body"
							action="frontController?command=reserveFilm&id=${film.id}"
							method="post">
							<button class="btn btn-primary" id="${film.id}">
								<fmt:message bundle="${i18n}" key="film.reserve" />
							</button>
						</form>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</div>
	<br>
	<c:forEach items="${reviews}" var="review" varStatus="status">
		<c:set var="authorLogin"
			value="${requestScope.reviewLogins[status.index]}" />

		<c:choose>
			<c:when test="${review.mark gt 2}">
				<c:set var="rColor" value="#ccffcc" />
			</c:when>
			<c:otherwise>
				<c:set var="rColor" value="#e6e6ff" />
			</c:otherwise>
		</c:choose>
		<div class="card">
			<div class="card-header" style="background-color:${rColor}">
				<h7 class="card-title">

					<fmt:message bundle="${i18n}" key="user.review" />
					${authorLogin}
				</h7>
			</div>
			<div class="card-body">
				<p class="card-text">${review.text}</p>
			</div>
			<div class="card-footer" style="background-color:${rColor}">
				<form action="frontController?command=deleteReview&id=${review.id}"
					method="post">
					<p class="card-text">
						<fmt:message bundle="${i18n}" key="review.mark" />
						:${review.mark}/5 ${date}:${review.date}

						<c:choose>
							<c:when test="${not empty suser}">
								<c:if test="${review.userId eq suser.id}">
									<input type="submit" class="btn btn-danger"
										value="<fmt:message bundle="${i18n}" key="review.delete"/>" />
								</c:if>
							</c:when>
							<c:when test="${not empty sadmin}">
								<input type="submit" class="btn btn-danger"
									value="<fmt:message bundle="${i18n}" key="review.delete"/>" />
							</c:when>
						</c:choose>
					</p>
				</form>
			</div>
		</div>
	</c:forEach>
</div>
