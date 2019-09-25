package service;

import javax.servlet.http.HttpServletRequest;

import utils.AlgorithmUtils;
import utils.StringUtils;

public class LoginService {
	
	/**
	 * 验证用户是否登录
	 * @author likaihao
	 * @date 2016年7月10日 下午5:58:41
	 * @param request
	 * @return
	 */
	public boolean checkLogin(HttpServletRequest request){
		String sessionStr = (String) request.getSession().getAttribute("userinfo");
		if(StringUtils.isEmpty(sessionStr)){
			return false;
		}
		
		//解密
		try {
			String desKey = new ParamsService().findParamsByName("desKey").getValue();
			byte[] sessionData = AlgorithmUtils.decodeBase64(sessionStr);
			sessionData = AlgorithmUtils.decryptDES(sessionData, desKey.getBytes());
			sessionStr = new String(sessionData);
		} catch (Exception e) {
			request.getSession().removeAttribute("userinfo");
			return false;
		}

		String username = sessionStr.split("_")[0];
		String password = sessionStr.split("_")[1];
		return checkUserPassword(username,password);
	}
	
	
	/**
	 * 验证用户密码是否正确
	 * @author likaihao
	 * @date 2016年5月26日 下午1:48:17
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean checkUserPassword(String username,String password){
		ParamsService ps = new ParamsService();
		String u = ps.findParamsByName("login_username").getValue();
		String p = ps.findParamsByName("login_password_md5").getValue();
		if(username.equals(u) && AlgorithmUtils.getMD5Value(password).equals(p)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 验证验证码
	 * @author likaihao
	 * @date 2016年7月10日 下午6:00:34
	 * @param request
	 * @param checkNum
	 * @return
	 */
	public boolean checkCheckNum(HttpServletRequest request, String checkNum) {
		String sessionCheckNum = (String)request.getSession().getAttribute("checkNum");
		request.getSession().removeAttribute("checkNum");
		if(StringUtils.isNotEmpty(checkNum) && checkNum.equals(sessionCheckNum)){
			return true;
		}else{
			return false;
		}
	}
}