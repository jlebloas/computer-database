<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="template/header.jsp" />

<section id="main">
	<div class="container">
		<div class="row">
			<div class="col-xs-8 col-xs-offset-2 box">
				<h1>Add Computer</h1>
				<form action="<c:url value="/computer/add" />" onsubmit="return checkForm()" method="POST">
					<fieldset>
						<div class="form-group has-feedback<c:if test="${computerNameError}"> has-error</c:if>">
							<label for="computerName" class="control-label" >Computer name</label>
							<input type="text" class="form-control" id="computerName" name="computerName" placeholder="Computer name" value="<c:out value="${computerName}" />" aria-describedby="computerNameErrorStatus">
							<span class="glyphicon glyphicon-remove form-control-feedback<c:if test="${!computerNameError}"> sr-only</c:if>"></span> 
							<span id="computerNameErrorMessage" class="help-block<c:if test="${!computerNameError}"> sr-only</c:if>">A computer name is required</span>
						</div>
					
						<div class="form-group has-feedback<c:if test="${introducedError || introducedEmptyError}"> has-error</c:if>">
							<label for="introduced" class="control-label" >Introduced date</label>
							<input type="date" placeholder="2015-10-01" class="form-control" id="introduced" name="introduced" placeholder="Introduced date" value="<c:out value="${introduced}" />" aria-describedby="introducedErrorStatus introducedEmptyErrorStatus">
							<span class="glyphicon glyphicon-remove form-control-feedback<c:if test="${!introducedError}"> sr-only</c:if>"></span> 
							<span id="introducedErrorStatus" class="help-block<c:if test="${!introducedError}"> sr-only</c:if>">Your date is invalid</span>
							<span id="introducedEmptyErrorStatus" class="help-block<c:if test="${!introducedEmptyError}"> sr-only</c:if>">The introduced date must be set if the discontinued date is set</span>
						</div>
			
						<div class="form-group has-feedback<c:if test="${discontinuedError || orderError}"> has-error</c:if>">
							<label for="discontinued" class="control-label" >Discontinued date</label>
							<input type="date" placeholder="2015-12-25" class="form-control" id="discontinued" name="discontinued" placeholder="Discontinued date" value="<c:out value="${discontinued}" />" aria-describedby="discontinuedErrorStatus orderErrorStatus">
							<span class="glyphicon glyphicon-remove form-control-feedback<c:if test="${!discontinuedError}"> sr-only</c:if>"></span> 
							<span id="discontinuedErrorStatus" class="help-block<c:if test="${!discontinuedError}"> sr-only</c:if>">Your date is invalid</span>
							<span id="orderErrorStatus" class="help-block<c:if test="${!orderError}"> sr-only</c:if>">The discontinued date must be greater than the introduced one</span>
						</div>
						
						<div class="form-group has-feedback">
							<label for="companyId" class="control-label" >Company</label>
							<select class="form-control" id="companyId" name="companyId">
								<c:forEach items="${companies}" var="company">
									<option value="${company.id}" <c:if test="${company.id == companyId}">selected</c:if>><c:out value="${company.name}" /></option>
								</c:forEach>
							</select>
						</div>
					</fieldset>
					<div class="actions pull-right">
						<input type="submit" value="Add" class="btn btn-primary"> or <a href="<c:url value="/" />" class="btn btn-default">Cancel</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</section>

<script src="<c:url value="/js/computer.js"/>"></script>

<jsp:include page="template/footer.jsp" />
