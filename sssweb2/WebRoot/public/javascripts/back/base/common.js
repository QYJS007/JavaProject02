//格式化时间
Date.prototype.format = function(format){ 
	var o = { 
	"M+" : this.getMonth()+1, //month 
	"d+" : this.getDate(), //day 
	"h+" : this.getHours(), //hour 
	"m+" : this.getMinutes(), //minute 
	"s+" : this.getSeconds(), //second 
	"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
	"S" : this.getMilliseconds() //millisecond 
	};

	if(/(y+)/.test(format)) { 
	format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	} 

	for(var k in o) { 
		if(new RegExp("("+ k +")").test(format)) { 
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
		} 
	} 
	return format; 
};

/**
 * @author lin
 */
$(function(){
	$('body').css("overflow","hidden");
	
	screenResize();
	
	//取消
	$("#cancel").click(function(){
		$('#myModal').modal('hide');
		$('#editForm :radio').removeAttr("checked");
	});
	$('#myModal').on('hidden.bs.modal', function (e) {
		$('#editForm')[0].reset();
		clearVerifymsg('#editForm');
	});
	//模态框关闭事件
	$('#dynamicModal').on('hidden.bs.modal', function (e) {
		clearForm();
	});
});

//消息提示
function showMsg(msg) {
	$.messager.show({
		title: '消息提示',
		msg: msg,
		timeout: 8000,
		style:{
			right:'',
			top:document.body.scrollTop+document.documentElement.scrollTop,
			bottom:''
		}
	});
}

//警告消息
function alertMsg(msg) {
	$.messager.alert('重要提示',msg,'error');
}

//清除校验信息
function clearVerifymsg(element) {
	var spans = $(element).find('span.col-sm-3');
	$.each(spans, function(index, value){
		$(value).html('');
	});
}

function validselfhtml(){
	var d = true;
	$('#editform .mark').each(function(){
		var ary = $(this).parent().next().find("input[type=text],input[type=password],textarea");
		var title = $(this).parent().find(".title").html();
		if(ary.length==1){
			if($(ary[0]).val()==""){
				$('#prompt').html(title+"不能为空");
				d = false;
				return false;
			}
		}else{
			if($(ary[1]).val()==""){
				$('#prompt').html(title+"不能为空");
				d = false;
				return false;
			}
		}
	});
	return d;
}

//必填、数字、格式校验离焦事件
function validselfEvent(){
	$('#editform .mark').each(function(){
		var ary = $(this).parent().next().find("input[type=text],input[type=password]");
		var title = $(this).parent().find(".title").html();
		if(ary.length==1){
			$(ary[0]).blur(function(){
				if($(ary[0]).val()==""){
					$('#prompt').html(title+"不能为空");
				}else{
					$('#prompt').html('');
				}
			});
		}else{
			$(ary[1]).blur(function(){
				if($(ary[1]).val()==""){
					$('#prompt').html(title+"不能为空");
				}else{
					$('#prompt').html('');
				}
			});
		}
	});
}

//请求失败时,获取错误信息对应的i18n信息
function i18nMsg(exp){
	var key = exp[0].key;
	var title = $('#editform :input[name="'+key+'"]').parents('div.form-group:eq(0)').find('span.title').text();
	var message = i18n(exp[0].message, title);
	return message;
}

//新增或修改时,修改弹出框标题
function modalTitle(prefix){
	$('.optmark').html(prefix);	
}

//初始话化下拉列表
function initSelect(element, list) {
	$.each(list, function(index, value){
		$(element).append('<option value="'+value.id+'">'+value.name+'</option>');
	}); 
}
//验证非汉字
function isChn(str){
    var reg = /^[u4E00-u9FA5]+$/;
    if(!reg.test(str)){
     return false;
    }
    return true;
}

//刷新表格数据
function reloadDG() {
	var pager = $('#dg').datagrid('getPager');
	var ops = pager.pagination('options');
	var page = ops.pageNumber;
	var rows = ops.pageSize;
	$('#dg').datagrid('loadData', loadData(page, rows));
}


//表格数据初始化查询
function initDG() {
	var pager = $('#dg').datagrid('getPager');
	var ops = pager.pagination('options');
	var rows = ops.pageSize;
	$('#dg').datagrid('loadData', loadData(1, rows));
}

//时间插件
function initDatetimepicker(id) {
	$('#' + id).datetimepicker({
		format: 'yyyy-mm-dd hh:ii:ss',
		language:  'zh-CN',
        weekStart: 1,
        todayBtn:  0,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 0,
		maxView: 1,
		forceParse: 0
    });
}

