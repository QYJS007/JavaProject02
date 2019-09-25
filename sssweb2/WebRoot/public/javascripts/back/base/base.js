var operatorid;
$(function(){
	screencount();
	$('body').css({"overflow-x":"auto","overflow-y":"hidden"});
    $("#mtree li").find(".firstMenu").removeClass("fir_show");
    $('#tabs').tabs({
		tools: '#tools',
		toolPosition: 'left',
		width:$(window).width()-150,
		height:parseFloat($(window).height())*0.9
    });
	
});

function screencount(){
	var ary = $('.countclass');
	$.each(ary,function(i,o){
		$(o).next().css("background","#3475c2");
	});
}
function mouseEvent(obj){
    $(obj).mouseover(function(){
        $(obj).find(".firstMenu").addClass("fir_show");
        $(obj).find(".subMenu").show();
    }).mouseout(function(){
        $(obj).find(".firstMenu").removeClass("fir_show");
        $(obj).find(".subMenu").hide();
    });
}

function f(menuname,menuurl){
    $('#tabs').css("display", "block");
    $('#tableWrap').css("display", "none");
    $("#mtree li").find(".firstMenu").removeClass("fir_show");
    $("#mtree li").find(".subMenu").hide();
    addTab(menuname,menuurl);
}

//调整tab页尺寸
function resizeTab(){
    var tab = $('#tabs').tabs('getSelected');
    var panel = tab.panel('panel');
    $(panel).removeAttr('style');
    $(tab).removeAttr('style').attr('style', 'padding:0 15px 0 0');
}
//添加tab页
function addTab(title,href) {
//	if ($('#tabs').tabs('exists', title)) {//标签页是否存在,存在刷新,不存在添加
//		var tab = $('#tabs').tabs('select', title);
//    }else{
		var contens = ''+
		'<div class="iframe">' + 
	    '<iframe src="' + href + '" style="width:100%;height:100%;border:0;"></iframe>' +
	    '</div>';
		 $('#tabs').tabs('add', {
		        title: title,
		        content: contens,
		        fit: true,
		        closable: true,
		        selected: true
	    });
//	}
}

//添加tab页
function refreshTab(title,href) {
	var tab = $('#tabs').tabs('getTab',title);
	var contens = '<div class="iframe" style="width:100%;height:99%;">' +
    '<iframe src="' +
    href +
    '" style="width:100%;height:100%;border:0;"></iframe></div>';
	 $('#tabs').tabs('update', {
	        tab: tab,
	        options: {
				content: contens
			}
    });
}
function close(title){
	var tab = $('#tabs').tabs('getTab',title);
	var index = $('#tabs').tabs('getTabIndex', tab);
	$('#tabs').tabs('close', index);

}

//菜单项单击触发事件
function menuHandler(item){
	var t = $('#tabs');
	var item = item.text;
	if(item=="关闭所有页") {
		var tabs = t.tabs('tabs');
		for(var i = tabs.length - 1; i >= 0; i--) {
			if(tabs[i].panel('options').title != "主页"){
				t.tabs('close', i);
			}
		}
	}else if(item=="关闭当前页") {
		var tab = t.tabs('getSelected');
		if(tab.panel('options').title!="主页"){
			var index = t.tabs('getTabIndex', tab);
			t.tabs('close', index);
	    }
	}else if(item == "刷新当前页") {
		var tab = t.tabs('getSelected');
		var src = tab.find('iframe')[0].src;
		tab.find('iframe')[0].contentWindow.location.href=src;//刷新tab页
	}
}
function indexf(obj,url,title){
	parent.f(obj,url,title);
}
function esc(){
	var msg = "你确定要退出吗";
	$.messager.confirm("确认", msg, function (r) {  
        if (r) {  
        	window.location = "/sssweb2/";
        }  
    });  
}	

/*判断字符串是否为空*/
function isEmpty(str) {
	if(str == null || str == "") {
		return true;
	}else{
		return false;
	}
}
