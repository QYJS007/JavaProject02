package controller.back.nogenerate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.generate.LocalProject;
import model.vo.AutocompleteResult;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.LocalProjectService;
import controller.BaseController;
import dao.CommonDao;

@Controller
@Scope("prototype")
@RequestMapping("/OtherBackController")
public class OtherBackController extends BaseController{
	//返回首页
	@RequestMapping("/index.do")
	public String index() {
		return "back/base/index.jsp";
	}
	
	//返回home页
	@RequestMapping("/home.do")
	public String home() {
		return "back/base/home.jsp";
	}
	
	//下拉列表,查询数据字典
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDictionaryDetail.do")
	@ResponseBody
	public void queryDictionaryDetail(String tableName,String columnName) {
		String sql = "select dd.value,dd.showText from t_dictionary_detail dd,t_dictionary d where dd.dictionaryId=d.id and d.tableName=? and d.columnName=?";
		List<Object[]> list = CommonDao.execSqlQuery(sql, new Object[]{tableName,columnName});
		
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		if(list!=null && list.size()>0){
			for(Object[] arr : list){
				Map<String,String> map = new LinkedHashMap<String,String>();
				map.put("value", (String)arr[0]);
				map.put("showText", (String)arr[1]);
				resultList.add(map);
			}
		}
		writeJsonToPage(resultList);
	}
	
	//下拉列表,统计表
	@SuppressWarnings("unchecked")
	@RequestMapping("/countTable.do")
	@ResponseBody
	public void countTable(String modelName,String columnName) {
		if(modelName.equals("LocalProject")){
			List<LocalProject> list = new LocalProjectService().getLocalProjectList();
			List<String> nameList = new ArrayList<String>();
			for(LocalProject p : list){
				nameList.add(p.getName());
			}
			writeJsonToPage(nameList);
		}else{
			String sql = "select DISTINCT "+columnName+" from "+modelName;
			List<String> list = null;
			if(sql.contains("t_")){
				list = CommonDao.execSqlQuery(sql);
			}else{
				list = CommonDao.execHqlQuery(sql);
			}
			writeJsonToPage(list);
		}
		
	}
	
	//下拉列表,统计表,返回value和text两列
	@SuppressWarnings("unchecked")
	@RequestMapping("/countTableValueText.do")
	@ResponseBody
	public void countTableValueText(String tableName,String valueColumn,String textColumn) {
		String sql = "select DISTINCT `"+valueColumn+"`,`"+textColumn+"` from "+tableName;
		List<Object[]> list = CommonDao.execSqlQuery(sql);
		
		List<Map<String,Object>> list2 = new ArrayList<Map<String,Object>>();
		for(Object[] arr : list){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put(valueColumn, arr[0]);
			map.put(textColumn, arr[1]);
			list2.add(map);
		}
		writeJsonToPage(list2);
	}
	
	//自动补全功能
	@SuppressWarnings("unchecked")
	@RequestMapping("/autoComplete.do")
	@ResponseBody
	public void autoComplete(String type,String text) {
		AutocompleteResult result = new AutocompleteResult();
		String sql = null;
		if(type.equals("remoteProject_RemoteCommandGroup")){
			result.setFieldList(Arrays.asList("id", "命令组名称"));
			result.setNameList(Arrays.asList("id", "name"));
			result.setFieldWidthList(Arrays.asList(80,100));
			sql = "select id,name from RemoteCommandGroup o where (o.name like :text)";
		}else if(type.equals("dictionaryDetail_Dictionary")){
			result.setFieldList(Arrays.asList("id", "字典名称"));
			result.setNameList(Arrays.asList("id", "name"));
			result.setFieldWidthList(Arrays.asList(80,100));
			sql = "select id,name from Dictionary o where (o.name like :text)";
		}else if(type.equals("remoteCommand_RemoteCommandGroup")){
			result.setFieldList(Arrays.asList("id", "命令组名称"));
			result.setNameList(Arrays.asList("id", "name"));
			result.setFieldWidthList(Arrays.asList(80,100));
			sql = "select id,name from RemoteCommandGroup o where (o.name like :text)";
		}else{
			throw new RuntimeException("自动补全,没有找到指定type:"+type);
		}
		Session session = CommonDao.openNewSession();
		Query query = null;
		if(sql.contains("t_")){
			query = session.createSQLQuery(sql);
		}else{
			query = session.createQuery(sql);
		}
		query.setParameter("text", "%"+text+"%");
		query.setFirstResult(0);
		query.setMaxResults(10);
		result.setResultList(query.list());
		result.setResultCount(result.getResultList().size());
		session.close();
		writeJsonToPage(result);
	}
}