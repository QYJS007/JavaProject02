package service;

import java.util.List;

import model.generate.Params;
import dao.generate.ParamsGenDao;

public class ParamsService {
	
	ParamsGenDao paramsGenDao = new ParamsGenDao();
	
	/**
	 * 通过名称查询参数
	 * @author likaihao
	 * @date 2016年12月1日 下午4:51:51
	 * @param name
	 * @return
	 */
	public Params findParamsByName(String name){
		List<Params> list = paramsGenDao.findCollectionByConditionNoPage(" and name=?", new Object[]{name}, null);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
}