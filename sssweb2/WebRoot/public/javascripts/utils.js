//发送ajax请求
function ajax(str1,str2,fn_ok,fn_no){		//通过此函数向服务器请求数据  参数中包含成功和失败的回调函数
	//1.返回ajax对象
	var oAjax=null;
	if(window.XMLHttpRequest){
		oAjax=new XMLHttpRequest();		//适用于IE6以外的浏览器
	}else{
		oAjax=new ActiveXObject("Microsoft.XMLHTTP");
	}
	//2.连接服务器
	oAjax.open("post",str1,true);
	//3.发送请求
	oAjax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	oAjax.send(str2);
	//4.接收返回
	oAjax.onreadystatechange=function(){
		if(oAjax.readyState==4){
			if(oAjax.status==200){			//服务器已响应,接下来判断数据是否更新成功到数据库
				if(fn_ok){					//调用成功的回调函数
					fn_ok(oAjax.responseText);
				}
			}else{
				if(fn_no){					//调用失败的回调函数
					fn_no();
				}else{
					alert("发送请求失败\nurl: "+str1+"\n原因: "+oAjax.responseText);
				}
			}
		}
	};
	return oAjax;
};

//发送ajax,并保持连接不断开
function ajax_long(str1,str2,fn_doing,fn_ok,fn_no){		//通过此函数向服务器请求数据  参数中包含成功和失败的回调函数
	//1.返回ajax对象
	var oAjax=null;
	if(window.XMLHttpRequest){
		oAjax=new XMLHttpRequest();		//适用于IE6以外的浏览器
	}else{
		oAjax=new ActiveXObject("Microsoft.XMLHTTP");
	}
	//2.连接服务器
	oAjax.open("post",str1,true);
	//3.发送请求
	oAjax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	oAjax.send(str2);
	//4.接收返回
	oAjax.onreadystatechange=function(){
		if(oAjax.readyState==3 || oAjax.readyState==4){
			if(oAjax.status==0 || oAjax.status==200){			//服务器已响应,接下来判断数据是否更新成功到数据库
				if(oAjax.readyState==3 && fn_doing){					//调用成功的回调函数
					var returnVal = fn_doing(oAjax.responseText);
					if(returnVal=='exit'){
						oAjax.onreadystatechange = null;
					}
				}else if(oAjax.readyState==4 && fn_ok){
					fn_ok(oAjax.responseText);
				}
			}else{
				alert("发送请求失败\nurl: "+str1+"\n原因: "+oAjax.responseText);
				if(fn_no){					//调用失败的回调函数
					fn_no(oAjax.responseText);
				}
			}
		}
	};
	return oAjax;
};

//获取样式
function getStyle(obj, attr){	//此方法可取出元素的style属性
	if(obj.currentStyle){
		return obj.currentStyle[attr];
	}else{
		return getComputedStyle(obj, false)[attr];	//非IE
	}
}

//复制功能, btnId 按钮id, callback 回调函数,返回值会被保存到剪贴板中
function copy(btnId,callback){
	var clip = new ZeroClipboard.Client();
  	clip.setHandCursor(true);  	
  	clip.addEventListener('mouseOver', function (client) {
    	clip.setText( callback() );
  	});
  	clip.glue(btnId);
  	
  	//为了主页面加载完成,这样获取的位置才正确
  	setTimeout(function(){
  		var oBtn = document.getElementById(btnId);
  		var oFlash = document.getElementById('ZeroClipboardMovie_1');
  		oFlash.parentNode.style.left = oBtn.offsetLeft + 'px';
  	},1000);
}

//选项卡
function tab(sName){
	var aParent=document.getElementsByClassName(sName);
	for(var i=0;i<aParent.length;i++){
		_tab(aParent[i]);
	}
	function _tab(oParent){
		var aBtn=oParent.getElementsByClassName('j-btn');
		var aBox=oParent.getElementsByClassName('j-box');
		for(var i=0;i<aBtn.length;i++){
			(function(index){
				aBtn[index].box = aBox[index];
				aBtn[index].onclick=function(){
					for(var j=0;j<aBtn.length;j++){
						aBtn[j].className='j-btn';
						aBox[j].className='j-box';
					}
					this.className='active j-btn';
					this.box.className='active j-box';
					
					//如果aBox[index]下的iframe的src为空, 且aBtn存在page属性,则赋值
					var iframe = this.box.children[0];
					if(iframe && !iframe.src && aBtn[index].getAttribute('page')){
						iframe.src = aBtn[index].getAttribute('page');
					}
				};
			})(i);
		}
	}
}

//添加文本编辑器
function createEditor(parentDivId,isAddMode){
	var parentDiv = document.getElementById(parentDivId);
	var oDiv = document.createElement('div');
	oDiv.id = "a"+Number(Math.random()*10000);
	oDiv.style.width=parentDiv.offsetWidth+'px';
	oDiv.style.height=parentDiv.offsetHeight+'px';
	oDiv.style.fontFamily='Consolas';
	oDiv.style.fontSize='13px';
	oDiv.style.border="1px solid rgb(204, 204, 204)";
	parentDiv.appendChild(oDiv);
	
	var editor = ace.edit(oDiv.id);
	if(isAddMode){
		var JavaScriptMode = require("ace/mode/php").Mode;
	    editor.getSession().setMode(new JavaScriptMode());//设置代码模式
	    editor.setTheme("ace/theme/eclipse");//设置主题
	}
	editor.renderer.setShowGutter(false);//隐藏行号
	editor.setHighlightActiveLine(false);//去除当前行高亮显示
	editor.renderer.setShowPrintMargin(false);//去除中间一道竖线
	//给对象添加方法
	editor.setContentValue = function(str,isScorllBottom){
		//设置值
		editor.session.setValue(str);
		//将横向滚动条设置到最左
		function clear(){
			editor.container.children[1].scrollLeft=0;
		}
		setTimeout(clear,10);
		setTimeout(clear,50);
		//将纵向滚动条设置到最下
		if(isScorllBottom){
			editor.gotoLine(999999);
		}
	};
	editor.getContentValue = function(){
		return editor.session.getValue();
	};
	
	return editor;
}