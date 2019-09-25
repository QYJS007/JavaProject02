package service;

import model.generate.Params;
import service.generate.ParamsGenService;


public class TempletEditorService {
	
	ParamsService paramsService = new ParamsService();
	ParamsGenService paramsGenService = new ParamsGenService();
	
	//templetEditor功能相关的参数key
	private final static String templetEditorStrKey = "templetEditorStr";
	
	//获得templetEditor功能相关的参数
	private Params getTempletEditorStrParams(){
		Params params = paramsService.findParamsByName(templetEditorStrKey);
		//如果首次不存在,则自动添加一个
		if(params==null){
			params = new Params();
			params.setName(templetEditorStrKey);
			params.setValue("");
			params.setRemark("模板文本编辑-保存的模板内容");
			paramsGenService.save(params);
		}
		return params;
	}
	
	//获得保存的模板内容
	public String getTempletStr(){
		Params params = getTempletEditorStrParams();
		return params.getValue();
	}
	
	//保存模板内容
	public void save(String templetStr){
		//查询
		Params params = getTempletEditorStrParams();
		//更新
		params.setValue(templetStr);
		paramsGenService.update(params);
	}
	
}
