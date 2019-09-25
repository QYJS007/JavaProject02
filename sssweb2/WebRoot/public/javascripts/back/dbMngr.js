
//表格显示的列
var columns = [[
	{checkbox:true},
	{field:'标题1', title:'标题1', halign:'center'},
	{field:'标题2', title:'标题2', halign:'center'}
]];

//增删改查的路由
var queryUrl = '/sssweb2/DbMngrController/query.do';
var saveUrl = '/sssweb2/DbMngrController/save.do';
var deleteUrl = '/sssweb2/DbMngrController/delete.do';

//记录数据字典信息
var typeNameValueObj = {};

//记录当前表信息
var tableInfo = null;
var allColumnInfoMap = [];
var allColumnNameArr = [];

//记录当前条件个数
var conditionCount = 0;

$(function(){
	//初始化表格
	$('#dg').datagrid({
		fit:true,
		method:'POST',
		columns: columns,
		rownumbers: true,
		pagination: true,
		striped: true
	});
	
	//定义分页条
	$('#dg').datagrid('getPager').pagination({
		showPageList:false,
		beforePageText:'第',
		afterPageText:'页    共 {pages} 页',
		displayMsg:'当前显示 {from} - {to} 条记录   共 {total} 条记录',
		onSelectPage:function(pageNumber, pageSize){
			$('#dg').datagrid('loadData', getPage(pageNumber,pageSize));
		}
	});
	
	//初始化日期控件

	//初始化下拉列表
	dictSelect($('#e_type'),'t_db_info','type',typeNameValueObj);
	dictSelectByCount($('#s_dbInfoName'),'DbInfo','name');
	
	//当修改数据库时,查询表名
	$('#s_dbInfoName').change(function(){
		$('#s_tableName')[0].innerHTML = '<option value="">请选择</option>';
		$('#s_showColumn')[0].innerHTML = '<option value="_all">全部</option>';
		$('#s_showColumn')[0].value='_all';
		//发送请求,获取所有表名
		var dbInfoName = $('#s_dbInfoName')[0].value;
		if(dbInfoName){
			$.ajax({
				url: '/sssweb2/DbMngrController/getAllTableName.do',
				type: 'POST',
				dataType: 'json',
				data: {'dbInfoName':dbInfoName},
				success: function(data){
					for(var i=0;i<data.length;i++){
						$('#s_tableName').append('<option value="'+data[i]+'">'+data[i]+'</option>');
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
					showMsg(XMLHttpRequest.responseText);
				}
			});
		}
	});
	
	//当修改表名时,查询表信息
	$('#s_tableName').change(function(){
		//清空显示列
		$('#s_showColumn')[0].innerHTML = '<option value="_all">全部</option>';
		$('#s_showColumn')[0].value='_all';
		//清空条件下拉列表
		$('.myCondition').each(function(){
			this.innerHTML = '<option value="">无</option>';
			this.value = '';
		});
		//发送请求,获取所有列名
		var dbInfoName = $('#s_dbInfoName')[0].value;
		var tableName = $('#s_tableName')[0].value;
		if(tableName){
			$.ajax({
				url: '/sssweb2/DbMngrController/getTableInfo.do',
				type: 'POST',
				dataType: 'json',
				data: {'dbInfoName':dbInfoName,'tableName':tableName},
				success: function(data){
					tableInfo = data;
					allColumnInfoMap = data.columnInfoMap;
					allColumnNameArr = [];
					//添加显示列 和 条件下拉列表
					for(var columnName in allColumnInfoMap){
						allColumnNameArr.push(columnName);
						$('#s_showColumn').append('<option value="'+columnName+'">'+columnName+'</option>');
						$('.myCondition').each(function(){
							$(this).append('<option value="'+columnName+'">'+columnName+'</option>');
						});
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
					showMsg(XMLHttpRequest.responseText);
				}
			});
		}
	});
	
	//当点击添加条件时,添加条件
	$('#addConditionA').click(function(){
		if(!tableInfo){
			showMsg('请选择数据库和表');
			return;
		}
		conditionCount++;
		var str = '';
		str += '<div class="row">';
		str += '	<div class="col-xs-3">';
		str += '	<div class="form-group">';
		str += '	<label class="col-xs-4 control-label" for="s_key'+conditionCount+'"><span class="title">条件'+conditionCount+'</span></label>';
		str += '	<div class="col-xs-8">';
		str += '	<select class="form-control input-sm myCondition" id="s_key'+conditionCount+'" name="key'+conditionCount+'">';
		str += '	<option value="">无</option>';
		for(var columnName in allColumnInfoMap){
			str += '<option value="'+columnName+'">'+columnName+'</option>';
		}
		str += '	</select>';
		str += '	</div>';
		str += '	</div>';
		str += '	</div>';
		str += '</div>';
		$('#searchform').append(str);
		
		$('#s_key'+conditionCount).change(conditionChange);
	});
	
	//当确定查询条件时,确定类型
	function conditionChange(){
		//获取值
		var columnName = $(this)[0].value;
		
		//删除原来的输入框
		var rowDiv = this;
		while(rowDiv.className!='row'){
			rowDiv = rowDiv.parentNode;
		}
		for(var i=rowDiv.children.length-1;i>0;i--){
			rowDiv.removeChild(rowDiv.children[i]);
		}
		
		if(!columnName){
			return;
		}
		
		//添加新的输入框(新的文本框的name为选择的字段名称)
		var columnType = allColumnInfoMap[columnName].javaType;
		if(columnType=='Date'){
			var str = '';
			str += '<div class="col-xs-3">';
			str += '	<div class="form-group">';
			str += '	<label class="col-xs-4 control-label" for="s_value'+conditionCount+'_start"><span class="title">值</span></label>';
			str += '	<div class="col-xs-8">';
			str += '	<input type="text" name="'+columnName+'_start" id="s_value'+conditionCount+'_start" class="form-control input-sm">';
			str += '	</div>';
			str += '	</div>';
			str += '</div>';
			str += '<div class="col-xs-3">';
			str += '	<div class="form-group">';
			str += '	<label class="col-xs-4 control-label" for="s_value'+conditionCount+'_end"><span class="title">至</span></label>';
			str += '	<div class="col-xs-8">';
			str += '	<input type="text" name="'+columnName+'_end" id="s_value'+conditionCount+'_end" class="form-control input-sm">';
			str += '	</div>';
			str += '	</div>';
			str += '</div>';
			
			$(rowDiv).append(str);
			
			initDatepicker('s_value'+conditionCount+'_start');
			showDate('s_value'+conditionCount+'_start');
			initDatepicker('s_value'+conditionCount+'_end');
			showDate('s_value'+conditionCount+'_end');
		}else{
			var str = '';
			str += '<div class="col-xs-3">';
			str += '	<div class="form-group">';
			str += '	<label class="col-xs-4 control-label" for="s_value'+conditionCount+'"><span class="title">值</span></label>';
			str += '	<div class="col-xs-8">';
			str += '	<input type="text" name="'+columnName+'" id="s_value'+conditionCount+'" class="form-control input-sm">';
			str += '	</div>';
			str += '	</div>';
			str += '</div>';
			$(rowDiv).append(str);
		}
	}
	
	
	//添加校验事件
	validselfEvent();
	
	//条件查询
	$('#search').click(function(){
		initTable();
	});

	//添加
	$('#add').click(function(){
		if(!tableInfo){
			showMsg('请选择数据库和表');
			return;
		}
		$('#dg').datagrid('clearSelections');
		//将选择列 重置为全部
		$('#s_showColumn').val('_all');
		modalTitle('新增');
		initDynamicModal();
		$('#dynamicModal').modal('show');
	});
	
	//修改
	$('#modify').click(function(){
		if(!tableInfo){
			showMsg('请选择数据库和表');
			return;
		}
		//判断是否存在选中行
		var row = $('#dg').datagrid('getSelected');
		if(!row){
			showMsg('请选择操作记录');
			return false;
		}
		
		//清空其它选择的列,只选择一行
		$('#dg').datagrid('clearSelections');
		var currentindex = $('#dg').datagrid('getRowIndex', row);
		$('#dg').datagrid('selectRow', currentindex);
		
		//显示修改区域
		modalTitle('修改');
		initDynamicModal(row);
		$('#dynamicModal').modal('show');
	});
	
	//删除
	$('#delete').click(function(){
		var rows = $('#dg').datagrid('getChecked');
		if (rows.length <= 0) {
			showMsg('请选择操作记录');
			return false;
		}
		$.messager.confirm('提示','是否确认删除?',function(r){
			if(r){
				var ids = [];
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i][tableInfo.primaryKeyName]);
				}
				var idstr = ids.join(',');
				$.ajax({
					url: deleteUrl,
					type: 'POST',
					dataType: 'json',
					data : {
						dbInfoName:$('#s_dbInfoName').val(),
						tableName:$('#s_tableName').val(),
						ids : idstr
					},
					success: function(data){
						showMsg(data.message);
						initTable();
					},
					error: function(XMLHttpRequest, textStatus, errorThrown){
						if(XMLHttpRequest.status==550){
							var exp = JSON.parse(XMLHttpRequest.responseText);
							showMsg(exp[0].message);
						}
					}
				});
			}
		});
	});
	
	//保存
	$('#save').click(function(){
		if(!validselfhtml()){
			return false;
		}
		
		$.ajax({
			url: saveUrl,
			type: 'POST',
			dataType: 'json',
			data: $('#editform').serialize(),
			success: function(data){
				showMsg(data.message);
				$('#dynamicModal').modal('hide');
				initTable();
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				if(XMLHttpRequest.status==550){
					var exp = JSON.parse(XMLHttpRequest.responseText);
					showMsg(exp[0].message);
				}
			}
		});
	});
	
	//模态框关闭事件
	$('#dynamicModal').on('hidden.bs.modal', function (e) {
		$('#prompt').html('');
		$('#editform')[0].reset();
		$('#editform :input[name][type="hidden"]').each(function(){
			$(this).val('');
		});
		$('#editform :input.combo-value').each(function(){
			var $target = $(this).parent().prev();
			$target.combogrid('clear');
		});
	});
	
});

