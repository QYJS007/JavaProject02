<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@ include file="base/head.jsp"%>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/public/stylesheets/common.css">
	<script src="${pageContext.request.contextPath}/public/javascripts/back/redisMngr.js"></script>
</head>
<body>
	<div class="panel panel-default" id="query">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a data-toggle="collapse" data-toggle="collapse" href="#collapseOne">
					查询条件
				</a>
			</h4>
		</div>
		<div id="collapseOne" class="panel-collapse collapse in">
			<div class="panel-body">
				<form id="searchform">
					<div class="row">
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_redisInfoName"><span class="title">redis</span></label>
								<div class="col-xs-8">
									<select class="form-control input-sm" id="s_redisInfoName" name="redisInfoName">
										<option value="">请选择</option>
		                            </select>
								</div>
							</div>
						</div>
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_dbNo"><span class="title">库号</span></label>
								<div class="col-xs-8">
									<select class="form-control input-sm" id="s_dbNo" name="dbNo">
										<option value="">请选择</option>
		                            </select>
								</div>
							</div>
						</div>
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_pattern"><span class="title">key-pattern</span></label>
								<div class="col-xs-8">
									<input type="text" name="pattern" id="s_pattern" class="form-control input-sm" value="*">
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_queryTime"><span class="title">查询过期时间</span></label>
								<div class="col-xs-8">
									<select class="form-control input-sm" id="s_queryTime" name="queryTime">
										<option value="false">否</option>
										<option value="true">是</option>
		                            </select>
								</div>
							</div>
						</div>
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_queryValue"><span class="title">查询值</span></label>
								<div class="col-xs-8">
									<select class="form-control input-sm" id="s_queryValue" name="queryValue">
										<option value="false">否</option>
										<option value="true">是</option>
		                            </select>
								</div>
							</div>
						</div>
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_num"><span class="title">最大显示结果</span></label>
								<div class="col-xs-4">
									<input type="text" name="num" id="s_num" class="form-control input-sm" value="500">
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div id="operate">
		<span class="title">操作</span>
		<button class="btn btn-primary" id="search" type="button"> <span class="glyphicon glyphicon-search"></span> 查询</button>
		<button class="btn btn-primary" id="delete" type="button"> <span class="glyphicon glyphicon-edit"></span> 删除</button>
		<button class="btn btn-primary" id="flushDB" type="button"> <span class="glyphicon glyphicon-trash"></span> 清库</button>
		<button class="btn btn-primary" id="searchKeyCount" type="button"> <span class="glyphicon glyphicon-trash"></span> 查询key个数</button>
	</div>

	<div id="dataset">
		<table id="dg"></table>
	</div>

</body>
</html>
