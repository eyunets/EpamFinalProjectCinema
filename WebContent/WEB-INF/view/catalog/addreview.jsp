<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html;charset=UTF-8"%>


<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" var="i18n" />

<div class="modal-dialog ">
	<div class="modal-content">
		<div class="modal-body">
			<div id="myTabContent" class="tab-content">
				<div role="tabpanel" class="tab-pane fade in active show"
					id="addfilm">
					<form class="form-horizontal"
						action="frontController?command=addreview&id=${film.id}" method="post"
						data-toggle="validator">
						<div class="form-group">
							<label class="col-md-2 col-sm-2 col-xs-2 col-lg-2 control-label">${yourMark}</label>
							<div class="col-md-10 col-sm-10 col-xs-10 col-lg-10">
								<label class="radio-inline"><input type="radio"
									name="mark" value="1">1</label> <label class="radio-inline"><input
									type="radio" name="mark" value="2">2</label> <label
									class="radio-inline"><input type="radio" name="mark"
									value="3">3</label> <label class="radio-inline"><input
									type="radio" name="mark" value="4">4</label> <label
									class="radio-inline"><input type="radio" name="mark"
									value="5">5</label>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 col-xs-2 col-lg-2 control-label">${reviewText}</label>
							<div class="col-md-10 col-sm-10 col-xs-10 col-lg-10">
								<textarea class="form-control" rows="10" name="text"
									id="text"></textarea>
							</div>
						</div>
						<div
							class="col-md-2 col-sm-2 col-xs-2 col-lg-2 col-md-offset-2 col-sm-offset-2 col-xs-offset-2 col-lg-offset-2">
							<button type="submit" class="btn btn-primary"><fmt:message bundle="${i18n}" key="user.add.review" /></button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