/**
 * 查询表格数据,默认跳转第一页
 */
function initTable(){
	if(!$('#s_tableName').val() || !$('#s_showColumn').val()){
		showMsg('请选择条件!');
		return;
	}
	
	//重新加载列
	var showColumnArr = getShowColumnArr();
	
	var showColumnStr = '';
	for(var i=0;i<showColumnArr.length;i++){
		showColumnStr += "{field:'"+showColumnArr[i]+"', title:'"+showColumnArr[i]+"', halign:'center'}";
		if(i!=showColumnArr.length-1){
			showColumnStr+=',\r\n';
		}
	}
	columns = eval('[[{checkbox:true},'+showColumnStr+']]');
	
	$('#dg').datagrid({
		fit:true,
		method:'POST',
		columns: columns,
		rownumbers: true,
		pagination: true,
		striped: true
	});
	
	//定义分页条
	$('#dg').datagrid('getPager').pagination({
		showPageList:false,
		beforePageText:'第',
		afterPageText:'页    共 {pages} 页',
		displayMsg:'当前显示 {from} - {to} 条记录   共 {total} 条记录',
		onSelectPage:function(pageNumber, pageSize){
			$('#dg').datagrid('loadData', getPage(pageNumber,pageSize));
		}
	});
	
    var pagerOpts = $('#dg').datagrid('getPager').pagination('options');
    $('#dg').datagrid('getPager').pagination({
        pageNumber: 1
    });
    var rows = pagerOpts.pageSize;
    $('#dg').datagrid('loadData', getPage(1, rows));
}

