package bp.pub_bp;

import conf.ResourceConfig;
import dubbo.api.bus365.bp.BusTicketService;
import dubbo.api.bus365.dto.CancleReserveSeatRequest;
import dubbo.api.bus365.dto.QueryReserveSeatRequest;
import dubbo.api.bus365.dto.ReserveBaseResponse;
import dubbo.api.bus365.dto.ReserveSeatRequest;
import dubbo.api.bus365.dto.ReserveSeatResponse;
import dubbo.api.qtrip.bp.wyc.DubboCJPCServiceInterface;
import dubbo.api.qtrip.consts.Consts;
import dubbo.api.qtrip.vo.wyc.OrderInfoVo;
import dubbo.api.qtrip.vo.wyc.response.CJPCListResponse;
import dubbo.play.proxy.core.ConsumerProxyUtil;
import dubbo.play.proxy.core.DubboReferenceProperties;

public class DubboCJPCServiceBP {

/*    // 和DUBBO服务端要对应
    private static String appName = ResourceConfig.getValue("dubbo.qtripcjpc.appname");

    // 和DUBBO服务端要对应
    private static String group = ResourceConfig.getValue("dubbo.qtripcjpc.group");*/
    
//	dubbo.qtripcjpc.appname=qtrip_business
//			dubbo.qtripcjpc.group=device-qtrip
	private static String appName = "qtrip_business";
//	private static String appName = "busdeviceserver";
	
	// 和DUBBO服务端要对应
	private static String group ="device-qtrip";
//	private static String group ="wwwt-vehiclescheduling";
	
	

    // 和DUBBO服务端要对应
    private static String version = ResourceConfig.getValue("dubbo.bus365.version");

    private static boolean isMonitor = Boolean.getBoolean(ResourceConfig.getValue("dubbo.bus365.ismonitor"));

    private static int timeOut = Integer.parseInt(ResourceConfig.getValue("dubbo.bus365.timeout"));

    
    /**  
     * 描述: 自动留位
     * @author: Qi Xianlong
     * @date:2017年6月19日 下午2:35:58
     * @param paramReserveSeatRequest
     * @return
     * @throws Exception 
     */
    public CJPCListResponse<OrderInfoVo> dubboTest(Long userId) {
        DubboReferenceProperties drp = new DubboReferenceProperties();
        drp.setAppName(appName);
        drp.setGroup(group);
        drp.setVersion(version);
        drp.setMonitor(isMonitor);
        drp.setReferenceTimeOut(timeOut);
        drp.setServiceImplFullname(Consts.QtripBpImplFullName.DubboCJPCServiceBpImpl.getCode());
        DubboCJPCServiceInterface cjpcServiceInterface = null;
        try {
            cjpcServiceInterface = ConsumerProxyUtil.getProxyInstance(drp, DubboCJPCServiceInterface.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CJPCListResponse<OrderInfoVo> unfinishedOrder = cjpcServiceInterface.getUnfinishedOrder(userId);

        return unfinishedOrder;
        
    }
    
}
