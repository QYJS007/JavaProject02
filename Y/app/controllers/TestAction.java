package controllers;

import java.util.Map;

import play.mvc.Controller;
import utils.JSONUtil;
import bp.pub_bp.DubboCJPCServiceBP;
import bp.pub_bp.DubboOrgcodeBP;
import bp.pub_bp.VehicleschedulingDubboBP;

import com.alibaba.fastjson.JSON;

import dubbo.api.qtrip.vo.wyc.OrderInfoVo;
import dubbo.api.qtrip.vo.wyc.response.CJPCListResponse;
import dubbo.api.uniplateform.dto.ResponseDto;


public class TestAction extends   Controller {

	public static void dubboTest(String orgcode) {
		DubboOrgcodeBP dubboOrgcodeBP = new DubboOrgcodeBP();
		ResponseDto findOrgcodebyorgcodes = dubboOrgcodeBP.findOrgcodebyorgcodes(orgcode);
		renderText(JSON.toJSON(findOrgcodebyorgcodes));
	}
	public static void getRouteListByOrgcode(String orgcode) {
		VehicleschedulingDubboBP dubboOrgcodeBP = new VehicleschedulingDubboBP();
		 dubboOrgcodeBP.getRouteListByOrgcode();
		renderText("OKOK");
	}
	
	
	/**
	 * 测试Map接收参数
	 */
	public static void pullMileInfo(Map<String, Object> params ) {
	
		System.out.println(JSONUtil.parseObject(params));
	}
	public static void getRoute() {
	
	}
	
	public static void testsd() {
		DubboCJPCServiceBP bp = new DubboCJPCServiceBP();
		CJPCListResponse<OrderInfoVo> dubboTest = bp.dubboTest(1L);
		renderText(JSONUtil.parseObject(dubboTest));
	}

}
