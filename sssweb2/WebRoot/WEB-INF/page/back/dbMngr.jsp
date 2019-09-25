<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@ include file="base/head.jsp"%>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/public/stylesheets/common.css">
	<script src="${pageContext.request.contextPath}/public/javascripts/back/dbMngr.js"></script>
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
								<label class="col-xs-4 control-label" for="s_dbInfoName"><span class="title">数据库</span></label>
								<div class="col-xs-8">
									<select class="form-control input-sm" id="s_dbInfoName" name="dbInfoName">
										<option value="">请选择</option>
		                            </select>
								</div>
							</div>
						</div>
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_tableName"><span class="title">表名</span></label>
								<div class="col-xs-8">
									<select class="form-control input-sm" id="s_tableName" name="tableName">
										<option value="">请选择</option>
		                            </select>
								</div>
							</div>
						</div>
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_updateType"><span class="title">修改方式</span></label>
								<div class="col-xs-8">
									<select class="form-control input-sm" id="s_updateType" name="updateType">
										<option value="">默认</option>
		                            </select>
								</div>
							</div>
						</div>
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_showColumn"><span class="title">显示列</span></label>
								<div class="col-xs-8">
									<select class="form-control input-sm" id="s_showColumn" name="showColumn" multiple="multiple" style="height:100px;">
										<option value="_all">全部</option>
		                            </select>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<a id="addConditionA" href="javascript:void(0);" style="margin-left:35px;">添加条件</a>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div id="operate">
		<span class="title">操作</span>
		<button class="btn btn-primary" id="search" type="button"> <span class="glyphicon glyphicon-search"></span> 查询</button>
		<button class="btn btn-primary" id="add" type="button">	<span class="glyphicon glyphicon-plus"></span> 新增</button>
		<button class="btn btn-primary" id="modify" type="button"> <span class="glyphicon glyphicon-edit"></span> 修改</button>
		<button class="btn btn-primary" id="delete" type="button"> <span class="glyphicon glyphicon-trash"></span> 删除</button>
	</div>

	<div id="dataset">
		<table id="dg"></table>
	</div>

	<div class="modal fade" id="dynamicModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button class="close" type="button" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">
						<span class="optmark"></span>
					</h4>
				</div>
				<div class="modal-body">
					<form id="editform">
						<div id="prompt"></div>
						<div class="row" id="modifyDiv">
							<input type="hidden" name="id" id="e_id">
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_name" class="col-xs-4 control-label"><span class="mark">*</span><span class="title">名称</span></label>
									<div class="col-xs-8">
										<input type="text" name="name" id="e_name" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_phone" class="col-xs-4 control-label"><span class="mark">*</span><span class="title">手机</span></label>
									<div class="col-xs-8">
										<input type="text" name="phone" id="e_phone" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_cardnum" class="col-xs-4 control-label"><span class="mark">*</span><span class="title">卡号</span></label>
									<div class="col-xs-8">
										<input type="text" name="cardnum" id="e_cardnum" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-12">
								<div class="form-group">
									<label for="e_remark" class="col-xs-2 control-label"><span class="title">描述</span></label>
									<div class="col-xs-9">
										<textarea type="text" name="remark" id="e_remark" class="form-control input-sm" style="height: 80px;"></textarea>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button class="btn btn-primary" id="save" type="button"> <span class="glyphicon glyphicon-pencil"></span> 保存 </button>
					<button class="btn btn-primary" type="button" data-dismiss="modal"> <span class="glyphicon glyphicon-remove"></span> 关闭</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
