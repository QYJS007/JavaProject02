
//表格显示的列
var columns = [[
	{checkbox:true},
	{field:'id', title:'id', halign:'center'},
	{field:'type', title:'类型', halign:'center'},
	{field:'name', title:'名称', halign:'center'},
	{field:'ip', title:'服务器ip', halign:'center'},
	{field:'username', title:'用户名', halign:'center'},
	{field:'password', title:'密码', halign:'center'},
	{field:'path', title:'路径', halign:'center'},
	{field:'port', title:'端口', halign:'center'},
	{field:'commandGroupId', title:'命令组', halign:'center',formatter:function(value,row,index){
			return row.remoteCommandGroupName;
		}},
	{field:'command', title:'命令', halign:'center'},
	{field:'localProject', title:'对应本地项目', halign:'center'},
	{field:'istest', title:'是否是测试环境', halign:'center',formatter:function(value,row,index){
			return istestNameValueObj[value];
		}}

]];

//增删改查的路由
var queryUrl = '/sssweb2/RemoteProjectBackController/search.do';
var saveUrl = '/sssweb2/RemoteProjectBackController/save.do';
var deleteUrl = '/sssweb2/RemoteProjectBackController/delete.do';

var istestNameValueObj = {};

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
	dictSelect($('#s_istest'),'t_remote_project','istest',istestNameValueObj);
	dictSelect($('#e_istest'),'t_remote_project','istest',istestNameValueObj);

	//自动补全

	autocomplete($('#e_commandGroupId'),'remoteProject_RemoteCommandGroup','id','name');
	
	//添加校验事件
	validselfEvent();
	
	//条件查询
	$('#search').click(function(){
		initTable();
	});

	//添加
	$('#add').click(function(){
		$('#dg').datagrid('clearSelections');
		modalTitle('新增');
		$('#dynamicModal').modal('show');
	});
	
	//修改
	$('#modify').click(function(){
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
		$('#dynamicModal').modal('show');
		
		//赋初始值
		$('#e_id').val(row.id);
		$('#e_type').val(row.type);
		$('#e_name').val(row.name);
		$('#e_ip').val(row.ip);
		$('#e_username').val(row.username);
		$('#e_password').val(row.password);
		$('#e_path').val(row.path);
		$('#e_port').val(row.port);
		$('#e_commandGroupId').val(row.commandGroupId);
		$('#e_command').val(row.command);
		$('#e_localProject').val(row.localProject);
		$('#e_istest').val(row.istest);
		
		//自动补全,赋初始值

		$('#e_commandGroupId').next().find('.combo-text').val(row.remoteCommandGroupName);
		$('#e_commandGroupId').next().find('.combo-value').val(row.commandGroupId);
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
					ids.push(rows[i].id);
				}
				var idstr = ids.join(',');
				$.ajax({
					url: deleteUrl,
					type: 'POST',
					dataType: 'json',
					data : {
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
	
	//ssh控制台
	$('#sshconsole').click(function(){
		var params = getSearchParams();
		var url = '/sssweb2/SshConsoleController/sshConsole_page.do';
		url += '?params='+params;
		window.open(url);
	});
});

/**
 * 查询表格数据,默认跳转第一页
 */
function initTable(){
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