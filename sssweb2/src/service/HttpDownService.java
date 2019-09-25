package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.vo.UrlInfo;
import utils.HttpUtils;
import utils.IOUtils;
import utils.RegexUtils;
import utils.StringUtils;
import utils.ThreadConcurrentUtils;
import utils.model.concurrent.Job;
import utils.model.concurrent.JobResult;
import utils.model.concurrent.JobPromise;

public class HttpDownService {
	
	private static JobPromise<?,?> currentPromise = null;
	private static Map<String,Boolean> downResultMap = new LinkedHashMap<String,Boolean>();
	
	/**
	 * 检测,从指定url中按规则抓取url
	 * @author likaihao
	 * @date 2016年7月23日 下午11:03:36
	 * @param baseUrl 目标链接
	 * @param reArr 链接正则
	 * @param nextLayerArr 下一层链接
	 * @param nextPageArr 下一页链接
	 * @param nameRegex 名称正则
	 * @param encoding 网页编码
	 * @param runInterval 运行间隔
	 * @return
	 */
	public List<List<UrlInfo>> check(String baseUrl,String[] reArr,String[] nextLayerArr,String[] nextPageArr,final String nameRegex,final String encoding, int runInterval){
		final List<String> nextTooList = Arrays.asList(nextLayerArr);
		final List<String> nextPageList = Arrays.asList(nextPageArr);
		
		//准备存储所有url的list, 每一层链接正则对应一个map
		List<Map<String,UrlInfo>> urlMapList = new ArrayList<Map<String,UrlInfo>>();
		
		//遍历链接正则数组
		for(int i=0;i<reArr.length;i++){
			//请求目标url并抓取子链接
			final Integer newI = i;
			final String re = reArr[i];
			final boolean isLastRe = (i == reArr.length-1);
			
			//获得每层正则开始匹配前的 目标url
			List<String> lastUrlList = new ArrayList<String>();
			if(i==0){//第一层为目标链接
				lastUrlList.add(baseUrl);
			}else{ //后续层为上一次匹配到的链接
				lastUrlList.addAll(urlMapList.get(i-1).keySet());
			}
			
			//开启30个线程,同时去 请求目标url并抓取子链接
			JobPromise<String,Map<String,UrlInfo>> promise = ThreadConcurrentUtils.common(lastUrlList, new Job<String,Map<String,UrlInfo>>(){
				public Map<String,UrlInfo> doJob(String url) {
					boolean isNextLayer = nextTooList.contains(newI+"");
					boolean isNextPage = nextPageList.contains(newI+1+"");
					
					//存储子链接的map
					Map<String,UrlInfo> subUrlMap = new LinkedHashMap<String,UrlInfo>();
					
					//是下一层链接,则把当前链接放到子链接中
					if(isNextLayer){
						UrlInfo urlInfo = new UrlInfo();
						urlInfo.url = url;
						subUrlMap.put(urlInfo.url,urlInfo);
					}
					
					//判断是否是下一页链接
					if(isNextPage){
						//循环从下一页链接中 获取下一页链接,直到没有获取到
						String newPageUrl = url;
						for(;;){
							//根据指定正则从链接中获取 下一页链接
							Map<String,UrlInfo> newPageMap = findSubUrl(newPageUrl,re,isLastRe?nameRegex:null,encoding);
							if(newPageMap==null || newPageMap.size()==0){
								//没有获取到下一页链接, 本层正则结束,继续下一层正则
								break;
							}
							//获取到下一页链接, 保存起来, 并尝试继续在新的链接中 获取
							newPageUrl = newPageMap.keySet().iterator().next();
							subUrlMap.put(newPageUrl,newPageMap.get(newPageUrl));
						}
					}else{
						//不是下一页链接, 直接抓取页面中符合条件的链接
						subUrlMap.putAll(findSubUrl(url,re,isLastRe?nameRegex:null,encoding));
					}
					
					//记录当前链接 和下面符合条件的子链接
					return subUrlMap;
				}
			}, 30, runInterval);
			
			List<JobResult<String,Map<String,UrlInfo>>> returnList = promise.getParamValueList();
			currentPromise = promise;
			
			//汇总结果 ,将本层所有链接的抓取结果合并
			Map<String,UrlInfo> urlMap = new LinkedHashMap<String,UrlInfo>();
			for(JobResult<String,Map<String,UrlInfo>> result : returnList){
				String url = result.getParam();
				Map<String,UrlInfo> subUrlMap = result.getValue();
				
				//修改上一层链接的子项总数
				if(i>0){
					urlMapList.get(i-1).get(url).subUrlCount = subUrlMap.size();
				}
				//合并本层的所有子链接
				urlMap.putAll(subUrlMap);
			}
			//将本层的urlMap 记录到总的urlMapList中
			urlMapList.add(urlMap);
			
			//判断运行的标志,如果为false,则终止当前线程的任务
		}
		
		//将map转为list
		List<List<UrlInfo>> urlListList = new ArrayList<List<UrlInfo>>();
		for(Map<String,UrlInfo> map : urlMapList){
			urlListList.add(new ArrayList<UrlInfo>(map.values()));
		}
		
		//处理文件名称
		List<UrlInfo> fileList = urlListList.get(urlListList.size()-1);
		List<String> fileNameList = new ArrayList<String>();
		//遍历最后一层 文件的链接
		for(UrlInfo urlInfo : fileList){
			String fileName = urlInfo.name;
			//如果没有文件名,从url中获取
			if(fileName == null || fileName.length()==0){
				fileName = IOUtils.getFileName(urlInfo.url);
			}
			
			//如果没有文件名,则文件名为随机字符串
			if(fileName == null || fileName.length()==0){
				fileName = StringUtils.getRandomStr();
			}
			
			//如果文件名重复,则在文件名后添加随机字符串
			if(fileNameList.contains(fileName)){
				String suffix = IOUtils.getSuffix(fileName);
				fileName = fileName.substring(0,fileName.length()-suffix.length()) + StringUtils.getRandomStr()+"."+suffix;
			}
			fileNameList.add(fileName);
			urlInfo.name = fileName;
		}
		return urlListList;
	}
	
