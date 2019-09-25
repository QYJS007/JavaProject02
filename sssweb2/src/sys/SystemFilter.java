package sys;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class SystemFilter implements Filter{

	
	@Override
	public void init(FilterConfig config) throws ServletException {
		//填充项目名称和项目路径
		SystemConf.projectPath = config.getServletContext().getRealPath("");
		SystemConf.projectName = SystemConf.projectPath.substring(SystemConf.projectPath.lastIndexOf(File.separator)+1);
		
		//设置log4j日志文件的路径(设置系统属性,log4j读取)
		System.setProperty("projectParentPath", new File(SystemConf.projectPath).getParent());
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		//处理乱码问题
		// * 强转
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		// * post
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		// * get
		HttpServletRequest request2 = new MyRequest(request);//MyRequest可处理get中文乱码
		
		//放行
		chain.doFilter(request2, response);
	}

	@Override
	public void destroy() {
		
	}
	

	
	
}


/**
 * 自定义的用于增强tomcat的request对象的类
 * @author Administrator
 * HttpServletRequestWrapper为HttpServletRequest接口的装饰类,不过无任何增强的方法,供编程人员使用
 */
class MyRequest extends HttpServletRequestWrapper{

	public MyRequest(HttpServletRequest request) {
		super(request);//父类缓存起来了
	}
	
	//增强方法
	public String getParameter(String name){
		//获取原来的值
		String value = super.getParameter(name);
		if(value == null){
			return null;
		}
		
		//对get请求乱码问题处理
		if(super.getMethod().equalsIgnoreCase("get")){
			//get请求
			try {
				value = new String(value.getBytes("ISO8859-1"),super.getCharacterEncoding());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return value;
	}
	
	//增强方法
	public String[] getParameterValues(String name) {
		//获取原来的值
		String[] valueArr = super.getParameterValues(name);
		if(valueArr == null){
			return null;
		}
		
		//对get请求乱码问题处理
		if(super.getMethod().equalsIgnoreCase("get")){
			//get请求
			try {
				for(int i=0;i<valueArr.length;i++){
					valueArr[i] = new String(valueArr[i].getBytes("ISO8859-1"),super.getCharacterEncoding());
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return valueArr;
	}
	
	//增强方法
	public Map<String,String[]> getParameterMap(){
		Map<String,String[]> map = super.getParameterMap();
		try {
			if(super.getMethod().equalsIgnoreCase("get")){
				Map<String,String[]> map2 = new HashMap<String,String[]>();
				for(String key : map.keySet()){
					String[] value = map.get(key);
					for(int i=0;i<value.length;i++){
						value[i] = new String(value[i].getBytes("ISO8859-1"),super.getCharacterEncoding());
					}
					map2.put(key, value);
				}
				map = map2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}