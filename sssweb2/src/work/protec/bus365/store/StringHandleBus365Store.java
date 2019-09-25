package work.protec.bus365.store;

public class StringHandleBus365Store {
	/**
	 * 打印需要升级的配置文件的修改内容
	 * @author likaihao
	 * @date 2016年11月7日 下午2:35:49
	 * @param str
	 * @param startNum
	 * @param onlineValue
	 * @param simulationValie
	 * @param testValue
	 */
	public String printAddConfStr(String str,Integer startNum,String onlineValue,String simulationValue,String testValue){
		String[] lineStrArr = str.split("[\r\n]+");
		
		int num = startNum;
		StringBuilder builder = new StringBuilder();
		for(String lineStr : lineStrArr){
			//跳过注释
			if(lineStr.length()==0 || lineStr.startsWith("#")){
				continue;
			}
			String[] arr = lineStr.trim().split("=");
			builder.append("key"+num+"="+arr[0]+"\r\n");
			builder.append("value"+num+"="+arr[1]+"\r\n");
			builder.append("online"+num+"="+onlineValue+"\r\n");
			builder.append("simulation"+num+"="+simulationValue+"\r\n");
			builder.append("test"+num+"="+testValue+"\r\n");
			builder.append("\r\n");
			num++;
		}
		return builder.toString();
	}
	
	/**
	 * 打印需要升级的配置文件的修改内容-按服务器循环
	 * @author likaihao
	 * @date 2016年11月7日 下午2:35:49
	 * @param str
	 * @param startNum
	 * @param onlineValue
	 * @param simulationValie
	 * @param testValue
	 */
	public String printAddConfStrSplit(String str,Integer startNum,String onlineValue,String simulationValue,String testValue){
		String[] lineStrArr = str.split("[\r\n]+");
		
		int num = startNum;
		StringBuilder builder = new StringBuilder();
		for(String lineStr : lineStrArr){
			//跳过注释
			if(lineStr.length()==0 || lineStr.startsWith("#")){
				continue;
			}
			String[] arr = lineStr.trim().split("=");
			
			for(String key : onlineValue.split(",")){
				builder.append("key"+num+"="+arr[0]+"\r\n");
				builder.append("value"+num+"="+arr[1]+"\r\n");
				builder.append("online"+num+"="+key+"\r\n");
				builder.append("simulation"+num+"=\r\n");
				builder.append("test"+num+"=\r\n");
				builder.append("\r\n");
				num++;
			}
			for(String key : simulationValue.split(",")){
				builder.append("key"+num+"="+arr[0]+"\r\n");
				builder.append("value"+num+"="+arr[1]+"\r\n");
				builder.append("online"+num+"=\r\n");
				builder.append("simulation"+num+"="+key+"\r\n");
				builder.append("test"+num+"=\r\n");
				builder.append("\r\n");
				num++;
			}
			for(String key : testValue.split(",")){
				builder.append("key"+num+"="+arr[0]+"\r\n");
				builder.append("value"+num+"="+arr[1]+"\r\n");
				builder.append("online"+num+"=\r\n");
				builder.append("simulation"+num+"=\r\n");
				builder.append("test"+num+"="+key+"\r\n");
				builder.append("\r\n");
				num++;
			}
		}
		return builder.toString();
	}
}
