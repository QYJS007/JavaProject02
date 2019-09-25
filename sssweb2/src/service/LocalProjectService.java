package service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.generate.LocalProject;
import service.generate.LocalProjectGenService;

public class LocalProjectService {

	LocalProjectGenService localProjectGenService = new LocalProjectGenService();
	/**
	 * 获取所有模板
	 * @author likaihao
	 * @date 2016年4月25日 下午5:11:56
	 * @return
	 */
	public List<LocalProject> getLocalProjectList(){
		List<LocalProject> list = localProjectGenService.findAll();
		//如果以workspace开头,认为是工作控件,遍历下面的文件夹
		List<LocalProject> list2 = new ArrayList<LocalProject>();
		for(LocalProject localProject : list){
			if(localProject.getName().startsWith("workspace")){
				File workDir = new File(localProject.getPath());
				File[] projectArr = workDir.listFiles();
				if(projectArr!=null){
					for(File project : projectArr){
						if(project.isDirectory() && !project.getName().startsWith(".")){
							LocalProject l = new LocalProject();
							l.setName(localProject.getName()+"> "+project.getName());
							l.setPath(project.getAbsolutePath());
							l.setPackDir(localProject.getPackDir());
							l.setPackFile(localProject.getPackFile());
							list2.add(l);
						}
					}
				}
			}else{
				list2.add(localProject);
			}
		}
		return list2;
	}
}