//当前时间
function showDatetime(id){
	var now = new Date();  
	var year = now.getFullYear();       //年   
	var month = now.getMonth() + 1;     //月   
	month = month >= 10 ? month : ('0'+ month);
	var day = now.getDate();            //日
	day = day >= 10 ? day : ('0' + day);
	
	var hh = now.getHours(); //时
	hh = hh >= 10 ? hh : ('0' + hh);
	var mm = now.getMinutes(); //时
	mm = mm >= 10 ? mm : ('0' + mm);
	var ss = now.getSeconds(); //时
	ss = ss >= 10 ? ss : ('0' + ss);
	
	time = year + "-" + month + "-" + day + " " + hh + ":" + mm + ":" + ss;
	
	$('#'+id).val(time);
}

//日期插件
function initDatepicker(id) {
	$('#' + id).datetimepicker({
		format: 'yyyy-mm-dd',
		language:  'zh-CN',
        weekStart: 1,
        todayBtn:  0,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		maxView: 4,
		forceParse: 0
    });
}
//当前日期
function showDate(id){
	var now = new Date();  
  
	var year = now.getFullYear();       //年   
	var month = now.getMonth() + 1;     //月   
	month = month >= 10 ? month : ('0'+ month);
	var day = now.getDate();            //日
	day = day >= 10 ? day : ('0' + day);
	
	var date = year + "-" + month + "-" + day;
	$('#' + id).val(date);
}

//初始化多选下拉框
function digitSelect($element,widthval){
	var width= $element.parent().width();
	var selectWidth = (widthval==null?width:widthval);
	$element.multiselect({
		includeSelectAllOption: true,
		selectAllText: '全选',
		nonSelectedText: '请选择',
		nSelectedText:'项已选择',
		selectedClass: null,
		dropRight:true,
		buttonClass:'form-control btn-sm',
		buttonWidth:width,
        maxHeight: 300
    });
	$element.next().find('button').css("height","30px");
	$element.next().find('button').next().width(selectWidth);
}

//查询快捷键
function keySearch(elements,callback){
	$.each(elements,function(){
		$(this).keydown(function(e){
			if(e.which == 13) {
				callback();
			}
		});
	});
}

/**
 * 根据屏幕尺寸动态调整表格高度
 */
function screenResize(){
	var queryheight = $('#query').height();
	var operate = $('#operate').height();
	number = queryheight+operate+114;
	var dt=$('#commonid').val();
	var ois = document.documentElement.clientHeight;
	var heightDG = ois -number;
	if(dt == "common"){
		$('#dg').css("height",heightDG/2);
		$('#dt').css("height",heightDG/2);
		$('.resize_dg_height').css("height",heightDG/2);
	}else{
		$('#dg').css("height",heightDG);
		$('.resize_dg_height').css("height",heightDG);
	}
	DGHeight(heightDG,queryheight);
}

/**
 * 折叠查询条件面板时,动态调整表格高度
 * @param {Object} heightDG
 */
function DGHeight(heightDG,queryheight){
	//关联折叠按钮，调整页面高度
	$('#collapseOne').on('hidden.bs.collapse', function () {
			var ois = $('#query').height();
			var dt=$('#commonid').val();
			console.info(ois+36);
			if(dt=="common"){
				$('#dg').datagrid('resize',{height:(heightDG+ois+36)/2});
				$('#dt').datagrid('resize',{height:(heightDG+ois+36)/2});
				$('.resize_dg_height').css("height",heightDG/2);
			}else{
				$('#dg').datagrid('resize',{height:heightDG+queryheight-36});
				$('.resize_dg_height').css("height",heightDG+queryheight-36);
			}
	});
	$('#collapseOne').on('show.bs.collapse', function () {
		    var ois = $('#query').height();
			var dt=$('#commonid').val();
			if(dt=="common"){
			$('#dg').datagrid('resize',{height:heightDG/2});
			$('#dt').datagrid('resize',{height:heightDG/2});
			$('.resize_dg_height').css("height",heightDG/2);
			}else{
			$('#dg').datagrid('resize',{height:heightDG});
			$('.resize_dg_height').css("height",heightDG);
			}
	});
}

function findselectdata(id, parentid){
	var s_parentid = parentid || 's_parentid';
	$("#"+id).multiselect('dataprovider', []);
	$('#'+parentid).change(function(e){
		var selectdata  = [];
		var data = $(this).val();
		if(data!=null){
			var ids = null;
			$.each(data,function(i,o){
				if(i==0){
					ids=o;
				}else{
					ids = ids+","+o;
				}
			});
			var ary = findBydata(ids);
			var dataary = new Array();
			$.each(ary.rows || [],function(j,n){
				selectdata[j] = {label: n.name+n.code, value: n.id}; 
				$('#'+id).multiselect('select', n.id);
			});
			$("#"+id).multiselect('dataprovider', selectdata);
			
			$.each(ary.rows || [],function(j,n){
				$('#'+id).multiselect('select', n.id);
			});
		}else{
			$("#"+id).multiselect('dataprovider', []);
		}
	});
}