	/**
	 * 从一个url中通过正则获取匹配的url和标题
	 * @author likaihao
	 * @date 2016年1月28日 下午2:16:47
	 * @param url 目标链接
	 * @param re 正则
	 * @param fileNameRegex 文件名称正则
	 * @param encoding 网页编码
	 * @return
	 */
	public Map<String,UrlInfo> findSubUrl(String url,String re,String fileNameRegex,String encoding){
		try {
			//发送请求
			byte[] returnBytes = HttpUtils.sendHttpGetReturnByteArray(url, null, null);
			String returnStr = new String(returnBytes,encoding);
			
			//按正则获取 子链接,标题
			Map<String,UrlInfo> urlInfoMap = new LinkedHashMap<String,UrlInfo>();
			List<List<String>> list = RegexUtils.getSubstrAllGroupByRegexReturnList(returnStr, re);
			for(List<String> subList : list){
				//获取标题(最后一层为名称)
				String title = null;
				if(subList.size()>2){
					//如果存在第二个组,则第二组为标题
					title = subList.get(2);
				}else if(fileNameRegex!=null && fileNameRegex.length()>0){
					//如果文件名称正则不为空,则根据正则获取名称
					title = RegexUtils.getSubstrByRegex(returnStr, fileNameRegex);
				}
				//记录子链接
				UrlInfo urlInfo = new UrlInfo();
				urlInfo.url = getAllUrl(url, subList.get(1)); //获得完整的url
				urlInfo.name = title;
				urlInfoMap.put(urlInfo.url,urlInfo);
			}
			return urlInfoMap;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage()+",url:"+url);
		}
	}
	
	
	/**
	 * 拼凑完整url
	 * @param urlPath 所在文件请求路径
	 * @param subPath 文件中的子路径
	 * @return
	 */
	public String getAllUrl(String urlPath,String subPath){
		//如果是http开头的说明是完整路径, 则直接返回
		if(subPath!=null && subPath.startsWith("http")){
			return subPath;
		}
		
		//如果是非法链接,则直接返回空  (null "" # javascript:void(0) about:blank)
		if(subPath==null || subPath.length()<2 || subPath.contains(":")){
			return null;
		}
		
		//16.06.08注释掉,当是/结尾的会有问题
		// * 去掉最后的斜杠
//		if(urlPath.endsWith("/")){
//			urlPath = urlPath.substring(0,urlPath.length()-1);
//		}
		
		// * 获得域名和uri
		int i1 = urlPath.indexOf("/",9);
		String domain = urlPath;//域名
		String uri = "";//uri
		if(i1!=-1){
			domain = urlPath.substring(0,i1);//域名
			int i2 = urlPath.lastIndexOf("/");
			if(i2 > i1+1){
				uri = urlPath.substring(i1+1,i2);//uri
			}
		}
		
		// * 以/开头,添加domain
		if(subPath.startsWith("/")){
			return domain + subPath;
		}
		// * 以../开头
		if(subPath.startsWith("../")){
			//相对路径抵消
			while(subPath.startsWith("../")){
				subPath = subPath.substring(3);
				if(uri.length()>0){
					int i3 = uri.lastIndexOf("/");
					if(i3 != -1){
						uri = uri.substring(0,i3);
					}else{
						uri="";
					}
				}
			}
			if(uri.length()>0){
				uri += "/";
			}
			return domain + "/" +uri+subPath;
		}
		//如果是./ 则直接去掉
		if(subPath.startsWith("./")){
			subPath = subPath.substring(2);
		}
		//如果存在子路径,则添加
		if(uri.length()>0){
			subPath = uri+"/"+subPath;
		}
		return domain + "/" +subPath;
	}
	
	
	/**
	 * 下载
	 * @param savePath 保存路径
	 * @param newDirNumArr 创建新文件夹的链接层数
	 * @param checkList 检测结果(List<List<Map>>
	 */
	@SuppressWarnings("all")
	public void down(String savePath,String[] newDirNumArr,List<List> checkList){
		//将List<Map>转换为List<UrlInfo>
		for(int i=0;i<checkList.size();i++){
			for(int j=0;j<checkList.get(i).size();j++){
				Map<String,Object> map = (Map<String, Object>) checkList.get(i).get(j);
				UrlInfo urlInfo = new UrlInfo();
				urlInfo.url = (String) map.get("url");
				urlInfo.name = (String) map.get("name");
				urlInfo.subUrlCount = (Integer) map.get("subUrlCount");
				checkList.get(i).set(j, urlInfo);
			}
		}
		//添加关联关系(确定子链接的 父链接, 按父链接的子项个数推算)
		for(int i=checkList.size()-1;i>0;i--){
			List<UrlInfo> subList = checkList.get(i);
			List<UrlInfo> parentList = checkList.get(i-1);
			
			int newNum = 0;//父链接在list中的索引
			int sumNum = parentList.get(0).subUrlCount;
			for(int j=0;j<subList.size();j++){
				//根据父链接的子项个数推算, 比如第一个父链接子项个数为18, 则前18个子链接的父链接就确定了
				while(j>=sumNum){
					newNum++;
					sumNum += parentList.get(newNum).subUrlCount;
				}
				//将子链接的parent设置为父链接
				subList.get(j).parent = parentList.get(newNum);
			}
		}
		
		//获得每个url文件的保存路径
		List<String> newDirNumList = Arrays.asList(newDirNumArr);
		List<UrlInfo> fileUrlInfo = checkList.get(checkList.size()-1);
		for(UrlInfo info : fileUrlInfo){
			getSavePath(savePath,newDirNumList,info);
		}
		
		//初始化结果map
		downResultMap.clear();
		for(UrlInfo info : fileUrlInfo){
			downResultMap.put(info.url, false);
		}
		
		//开五个线程下载文件,下载间隔3毫秒
		currentPromise = ThreadConcurrentUtils.common(fileUrlInfo, new Job<UrlInfo,Boolean>(){
			public Boolean doJob(UrlInfo urlInfo) {
				OutputStream out = null;
				InputStream in = null;
				HttpURLConnection conn = null;
				try {
					//LoggerUtils.info(urlInfo.url);
					//建立连接
					URL urlPath = new URL(urlInfo.url);
					conn = (HttpURLConnection) urlPath.openConnection();
					//设置请求方式
					conn.setRequestMethod("GET");
					
					//判断文件或文件夹是否存在
					File file = new File(urlInfo.localDownPath);
					if(file.exists()){
						throw new RuntimeException("文件已存在:"+file.getAbsolutePath());
					}else{
						if(!file.getParentFile().exists()){
							file.getParentFile().mkdirs();
						}
					}
					
					//接收并保存文件
					out = new FileOutputStream(file);
					in = conn.getInputStream();
					byte[] bytes = new byte[204800];
					int len = 0;
					while((len = in.read(bytes))>0){
						out.write(bytes, 0, len);
					}
					
					//将此url的下载状态设置为完成
					downResultMap.put(urlInfo.url, true);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				} finally{
					//关闭资源
					try {
						if(in!=null){
							in.close();
						}
						if(out!=null){
							out.close();
						}
						if(conn!=null){
							conn.disconnect();
						}
					} catch (IOException e) {
						e.printStackTrace();
						throw new RuntimeException("关闭资源失败");
					}
				}
			}
		}, 5, 3);
		currentPromise.getValueList();
	}
	
	/**
	 * 获得保存路径(总路径=根路径+创建新文件夹的父链接的标题+文件名称)
	 * @author likaihao
	 * @date 2016年2月14日 下午3:26:13
	 * @param savePath 根保存路径
	 * @param newDirNumList 创建新文件夹的链接层数
	 * @param urlInfo url信息
	 */
	public void getSavePath(String savePath,List<String> newDirNumList,UrlInfo urlInfo){
		if(newDirNumList!=null && newDirNumList.size()>0){
			//获得所有父类
			List<UrlInfo> parentList = new ArrayList<UrlInfo>();
			UrlInfo info = urlInfo;
			while((info = info.parent)!=null){
				parentList.add(info);
			}
			Collections.reverse(parentList);//反转顺序
			
			//添加父链接的文件夹路径
			for(int i=0;i<parentList.size();i++){
				if(newDirNumList.contains(i+1+"")){
					savePath += "/"+parentList.get(i).name;
				}
			}
		}
		//总路径=根路径+创建新文件夹的父链接的标题+文件名称
		urlInfo.localDownPath = savePath + "/" + urlInfo.name;
	}
	
	/**
	 * 获得下载结果
	 * @author likaihao
	 * @date 2016年2月14日 下午6:01:15
	 * @return
	 */
	public Boolean[] getDownResult(){
		return new ArrayList<Boolean>(downResultMap.values()).toArray(new Boolean[0]);
	}
	
	/**
	 * 停止当前进行的任务
	 * @author likaihao
	 * @date 2016年2月16日 上午9:51:30
	 */
	public static void stopTask(){
		currentPromise.aboutJob();//停止未运行或休眠中的任务
	}
	
}