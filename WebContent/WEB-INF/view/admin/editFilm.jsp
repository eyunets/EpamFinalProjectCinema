<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>


<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages" var="i18n"/>

<c:if test="${not empty errorMsg and not (errorMsg eq '')}">
    <div class="alert alert-warning alert-dismissible fade show" role="alert">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <i class="fa fa-times-circle-o"></i>
        </button>
        <fmt:message bundle="${i18n}" key="data.invalid-rerty"/>
    </div>
</c:if>

<div class="modal-dialog ">
    <div class="modal-content">
        <div class="modal-body">
            <div id="myTabContent" class="tab-content">
                <div role="tabpanel" class="tab-pane fade in active show" id="editfilm">
                    <p><b><fmt:message bundle="${i18n}" key="editFilm.title"/></b></p>
                    <form class="form-horizontal" action="frontController?command=editFilm&id=${film.id}"
                          method="post"
                          data-toggle="validator">
                        <fieldset>
                            <!-- Edit Film Form -->
                           		<!-- name-->
							<div class="form-group">
								<label class="control-label" for="name"> <fmt:message
										bundle="${i18n}" key="film.name" />:
								</label>
								<div class="controls">
									<input id="name" name="name" class="form-control input-large"
										data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
										data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
										required pattern="^.{1,29}$">
								</div>
								<small class=" form-text text-muted help-block with-errors">
									<fmt:message bundle='${i18n}' key='data.less-30' />
								</small>
							</div>


							<!--genre-->
							<div class="form-group">
								<label class="control-label" for="genre"> <fmt:message
										bundle="${i18n}" key="catalog.genre" />:
								</label>
								<div class="controls">
									<input id="genre" class="form-control input-large" name="genre"
										data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
										data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
										required pattern="^.{1,30}$">
								</div>
								<small class=" form-text text-muted help-block with-errors">

								</small>
							</div>

							<!--year-->
							<div class="form-group">
								<label class="control-label" for="year"> <fmt:message
										bundle="${i18n}" key="catalog.year" />:
								</label>
								<div class="controls">
									<input id="year" class="form-control input-large" name="year"
										type="date"
										data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
										data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
										required pattern="^[0-9]{2}.[0-9]{2}.[0-9]{4}$">
								</div>
								<small class=" form-text text-muted help-block with-errors">
								</small>
							</div>

							<!--description-->
							<div class="form-group">
								<label class="control-label" for="description"> <fmt:message
										bundle="${i18n}" key="catalog.description" />:
								</label>
								<div class="controls">
									<textarea id="description" class="form-control" rows="3"
										name="description"
										data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
										maxlength="250"></textarea>
								</div>
								<small class=" form-text text-muted help-block with-errors">

								</small>
							</div>

							<!-- price -->
							<div class="form-group">
								<label class="control-label" for="price"> <fmt:message
										bundle="${i18n}" key="film.price" />:
								</label>
								<div class="controls">
									<input id="price" name="price" class="form-control input-large"
										data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
										data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
										type="number" min="0">
								</div>
								<small class=" form-text text-muted help-block with-errors">
									<fmt:message bundle='${i18n}' key='data.less-30' />
								</small>
							</div>

							<!-- picture -->
							<div class="form-group">
								<label class="control-label"><fmt:message
										bundle="${i18n}" key="catalog.picture" />: </label>
								<div class="controls">
									<input type="file" name="picture" size="10"
										accept=".gif,.jpg,.jpeg,.png, image/png, image/gif, image/jpg, image/jpeg">
								</div>
							</div>


							<!--button-->
							<div class="form-group">
								<label class="control-label"></label>
								<div class="controls">
									<input type="submit" class="btn btn-success"
										value="<fmt:message bundle="${i18n}" key="addfilm.title"/>" />
									<a
										href="${pageContext.request.contextPath}/frontController?command=main"
										class="btn btn-secondary"> <fmt:message bundle='${i18n}'
											key="login.close" />
									</a>
								</div>
							</div>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>