function findselectdatacode(id, parentid){
	var s_parentid = parentid || 's_parentid';
	$("#"+id).multiselect('dataprovider', []);
	$('#'+parentid).change(function(e){
		var selectdata  = [];
		var data = $(this).val();
		if(data!=null){
			var ids = null;
			$.each(data,function(i,o){
				if(i==0){
					ids=o;
				}else{
					ids = ids+","+o;
				}
			});
			var ary = findBydata(ids);
			var dataary = new Array();
			$.each(ary.rows || [],function(j,n){
				selectdata[j] = {label: n.name+n.code, value: n.code}; 
				$('#'+id).multiselect('select', n.code);
			});
			$("#"+id).multiselect('dataprovider', selectdata);
			
			$.each(ary.rows || [],function(j,n){
				$('#'+id).multiselect('select', n.code);
			});
		}else{
			$("#"+id).multiselect('dataprovider', []);
		}
	});
}



function findBydata(ids){
	    var d = null;
	    $.ajax({
	        url: "/linkage/findbydata",
	        type: "GET",
	        dataType: "json",
	        data: {ids:ids},
	        async: false,
	        success: function(data){
	            d = data;
	        },
	        error: function(XMLHttpRequest, textStatus, errorThrown){
	            if (XMLHttpRequest.status == 550) {
	                var exp = JSON.parse(XMLHttpRequest.responseText);
	                showMsg(i18n(exp[0].message));
	            }
	            var str = '{"total":0, "rows":[]}';
	            d = JSON.parse(str);
	        }
	    });
	    return d;
}

function findselectdata1(id, parentid){
	var s_parentid = parentid || 's_parentid';
	$("#"+id).multiselect('dataprovider', []);
	$('#'+parentid).change(function(e){
		var selectdata  = [];
		var data = $(this).val();
		if(data!=null){
			var ids = null;
			$.each(data,function(i,o){
				if(i==0){
					ids=o;
				}else{
					ids = ids+","+o;
				}
			});
			var ary = findBydata1(ids);
			var dataary = new Array();
			$.each(ary.rows || [],function(j,n){
				selectdata[j] = {label: n.name, value: n.id}; 
				$('#'+id).multiselect('select', n.id);
			});
			$("#"+id).multiselect('dataprovider', selectdata);
			
			$.each(ary.rows || [],function(j,n){
				$('#'+id).multiselect('select', n.id);
			});
		}else{
			$("#"+id).multiselect('dataprovider', []);
		}
	});
}
function findBydata1(ids){
	    var d = null;
	    $.ajax({
	        url: "/linkage/findbydata1",
	        type: "GET",
	        dataType: "json",
	        data: {ids:ids},
	        async: false,
	        success: function(data){
	            d = data;
	        },
	        error: function(XMLHttpRequest, textStatus, errorThrown){
	            if (XMLHttpRequest.status == 550) {
	                var exp = JSON.parse(XMLHttpRequest.responseText);
	                showMsg(i18n(exp[0].message));
	            }
	            var str = '{"total":0, "rows":[]}';
	            d = JSON.parse(str);
	        }
	    });
	    return d;
}

//加载所属分公司与所属车站    均为单选
function autoSelectBO($branchOrg, $station) {
	var selectdata = [];
	var data = getAutoData('branchorg','','').resultslist;
//	$branchOrg.append('<option value="">请选择</option>');
	$station.append('<option value="">请选择</option>');
//	$.each(data, function(index, record){
//		console.log(record[1]);
//		$branchOrg.append('<option value="'+record[0]+'">'+record[1]+'</option>');
//	}); 
	$branchOrg.change(function(){
		var selectdata  = [];
		var data = $(this).val();
		if(data){
			var ids = null;
			var ary = findBydata(data);
			$station.empty();
			$station.append('<option value="">请选择</option>');
			$.each(ary.rows,function(j,n){
				selectdata[j] = {label: n.name, value: n.id}; 
				$station.append('<option value="'+n.id+'">'+n.name+'</option>');
			});
		}else{
			$station.empty();
			$station.append('<option value="">请选择</option>');
		}
	});
}
var myview = $.extend({}, $.fn.datagrid.defaults.view, {
    renderFooter: function(target, container, frozen){
        var opts = $.data(target, 'datagrid').options;
        var rows = $.data(target, 'datagrid').footer || [];
        var fields = $(target).datagrid('getColumnFields', frozen);
        var table = ['<table class="datagrid-ftable" cellspacing="0" cellpadding="0" border="0"><tbody>'];
        
        for(var i=0; i<rows.length; i++){
            var style = ' style="background-color:#cba;"';
            table.push('<tr class="datagrid-row" datagrid-row-index="' + i + '"' + style + '>');
            table.push(this.renderRow.call(this, target, fields, frozen, i, rows[i]));
            table.push('</tr>');
        }
         
        table.push('</tbody></table>');
        $(container).html(table.join(''));
    }
});


