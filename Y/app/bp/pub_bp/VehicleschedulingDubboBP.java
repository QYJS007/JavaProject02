package bp.pub_bp;

import play.Logger;
import utils.JSONUtil;
import conf.ResourceConfig;
import dubbo.api.qtrip.vo.ResultVO;
import dubbo.api.uniplateform.dto.MechanismDto;
import dubbo.api.vehiclescheduling.bp.DubboOrgInfoInterface;
import dubbo.api.vehiclescheduling.consts.Consts;
import dubbo.play.proxy.core.ConsumerProxyUtil;
import dubbo.play.proxy.core.DubboReferenceProperties;


public class VehicleschedulingDubboBP {

	private static String appName = "vehiclescheduling";
//	private static String appName = "busdeviceserver";
	
	// 和DUBBO服务端要对应
	private static String group ="wwwd-vehiclescheduling";
//	private static String group ="wwwt-vehiclescheduling";

	// 和DUBBO服务端要对应
//	private static String version = "dubbo_version_1.0.0";
    private static String version = ResourceConfig.getValue("dubbo.bus365.version");

	private static boolean isMonitor = Boolean.getBoolean(ResourceConfig.getValue("dubbo.bus365.ismonitor"));

	private static int timeOut = Integer.parseInt(ResourceConfig.getValue("dubbo.bus365.timeout"));

	private static DubboReferenceProperties drp = new DubboReferenceProperties();

	static {
		drp.setAppName(appName);
		drp.setGroup(group);
		drp.setVersion(version);
		drp.setMonitor(isMonitor);
		drp.setReferenceTimeOut(timeOut);
	}

	/** 根据机构获取线路集合
	 * @return
	 */
	public void getRouteListByOrgcode(){

		drp.setServiceImplFullname(Consts.BpImplFullName.DubboOrgInfoInterface.getCode());
 
		DubboOrgInfoInterface  bp = null;
 
		Logger.applogInfoDetail(null, "企业dubbo接口调用 ", "开始调用dubbo+++++++++++++++++++++++++++++++++++++++++++");

		try {
			bp = ConsumerProxyUtil.getProxyInstance(drp, DubboOrgInfoInterface.class);
 
		} catch (Exception e) {
			Logger.applogErrorDetail(e, null, "准备调用综合服务器", false, "生成代理失败", null, null, "请检查配置文件和DUBBO环境");
		}
		try {
			ResultVO orgInfo = bp.OrgInfo(new MechanismDto());
			Logger.applogInfoDetail(null, "专线大屏站点信息Dubbo接口", "专线大屏站点信息Dubbo接口---返回值： "+JSONUtil.parseObject(orgInfo));
		} catch (Exception e) {
			Logger.applogErrorDetail(e, null, "调用快行企业端的dubbo ", "调用快行企业端的dubbo, 调用方法异常 " );
		}
		Logger.applogInfoDetail(null, "企业dubbo接口调用 ", "调用dubbo 结束");
	}
}
