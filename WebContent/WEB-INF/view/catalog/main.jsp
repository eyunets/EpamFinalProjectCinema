<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html;charset=UTF-8"%>


<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" var="i18n" />
<div class="container">
	<c:if test="${not empty Msg and not (Msg eq '')}">
		<c:choose>
			<c:when test="${Msg eq 'Invalid input'}">
				<div class="text-center">
					<h1 class="display-3">
						<fmt:message bundle="${i18n}" key="data.sorry" />
					</h1>
					<h1 class="display-4">
						<fmt:message bundle="${i18n}" key="data.non-valid" />
					</h1>
				</div>
			</c:when>
			<c:otherwise>
				<div class="text-center">
					<h1 class="display-3">
						<fmt:message bundle="${i18n}" key="data.sorry" />
					</h1>
					<h1 class="display-4">
						<fmt:message bundle="${i18n}" key="data.no-such-films" />
					</h1>
				</div>
			</c:otherwise>
		</c:choose>
	</c:if>
	<div class="card-columns">

		<c:forEach var="film" items="${films}">
			<div class="card">
				<div class="card-body">
					<h4 class="card-title">${film.name}</h4>
					<p class="card-text">
						<b> <fmt:message bundle="${i18n}" key="catalog.genre" />:
						</b> ${film.genre}
					</p>
				</div>
				<ul class="list-group list-group-flush">
					<li class="list-group-item"><b><fmt:message
								bundle="${i18n}" key="catalog.year" />: </b> ${film.year}</li>
					<li class="list-group-item"><b><fmt:message
								bundle="${i18n}" key="catalog.price"></fmt:message>: </b>
						${film.price}</li>
					<li class="list-group-item"><b><fmt:message
								bundle="${i18n}" key="catalog.rating"></fmt:message>: </b>
						${film.rating}</li>
				</ul>
				<c:choose>
					<c:when test="${not empty sadmin}">
						<div class="card-body">
							<a
								href="${pageContext.request.contextPath}/frontController?command=film&id=${film.id}"
								class="btn btn-primary"> <fmt:message bundle="${i18n}"
									key="catalog.more" />
							</a> <a
								href="${pageContext.request.contextPath}/frontController?command=editfilm&id=${film.id}"
								class="btn btn-primary"> <fmt:message bundle="${i18n}"
									key="cabinet.edit" />
							</a> <a
								href="${pageContext.request.contextPath}/frontController?command=deletefilm&id=${film.id}"
								class="btn btn-danger"> <fmt:message bundle="${i18n}"
									key="film.delete" />
							</a>
						</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${(not empty suser) and (suser.type eq 'banned')}">
								<div class="card-body">
									<a href="#" class="btn btn-secondary"> <fmt:message
											bundle="${i18n}" key="user.banned" />
									</a>
								</div>
							</c:when>
							<c:otherwise>
								<div class="card-body">
									<a
										href="${pageContext.request.contextPath}/frontController?command=film&id=${film.id}"
										class="btn btn-primary"> <fmt:message bundle="${i18n}"
											key="catalog.reserve" />
									</a>
								</div>
							</c:otherwise>
						</c:choose>

					</c:otherwise>
				</c:choose>
			</div>
		</c:forEach>
	</div>
	<c:if test="${pageNum> 1}">
		<div class="text-xs-center">
			<nav aria-label="Page navigation">
				<ul class="pagination justify-content-center">
					<c:forEach begin="1" end="${totalPageAmount}" step="1"
						var="curPage">
						<c:choose>
							<c:when test="${curPage eq pageNum}">
								<li class="page-item active"><a class="page-link"
									href="<c:url value="/frontController?command=catalog&page=${curPage}" />">${curPage}</a></li>
							</c:when>
							<c:otherwise>
								<li class="page-item"><a class="page-link"
									href="<c:url value="/frontController?command=catalog&page=${curPage}" />">${curPage}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>
			</nav>
		</div>
	</c:if>
</div>