//自动补全
function autocomplete($com,type,idField,textField,params,setConnect,clearConnect){
	params = params || {};
	setConnect = setConnect || function(){};
	clearConnect = clearConnect || function(){};
	var flag = true;
	var setting = commonSetting(getAutoCompleteData(type,'',params));
	$com.combogrid({
		panelWidth:setting[1],
		idField: idField,
		textField: textField,
		hasDownArrow:false,
		columns: setting[0],
		onSelect:function(rowIndex,rowData){
			setConnect(rowIndex,rowData);
			flag = false;
		},
		onChange:function(nowValue,oldValue){
			var data = commonData(getAutoCompleteData(type,nowValue,params));
			var regex = new RegExp("^\\s*$");
			var isEmpty = regex.test(nowValue);
			if(flag && !isEmpty) {
				$com.combogrid('grid').datagrid('loadData', data);
			}
			if(isEmpty || data.rows.length <= 0) {
				clearConnect();
			}
			flag = true;
		}
	});
	snt($com);
}

//调整样式
function snt($com){
    var obj = $com.combo('textbox');
    
	obj.removeClass("validatebox-text").removeAttr('style').css("background", "##CCE8CF")
    	.parent().removeClass("combo").removeAttr('style').css("width", "100%");
    if(obj.parent().prev().hasClass('form-control')){
		if(obj.parent().prev().hasClass('input-sm')){
        	obj.addClass("input-sm");
		}
    	obj.addClass("form-control");
    	obj.css("display", "inline-block");
    }
}

//动态表头
function commonSetting(obj) {
	var column = '[[';
	var sNumber = 0;
	var len = (obj == null)?0:obj.nameList.length;
	for(var i=0;i<len;i++) {
		if(obj.fieldList[i].slice(-1) == ' ') {//field最后一个字符为空格,则该列隐藏
			column += '{"field":"'+obj.nameList[i]+'","hidden":"true"},';
		}else{
			column += '{"field":"'+obj.nameList[i]+'","title":"'+obj.fieldList[i]+'","width":'+obj.fieldWidthList[i]+'},';
			sNumber += obj.fieldWidthList[i];
		}
	}
	if(len > 0) {
		column = column.slice(0,-1);
	}
	column += ']]';
	
	column = JSON.parse(column);
	var array = new Array();
	array[0] = column;
	array[1] = sNumber+obj.fieldWidthList[0];
	return array;
}

//列表数据
function commonData(obj) {
	var data = '';
	if(obj == null) {
		data = '{"total":"0","rows":[]}';
		data = JSON.parse(data);
		return data;
	}
	data = '{"total":"'+obj.resultCount+'","rows":[';
	var len = obj.resultList.length;
	for(var i=0;i<len;i++) {
		data += '{';
		if(obj.nameList.length == 1){
			data += '"' + obj.nameList[0] + '":"' + mergeNull(obj.resultList[i]) + '",';
		}else{
			for(var j=0;j<obj.nameList.length;j++) {
				data += '"' + obj.nameList[j] + '":"' + mergeNull(obj.resultList[i][j]) + '",';
			}
		}
		data = data.slice(0,-1);
		data += '},';
	}
	if(len > 0) {
		data = data.slice(0,-1);
	}
	data += ']}';
	data = JSON.parse(data);
	return data;
}

//控制空字符串
function mergeNull(str) {
	if (str == null) {
        return "";
    }
    return str;
}

//获得html转义字符串
function getHtmlEscapeString(str){
	return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/, "&gt;");
}

//获取自动补全的数据
function getAutoCompleteData(type,text,params) {
    var d = null;
    $.ajax({  
        url: "/sssweb2/OtherBackController/autoComplete.do",
        type: "GET",
        dataType: "json",
        data: {
            type: type,
            text: text,
			params: params
        },
        async: false,
        success: function(data){
            d = data;
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if (XMLHttpRequest.status == 550) {
                var exp = JSON.parse(XMLHttpRequest.responseText);
				showMsg(i18n(exp[0].message));
            }
        }
    });
    return d;
}

//获取下拉列表的值(读取数据字典详情)
function dictSelect($element,tableName,columnName,selectNameValueObj){
	$.ajax({  
        url: "/sssweb2/OtherBackController/queryDictionaryDetail.do",
        type: "GET",
        dataType: "json",
        data: {
            tableName: tableName,
            columnName: columnName
        },
        success: function(data){
            for(var i = 0; i < data.length; i++){
				$element.append('<option value="'+data[i].value+'">'+data[i].showText+'</option>');
				if(selectNameValueObj){
					selectNameValueObj[data[i].value] = data[i].showText;
				}
			}
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if (XMLHttpRequest.status == 550) {
                var exp = JSON.parse(XMLHttpRequest.responseText);
				showMsg(i18n(exp[0].message));
            }
        }
    });
}

//获取下拉列表的值(从数据库统计)
function dictSelectByCount($element,modelName,columnName){
	$.ajax({  
        url: "/sssweb2/OtherBackController/countTable.do",
        type: "GET",
        dataType: "json",
        data: {
        	modelName: modelName,
            columnName: columnName
        },
        success: function(data){
            for(var i = 0; i < data.length; i++){
				$element.append('<option value="'+data[i]+'">'+data[i]+'</option>');
			}
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if (XMLHttpRequest.status == 550) {
                var exp = JSON.parse(XMLHttpRequest.responseText);
				showMsg(i18n(exp[0].message));
            }
        }
    });
}

//获取下拉列表的值(从数据库统计)(返回value和text两列)
function dictSelectByCountValueText($element,tableName,valueColumn,textColumn,selectNameValueObj){
	$.ajax({  
        url: "/sssweb2/OtherBackController/countTableValueText.do",
        type: "GET",
        dataType: "json",
        data: {
        	tableName: tableName,
            valueColumn: valueColumn,
            textColumn: textColumn
        },
        success: function(data){
            for(var i = 0; i < data.length; i++){
				$element.append('<option value="'+data[i][valueColumn]+'">'+data[i][textColumn]+'</option>');
				if(selectNameValueObj){
					selectNameValueObj[data[i].value] = data[i].showText;
				}
			}
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if (XMLHttpRequest.status == 550) {
                var exp = JSON.parse(XMLHttpRequest.responseText);
				showMsg(i18n(exp[0].message));
            }
        }
    });
}