/**
 * 表格分页查询
 */
function getPage(pageNum,pageSize){
	var params = getSearchParams();
	var d = null;
    $.ajax({
        url: queryUrl,
        type: "GET",
        async: false,
		dataType: 'JSON',
        data: {
        	params : params,
        	pageNum: pageNum,
        	pageSize: pageSize
        },
        success: function(data){
            d = data;
            if(d.total > 0){
        		$('#exportexcel').removeAttr('disabled');
            }else{
            	$('#exportexcel').attr('disabled','disabled');
            	showMsg("查无数据");
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if (XMLHttpRequest.status == 550) {
                var exp = JSON.parse(XMLHttpRequest.responseText);
                showMsg(exp.message);
            }
            d = {
                "total": 0,
                "rows": []
            };
        }
    });
	return d;
}

function getSearchParams(){
	var params = new Object();
	$('#searchform :input[name]').each(function(){
		var name = $(this).attr('name');
		if(name == "multiselect"){
			return true;
		}
		params[name] = $(this).val();
	});
	params['department.code'] = $('#s_code').next().find('.combo-text').val();
	params['department.name'] = $('#s_name').next().find('.combo-text').val();
	params = JSON.stringify(params);
	return params;
}

//初始化修改div的元素
function initDynamicModal(row){
	//清空原来的元素
	$('#modifyDiv').html('');
	
	var showColumnArr = getShowColumnArr();
	
	var str = '';
	//添加数据库信息
	str += '<input type="hidden" name="dbInfoName" value="'+$('#s_dbInfoName').val()+'">';
	str += '<input type="hidden" name="tableName" value="'+tableInfo.tableName+'">';
	str += '<input type="hidden" name="operateType" value="'+(row==null?'add':'update')+'">';
	str += '<input type="hidden" name="showColumn" value="'+showColumnArr.join(',')+'">';
	
	//添加列
	for(var i=0;i<showColumnArr.length;i++){
		var columnName = showColumnArr[i];
		str += '<div class="col-xs-6">';
		str += '	<div class="form-group">';
		str += '	<label for="e_'+columnName+'" class="col-xs-4 control-label"><span class="title">'+columnName+'</span></label>';
		str += '	<div class="col-xs-8">';
		str += '	<input type="text" name="'+columnName+'" id="e_'+columnName+'" class="form-control input-sm">';
		str += '	</div>';
		str += '	</div>';
		str += '</div>';
	}
	$('#modifyDiv').append(str);
	
	//赋初始值
	if(row){
		for(var i=0;i<showColumnArr.length;i++){
			var columnName = showColumnArr[i];
			$('#e_'+columnName).val(row[columnName]);
		}
		//将主键设置为只读
		$('#e_'+tableInfo.primaryKeyName).attr('readonly',true);
	}
}

//获得要显示的列的列名
function getShowColumnArr(){
	var showColumnArr = $('#s_showColumn').val();
	var isAll = false;
	for(var i=0;i<showColumnArr.length;i++){
		if(showColumnArr[i]=='_all'){
			isAll = true;
			break;
		}
	}
	if(isAll){
		showColumnArr = allColumnNameArr;
	}
	
	if(showColumnArr[0]!=tableInfo.primaryKeyName){
		showColumnArr.unshift(tableInfo.primaryKeyName);
	}
	
	return showColumnArr;
}
