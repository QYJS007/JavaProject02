package service.model;

import java.util.List;

public class Inter {
	private String name;
	private String url;
	private String method;
	private Model requestModel;
	private Model responseModel;
	private List<Model> modelList;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Model getRequestModel() {
		return requestModel;
	}
	public void setRequestModel(Model requestModel) {
		this.requestModel = requestModel;
	}
	public Model getResponseModel() {
		return responseModel;
	}
	public void setResponseModel(Model responseModel) {
		this.responseModel = responseModel;
	}
	public List<Model> getModelList() {
		return modelList;
	}
	public void setModelList(List<Model> modelList) {
		this.modelList = modelList;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("接口名称:"+name+"\r\n");
		builder.append("url:"+url+"\r\n");
		builder.append("method:"+method+"\r\n");
		builder.append("\r\n请求实体:\r\n");
		builder.append(requestModel);
		builder.append("\r\n响应实体:\r\n");
		builder.append(responseModel);
		builder.append("\r\n其它实体:\r\n");
		for(Model model : modelList){
			builder.append("\r\n");
			builder.append(model);
		}
		return builder.toString();
	}
}
