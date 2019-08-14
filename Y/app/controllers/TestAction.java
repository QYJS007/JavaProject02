package controllers;

import java.util.Map;

import play.mvc.Controller;
import utils.JSONUtil;
import bp.pub_bp.DubboOrgcodeBP;
import bp.pub_bp.VehicleschedulingDubboBP;

import com.alibaba.fastjson.JSON;

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

}
