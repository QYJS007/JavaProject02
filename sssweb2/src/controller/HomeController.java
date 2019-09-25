package controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sys.SystemConf;
import utils.IOUtils;
import utils.JsonUtils;
import utils.StringUtils;

@Controller
@Scope("prototype")
@RequestMapping("/")
public class HomeController extends BaseController {
	
	//json格式化
	@RequestMapping("/jsonFormat.do")
	@ResponseBody
	public void jsonFormat(String json){
		writeStrToPage(JsonUtils.getFormatJsonStr(json));
	}
	
	//返回首页
	@RequestMapping("/index.do")
	public String index() {
		return "index.html";
	}
	
	//返回freme
	@RequestMapping("/pageFrame.do")
	public String pageFrame() {
		return "pageFrame.html";
	}
	
	//返回首页2
	@RequestMapping("/index2.do")
	public String index2() {
		return "index2/index.html";
	}
	
	//返回使用说明页面
	@RequestMapping("/help.do")
	public String help() {
		return "help/help.html";
	}
	
	//提供下载功能
	@RequestMapping("/download.do")
	@ResponseBody
	public void download(String filePath) throws Exception{
		String baseDir = SystemConf.getProjectPath()+"/public/download";
		if(filePath==null || filePath.length()==0){
			//查询所有文件
			List<File> fileList = IOUtils.getFileListByPattern(baseDir, null, null, null, null);
			StringBuilder builder = new StringBuilder();
			for(File file : fileList){
				String subPath = file.getAbsolutePath().substring(baseDir.length()).replace("\\", "/");
				builder.append("<a href=\""+SystemConf.getProjectBaseUrl()+"/download.do?filePath="+StringUtils.urlEncoding(subPath)+"\" style=\"color:#06c; text-decoration:none;\">"+subPath + "</a><br>");
			}
			writeStrToPage("共找到"+fileList.size()+"个文件:<br>"+builder.toString());
		}else{
			//下载指定文件(不允许存在相对路径)
			if(filePath.contains("..")){
				throw new RuntimeException("非法文件名称");
			}
			File file = new File(baseDir,filePath);
			if(!file.exists()){
				throw new RuntimeException("文件不存在:"+filePath);
			}
			
			FileInputStream in = new FileInputStream(file);
			byte[] bytes = IOUtils.inputStreamToByteArray(in);
			response.setHeader("content-disposition", "attachment;filename="+new String(file.getName().getBytes("gbk"),"iso8859-1"));
			response.getOutputStream().write(bytes);
		}
	}
}