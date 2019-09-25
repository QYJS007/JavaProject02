<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@ include file="base/head.jsp"%>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/public/stylesheets/common.css">
	<script src="${pageContext.request.contextPath}/public/javascripts/back/queryTemplet.js"></script>
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
								<label class="col-xs-4 control-label" for="s_name"><span class="title">模板名称</span></label>
								<div class="col-xs-8">
									<input type="text" name="name" id="s_name" class="form-control input-sm">
								</div>
							</div>
						</div>
						<div class="col-xs-3">
							<div class="form-group">
								<label class="col-xs-4 control-label" for="s_path"><span class="title">查询路径</span></label>
								<div class="col-xs-8">
									<input type="text" name="path" id="s_path" class="form-control input-sm">
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
						<span class="optmark"></span>文件查询模板
					</h4>
				</div>
				<div class="modal-body">
					<form id="editform">
						<div id="prompt"></div>

						<input type="hidden" name="id" id="e_id">
						<div class="row">
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_name" class="col-xs-4 control-label"><span class="mark">*</span><span class="title">模板名称</span></label>
									<div class="col-xs-8">
										<input type="text" name="name" id="e_name" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_path" class="col-xs-4 control-label"><span class="mark">*</span><span class="title">查询路径</span></label>
									<div class="col-xs-8">
										<input type="text" name="path" id="e_path" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_pathPattern" class="col-xs-4 control-label"><span class="title">路径规则</span></label>
									<div class="col-xs-8">
										<input type="text" name="pathPattern" id="e_pathPattern" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_noPathPattern" class="col-xs-4 control-label"><span class="title">排除的路径规则</span></label>
									<div class="col-xs-8">
										<input type="text" name="noPathPattern" id="e_noPathPattern" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_fileNamePattern" class="col-xs-4 control-label"><span class="title">文件名称规则</span></label>
									<div class="col-xs-8">
										<input type="text" name="fileNamePattern" id="e_fileNamePattern" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_noFileNamePattern" class="col-xs-4 control-label"><span class="title">排除的文件名称规则</span></label>
									<div class="col-xs-8">
										<input type="text" name="noFileNamePattern" id="e_noFileNamePattern" class="form-control input-sm">
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="e_encoding" class="col-xs-4 control-label"><span class="mark">*</span><span class="title">编码</span></label>
									<div class="col-xs-8">
										<select class="form-control input-sm" id="e_encoding" name="encoding">
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
