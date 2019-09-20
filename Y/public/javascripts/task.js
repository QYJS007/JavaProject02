function querydriver(){
	$("#settData2").text("正在获取...");
	$.ajax({
		url :"./pullcenterdrivers",
		type:"GET",
		data: {},
		success: function(data){
			$("#settData2").text(data);
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			// alert("网络异常，请稍后在试！");
			$("#settData2").text("ERROR!!");
			return;
		}
	});
}

function queryvehicle(){
	$("#settData3").text("正在获取...");
	$.ajax({
		url :"./pullCenterVehicles",
		type:"GET",
		data: {},
		success: function(data){
			$("#settData3").text(data);
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			// alert("网络异常，请稍后在试！");
			$("#settData3").text("ERROR!!");
			return;
		}
	});
}
function pullMileInfo(){
alert("11123231");
	$("#settData4").text("正在获取...");
	Object paramMap = new Object;  
	paramMap['status'] =1;
	$.ajax({
		url :"./pullMileInfo",
		type:"GET",
		data: {
			"params": paramMap
		},
		success: function(data){
			$("#settData4").text(data);
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			// alert("网络异常，请稍后在试！");
			$("#settData4").text("ERROR!!");
			return;
		}
	});
}