<%--<!-- Modal -->--%>
<%--<div class="modal fade bs-modal-sm" id="Modal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">--%>
<%--<div class="modal-dialog ">--%>
<%--<div class="modal-content">--%>
<%--<div class="modal-body">--%>
<%--<div id="TabContent" class="tab-content">--%>
<%--<div role="tabpanel" class="tab-pane fade in active show" id="addauthor">--%>
<%--<p><b>Добавление книги</b></p>--%>
<%--<form class="form-horizontal" action="frontController?command=addauthor" method="post"--%>
<%--data-toggle="validator">--%>
<%--<fieldset>--%>
<%--<!-- Add Author Form -->--%>
<%--<!-- surname-->--%>
<%--<div class="form-group">--%>
<%--<label class="control-label" for="surname">--%>
<%--<fmt:message bundle="${i18n}" key="login.surname"/>:--%>
<%--</label>--%>
<%--<div class="controls">--%>
<%--<input id="surname" class="form-control input-large" name="surname"--%>
<%--data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"--%>
<%--data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"--%>
<%--required pattern="^[А-ЯЁ]([a-яё]){0,29}$">--%>
<%--</div>--%>
<%--<small class=" form-text text-muted help-block with-errors">--%>
<%--<fmt:message bundle='${i18n}' key='data.less-30'/>--%>
<%--</small>--%>
<%--</div>--%>

<%--<!-- name-->--%>
<%--<div class="form-group">--%>
<%--<label class="control-label" for="authorname"><fmt:message bundle="${i18n}"--%>
<%--key="login.name"/>:</label>--%>
<%--<div class="controls">--%>
<%--<input id="authorname" class="form-control input-large" name="name"--%>
<%--data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"--%>
<%--data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"--%>
<%--required pattern="^[А-ЯЁ][a-яё]{0,29}$">--%>
<%--</div>--%>
<%--<small class=" form-text text-muted help-block with-errors">--%>
<%--<fmt:message bundle='${i18n}' key='data.less-30'/>--%>
<%--</small>--%>
<%--</div>--%>

<%--<!-- second name-->--%>
<%--<div class="form-group">--%>
<%--<label class="control-label" for="secondname"><fmt:message bundle="${i18n}"--%>
<%--key="login.secondname"/>:</label>--%>
<%--<div class="controls">--%>
<%--<input id="secondname" class="form-control input-large" name="secondname"--%>
<%--data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"--%>
<%--data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"--%>
<%--required pattern="^[А-ЯЁ][a-яё]{0,29}$">--%>
<%--</div>--%>
<%--<small class=" form-text text-muted help-block with-errors">--%>
<%--<fmt:message bundle='${i18n}' key='data.less-30'/>--%>
<%--</small>--%>
<%--</div>--%>


<%--<!-- birthday -->--%>
<%--<div class="form-group">--%>
<%--<label class="control-label" for="birthday"><fmt:message bundle="${i18n}"--%>
<%--key="login.birthday"/>:</label>--%>
<%--<div class="controls">--%>
<%--<input id="birthday" class="form-control input-large" name="birthday"--%>
<%--type="Date" max="2000-01-01" value="1999-05-09"--%>
<%--data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"--%>
<%--required>--%>
<%--</div>--%>
<%--<small class=" form-text text-muted help-block with-errors">--%>
<%--</small>--%>
<%--</div>--%>

<%--<!-- country-->--%>
<%--<div class="form-group">--%>
<%--<label class="control-label" for="country">--%>
<%--<fmt:message bundle="${i18n}" key="author.country"/>:--%>
<%--</label>--%>
<%--<div class="controls">--%>
<%--<input id="country" name="country" class="form-control input-medium"--%>
<%--data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"--%>
<%--data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"--%>
<%--required pattern="^[А-ЯЁ][a-яё]{0,29}$">--%>
<%--</div>--%>
<%--<small class=" form-text text-muted help-block with-errors">--%>
<%--<fmt:message bundle='${i18n}' key='data.less-30'/>--%>
<%--</small>--%>
<%--</div>--%>

<%--<!-- submit -->--%>
<%--<div class="form-group">--%>
<%--<label class="control-label"></label>--%>
<%--<div class="controls">--%>
<%--<input type="submit" class="btn btn-success"--%>
<%--value="<fmt:message bundle='${i18n}' key='author.add'/>"/>--%>
<%--</div>--%>
<%--</div>--%>
<%--</fieldset>--%>
<%--</form>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--<div class="modal-footer">--%>
<%--<button type="button" class="btn btn-default" data-dismiss="modal">--%>
<%--<fmt:message bundle="${i18n}" key="login.close"/>--%>
<%--</button>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>

