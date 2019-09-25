package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.generate.Params;
import model.generate.RemoteCommand;
import model.generate.RemoteProject;
import service.generate.RemoteCommandGenService;
import service.generate.RemoteProjectGenService;
import sys.SystemConf;
import utils.ListUtils;
import utils.TempletUtils;
import dao.generate.RemoteCommandGenDao;
import dao.generate.RemoteProjectGenDao;

public class RemoteProjectService {

	RemoteProjectGenService remoteProjectGenService = new RemoteProjectGenService();
	RemoteProjectGenDao remoteProjectGenDao = new RemoteProjectGenDao();
	RemoteCommandGenService remoteCommandGenService = new RemoteCommandGenService();
	
	/**
	 * 获取全部类型
	 * @author likaihao
	 * @date 2016年12月20日 上午9:22:48
	 * @return
	 */
	public List<String> getAllType(){
		List<RemoteProject> list = remoteProjectGenService.findAll();
		Set<String> typeSet = new HashSet<String>();
		for(RemoteProject r : list){
			typeSet.add(r.getType());
		}
		List<String> typeList = new ArrayList<String>(typeSet);
		//对list进行排序
		String remoteProjectTypeSort = SystemConf.getResourceValue("remoteProjectTypeSort");
		List<String> valueOrderBy = Arrays.asList(remoteProjectTypeSort.split(","));
		ListUtils.sort(typeList, null, new ArrayList<Object>(valueOrderBy));
		return typeList;
	}
	
	/**
	 * 根据类型获取远程项目
	 * @author likaihao
	 * @date 2016年4月25日 下午5:11:56
	 * @return
	 */
	public List<RemoteProject> getRemoteProjectListByType(String type){
		List<RemoteProject> list = null;
		if(type!=null && type.length()>0){
			//查询指定type的值
			list = remoteProjectGenDao.findCollectionByConditionNoPage(" and type=?", new Object[]{type}, null);
		}else{
			list = remoteProjectGenService.findAll();
		}
		
		//保留id,name,path
		ListUtils.clearListValAttr(list, new String[]{"id","name","path"});
		//按名称进行排序
		ListUtils.sort(list, "name", true);
		return list;
	}
	
	/**
	 * 获取打包的远程项目
	 * @author likaihao
	 * @date 2016年4月25日 下午5:11:56
	 * @return
	 */
	public List<RemoteProject> getPackRemoteProjectVOList(String localProject){
		List<RemoteProject> list = remoteProjectGenService.findAll();
		List<RemoteProject> list2 = new ArrayList<RemoteProject>();
		Params params = new ParamsService().findParamsByName("onLinePackage");
		for(RemoteProject r : list){
			//筛选本地项目对应的远程项目
			boolean isContains = false;
			if(r.getLocalProject()!=null){
				isContains = Arrays.asList(r.getLocalProject().split(",")).contains(localProject);
			}
			if(localProject==null || isContains){
				if(r.getIstest()==1 || (params!=null && params.getValue().equals("1"))){//判断是否忽略正式环境
					RemoteProject r2 = new RemoteProject();
					r2.setId(r.getId());
					r2.setName(r.getName());
					r2.setPath(r.getPath());
					list2.add(r2);
				}
			}
		}
		//按名称排序
		Collections.sort(list2, new Comparator<RemoteProject>() {
			public int compare(RemoteProject o1, RemoteProject o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return list2;
	}
	
	/**
	 * 获取远程项目的命令
	 * @author likaihao
	 * @date 2016年5月25日 下午10:38:47
	 * @param remoteProjectId
	 * @param func 功能名称
	 * @param gzFileName 压缩文件名称(如果是打包命令,需要填充)
	 * @return
	 */
	public List<RemoteCommand> getRemoteCommandList(Integer remoteProjectId, String func, String gzFileName){
		//获取远程项目
		RemoteProject remoteProject = remoteProjectGenService.findById(remoteProjectId);
		//获取命令组的命令
		List<RemoteCommand> list =  new RemoteCommandGenDao().findCollectionByConditionNoPage("and commandGroupId=?", new Object[]{remoteProject.getCommandGroupId()}, null);
		
		//筛选功能名称
		List<RemoteCommand> list2 = new ArrayList<RemoteCommand>();
		for(RemoteCommand command : list){
			if(command.getFunc()!=null && Arrays.asList(command.getFunc().split(",")).contains(func)){
				list2.add(command);
			}
		}
		
		//模板填充
		Map<String,Object> paramMap = new HashMap<String,Object>();
		// * 添加远程项目信息
		paramMap.put("remoteProject", remoteProject);//添加远程项目信息
		// * 添加gzFileName
		if(gzFileName!=null && gzFileName.length()>0){
			paramMap.put("gzFileName", gzFileName);
		}else{
			paramMap.put("gzFileName", "{:gzFileName}");//gzFileName保持原样
		}
		
		for(RemoteCommand r : list2){
			String command = r.getCommand();
			command = TempletUtils.templetFillOneLine(command, paramMap);//模板填充
			r.setCommand(command);
		}
		return list2;
	}
	
	/**
	 * 获取远程项目的命令
	 * @author likaihao
	 * @date 2016年5月25日 下午10:38:47
	 * @param remoteProjectId
	 * @return
	 */
	public String getRemoteCommand(Integer remoteProjectId, String func, String name,String gzFileName){
		List<RemoteCommand> list = getRemoteCommandList(remoteProjectId, func, gzFileName);
		if(list!=null && list.size()>0){
			for(RemoteCommand command : list){
				if(command.getName().equals(name)){
					return command.getCommand();
				}
			}
		}
		return null;
	}
}