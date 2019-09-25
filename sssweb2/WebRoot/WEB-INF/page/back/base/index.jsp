<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<%@ include file="head.jsp"%>
		
		<link rel="stylesheet" media="screen" href="${pageContext.request.contextPath}/public/stylesheets/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/public/javascripts/back/base/base.js"></script>
		<style>
			.leftTree{
				z-index:200000;
			}
			.cssSPlice{
				float:left;
				margin-right:0px;
			}
			.img-rounded{
				width:70px;
				height:40px;
			}
			.systion,.NewDate{
				font-weight:bold;
			}
			.fonts{
				position:relative;
				top:-5px;
			}
			.sub_wrap dl{
				margin-bottom:20px;
			}
			.iframe { 
				width:100%; height:100%; overflow:hidden;
			}
		</style>
	</head>
	<body>
		<div class="roll_wrap20140319">
			<div class="leftTree">
				<div class="wrap">
					<h1 class="navigation"><img alt="" src="${pageContext.request.contextPath}/public/images/head.png" style="float:left;width:25px;height:25px;"><span class="cssSPlice">超级用户</span></h1>
					<ul class="tree" id="mtree">
						<li class="l1">
							<script>
								mouseEvent($('.l1'))
							</script>
							<a href="javascript:void(0);" class="firstMenu fir_show">
								<span class="s1"></span>
								<p>首页模板管理</p>
							</a>
							<input class="countclass" type="hidden" value="17" />
							<div class="subMenu">
								<div class="sub_wrap">
									<dl>
										<dt>
											<div class='menu2head'>
												<span  class='menu2head'><img src="${pageContext.request.contextPath}/public/images/menu2head.png"></span><span class="fonts">首页模板管理</span>
											</div>
											<div>
												<span><img src="${pageContext.request.contextPath}/public/images/menu2splice.png"></span>
											</div>
										</dt>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/QueryTempletBackController/page.do');">文件查询模板管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/HttpTempletBackController/page.do');">http请求模板管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/LineReplaceTempletBackController/page.do');">单行模板替换模板管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/CodeGenerateTempletBackController/page.do');">模板代码生成模板管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/StringHandleTempletBackController/page.do');">字符串处理模板管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/CodeCallTempletBackController/page.do');">代码调用模板管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/LocalProjectBackController/page.do');">本地项目管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/RemoteProjectBackController/page.do');">远程项目管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/RemoteCommandGroupBackController/page.do');">命令组管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/RemoteCommandBackController/page.do');">远程命令管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/HttpDownTempletBackController/page.do');">http批量下载模板管理</a>
										</dd>
									</dl>
								</div>
						 	</div>
						</li>
						<li class="l2">
							<script>
								mouseEvent($('.l2'))
							</script>
							<a href="javascript:void(0);" class="firstMenu ">
								<span class="s2"></span>
								<p>代码生成模板</p>
							</a>
							<input class="countclass" type="hidden" value="12" />
							<div class="subMenu">
								<div class="sub_wrap">
									<dl>
										<dt>
											<div class='menu2head'>
												<span><img src="${pageContext.request.contextPath}/public/images/menu2head.png"></span><span class="fonts" >代码生成模板</span>
											</div>
											<div>
												<span><img src="${pageContext.request.contextPath}/public/images/menu2splice.png"></span>
											</div>
										</dt>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/DbCodeGenerateTempletBackController/page.do');">db模板代码生成管理</a>
										</dd>
									</dl>
								</div>
						 	</div>
						</li>
						
						<li class="l3">
							<script>
								mouseEvent($('.l3'))
							</script>
							<a href="javascript:void(0);" class="firstMenu ">
								<span class="s2"></span>
								<p>后台功能</p>
							</a>
							<input class="countclass" type="hidden" value="13" />
							<div class="subMenu">
								<div class="sub_wrap">
									<dl>
										<dt>
											<div class='menu2head'>
												<span><img src="${pageContext.request.contextPath}/public/images/menu2head.png"></span><span class="fonts" >后台功能</span>
											</div>
											<div>
												<span><img src="${pageContext.request.contextPath}/public/images/menu2splice.png"></span>
											</div>
										</dt>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/DbMngrController/page.do');">数据库管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/RedisMngrController/page.do');">redis管理</a>
										</dd>
									</dl>
								</div>
						 	</div>
						</li>
						
						<li class="l4">
							<script>
								mouseEvent($('.l4'))
							</script>
							<a href="javascript:void(0);" class="firstMenu ">
								<span class="s2"></span>
								<p>系统管理</p>
							</a>
							<input class="countclass" type="hidden" value="14" />
							<div class="subMenu">
								<div class="sub_wrap">
									<dl>
										<dt>
											<div class='menu2head'>
												<span><img src="${pageContext.request.contextPath}/public/images/menu2head.png"></span><span class="fonts" >系统管理</span>
											</div>
											<div>
												<span><img src="${pageContext.request.contextPath}/public/images/menu2splice.png"></span>
											</div>
										</dt>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/DictionaryBackController/page.do');">字典管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/DictionaryDetailBackController/page.do');">字典详情管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/DbInfoBackController/page.do');">数据库信息管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/RedisInfoBackController/page.do');">reids信息管理</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" onclick="f(this.innerHTML,'${pageContext.request.contextPath}/ParamsBackController/page.do');">参数管理</a>
										</dd>
									</dl>
								</div>
						 	</div>
						</li>
						
						<li onclick="esc()" >
							<a class="outSystem" title="退出系统" href="javascript:;" ></a>
							<p class="outmsg">退出</p>
						</li>
					</ul>
				</div>
			</div>
			
			<div class="nav">
				<h1 class="sysName">My后台管理系统</h1>
			</div>
			<div class="minFrame">
				<div id="tabs" class="easyui-tabs" data-options="border:false" style="margin-left:142px;display:block;z-index:1;">
					<div title="主页">
						<div class="iframe">
							<iframe src="${pageContext.request.contextPath}/OtherBackController/home.do" scrolling="no" style="width:100%;height:100%;border:0;"></iframe>
						</div>
					</div>
					
				</div>
				<div class="rBar"></div>
			</div>
		</div>
		<div class="clearStyle"></div>
			<!--tabs工具菜单-->
			<div id="tools">
				<a href="javascript:void(0)" class="easyui-menubutton" data-options="menu:'#mm'"></a>
				<div id="mm" data-options="onClick:menuHandler" style="width:150px;">
					<div>关闭所有页</div>
					<div>关闭当前页</div>
					<div>刷新当前页</div>
				</div>
			</div>
		</div>
	</body>
</html>