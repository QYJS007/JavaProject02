//package controller;
//
//import ch.ethz.ssh2.Connection;
//import ch.ethz.ssh2.Session;
//import dao.generate.RemoteProjectGenDao;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import model.generate.RemoteProject;
//import utils.HttpUtils;
//import utils.LoggerUtils;
//import utils.RegexUtils;
//import utils.SSHUtils;
//import utils.StringUtils;
//import utils.ThreadConcurrentUtils;
//import utils.TimerManager;
//import utils.model.concurrent.Job;
//import utils.model.concurrent.JobPromise;
//import utils.model.concurrent.JobResult;
//
//public class SshConsoleService
//{
//  private static Map<String, SshConsoleService> sshConsoleServiceMap = new HashMap();
//  private RemoteProject remoteproject;
//  private Connection conn = null;
//  private Session ssh = null;
//  
//  public static SshConsoleService createSshConsoleService(String projectId)
//  {
//    SshConsoleService sshConsoleService = new SshConsoleService();
//    sshConsoleService.remoteproject = ((RemoteProject)new RemoteProjectGenDao().findById(new Integer(projectId)));
//    sshConsoleServiceMap.put(projectId, sshConsoleService);
//    return sshConsoleService;
//  }
//  
//  public static SshConsoleService getSshConsoleService(String projectId)
//  {
//    return (SshConsoleService)sshConsoleServiceMap.get(projectId);
//  }
//  
//  public String openConn()
//  {
//    try
//    {
//      LoggerUtils.info("ssh控制台-建立连接(" + this.remoteproject.getName() + "," + this.remoteproject.getIp() + ")");
//      this.conn = SSHUtils.getSSHConnection(this.remoteproject.getIp(), this.remoteproject.getUsername(), this.remoteproject.getPassword());
//      this.ssh = this.conn.openSession();
//      
//
//      this.ssh.requestPTY("dump", 500, 100, 2000, 1000, null);
//      this.ssh.startShell();
//      
//      Thread.sleep(100L);
//      this.ssh.getStdin().write("\r".getBytes());
//      
//
//      return getOutStr();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      throw new RuntimeException(e);
//    }
//  }
//  
//  public void disConn()
//  {
//    try
//    {
//      LoggerUtils.info("ssh控制台-关闭连接(" + this.remoteproject.getName() + "," + this.remoteproject.getIp() + ")");
//      if (this.ssh != null)
//      {
//        this.ssh.getStdout().close();
//        this.ssh.close();
//        this.ssh = null;
//      }
//      if (this.conn != null)
//      {
//        this.conn.close();
//        this.conn = null;
//      }
//      sshConsoleServiceMap.remove(this.remoteproject.getId());
//    }
//    catch (Exception e)
//    {
//      throw new RuntimeException(e);
//    }
//  }
//  
//  public String execCommand(String command)
//  {
//    LoggerUtils.info("ssh控制台-执行命令(" + this.remoteproject.getName() + "," + this.remoteproject.getIp() + "): " + command);
//    try
//    {
//      OutputStream out = this.ssh.getStdin();
//      if (command.equals("cd")) {
//        command = "cd " + this.remoteproject.getPath();
//      }
//      if (command.equals("^C"))
//      {
//        out.write(3);
//        out.write("\r".getBytes());
//      }
//      else
//      {
//        command = command + "\r\r";
//        out.write(command.getBytes());
//      }
//      return getOutStr();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      throw new RuntimeException(e);
//    }
//  }
//  
//  private StringBuilder outStr = new StringBuilder();
//  
//  private String getOutStr()
//  {
//    try
//    {
//      StringBuilder builder = new StringBuilder();
//      BufferedReader reader = new BufferedReader(new InputStreamReader(this.ssh.getStdout()));
//      
//      String okRe = "[^\r\n]*(\\[.*\\][#$] )";
//      
//      String str = null;
//      while ((str = reader.readLine()) != null)
//      {
//        builder.append(str + "\r");
//        this.outStr.append(str + "\r");
//        if (str.matches(okRe)) {
//          break;
//        }
//      }
//      String reuslt = builder.toString();
//      reuslt = StringUtils.getHtmlEscapeString(reuslt, false);
//      return reuslt.replaceAll("[\r]+", "\r");
//    }
//    catch (IOException e)
//    {
//      e.printStackTrace();
//      throw new RuntimeException(e);
//    }
//  }
//  
//  public static Map<String, Boolean> requestProject(String projectIds)
//  {
//    List<RemoteProject> projectList = new RemoteProjectGenDao().findCollectionByConditionNoPage(" and id in (" + projectIds + ")", null, null);
//    
//
//    List<JobResult<RemoteProject, Boolean>> resultList = ThreadConcurrentUtils.common(projectList, new Job()
//    {
//      public Boolean doJob(RemoteProject remoteProject)
//      {
//        String url = "http://" + remoteProject.getIp() + ":" + remoteProject.getPort();
//        try
//        {
//          new HttpUtils(4000, 6000, false).sendGet(url);
//        }
//        catch (Exception e)
//        {
//          LoggerUtils.error("访问项目失败,url: " + url + ", 错误消息:" + e.getMessage());
//          return Boolean.valueOf(false);
//        }
//        return Boolean.valueOf(true);
//      }
//    }, projectList.size(), 0).getParamValueList();
//    
//    Map<String, Boolean> resultMap = new HashMap();
//    for (JobResult<RemoteProject, Boolean> result : resultList) {
//      resultMap.put("p" + ((RemoteProject)result.getParam()).getId(), (Boolean)result.getValue());
//    }
//    return resultMap;
//  }
//  
//  public static Map<String, String> allConn(String projectIds)
//  {
//    List<String> projectIdList = Arrays.asList(projectIds.split(","));
//    Map<String, String> errorMap = ThreadConcurrentUtils.common(projectIdList, new Job()
//    {
//      public String doJob(String projectId)
//      {
//        try
//        {
//          if (SshConsoleService.getSshConsoleService(projectId) == null) {
//            SshConsoleService.createSshConsoleService(projectId).openConn();
//          }
//          return null;
//        }
//        catch (Exception e)
//        {
//          return e.getMessage();
//        }
//      }
//    }, projectIdList.size(), 0).getParamValueMap();
//    
//
//    Map<String, String> errorMap2 = new HashMap();
//    for (String key : errorMap.keySet()) {
//      if (errorMap.get(key) != null) {
//        errorMap2.put(key, (String)errorMap.get(key));
//      }
//    }
//    return errorMap2;
//  }
//  
//  public static void disAllConn()
//  {
//    Map<String, SshConsoleService> map = new HashMap(sshConsoleServiceMap);
//    for (String key : map.keySet()) {
//      try
//      {
//        ((SshConsoleService)sshConsoleServiceMap.get(key)).disConn();
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//      }
//    }
//    sshConsoleServiceMap.clear();
//  }
//  
//  public static Map<String, Map<String, String>> playStatus(String projectIds)
//  {
//    List<String> projectIdList = Arrays.asList(projectIds.split(","));
//    Map<String, Map<String, String>> map = ThreadConcurrentUtils.common(projectIdList, new Job()
//    {
//      public Map<String, String> doJob(String projectId)
//      {
//        try
//        {
//          SshConsoleService.getSshConsoleService(projectId).execCommand("cd");
//          
//          TimerManager<String, String> timer = TimerManager.setTimeout(projectId, new Job()
//          {
//            public String doJob(String projectId)
//            {
//              try
//              {
//                LoggerUtils.info("playstatus取消订单器启动,projectId:" + projectId);
//                OutputStream out = SshConsoleService.getSshConsoleService(projectId).ssh.getStdin();
//                out.write(3);
//                out.write("\r".getBytes());
//              }
//              catch (IOException e)
//              {
//                e.printStackTrace();
//              }
//              return null;
//            }
//          }, 5000, "sshConsole-" + projectId);
//          
//          String outStr = SshConsoleService.getSshConsoleService(projectId).execCommand("play status");
//          if (outStr.contains("^C")) {
//            throw new RuntimeException("连接不上项目");
//          }
//          timer.cancel();
//          
//
//          List<String> requestPool = RegexUtils.getSubstrAllGroupByRegex(outStr, "Requests execution pool:\r~~~~~~~~~~~~~~~~~~~~~~~~\rPool size: (\\d+)\rActive count: (\\d+)");
//          List<String> jobPool = RegexUtils.getSubstrAllGroupByRegex(outStr, "Jobs execution pool:\r~~~~~~~~~~~~~~~~~~~\rPool size: (\\d+)\rActive count: (\\d+)");
//          List<List<String>> dbPoolList = RegexUtils.getSubstrAllGroupByRegexReturnList(outStr, "busy connection num: (\\d+)\ridle connection num: 10\rtotal connection num: (\\d+)");
//          String dbPoolMsg = "有空闲";
//          for (List<String> dbPool : dbPoolList) {
//            if (((String)dbPool.get(1)).equals(dbPool.get(2))) {
//              dbPoolMsg = "无空闲";
//            }
//          }
//          Map<String, String> map = new HashMap();
//          map.put("requestPool", (String)requestPool.get(2) + "/" + (String)requestPool.get(1));
//          map.put("jobPool", (String)jobPool.get(2) + "/" + (String)jobPool.get(1));
//          map.put("dbPool", dbPoolMsg);
//          return map;
//        }
//        catch (Exception e)
//        {
//          LoggerUtils.error("获取playstatus信息失败", e);
//          Map<String, String> map = new HashMap();
//          map.put("requestPool", "获取失败");
//          map.put("jobPool", "获取失败");
//          map.put("dbPool", "获取失败," + e.getMessage());
//          return map;
//        }
//      }
//    }, projectIdList.size(), 0).getParamValueMap();
//    
//    LoggerUtils.info("playStatus返回:" + map);
//    return map;
//  }
//}
