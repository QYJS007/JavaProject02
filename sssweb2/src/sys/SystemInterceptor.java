package sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import controller.BaseController;

public class SystemInterceptor implements HandlerInterceptor {

	/**
	 * 执行时机：在controller执行之前执行
	 * 返回值:true表示放行，false表示不放行
	 * 主要作用:权限控制,没有权限时用response转发到错误页面
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object action) throws Exception {
		//填充request和response,执行_before方法
		if (action instanceof BaseController) {
			BaseController controller = (BaseController) action;
			controller.request = request;
			controller.response = response;
			boolean beforeResult = controller._before();
			if(!beforeResult){
				//如果验证不通过
				return false;
			}
		}
		
		//放行
		return true;
	}
	
	/**
	 * 执行时机:在controller执行完成并且在视图解析器解析视图之前
	 * 主要作用:处理ModelAndView
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object active, ModelAndView mv) throws Exception {
		
	}
	
	
	/**
	 * 执行时机:视图解析器解析页面完成之后
	 * 主要作用:发生异常后的日志记录(不能处理异常),相当于try..catch..finally中的finally
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object active, Exception e)
			throws Exception {
		
	}
}