function checkOrg(orgname){
	var d = false;
	$.ajax({
		url: '/organization/checkOrg',
		type: 'GET',
		dataType: 'json',
		async: false,
		data: {
			orgname : orgname
		},
		success: function(data){
			if(data.success){
				d = true;
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			if(XMLHttpRequest.status==550){
				var exp = JSON.parse(XMLHttpRequest.responseText);
				showMsg(i18n(exp[0].message));
			}
		}
	});
	return d;
}

function checkDepart(departid){
	var d = false;
	$.ajax({
		url: '/department/checkDepart',
		type: 'GET',
		dataType: 'json',
		async: false,
		data: {
			departid : departid
		},
		success: function(data){
			if(data.success){
				d = true;
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			if(XMLHttpRequest.status==550){
				var exp = JSON.parse(XMLHttpRequest.responseText);
				showMsg(i18n(exp[0].message));
			}
		}
	});
	return d;
}

function checkUserInfo(id){
	var d = false;
	if(id == null || id == ""){
		return true;
	}
	$.ajax({
		url: '/userinfo/checkUserInfo',
		type: 'GET',
		dataType: 'json',
		async: false,
		data: {
			userid : id
		},
		success: function(data){
			if(data.success){
				d = true;
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			if(XMLHttpRequest.status==550){
				var exp = JSON.parse(XMLHttpRequest.responseText);
				showMsg(i18n(exp[0].message));
			}
		}
	});
	return d;
}

function IsNum(e) {
    var k = window.event ? e.keyCode : e.which;
    if (((k >= 48) && (k <= 57)) || k == 8 || k == 0) {
    } else {
        if (window.event) {
            window.event.returnValue = false;
        }
        else {
            e.preventDefault(); //for firefox 
        }
    }
} 


/*验证身份证号方法入口*/
function isIDNum(idnum){
	if(idnum == null || idnum == ""){
		return true;
	}else{
		var num = idnum.toUpperCase();  
		//身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X。
		if (!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(num))) {
			return false;
		}
		//校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。
		//下面分别分析出生日期和校验位
		var len, re;
		len = num.length;
		if (len == 15) {
			re = new RegExp(/^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/);
			var arrSplit = num.match(re);
			//检查生日日期是否正确
			var dtmBirth = new Date('19' + arrSplit[2] + '/' + arrSplit[3] + '/' + arrSplit[4]);
			var bGoodDay,goodyear;
			bGoodDay = (dtmBirth.getYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));
			if (!bGoodDay) {
				return false;
			} else {
				//将15位身份证转成18位
				//校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。
				var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
				var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
				var nTemp = 0, i;
				num = num.substr(0, 6) + '19' + num.substr(6, num.length - 6);
				for (i = 0; i < 17; i++) {
					nTemp += num.substr(i, 1) * arrInt[i];
				}
				num += arrCh[nTemp % 11];
				return true;
			}
		}
		if (len == 18) {
			re = new RegExp(/^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/);
			var arrSplit = num.match(re);
			//检查生日日期是否正确
			var dtmBirth = new Date(arrSplit[2] + "/" + arrSplit[3] + "/" + arrSplit[4]), bGoodDay,thisYear = new Date().getFullYear();
			bGoodDay = (dtmBirth.getFullYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));
			if (!bGoodDay) {
				return true;
			} else if(dtmBirth.getFullYear() > new Date().getFullYear()){//判断出生年份是否大于当前年份
				return false;
			}else if(dtmBirth.getFullYear() < 1900){
				return false;
			}else{
				//检验18位身份证的校验码是否正确。
				//校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。
				var valnum;
				var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
				var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
				var nTemp = 0, i;
				for (i = 0; i < 17; i++) {
					nTemp += num.substr(i, 1) * arrInt[i];
				}
				valnum = arrCh[nTemp % 11];
				if (valnum != num.substr(17, 1)) {
					return false;
				}
				return true;
			}
		}
		return false; 
	}
}

function isEmail(email){
	var emailReg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if(email == null || email == ""){
		return true;
	}else{
		if(emailReg.test(email)){
			return true;
		}
	}
	return false;
}


/**
 * 清空表单数据
 */
function clearForm(){
	$('#prompt').html('');
	$('#editform')[0].reset();
	$('#editform :input[name!="authenticityToken"][type="hidden"]:not(#idstr)').each(function(){
		$(this).val('');
	});
	$('#editform :input.combo-value').each(function(){
		var $target = $(this).parent().prev();
		$target.combogrid('clear');
	});
}
