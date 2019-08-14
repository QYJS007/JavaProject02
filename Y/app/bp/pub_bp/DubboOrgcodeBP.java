package bp.pub_bp;

import play.Logger;
import utils.JSONUtil;
import conf.ResourceConfig;
import dubbo.api.uniplateform.bp.DistributorManagerInterface;
import dubbo.api.uniplateform.bp.ModifyStatusinterface;
import dubbo.api.uniplateform.consts.Consts;
import dubbo.api.uniplateform.consts.Consts.REQUEST_TYPE;
import dubbo.api.uniplateform.dto.ResponseDto;
import dubbo.api.uniplateform.dto.Result;
import dubbo.play.proxy.core.ConsumerProxyUtil;
import dubbo.play.proxy.core.DubboReferenceProperties;

public class DubboOrgcodeBP {
	
	// 和DUBBO服务端要对应
    private static String appName = ResourceConfig.getValue("dubbo.uniplata.name");

    // 和DUBBO服务端要对应
    private static String group = ResourceConfig.getValue("dubbo.uniplata.group");

    // 和DUBBO服务端要对应
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
    
    
    /**
     * 机构的修改中进行dubbo接口的调用
     * @param status 机构是否可用 1 可用 0 不可用
     * @param orgcode
     * @return
     */
    public static ResponseDto findOrgcodebyorgcodes(String orgcode) {
		drp.setServiceImplFullname(Consts.DUBBO_SERVICE_IMPL_FULLNAME.DISTRIBUTOR.DISTRIBUTOR_MANAGER.getCode());
		DistributorManagerInterface bp = null;
		ResponseDto responseDto = null;
		try {
			bp = ConsumerProxyUtil.getProxyInstance(drp, DistributorManagerInterface.class);
			Logger.info("调用dubbo成功，orgcode：", orgcode);
		} catch (Exception e) {
			Logger.error(e, null, "准备调用综合服务器", false, "生成代理失败", null, null, "请检查配置文件和DUBBO环境");
			return null;
		}
		try {
			Logger.applogInfoDetail(null, "准备调用统一企业平台","准备调用统一企业平台,机构编码:"+orgcode);
			responseDto = bp.searchMachineryTree(orgcode, REQUEST_TYPE.ORGCODE, 1);
		} catch (Exception e) {
			Logger.applogErrorDetail(e, null, "调用综合服务器 ", false, "调用失败", null, null, "调用DUBBO服务失败，请检查DUBBO服务生产消费情况，机构编码orgcode:" + orgcode);
			return null;
		}
		return responseDto;
	}
   
    
	

	









}
