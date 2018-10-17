<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages" var="i18n"/>

<!-- Button trigger modal -->
<c:if test="${not empty errorMsg and not (errorMsg eq '')}">
    <div class="alert alert-warning alert-dismissible fade show" role="alert">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <i class="fa fa-times-circle-o"></i>
        </button>
        <c:choose>
            <c:when test="${errorMsg eq 'Invalid Login or Password'}">
                <fmt:message bundle="${i18n}" key="data.lp"/>
            </c:when>
            <c:otherwise>
                <fmt:message bundle="${i18n}" key="data.invalid-rerty"/>
            </c:otherwise>
        </c:choose>

    </div>
</c:if>

<div class="container">
    <hr class="prettyline">
    <br>
    <div class="text-center">
        <h3><fmt:message bundle="${i18n}" key="login.continue"/></h3>
        <br>
        <a class="btn btn-primary btn-lg" href="#signin" data-toggle="modal" data-target=".bs-modal-sm">
            <fmt:message bundle="${i18n}" key="login.signin"/>/<fmt:message bundle="${i18n}" key="login.signup"/>
        </a>
    </div>
    <br>
    <hr class="prettyline">
</div>


<!-- Modal -->
<div class="modal fade bs-modal-sm" id="myModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel"
     aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <br>
            <div class="bs-example bs-example-tabs">
                <ul class="nav nav-tabs" role="tablist">
                    <li class="nav-item">
                        <a class="nav-link active" href="#signin" role="tab" data-toggle="tab"><fmt:message
                                bundle="${i18n}" key="login.signin"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#signup" role="tab" data-toggle="tab"><fmt:message bundle="${i18n}"
                                                                                                     key="login.signup"/></a>
                    </li>
                </ul>
            </div>


            <div class="modal-body">
                <div id="myTabContent" class="tab-content">

                    <div role="tabpanel" class="tab-pane fade in active show" id="signin">
                        <form class="form-horizontal" action="frontController?command=login" method="post"
                              data-toggle="validator">
                            <fieldset>
                                <!-- Sign In Form -->
                                <!-- email-->
                                <div class="form-group">
                                    <label class="control-label" for="log"><fmt:message bundle="${i18n}"
                                                                                        key="login.email"/>:</label>
                                    <div class="controls">
                                        <input id="log" name="login" type="email" class="form-control input-medium"
                                               data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
                                               data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
                                               required>
                                        <small class=" form-text text-muted help-block with-errors"></small>
                                    </div>
                                </div>

                                <!--password-->
                                <div class="form-group">
                                    <label class="control-label" for="pass"><fmt:message bundle="${i18n}"
                                                                                         key="login.password"/>:</label>
                                    <div class="controls">
                                        <input id="pass" name="password"
                                               class="form-control input-medium" type="password" placeholder="********"
                                               data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
                                               required>
                                        <small class=" form-text text-muted help-block with-errors">
                                        </small>
                                    </div>
                                </div>

                                <!-- submit -->
                                <div class="form-group">
                                    <label class="control-label"></label>
                                    <div class="controls">
                                        <input type="submit" class="btn btn-success"
                                               value="<fmt:message bundle="${i18n}" key="login.signin"/>"/>
                                    </div>
                                </div>
                            </fieldset>
                        </form>
                    </div>


                    <div role="tabpanel" class="tab-pane fade" id="signup">
                        <form class="form-horizontal" action="frontController?command=signup" method="post"
                              data-toggle="validator" role="form">
                            <fieldset>
                                <!-- Sign Up Form -->
                                <!-- email-->
                                <div class="form-group">
                                    <label class="control-label" for="email"><fmt:message bundle="${i18n}"
                                                                                          key="login.email"/>:</label>
                                    <div class="controls">
                                        <input type="email" id="email" name="email" class="form-control input-large"
                                               placeholder="JohnDoe@example.com"
                                               data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
                                               data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
                                               required>
                                        <small class=" form-text text-muted help-block with-errors"></small>
                                    </div>
                                </div>

                                <!--password-->
                                <div class="form-group">
                                    <label class="control-label" for="password"><fmt:message bundle="${i18n}"
                                                                                             key="login.password"/>:</label>
                                    <div class="controls">
                                        <input id="password" name="password" class="form-control input-large"
                                               type="password" placeholder="********"
                                               data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
                                               data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
                                               required pattern=".{6,30}">
                                        <small class=" form-text text-muted help-block with-errors"><fmt:message
                                                bundle='${i18n}' key='data.6-30'/>
                                        </small>
                                    </div>
                                </div>

                                <!-- password2-->
                                <div class="form-group has-danger">
                                    <label class="control-label" for="reenterpassword"><fmt:message bundle="${i18n}"
                                                                                                    key="login.repassword"/>:</label>
                                    <div class="controls">
                                        <input id="reenterpassword" class="form-control input-large"
                                               data-match="#password"
                                               data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
                                               data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
                                               data-match-error="<fmt:message bundle='${i18n}' key='data.no-match'/>"
                                               name="reenterpassword" type="password" placeholder="********"
                                               required pattern=".{6,30}">
                                        <small class=" form-text text-muted help-block with-errors"></small>
                                    </div>

                                </div>

                                <br>
                                <!-- surname-->
                                <div class="form-group">
                                    <label class="control-label" for="surname">
                                        <fmt:message bundle="${i18n}" key="login.surname"/>:
                                    </label>
                                    <div class="controls">
                                        <input id="surname" class="form-control input-large" name="surname"
                                               data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
                                               data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
                                               required pattern="^[А-ЯЁ]([a-яё]){0,29}$">
                                    </div>
                                    <small class=" form-text text-muted help-block with-errors">
                                        <fmt:message bundle='${i18n}' key='data.less-30'/>
                                    </small>
                                </div>

                                <!-- name-->
                                <div class="form-group">
                                    <label class="control-label" for="name"><fmt:message bundle="${i18n}"
                                                                                         key="login.name"/>:</label>
                                    <div class="controls">
                                        <input id="name" class="form-control input-large" name="name"
                                               data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
                                               data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
                                               required pattern="^[А-ЯЁ][a-яё]{0,29}$">
                                    </div>
                                    <small class=" form-text text-muted help-block with-errors">
                                        <fmt:message bundle='${i18n}' key='data.less-30'/>
                                    </small>
                                </div>

                               

                                <br>
                                
                                <div class="form-group">
                                    <label class="control-label"></label>
                                    <div class="controls">
                                        <input type="submit" class="btn btn-success"
                                               value="<fmt:message bundle="${i18n}" key="login.signup"/>"/>
                                    </div>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message bundle="${i18n}"
                                                                                                key="login.close"/></button>
            </div>
        </div>
    </div>
</div>
