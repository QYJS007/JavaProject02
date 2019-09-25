
//表格显示的列
var columns = [[
	{checkbox:true},
	{field:'id', title:'id', halign:'center'},
	{field:'name', title:'模板名称', halign:'center'},
	{field:'path', title:'查询路径', halign:'center'},
	{field:'pathPattern', title:'路径规则', halign:'center'},
	{field:'noPathPattern', title:'排除的路径规则', halign:'center'},
	{field:'fileNamePattern', title:'文件名称规则', halign:'center'},
	{field:'noFileNamePattern', title:'排除的文件名称规则', halign:'center'},
	{field:'encoding', title:'编码', halign:'center',formatter:function(value,row,index){
			return encodingNameValueObj[value];
		}}

]];

//增删改查的路由
var queryUrl = '/sssweb2/QueryTempletBackController/search.do';
var saveUrl = '/sssweb2/QueryTempletBackController/save.do';
var deleteUrl = '/sssweb2/QueryTempletBackController/delete.do';

var encodingNameValueObj = {};

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
	dictSelect($('#e_encoding'),'t_query_templet','encoding',encodingNameValueObj);

	//自动补全
	
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
		$('#e_name').val(row.name);
		$('#e_path').val(row.path);
		$('#e_pathPattern').val(row.pathPattern);
		$('#e_noPathPattern').val(row.noPathPattern);
		$('#e_fileNamePattern').val(row.fileNamePattern);
		$('#e_noFileNamePattern').val(row.noFileNamePattern);
		$('#e_encoding').val(row.encoding);
		
		//自动补全,赋初始值
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