package controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import service.LoginService;
import service.SshClientService;
import sys.SystemConf;
import utils.ExceptionUtils;
import utils.JsonUtils;
import utils.LoggerUtils;
import utils.SpringDateConvert;

public class BaseController {
	
	//由拦截器自动赋值
	public HttpServletRequest request = null;
	public HttpServletResponse response = null;
	
	
	/**
	 * 执行所有action前执行
	 * @author likaihao
	 * @date 2016年7月8日 下午11:59:16
	 * @return 是否放行,可在方法内部进行重定向
	 */
	public boolean _before(){
		if(request==null || response==null){
			throw new RuntimeException("请先给request和response赋值!");
		}
		
		try {
			//打印日志
			Map<String,String[]> paramMap = request.getParameterMap();
			Map<String,String> paramMap2 = new HashMap<String,String>();
			for(String name : paramMap.keySet()){
				String[] valueArr = paramMap.get(name);
				if(valueArr.length==1){
					paramMap2.put(name, valueArr[0]);
				}else{
					paramMap2.put(name, Arrays.toString(valueArr));
				}
			}
			LoggerUtils.info("接收请求,"+request.getRequestURI()+",参数:"+ paramMap2+",ip:"+getCurrentRequestRemoteAddr());
			
			//检查登录状态
			String requestUri = request.getRequestURI();
			if(!requestUri.endsWith("/") && !requestUri.contains("/LoginController/") 
					&& !requestUri.contains("/t/")
					&& !requestUri.contains("download.do")){
				if(!getCurrentRequestRemoteAddr().equals("127.0.0.1") && !new LoginService().checkLogin(request)){
					LoggerUtils.info("检测到没有登录的请求:"+requestUri);
					response.sendRedirect(SystemConf.getProjectBaseUrl()+"/LoginController/login_page.do");
					return false;
				}
			}
			
			//重置定时器
			if(!request.getRequestURI().endsWith("/SshClientController/state.do") &&!request.getRequestURI().endsWith("/SshClientController/allConn.do")){
				if(SshClientService.noActiveTimerManager!=null){
					SshClientService.noActiveTimerManager.reset();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return true;
	}
	
	/**
	 * 注册日期格式
	 * @author likaihao
	 * @date 2016年7月20日 下午3:27:08
	 * @param binder
	 */
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder){
		binder.registerCustomEditor(Date.class, new SpringDateConvert());
	}
	
	/**
	 * 当发生异常时执行的方法
	 * @author likaihao
	 * @date 2016年7月20日 下午3:27:32
	 * @param e
	 * @return
	 */
	@ExceptionHandler
	public String _catch(Exception e){
		//打印日志
		LoggerUtils.error("出现未处理的异常",e);
		
		String errMsg = ExceptionUtils.getErrMsg(e);
		
		//发送错误消息到页面
		response.setStatus(550);
		writeStrToPage(errMsg);
		return null;
	}
	
	/**
	 * 发送字符串
	 * @author likaihao
	 * @date 2015年12月15日 下午5:38:36
	 * @param str
	 */
	public void writeStrToPage(String str){
		try {
			response.getOutputStream().write(str.getBytes("utf-8"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 向页面发送json字符串
	 * @author likaihao
	 * @date 2015年12月15日 下午5:38:36
	 * @param str
	 */
	public void writeJsonToPage(Object obj){
		String jsonStr = JsonUtils.parseObject(obj);
		writeStrToPage(jsonStr);
	}
	
	//获取当前线程的response的输出流
	public OutputStream getCurrentResponseOutputStream(){
		try {
			return response.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("获取当前response的输出流失败");
		}
	}
	
	
	
	//获取远程ip地址
	public String getCurrentRequestRemoteAddr(){
//			Enumeration<String> itor = request.getHeaderNames();
//			while(itor.hasMoreElements()){
//				String name = itor.nextElement();
//				String value = request.getHeader(name);
//				System.out.println(name +" --> "+value);
//			}
		//注意head的大小写和 -_ 可能不同web容器不一样
		String ip = request.getHeader("x_forwarded_for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("Proxy_Client_IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("WL_Proxy-Client_IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
	
}