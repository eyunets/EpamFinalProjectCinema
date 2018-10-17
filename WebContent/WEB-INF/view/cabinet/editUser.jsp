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
		<fmt:message bundle="${i18n}" key="data.invalid-rerty" />
	</div>
</c:if>

<div class="modal-dialog modal-sm">
	<div class="modal-content">
		<br>
		<div class="modal-body">
			<div id="myTabContent" class="tab-content">

				<div role="tabpanel" class="tab-pane fade in active show"
					id="signup">
					<p>
						<b><fmt:message bundle="${i18n}" key="menu.edit" /></b>
					</p>

					<!-- Edit User Form -->

					<form class="form-horizontal"
						action="frontController?command=edituser" method="post"
						data-toggle="validator">
						<fieldset>
							<!-- edit user Form -->
							<!-- email-->
							<div class="form-group">
								<label class="control-label" for="email"><fmt:message
										bundle="${i18n}" key="login.email" />:</label>
								<div class="controls">
									<input type="email" id="email" name="email"
										class="form-control input-large"
										placeholder="JohnDoe@example.com"
										data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
										data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
										required> <small
										class=" form-text text-muted help-block with-errors"></small>
								</div>
							</div>

							<!--password-->
							<div class="form-group">
								<label class="control-label" for="password"><fmt:message
										bundle="${i18n}" key="login.password" />:</label>
								<div class="controls">
									<input id="password" name="password"
										class="form-control input-large" type="password"
										placeholder="********"
										data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
										data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
										required pattern=".{6,30}"> <small
										class=" form-text text-muted help-block with-errors"><fmt:message
											bundle='${i18n}' key='data.6-30' /> </small>
								</div>
							</div>

							<!-- password2-->
							<div class="form-group has-danger">
								<label class="control-label" for="reenterpassword"><fmt:message
										bundle="${i18n}" key="login.repassword" />:</label>
								<div class="controls">
									<input id="reenterpassword" class="form-control input-large"
										data-match="#password"
										data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
										data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
										data-match-error="<fmt:message bundle='${i18n}' key='data.no-match'/>"
										name="reenterpassword" type="password" placeholder="********"
										required pattern=".{6,30}"> <small
										class=" form-text text-muted help-block with-errors"></small>
								</div>

							</div>

							<br>
							<!-- surname-->
							<div class="form-group">
								<label class="control-label" for="surname"> <fmt:message
										bundle="${i18n}" key="login.surname" />:
								</label>
								<div class="controls">
									<input id="surname" class="form-control input-large"
										name="surname"
										data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
										data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
										required pattern="^[А-ЯЁ]([a-яё]){0,29}$">
								</div>
								<small class=" form-text text-muted help-block with-errors">
									<fmt:message bundle='${i18n}' key='data.less-30' />
								</small>
							</div>

							<!-- name-->
							<div class="form-group">
								<label class="control-label" for="name"><fmt:message
										bundle="${i18n}" key="login.name" />:</label>
								<div class="controls">
									<input id="name" class="form-control input-large" name="name"
										data-pattern-error="<fmt:message bundle='${i18n}' key='data.non-valid'/>"
										data-required-error="<fmt:message bundle='${i18n}' key='data.required'/>"
										required pattern="^[А-ЯЁ][a-яё]{0,29}$">
								</div>
								<small class=" form-text text-muted help-block with-errors">
									<fmt:message bundle='${i18n}' key='data.less-30' />
								</small>
							</div>
							<!--button-->
							<div class="control-group">
								<label class="control-label"></label>
								<div class="controls">
									<input type="submit" class="btn btn-success"
										value="<fmt:message bundle="${i18n}" key="cabinet.edit"/>" />
									<input type="submit" class="btn btn-secondary"
										value="<fmt:message bundle="${i18n}" key="login.close"/>" />
								</div>
							</div>
						</fieldset>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>