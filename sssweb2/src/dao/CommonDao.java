package dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import utils.JsonUtils;
import utils.Page;
import utils.TUtils;

/**
 * Dao公共方法类
 * @author Administrator
 * @param <T> 模型类
 */
@SuppressWarnings("unchecked")
public class CommonDao<T>{
	
	//session工厂
	private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
	
	//模型类的class对象(本类不能创建对象,否则报错,不能注册spring的bean)
	private Class<T> entityClass = TUtils.getTGenericSuperClass(this.getClass());
	
	/**
	 * 通过id查询对象
	 * @param id 对象的id
	 * @return 查询到的对象
	 */
	public T findById(Serializable id) {
		Session session = openNewSession();
		T t =  (T) session.get(entityClass, id);
		session.close();
		return t;
	}
	
	/**
	 * 通过条件查询对象(不分页)
	 * @param condition 查询条件
	 * @param params 参数
	 * @param order 排序
	 * @return 对象集合
	 */
	public List<T> findCollectionByConditionNoPage(String condition,
			Object[] params, Map<String, String> order) {
		Session session = openNewSession();
		//组织hql语句
		String hql = "from "+entityClass.getSimpleName()+" o";
		String conditionStr = getConditionStr(condition,order);
		hql = hql + conditionStr;
		//查询
		Query query = session.createQuery(hql);
		if(params!=null && params.length>0){
			for(int i=0;i<params.length;i++){
				query.setParameter(i, params[i]);
			}
		}
		List<T> list = query.list();
		session.close();
		return list;
	}
	
	/**
	 * 通过条件查询对象(分页)
	 * @param condition 查询条件
	 * @param params 参数
	 * @param order 排序
	 * @param page 分页对象,包含开始索引和每页大小
	 * @return 分页对象,包含查询数据
	 */
	public Page<T> findCollectionByConditionWithPage(String condition,
			Object[] params, Map<String, String> order, Page<T> page) {
		Session session = openNewSession();
		//组织hql语句
		String countHql = "select count(*) from "+entityClass.getSimpleName()+" o";
		String hql = "from "+entityClass.getSimpleName()+" o";//初始hql语句
		//添加条件
		String conditionStr = getConditionStr(condition,order);
		countHql += conditionStr;
		hql += conditionStr;
		//查询
		Query countQuery = session.createQuery(countHql);
		Query query = session.createQuery(hql);
		if(params!=null && params.length>0){
			for(int i=0;i<params.length;i++){
				countQuery.setParameter(i, params[i]);
				query.setParameter(i, params[i]);
			}
		}
		//设置总记录数并分页
		Integer count = new Integer(countQuery.uniqueResult().toString());
		page.setAllDataNum(count);
		query.setFirstResult(page.getStartIndex());
		query.setMaxResults(page.getPageSize());
		//查询分页数据
		page.setData(query.list());
		session.close();
		return page;
	}
	
	/**组装条件字符串*/
	private String getConditionStr(String condition,Map<String, String> orderby){
		StringBuffer conditionStr = new StringBuffer();
		//添加条件语句
		if(condition!=null && !condition.equals("")){
			conditionStr.append(" where 1=1").append(condition);
		}
		//添加排序语句
		if(orderby!=null && orderby.size()>0){
			conditionStr.append(" order by ");
			for(Map.Entry<String, String> entry : orderby.entrySet()){
				conditionStr.append(entry.getKey()+" "+entry.getValue()+",");
			}
			conditionStr.deleteCharAt(conditionStr.length()-1);
		}
		return conditionStr.toString();
	}
	
	
	/**
	 * 保存 并且提交
	 * @param entity 要保存的对象
	 */
	public void saveAndCommit(T entity) {
		Session session = openNewSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.save(entity);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw new RuntimeException("保存失败:"+JsonUtils.parseObject(entity),e);
		} finally{
			session.close();
		}
	}
	
	/**
	 * 删除记录 并且提交
	 * @param obj 要删除的记录
	 */
	public void deleteAndCommit(T entity) {
		Session session = openNewSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.delete(entity);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw new RuntimeException("删除失败:"+JsonUtils.parseObject(entity),e);
		} finally{
			session.close();
		}
	}
	
	/**
	 * 通过id删除记录(可以同时删除多个) 并且提交
	 * @param ids 要删除的记录的id
	 */
	public void deleteByIDsAndCommit(Serializable... ids) {
		if(ids!=null && ids.length>0){
			Session session = openNewSession();
			Transaction transaction = session.beginTransaction();
			try {
				for(Serializable id : ids){
					//先查询,再删除
					T t = findById(id);
					session.delete(t);
				}
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				throw new RuntimeException("删除失败,id:"+ids,e);
			} finally{
				session.close();
			}
		}
	}
	
	/**
	 * 更新 并且提交
	 * @param entity 要更新的对象
	 */
	public void updateAndCommit(T entity) {
		Session session = openNewSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.update(entity);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw new RuntimeException("更新失败:"+JsonUtils.parseObject(entity),e);
		} finally{
			session.close();
		}
	}
	
	/**
	 * 创建一个session
	 * @return
	 */
	public static Session openNewSession(){
		return sessionFactory.openSession();
	}
	
	/**
	 * 执行sql查询语句,返回list
	 * @author likaihao
	 * @date 2016年12月22日 下午3:08:51
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List execSqlQuery(String sql){
		return execSqlQuery(sql,new Object[0]);
	}
	
	/**
	 * 执行sql查询语句,返回list
	 * @author likaihao
	 * @date 2016年12月22日 下午3:08:51
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List execSqlQuery(String sql, Map<String,Object> paramMap){
		Session session = openNewSession();
		Query query = session.createSQLQuery(sql);
		if(paramMap!=null){
			for(String name : paramMap.keySet()){
				query.setParameter(name, paramMap.get(name));
			}
		}
		List list = query.list();
		session.close();
		return list;
	}
	
	/**
	 * 执行sql查询语句,返回list
	 * @author likaihao
	 * @date 2016年12月22日 下午3:08:51
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List execSqlQuery(String sql, Object[] paramArr){
		Session session = openNewSession();
		Query query = session.createSQLQuery(sql);
		if(paramArr!=null){
			for(int i=0;i<paramArr.length;i++){
				query.setParameter(i, paramArr[i]);
			}
		}
		List list = query.list();
		session.close();
		return list;
	}
	
	/**
	 * 执行hql查询语句,返回list
	 * @author likaihao
	 * @date 2016年12月22日 下午3:08:51
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List execHqlQuery(String sql){
		return execHqlQuery(sql,new Object[0]);
	}
	
	/**
	 * 执行hql查询语句,返回list
	 * @author likaihao
	 * @date 2016年12月22日 下午3:08:51
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List execHqlQuery(String sql, Map<String,Object> paramMap){
		Session session = openNewSession();
		Query query = session.createQuery(sql);
		if(paramMap!=null){
			for(String name : paramMap.keySet()){
				query.setParameter(name, paramMap.get(name));
			}
		}
		List list = query.list();
		session.close();
		return list;
	}
	
	/**
	 * 执行hql查询语句,返回list
	 * @author likaihao
	 * @date 2016年12月22日 下午3:08:51
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List execHqlQuery(String sql, Object[] paramArr){
		Session session = openNewSession();
		Query query = session.createQuery(sql);
		if(paramArr!=null){
			for(int i=0;i<paramArr.length;i++){
				query.setParameter(i, paramArr[i]);
			}
		}
		List list = query.list();
		session.close();
		return list;
	}
	
}