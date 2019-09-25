<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@ include file="base/head.jsp"%>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/public/stylesheets/common.css">
	<script src="${pageContext.request.contextPath}/public/javascripts/back/remoteProject.js"></script>
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
								<label class="col-xs-4 control-label" for="s_type"><span class="title">类型</span></label>
								<div class="col-xs-8">
									<input type="text" name="type" id="s_type" class="form-control input-sm">
								</div>
							</div>
						</div>
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_name"><span class="title">名称</span></label>
								<div class="col-xs-8">
									<input type="text" name="name" id="s_name" class="form-control input-sm">
								</div>
							</div>
						</div>
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_ip"><span class="title">服务器ip</span></label>
								<div class="col-xs-8">
									<input type="text" name="ip" id="s_ip" class="form-control input-sm">
								</div>
							</div>
						</div>
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_port"><span class="title">端口</span></label>
								<div class="col-xs-8">
									<input type="text" name="port" id="s_port" class="form-control input-sm">
								</div>
							</div>
						</div>
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_istest"><span class="title">是否是测试环境</span></label>
								<div class="col-xs-8">
									<select class="form-control input-sm" id="s_istest" name="istest">
										<option value="">请选择</option>														
		                            </select>
								</div>
							</div>
						</div>
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_localProject"><span class="title">对应本地项目</span></label>
								<div class="col-xs-8">
									<input type="text" name="localProject" id="s_localProject" class="form-control input-sm">
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
						<span class="optmark"></span>远程项目
					</h4>
				</div>
				<div class="modal-body">
					<form id="editform">
						<div id="prompt"></div>

						<input type="hidden" name="id" id="e_id">
						<div class="row">
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_type" class="col-xs-4 control-label"><span class="mark">*</span><span class="title">类型</span></label>
									<div class="col-xs-8">
										<input type="text" name="type" id="e_type" class="form-control input-sm">
									</div>
								</div>
							</div>
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
									<label for="e_ip" class="col-xs-4 control-label"><span class="mark">*</span><span class="title">服务器ip</span></label>
									<div class="col-xs-8">
										<input type="text" name="ip" id="e_ip" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_username" class="col-xs-4 control-label"><span class="mark">*</span><span class="title">用户名</span></label>
									<div class="col-xs-8">
										<input type="text" name="username" id="e_username" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_password" class="col-xs-4 control-label"><span class="mark">*</span><span class="title">密码</span></label>
									<div class="col-xs-8">
										<input type="text" name="password" id="e_password" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_path" class="col-xs-4 control-label"><span class="mark">*</span><span class="title">路径</span></label>
									<div class="col-xs-8">
										<input type="text" name="path" id="e_path" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_port" class="col-xs-4 control-label"><span class="mark">*</span><span class="title">端口</span></label>
									<div class="col-xs-8">
										<input type="text" name="port" id="e_port" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_commandGroupId" class="col-xs-4 control-label"><span class="mark">*</span><span class="title">命令组</span></label>
									<div class="col-xs-8">
										<input type="text" name="commandGroupId" id="e_commandGroupId" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_localProject" class="col-xs-4 control-label"><span class="title">对应本地项目</span></label>
									<div class="col-xs-8">
										<input type="text" name="localProject" id="e_localProject" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_istest" class="col-xs-4 control-label"><span class="mark">*</span><span class="title">是否是测试环境</span></label>
									<div class="col-xs-8">
										<select class="form-control input-sm" id="e_istest" name="istest">
			                            </select>
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