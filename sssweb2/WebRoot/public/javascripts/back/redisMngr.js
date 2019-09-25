
//表格显示的列
var columns = [[
	{checkbox:true},
	{field:'key', title:'名称', halign:'center'},
	{field:'expireDate', title:'过期时间', halign:'center'},
	{field:'value', title:'值', halign:'center'}

]];

//增删改查的路由
var queryUrl = '/sssweb2/RedisMngrController/getKeyValue.do';
var queryKeyCountUrl = '/sssweb2/RedisMngrController/queryKeyCount.do';
var deleteUrl = '/sssweb2/RedisMngrController/delete.do';
var flushDBUrl = '/sssweb2/RedisMngrController/flushDB.do';

$(function(){
	//初始化表格
	$('#dg').datagrid({
		fit:true,
		method:'POST',
		columns: columns,
		rownumbers: true,
		striped: true
	});
	
	//初始化日期控件

	//初始化下拉列表
	dictSelectByCount($('#s_redisInfoName'),'RedisInfo','name');

	//自动补全
	
	//添加校验事件
	validselfEvent();
	
	//当修改reids时,查询库号
	$('#s_redisInfoName').change(function(){
		$('#s_dbNo')[0].innerHTML = '<option value="">请选择</option>';
		//发送请求,获取所有表名
		var redisInfoName = $('#s_redisInfoName')[0].value;
		if(redisInfoName){
			$.ajax({
				url: '/sssweb2/RedisMngrController/getAllDbNo.do',
				type: 'POST',
				dataType: 'json',
				data: {'redisInfoName':redisInfoName},
				success: function(data){
					for(var i=0;i<data.length;i++){
						$('#s_dbNo').append('<option value="'+data[i]+'">'+data[i]+'</option>');
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
					showMsg(XMLHttpRequest.responseText);
				}
			});
		}
	});
	
	//查询
	$('#search').click(function(){
		if(!$('#s_dbNo').val()){
			showMsg('请选择redis和库号');
			return false;
		}
		initTable();
	});
	
	//查询key个数
	$('#searchKeyCount').click(function(){
		if(!$('#s_dbNo').val()){
			showMsg('请选择redis和库号');
			return false;
		}
		$.ajax({
			url: queryKeyCountUrl,
			type: 'GET',
			data : {
				redisInfoName: $('#s_redisInfoName').val(),
				dbNo: $('#s_dbNo').val(),
				pattern : $('#s_pattern').val(),
			},
			success: function(message){
				showMsg(message);
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				if(XMLHttpRequest.status==550){
					showMsg(XMLHttpRequest.responseText);
				}
			}
		});
	});
	
	//删除
	$('#delete').click(function(){
		if(!$('#s_dbNo').val()){
			showMsg('请选择redis和库号');
			return false;
		}
		var rows = $('#dg').datagrid('getChecked');
		if (rows.length <= 0) {
			showMsg('请选择操作记录');
			return false;
		}
		$.messager.confirm('提示','是否确认删除?',function(r){
			if(r){
				var keyArr = [];
				for (var i = 0; i < rows.length; i++) {
					keyArr.push(rows[i].key);
				}
				var keyStr = keyArr.join('@@');
				$.ajax({
					url: deleteUrl,
					type: 'POST',
					dataType: 'json',
					data : {
						redisInfoName: $('#s_redisInfoName').val(),
						dbNo: $('#s_dbNo').val(),
						keyStr : keyStr
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
	
	//清库
	$('#flushDB').click(function(){
		if(!$('#s_dbNo').val()){
			showMsg('请选择redis和库号');
			return false;
		}
		$.messager.confirm('提示','是否确认清库?',function(r){
			if(r){
				$.ajax({
					url: flushDBUrl,
					type: 'POST',
					dataType: 'json',
					data : {
						redisInfoName: $('#s_redisInfoName').val(),
						dbNo: $('#s_dbNo').val()
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
	
	
});

/**
 * 查询表格数据,默认跳转第一页
 */
function initTable(){
    $('#dg').datagrid('loadData', getPage());
}

/**
 * 表格分页查询
 */
function getPage(){
	var params = getSearchParams();
	var d = null;
    $.ajax({
        url: queryUrl,
        type: "GET",
        async: false,
		dataType: 'JSON',
        data: {
        	params : params
        },
        success: function(data){
            d = data;
            if(data.length > 0){
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
            d = [];
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