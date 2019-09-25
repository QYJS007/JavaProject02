-- ------@2017-01@start--------
-- @repeat{select COUNT(*) from information_schema.`TABLES` WHERE TABLE_SCHEMA = '@@dbname' AND TABLE_NAME = 't_code_call_templet'}

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_c3p0_test
-- ----------------------------
DROP TABLE IF EXISTS `t_c3p0_test`;
CREATE TABLE `t_c3p0_test` (
  `a` char(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_c3p0_test
-- ----------------------------

-- ----------------------------
-- Table structure for t_code_call_templet
-- ----------------------------
DROP TABLE IF EXISTS `t_code_call_templet`;
CREATE TABLE `t_code_call_templet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `code` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_code_call_templet
-- ----------------------------
INSERT INTO `t_code_call_templet` VALUES ('1', '其它-生成身份证', '_codeBlock:{:idCardGeneratorUtils.generate()}');
INSERT INTO `t_code_call_templet` VALUES ('2', '其它-生成二维码', 'content:https://qr.alipay.com/bax02374ftnbxcyp46q320d7\r\npath:C:/Users/Administrator/Desktop/a.jpg\r\n_codeBlock:{:qrCodeUtils.writeQrCodeToFile(content, 300, 300, path)}');
INSERT INTO `t_code_call_templet` VALUES ('3', '其它-删除.svn文件夹', 'path:F:\\git\\bj_1602_packagemngr\\SRC\r\n_codeBlock:{:codeCallPubStore.deleteSVNFile(path)}');
INSERT INTO `t_code_call_templet` VALUES ('4', '其它-分割大文本文件', 'path:C:\\Users\\Administrator\\Desktop\\-applogs-bus365task-www-ANALYSIS_GENERAL-bus365task_msg_2016-02-13.log\r\nsavePath:C:\\Users\\Administrator\\Desktop\\abc\r\nblocksize:50 //单位 M\r\n_codeBlock:{:codeCallPubStore.splitBigText(path,savePath,blocksize)}');

-- ----------------------------
-- Table structure for t_code_generate_templet
-- ----------------------------
DROP TABLE IF EXISTS `t_code_generate_templet`;
CREATE TABLE `t_code_generate_templet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `code` text,
  `param` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_code_generate_templet
-- ----------------------------
INSERT INTO `t_code_generate_templet` VALUES ('1', 'sql-column-add', '@repeat{select count(*) from information_schema.`COLUMNS` WHERE TABLE_SCHEMA = \'@@dbname\' AND TABLE_NAME = \'${tableName}\' AND COLUMN_NAME = \'${columnName}\'}\r\nALTER TABLE `${tableName}` add ${columnName} ${newType} ${isNull==\'true\'?\'\':\'not \'}null COMMENT \'${comment}\';', 'tableName:dynamichtml\r\ncolumnName:code\r\nnewType:varchar(10)\r\nisNull:true\r\ncomment:备注');

-- ----------------------------
-- Table structure for t_db_code_generate_templet
-- ----------------------------
DROP TABLE IF EXISTS `t_db_code_generate_templet`;
CREATE TABLE `t_db_code_generate_templet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `code` text,
  `param` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_db_code_generate_templet
-- ----------------------------
INSERT INTO `t_db_code_generate_templet` VALUES ('1', '生成model', '示例', 'package models;\r\n\r\nimport javax.persistence.Entity;\r\nimport javax.persistence.Id;\r\nimport javax.persistence.Table;\r\n\r\n#if(commonUtils.containsDateColumn(tableInfo))\r\n	import java.util.Date;#b\r\n#end\r\n\r\n@Entity\r\n@Table(name=\"${tableName}\")\r\npublic class ${modelName} extends SimpleModel{\r\n\r\n	@Id\r\n	public ${primaryKeyType} ${primaryKeyName};\r\n	#for(Map.Entry entry : tableInfo.columnInfoMap)\r\n		#if(primaryKeyName!=entry.key)\r\n			public ${entry.value.javaType} ${entry.key}; ${entry.value.comment?\"//\"+entry.value.comment:\"\"} #b#b\r\n		#end\r\n	#end\r\n	\r\n	public Integer getId() {\r\n		return ${primaryKeyName};\r\n	}\r\n}', 'commonUtils:{:new work.codegenerate.base.CommonUtils()}\r\nmodelName:{:commonUtils.getModelNameByTableName(tableName,\"\")}\r\nprimaryKeyName:{:tableInfo.primaryKeyName}\r\nprimaryKeyType:{:tableInfo.columnInfoMap.get(primaryKeyName).javaType}');
INSERT INTO `t_db_code_generate_templet` VALUES ('2', '查询单个对象', '方法-使用sql', 'public ${modelName} findById(${primaryKeyType} ${primaryKeyName}){\r\n	String lastDbName = JPA.getCurrentConfigName();\r\n	JPA.setCurrentConfigName(库名);\r\n	${modelName} ${modelVarName} = null;\r\n	try {\r\n		String sql = \"select * from ${tableName} where ${primaryKeyName}=:${primaryKeyName}\";\r\n		Query query = JPA.em().createNativeQuery(sql, ${modelName}.class);\r\n		query.setParameter(\"${primaryKeyName}\", ${primaryKeyName});\r\n		${modelVarName} = (${modelName}) query.getSingleResult();\r\n		return ${modelVarName};\r\n	}catch(Exception e){\r\n		Logger.error(e,\"查询${modelVarName}失败\");\r\n	}finally{\r\n		JPA.setCurrentConfigName(lastDbName);\r\n	}\r\n}', 'commonUtils:{:new work.codegenerate.base.CommonUtils()}\r\nmodelName:{:commonUtils.getModelNameByTableName(tableName,\"\")}\r\nmodelVarName:{:commonUtils.firstCharLowerCase(modelName)}\r\nprimaryKeyName:{:tableInfo.primaryKeyName}\r\nprimaryKeyType:{:tableInfo.columnInfoMap.get(primaryKeyName).javaType}');
INSERT INTO `t_db_code_generate_templet` VALUES ('3', '条件查询', '代码片段-示例', 'String lastDbName = JPA.getCurrentConfigName();\r\nJPA.setCurrentConfigName(库名);\r\nList<${modelName}> ${modelVarName}List = null;\r\ntry {\r\n	String condition = \"\";\r\n	List<Object> paramList = new ArrayList<Object>();\r\n	#for(String columnName : searchParamList)\r\n		#set(String columnType=tableInfo.columnInfoMap.get(columnName).javaType)\r\n		#if(columnType==\'Integer\' || columnType==\'Long\')\r\n			if(${columnName}!=null && ${columnName}.length()>0){#b#b\r\n				condition += \" and ${columnName} = ?\";#b#b\r\n				paramList.add(new ${columnType}(${columnName}));#b#b\r\n			}#b#b\r\n		#elseif(columnType==\'String\')\r\n			if(${columnName}!=null && ${columnName}.length()>0){#b#b\r\n				condition += \" and ${columnName} like ?\";#b#b\r\n				paramList.add(\'%\'+${columnName}+\'%\');#b#b\r\n			}#b#b\r\n		#elseif(columnType==\'Date\')\r\n			if(${columnName}_start!=null && ${columnName}_start.length()>0){#b#b\r\n				condition += \" and date(${columnName}) >= ?\";#b#b\r\n				paramList.add(DateUtils.stringToDateYMd(${columnName}_start));#b#b\r\n			}#b#b\r\n			if(${columnName}_end!=null && ${columnName}_end.length()>0){#b#b\r\n				condition += \" and date(${columnName}) <= ?\";#b#b\r\n				paramList.add(DateUtils.stringToDateYMd(${columnName}_end));#b#b\r\n			}#b#b\r\n		#end\r\n	#end\r\n	String sql = \"select * from ${tableName} where 1=1\" + condition;\r\n	Query query = JPA.em().createNativeQuery(sql, ${modelName}.class);\r\n	\r\n	if(paramList.size()>0){\r\n		for(int i=0;i<paramList.size();i++){\r\n			query.setParameter(i+1, paramList.get(i));\r\n		}\r\n	}\r\n	\r\n	${modelVarName}List = query.getResultList();\r\n}catch(Exception e){\r\n	Logger.error(e,\"查询${modelVarName}List失败\");\r\n}finally{\r\n	JPA.setCurrentConfigName(lastDbName);\r\n}', 'searchParamList:{:columnNameList}\r\ncommonUtils:{:new work.codegenerate.base.CommonUtils()}\r\nmodelName:{:commonUtils.getModelNameByTableName(tableName,\"\")}\r\nmodelVarName:{:commonUtils.firstCharLowerCase(modelName)}\r\nprimaryKeyName:{:tableInfo.primaryKeyName}\r\nprimaryKeyType:{:tableInfo.columnInfoMap.get(primaryKeyName).javaType}');
INSERT INTO `t_db_code_generate_templet` VALUES ('4', '条件查询', '方法-分页,参数为map', 'public Map<String, Object> search(Map<String,Object> paramMap, int page, int rows){\r\n	Map<String,Object> map=null;\r\n	String lastDbName = JPA.getCurrentConfigName();\r\n	JPA.setCurrentConfigName(库名);\r\n	try {\r\n		String condition = \"\";\r\n		List<Object> paramList = new ArrayList<Object>();\r\n#for(String columnName : searchParamList)\r\n	#set(String columnType=tableInfo.columnInfoMap.get(columnName).javaType)\r\n	#if(columnType==\'Integer\' || columnType==\'Long\')\r\n		String ${columnName} = (String)paramMap.get(\"${columnName}\");\r\n		if(${columnName}!=null && ${columnName}.length()>0){\r\n			condition += \" and ${columnName} = ?\";\r\n			paramList.add(new ${columnType}(${columnName}));\r\n		}\r\n	#elseif(columnType==\'String\')\r\n		String ${columnName} = (String)paramMap.get(\"${columnName}\");\r\n		if(${columnName}!=null && ${columnName}.length()>0){\r\n			condition += \" and ${columnName} like ?\";\r\n			paramList.add(\'%\'+${columnName}+\'%\');\r\n		}\r\n	#elseif(columnType==\'Date\')\r\n		String ${columnName}_start = (String)paramMap.get(\"${columnName}_start\");\r\n		if(${columnName}_start!=null && ${columnName}_start.length()>0){\r\n			condition += \" and date(${columnName}) >= ?\";\r\n			paramList.add(DateUtils.stringToDateYMd(${columnName}_start));\r\n		}\r\n		String ${columnName}_end = (String)paramMap.get(\"${columnName}_end\");\r\n		if(${columnName}_end!=null && ${columnName}_end.length()>0){\r\n			condition += \" and date(${columnName}) <= ?\";\r\n			paramList.add(DateUtils.stringToDateYMd(${columnName}_end));\r\n		}\r\n	#end\r\n#end\r\n		String sql = \"select * from ${tableName} where 1=1\" + condition;\r\n		Query query = JPA.em().createNativeQuery(sql, ${modelName}.class);\r\n		\r\n		if(paramList.size()>0){\r\n			for(int i=0;i<paramList.size();i++){\r\n				query.setParameter(i+1, paramList.get(i));\r\n			}\r\n		}\r\n		\r\n		int count = query.getResultList().size();\r\n		query.setFirstResult((page-1)*rows);\r\n		query.setMaxResults(rows);\r\n		\r\n		List<${modelName}> list = query.getResultList();\r\n		map = new HashMap<String, Object>();\r\n		map.put(\"total\", count);\r\n		map.put(\"rows\", list);\r\n		return map;\r\n	} catch (Exception e) {\r\n		Logger.error(e,\"查询${tableName}出错\");\r\n		return map;\r\n	}finally{\r\n		JPA.setCurrentConfigName(lastDbName);\r\n	}\r\n}', 'searchParamList:{:columnNameList}\r\ncommonUtils:{:new work.codegenerate.base.CommonUtils()}\r\nmodelName:{:commonUtils.getModelNameByTableName(tableName,\"\")}');
INSERT INTO `t_db_code_generate_templet` VALUES ('5', '条件查询', '方法-不分页,参数为map', 'public List<${modelName}> search(Map<String,Object> paramMap){\r\n	String lastDbName = JPA.getCurrentConfigName();\r\n	JPA.setCurrentConfigName(库名);	\r\n	try {\r\n		String condition = \"\";\r\n		List<Object> paramList = new ArrayList<Object>();\r\n#for(String columnName : searchParamList)\r\n	#set(String columnType=tableInfo.columnInfoMap.get(columnName).javaType)\r\n	#if(columnType==\'Integer\' || columnType==\'Long\')\r\n		String ${columnName} = (String)paramMap.get(\"${columnName}\");\r\n		if(${columnName}!=null && ${columnName}.length()>0){\r\n			condition += \" and ${columnName} = ?\";\r\n			paramList.add(new ${columnType}(${columnName}));\r\n		}\r\n	#elseif(columnType==\'String\')\r\n		String ${columnName} = (String)paramMap.get(\"${columnName}\");\r\n		if(${columnName}!=null && ${columnName}.length()>0){\r\n			condition += \" and ${columnName} like ?\";\r\n			paramList.add(\'%\'+${columnName}+\'%\');\r\n		}\r\n	#elseif(columnType==\'Date\')\r\n		String ${columnName}_start = (String)paramMap.get(\"${columnName}_start\");\r\n		if(${columnName}_start!=null && ${columnName}_start.length()>0){\r\n			condition += \" and date(${columnName}) >= ?\";\r\n			paramList.add(DateUtils.stringToDateYMd(${columnName}_start));\r\n		}\r\n		String ${columnName}_end = (String)paramMap.get(\"${columnName}_end\");\r\n		if(${columnName}_end!=null && ${columnName}_end.length()>0){\r\n			condition += \" and date(${columnName}) <= ?\";\r\n			paramList.add(DateUtils.stringToDateYMd(${columnName}_end));\r\n		}\r\n	#end\r\n#end\r\n		String sql = \"select * from ${tableName} where 1=1\" + condition;\r\n		Query query = JPA.em().createNativeQuery(sql, ${modelName}.class);\r\n		\r\n		if(paramList.size()>0){\r\n			for(int i=0;i<paramList.size();i++){\r\n				query.setParameter(i+1, paramList.get(i));\r\n			}\r\n		}\r\n		\r\n		List<${modelName}> list = query.getResultList();\r\n		return list;\r\n	} catch (Exception e) {\r\n		Logger.error(e,\"查询${tableName}出错\");\r\n		return null;\r\n	}finally{\r\n		JPA.setCurrentConfigName(lastDbName);\r\n	}\r\n}', 'searchParamList:{:columnNameList}\r\ncommonUtils:{:new work.codegenerate.base.CommonUtils()}\r\nmodelName:{:commonUtils.getModelNameByTableName(tableName,\"\")}');
INSERT INTO `t_db_code_generate_templet` VALUES ('6', '条件查询', '方法-不分页,参数为基本类型', 'public List<${modelName}> search(${macroGet(\"searchStr\").trim()}){\r\n	String lastDbName = JPA.getCurrentConfigName();\r\n	JPA.setCurrentConfigName(库名);	\r\n	try {\r\n		String condition = \"\";\r\n		List<Object> paramList = new ArrayList<Object>();\r\n#for(String columnName : searchParamList)\r\n	#set(String columnType=tableInfo.columnInfoMap.get(columnName).javaType)\r\n	#if(columnType==\'Integer\' || columnType==\'Long\')\r\n		if(${columnName}!=null){\r\n			condition += \" and ${columnName} = ?\";\r\n			paramList.add(${columnName});\r\n		}\r\n	#elseif(columnType==\'String\')\r\n		if(${columnName}!=null && ${columnName}.length()>0){\r\n			condition += \" and ${columnName} like ?\";\r\n			paramList.add(\'%\'+${columnName}+\'%\');\r\n		}\r\n	#elseif(columnType==\'Date\')\r\n		if(${columnName}!=null){\r\n			condition += \" and ${columnName}= ?\";\r\n			paramList.add(${columnName});\r\n		}\r\n	#end\r\n#end\r\n\r\n		String sql = \"select * from ${tableName} where 1=1\" + condition;\r\n		Query query = JPA.em().createNativeQuery(sql, ${modelName}.class);\r\n		\r\n		if(paramList.size()>0){\r\n			for(int i=0;i<paramList.size();i++){\r\n				query.setParameter(i+1, paramList.get(i));\r\n			}\r\n		}\r\n		\r\n		List<${modelName}> list = query.getResultList();\r\n		return list;\r\n	} catch (Exception e) {\r\n		Logger.error(e,\"查询${tableName}出错\");\r\n		return null;\r\n	}finally{\r\n		JPA.setCurrentConfigName(lastDbName);\r\n	}\r\n}\r\n\r\n#macro searchStr()\r\n	#for(String columnName : searchParamList)${tableInfo.columnInfoMap.get(columnName).javaType} ${columnName}${!for.last?\", \":\"\"}#end\r\n#end', 'searchParamList:{:columnNameList}\r\ncommonUtils:{:new work.codegenerate.base.CommonUtils()}\r\nmodelName:{:commonUtils.getModelNameByTableName(tableName,\"\")}');
INSERT INTO `t_db_code_generate_templet` VALUES ('7', '条件查询', '方法-不分页,参数必传', 'public List<${modelName}> search${modelName}(${macroGet(\"searchParams\").trim()}){\r\n	String lastDbName = JPA.getCurrentConfigName();\r\n	JPA.setCurrentConfigName(库名);	\r\n	try {\r\n		String sql = \"select * from ${tableName} where ${macroGet(\"searchSql\").trim()}\";\r\n		Query query = JPA.em().createNativeQuery(sql, ${modelName}.class);\r\n		#for(String columnName : searchParamList)\r\n			query.setParameter(\"${columnName}\",${columnName});#b\r\n		#end\r\n		List<${modelName}> list = query.getResultList();\r\n		return list;\r\n	} catch (Exception e) {\r\n		Logger.error(e,\"查询${tableName}出错\");\r\n		return null;\r\n	}finally{\r\n		JPA.setCurrentConfigName(lastDbName);\r\n	}\r\n}\r\n\r\n#macro searchParams()\r\n	#for(String columnName : searchParamList)${tableInfo.columnInfoMap.get(columnName).javaType} ${columnName}${!for.last?\", \":\"\"}#end\r\n#end\r\n#macro searchSql()\r\n	#for(String columnName : searchParamList)${columnName}=:${columnName}${!for.last?\" and \":\"\"}#end\r\n#end', 'searchParamList:{:columnNameList}\r\ncommonUtils:{:new work.codegenerate.base.CommonUtils()}\r\nmodelName:{:commonUtils.getModelNameByTableName(tableName,\"\")}');
INSERT INTO `t_db_code_generate_templet` VALUES ('8', '添加记录', '代码片段-使用sql', 'String lastDbName = JPA.getCurrentConfigName();\r\nJPA.setCurrentConfigName(库名);\r\ntry {\r\n	if(!JPA.em().getTransaction().isActive()){\r\n		JPA.em().getTransaction().begin();\r\n	}\r\n	#set(List columnNameList = new ArrayList(tableInfo.columnInfoMap.keySet()))\r\n	String sql = \"insert into `${tableName}`(`${commonUtils.join(columnNameList,\'`,`\')}`) values (:${commonUtils.join(columnNameList,\',:\')})\";\r\n	Query query = JPA.em().createNativeQuery(sql);\r\n	##主键\r\n	query.setParameter(\"${primaryKeyName}\", \"\");\r\n	##普通列\r\n	#for(String columnName : columnNameList)\r\n		#if(!columnName.equals(primaryKeyName))\r\n			query.setParameter(\"${columnName}\",null);#b#b\r\n		#end\r\n	#end\r\n	query.executeUpdate();\r\n	\r\n	if(JPA.em().getTransaction().isActive()){\r\n		JPA.em().getTransaction().commit();\r\n	}\r\n}catch(Exception e){\r\n	if(JPA.em().getTransaction().isActive()){\r\n		JPA.em().getTransaction().rollback();\r\n	}\r\n	Logger.error(e,\"添加${modelVarName}失败\");\r\n}finally{\r\n	JPA.setCurrentConfigName(lastDbName);\r\n}', 'commonUtils:{:new work.codegenerate.base.CommonUtils()}\r\nmodelName:{:commonUtils.getModelNameByTableName(tableName,\"\")}\r\nmodelVarName:{:commonUtils.firstCharLowerCase(modelName)}\r\nprimaryKeyName:{:tableInfo.primaryKeyName}\r\nprimaryKeyType:{:tableInfo.columnInfoMap.get(primaryKeyName).javaType}');
INSERT INTO `t_db_code_generate_templet` VALUES ('9', '添加记录', '代码片段-使用model', 'String lastDbName = JPA.getCurrentConfigName();\r\nJPA.setCurrentConfigName(库名);\r\ntry {\r\n	${modelName} ${modelVarName} = new ${modelName}();\r\n	##主键\r\n	${modelVarName}.${primaryKeyName} = null;\r\n	##普通列\r\n	#for(String columnName : tableInfo.columnInfoMap.keySet())\r\n		#if(!columnName.equals(primaryKeyName))\r\n			${modelVarName}.${columnName} = null;#b#b\r\n		#end\r\n	#end\r\n	\r\n	if(!JPA.em().getTransaction().isActive()){\r\n		JPA.em().getTransaction().begin();\r\n	}\r\n	${modelVarName}.save();\r\n	if(JPA.em().getTransaction().isActive()){\r\n		JPA.em().getTransaction().commit();\r\n	}\r\n}catch(Exception e){\r\n	if(JPA.em().getTransaction().isActive()){\r\n		JPA.em().getTransaction().rollback();\r\n	}\r\n	Logger.error(e,\"添加${modelVarName}失败\");\r\n}finally{\r\n	JPA.setCurrentConfigName(lastDbName);\r\n}', 'commonUtils:{:new work.codegenerate.base.CommonUtils()}\r\nmodelName:{:commonUtils.getModelNameByTableName(tableName,\"\")}\r\nmodelVarName:{:commonUtils.firstCharLowerCase(modelName)}\r\nprimaryKeyName:{:tableInfo.primaryKeyName}\r\nprimaryKeyType:{:tableInfo.columnInfoMap.get(primaryKeyName).javaType}');
INSERT INTO `t_db_code_generate_templet` VALUES ('10', '修改记录', '代码片段-使用sql', 'String lastDbName = JPA.getCurrentConfigName();\r\nJPA.setCurrentConfigName(库名);\r\ntry {\r\n	if(!JPA.em().getTransaction().isActive()){\r\n		JPA.em().getTransaction().begin();\r\n	}\r\n	String sql = \"update `${tableName}` set ${macroGet(\"updateStr\").trim()} where `${primaryKeyName}`=:${primaryKeyName}\";\r\n	Query query = JPA.em().createNativeQuery(sql);\r\n	#for(String columnName : searchParamList)\r\n		query.setParameter(\"${columnName}\",null);#b\r\n	#end\r\n	query.setParameter(\"${primaryKeyName}\",null);\r\n	query.executeUpdate();\r\n	\r\n	if(JPA.em().getTransaction().isActive()){\r\n		JPA.em().getTransaction().commit();\r\n	}\r\n}catch(Exception e){\r\n	if(JPA.em().getTransaction().isActive()){\r\n		JPA.em().getTransaction().rollback();\r\n	}\r\n	Logger.error(e,\"修改${modelVarName}失败\");\r\n}finally{\r\n	JPA.setCurrentConfigName(lastDbName);\r\n}\r\n\r\n#macro updateStr()\r\n	#for(String columnName : searchParamList)`${columnName}`=:${columnName}${for.last?\'\':\',\'}#end\r\n#end', 'searchParamList:{:columnNameList}\r\ncommonUtils:{:new work.codegenerate.base.CommonUtils()}\r\nmodelName:{:commonUtils.getModelNameByTableName(tableName,\"\")}\r\nmodelVarName:{:commonUtils.firstCharLowerCase(modelName)}\r\nprimaryKeyName:{:tableInfo.primaryKeyName}\r\nprimaryKeyType:{:tableInfo.columnInfoMap.get(primaryKeyName).javaType}');
INSERT INTO `t_db_code_generate_templet` VALUES ('11', '删除记录', '方法-使用sql,通过id删除一个', 'public void deleteById(${primaryKeyType} ${primaryKeyName}){\r\n	String lastDbName = JPA.getCurrentConfigName();\r\n	JPA.setCurrentConfigName(库名);\r\n	try {\r\n		if(!JPA.em().getTransaction().isActive()){\r\n			JPA.em().getTransaction().begin();\r\n		}\r\n		String sql = \"delete from ${tableName} where ${primaryKeyName}=:${primaryKeyName}\";\r\n		Query query = JPA.em().createNativeQuery(sql);\r\n		query.setParameter(\"${primaryKeyName}\", ${primaryKeyName});\r\n		query.executeUpdate();\r\n		if(JPA.em().getTransaction().isActive()){\r\n			JPA.em().getTransaction().commit();\r\n		}\r\n	}catch(Exception e){\r\n		if(JPA.em().getTransaction().isActive()){\r\n			JPA.em().getTransaction().rollback();\r\n		}\r\n		Logger.error(e,\"删除${modelVarName}失败\");\r\n	}finally{\r\n		JPA.setCurrentConfigName(lastDbName);\r\n	}\r\n}', 'commonUtils:{:new work.codegenerate.base.CommonUtils()}\r\nmodelName:{:commonUtils.getModelNameByTableName(tableName,\"\")}\r\nmodelVarName:{:commonUtils.firstCharLowerCase(modelName)}\r\nprimaryKeyName:{:tableInfo.primaryKeyName}\r\nprimaryKeyType:{:tableInfo.columnInfoMap.get(primaryKeyName).javaType}');
INSERT INTO `t_db_code_generate_templet` VALUES ('12', '删除记录', '方法-使用sql,通过id删除多个', 'public void delete${modelName}ByIds(${primaryKeyType}... ${primaryKeyName}s) {\r\n	String lastDbName = JPA.getCurrentConfigName();\r\n	JPA.setCurrentConfigName(库名);\r\n	try {\r\n		if(!JPA.em().getTransaction().isActive()){\r\n			JPA.em().getTransaction().begin();\r\n		}\r\n		String sql = \"delete from ${tableName} where ${primaryKeyName} in (:${primaryKeyName}s)\";\r\n		Query query = JPA.em().createNativeQuery(sql);\r\n		query.setParameter(\"${primaryKeyName}s\", Arrays.asList(${primaryKeyName}s));\r\n		query.executeUpdate();\r\n		if(JPA.em().getTransaction().isActive()){\r\n			JPA.em().getTransaction().commit();\r\n		}\r\n	} catch (Exception e) {\r\n		if(JPA.em().getTransaction().isActive()){\r\n			JPA.em().getTransaction().rollback();\r\n		}\r\n		Logger.error(e,\"删除${modelVarName}失败,\"+ids);\r\n	}finally{\r\n		JPA.setCurrentConfigName(lastDbName);\r\n	}\r\n}', 'commonUtils:{:new work.codegenerate.base.CommonUtils()}\r\nmodelName:{:commonUtils.getModelNameByTableName(tableName,\"\")}\r\nmodelVarName:{:commonUtils.firstCharLowerCase(modelName)}\r\nprimaryKeyName:{:tableInfo.primaryKeyName}\r\nprimaryKeyType:{:tableInfo.columnInfoMap.get(primaryKeyName).javaType}');

-- ----------------------------
-- Table structure for t_db_info
-- ----------------------------
DROP TABLE IF EXISTS `t_db_info`;
CREATE TABLE `t_db_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `dbname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_db_info
-- ----------------------------
INSERT INTO `t_db_info` VALUES ('1', '开发环境_快行_qtrp_b', 'mysql', '192.168.3.60', '3311', 'wztkt', 'bus365_0502', 'qtrp_b');

-- ----------------------------
-- Table structure for t_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `t_dictionary`;
CREATE TABLE `t_dictionary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `tableName` varchar(255) DEFAULT NULL,
  `columnName` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_dictionary
-- ----------------------------
INSERT INTO `t_dictionary` VALUES ('2', '是否是测试环境', 't_remote_project', 'istest', '');
INSERT INTO `t_dictionary` VALUES ('3', '编码(文件查询模板)', 't_query_templet', 'encoding', '');
INSERT INTO `t_dictionary` VALUES ('4', '编码(http请求模板)', 't_http_templet', 'encoding', '');
INSERT INTO `t_dictionary` VALUES ('5', '请求方式', 't_http_templet', 'method', '');
INSERT INTO `t_dictionary` VALUES ('6', '编码(http批量下载模板)', 't_http_down_templet', 'encoding', '');
INSERT INTO `t_dictionary` VALUES ('7', '数据库类型', 't_db_info', 'type', '');

-- ----------------------------
-- Table structure for t_dictionary_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_dictionary_detail`;
CREATE TABLE `t_dictionary_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dictionaryId` int(11) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `showText` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_dictionary_detail
-- ----------------------------
INSERT INTO `t_dictionary_detail` VALUES ('1', '2', '1', '是', '');
INSERT INTO `t_dictionary_detail` VALUES ('2', '2', '0', '否', '');
INSERT INTO `t_dictionary_detail` VALUES ('3', '3', 'utf-8', 'utf-8', '');
INSERT INTO `t_dictionary_detail` VALUES ('4', '3', 'gbk', 'gbk', '');
INSERT INTO `t_dictionary_detail` VALUES ('5', '4', 'utf-8', 'utf-8', '');
INSERT INTO `t_dictionary_detail` VALUES ('6', '4', 'gbk', 'gbk', '');
INSERT INTO `t_dictionary_detail` VALUES ('7', '5', 'get', 'get', '');
INSERT INTO `t_dictionary_detail` VALUES ('8', '5', 'post', 'post', '');
INSERT INTO `t_dictionary_detail` VALUES ('9', '6', 'utf-8', 'utf-8', '');
INSERT INTO `t_dictionary_detail` VALUES ('10', '6', 'gbk', 'gbk', '');
INSERT INTO `t_dictionary_detail` VALUES ('11', '7', 'mysql', 'mysql', '');
INSERT INTO `t_dictionary_detail` VALUES ('12', '5', 'put', 'put', '');
INSERT INTO `t_dictionary_detail` VALUES ('13', '5', 'delete', 'delete', '');

-- ----------------------------
-- Table structure for t_http_down_templet
-- ----------------------------
DROP TABLE IF EXISTS `t_http_down_templet`;
CREATE TABLE `t_http_down_templet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `urlRegex` varchar(255) DEFAULT NULL,
  `nameRegex` varchar(255) DEFAULT NULL,
  `nextLayer` varchar(255) DEFAULT NULL,
  `nextPage` varchar(255) DEFAULT NULL,
  `encoding` varchar(255) DEFAULT NULL,
  `intervalTime` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_http_down_templet
-- ----------------------------
INSERT INTO `t_http_down_templet` VALUES ('1', '天堂图片网', 'http://www.ivsky.com/tupian/yishu/', '<a.*?href=[\'\"]([^\']*?.html)[\'\"]>[2,3]</a>\r\n<div class=\"il_img\"><a href=\"(.*?)\" title=\"(.*?)\"\r\n<div class=\"il_img\">.*?<img.*?src=[\'\"](.*?)[\'\"]', '', '0', '', 'utf-8', '0');

-- ----------------------------
-- Table structure for t_http_templet
-- ----------------------------
DROP TABLE IF EXISTS `t_http_templet`;
CREATE TABLE `t_http_templet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(1000) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `head` text,
  `param` text,
  `encoding` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_http_templet
-- ----------------------------
INSERT INTO `t_http_templet` VALUES ('1', '示例', '百度', 'http://www.baidu.com', 'get', null, null, 'utf-8');
INSERT INTO `t_http_templet` VALUES ('2', '网站-网站', '网站-班次查询', '{:subSite}/scheduleaction/searchscheduler2', 'get', 'accept:application/json,*/*', 'subSite:http://www.bus365.com\r\ndepartcityname:磐石市\r\ndepartcityid:220284000000\r\nreachstationname:吉林\r\ndepartdate:2017-05-01', 'utf-8');

-- ----------------------------
-- Table structure for t_line_replace_templet
-- ----------------------------
DROP TABLE IF EXISTS `t_line_replace_templet`;
CREATE TABLE `t_line_replace_templet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `oldTemplet` varchar(255) DEFAULT NULL,
  `newTemplet` varchar(255) DEFAULT NULL,
  `text` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_line_replace_templet
-- ----------------------------
INSERT INTO `t_line_replace_templet` VALUES ('1', '测试', '{:name}:{:value}', 'paramMap.put(\"{:name}\",\"{:value}\");', 'name:张三\r\nage:14');
INSERT INTO `t_line_replace_templet` VALUES ('4', 'java-获得字段名称(通过map)', '{:mapname}.put(\"{:key}\",{:value});', '{:key}', 'item.put(\"id\", schedule.id);\r\nitem.put(\"stationorgid\", schedule.stationorgid);\r\nitem.put(\"departstationcode\", schedule.buscode);\r\nitem.put(\"schedulecode\", schedule.schedulecode);\r\nitem.put(\"busshortname\",schedule.busshortname);\r\nitem.put(\"departtime\", schedule.departtime);\r\nitem.put(\"endtime\", schedule.endtime);\r\nitem.put(\"seattype\", schedule.seattype);\r\nitem.put(\"residualnumber\", schedule.residualnumber);\r\nitem.put(\"isaddschedule\", schedule.isaddschedule);\r\nitem.put(\"islineschedule\", schedule.islineschedule);\r\nitem.put(\"fullprice\", schedule.fullprice);\r\nitem.put(\"discountprice\", schedule.discountprice);\r\nitem.put(\"studentprice\", schedule.studentprice);\r\nitem.put(\"halfprice\", schedule.halfprice);\r\nitem.put(\"stationid\", schedule.stationid);\r\nitem.put(\"stationname\", schedule.stationname);\r\nitem.put(\"endstationname\", schedule.endstationname);\r\nitem.put(\"runtime\", schedule.runtime);\r\nitem.put(\"rundistance\", schedule.rundistance);\r\nitem.put(\"vehicletype\", schedule.vehicletype);\r\nitem.put(\"hashid\", schedule.centerscheduleplanid);\r\nitem.put(\"iscansell\", schedule.iscansell);\r\nitem.put(\"scheduletypeval\", schedule.scheduletypeval);');
INSERT INTO `t_line_replace_templet` VALUES ('5', 'java-获得字段名称(通过model声明)', '{:<\\s*>before}public {:type} {:name};{:after}', '{:name}', '    public Long id;\r\n    public Long stationorgid;	//出发站\r\n	public String departstationcode;\r\n	public String schedulecode;\r\n	public String busshortname;//出发站名称\r\n	public Time departtime;\r\n	public Time endtime;\r\n	public String seattype;\r\n	public Integer residualnumber;\r\n	public Integer isaddschedule;\r\n	public Integer islineschedule;\r\n	public Double fullprice;\r\n	public Double discountprice;\r\n	public Double studentprice;\r\n	public Double halfprice;\r\n	public Long stationid;	//停靠站id\r\n	public String stationname;//停靠站名称\r\n	public String endstationname;//终到站名称\r\n	public Integer runtime;\r\n	public Integer rundistance;\r\n	public Integer vehicletype;\r\n	public Long hashid;//数据交换平台数据库班次计划Id\r\n	public Integer iscansell;\r\n	public String scheduletypeval;  //班次类型');
INSERT INTO `t_line_replace_templet` VALUES ('6', 'java-通过word获取键值对(name_common)', '{:name}{:<\\s+>space}{:common}', '{:name}: //{:common}', 'uid    商户id\r\nreturntype	返回参数的字段类型\r\ndepartcityid	出发城市id\r\nstationid	乘车站id\r\nreachstationname	目的站名称\r\ndepartdate	发车日期\r\ndisable	是否查询不可售\r\nprecise	精确查询\r\nstart	开始条数\r\ncount	总条数');
INSERT INTO `t_line_replace_templet` VALUES ('7', 'java-键值对转换为map', '{:name}:{:value}{:<( //.*)?>other}', 'paramMap.put(\"{:name}\",\"{:value}\");{:other}', 'subSite:http://openapi.bus365.cn\r\nuid:8102000021 //商户id\r\nreturntype:0 //返回参数的字段类型\r\ndepartcityid:220284000000 //出发城市id\r\nstationid: //乘车站id\r\nreachstationname:吉林 //目的站名称\r\ndepartdate:2017-01-10 //发车日期\r\ndisable:0 //是否查询不可售\r\nprecise:1 //精确查询\r\nstart:0 //开始条数\r\ncount:20 //总条数');
INSERT INTO `t_line_replace_templet` VALUES ('8', 'java-通过word生成model(name_common_type)', '{:name}{:<\\s+>space}{:common}{:<\\s+>space}{:type}', 'public {:type} {:name}; //{:common}', 'rescode    返回状态    String\r\nresmsg	返回信息	String\r\nid	班次id	Long\r\nstationorgid	发车站id	Long\r\nschedulecode	班次编码	String\r\nbusshortname	客运站简称	String\r\ndepartdate	发车日期	String\r\ndeparttime	发车时间	String\r\nendtime	截至时间	String\r\nseattype	座位类型	String\r\nresidualnumber	余票数	Int\r\nisaddschedule	是否是加班车	Int\r\nislineschedule	是否是流水班	Int\r\nfullprice	全票价	Double\r\ndiscountprice	打折票价	Double\r\nstudentprice	学生票价	Double\r\nhalfprice	半票价	double\r\nstationid	目的站id	Long\r\nstationname	目的站名称	String\r\nendstationname	终到站名称	String\r\nruntime	运行时间	Int\r\nrundistance	运行里程	Int\r\nvehicletype	车辆类型	Int');

-- ----------------------------
-- Table structure for t_local_project
-- ----------------------------
DROP TABLE IF EXISTS `t_local_project`;
CREATE TABLE `t_local_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `packDir` varchar(255) DEFAULT NULL,
  `packFile` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_local_project
-- ----------------------------
INSERT INTO `t_local_project` VALUES ('1', 'workspace_bus', 'F:\\git\\bj_1602_wbtckt', '/app/[^/]*\r\n/public/javascripts\r\n/public/stylesheets', '/conf/routes\r\n/conf/messages');
INSERT INTO `t_local_project` VALUES ('2', 'bus365_jtb', 'F:\\git\\bj_1601_wbtckt_tm\\SRC\\bus365', '/app/[^/]*\r\n/public/javascripts\r\n/public/stylesheets', '/conf/routes\r\n/conf/messages');

-- ----------------------------
-- Table structure for t_params
-- ----------------------------
DROP TABLE IF EXISTS `t_params`;
CREATE TABLE `t_params` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `remark` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_params
-- ----------------------------
INSERT INTO `t_params` VALUES ('1', '登录账号', 'login_username', 'admin');
INSERT INTO `t_params` VALUES ('2', '登录密码', 'login_password_md5', 'c4ca4238a0b923820dcc509a6f75849b');
INSERT INTO `t_params` VALUES ('3', '加密des密钥', 'desKey', 'sssweb2_sssweb2');
INSERT INTO `t_params` VALUES ('4', '禅道账号', 'zentao_username', 'likaihao');
INSERT INTO `t_params` VALUES ('5', '禅道密码', 'zentao_password', 'xxx');
INSERT INTO `t_params` VALUES ('6', '禅道用户中文名称', 'zentao_chineseName', '李凯昊');
INSERT INTO `t_params` VALUES ('7', 'svn账号', 'svn_username', 'likaihao');
INSERT INTO `t_params` VALUES ('8', 'svn密码', 'svn_password', 'xxx');
INSERT INTO `t_params` VALUES ('9', 'svn导出文件命令window', 'svn_export_window_command', 'F:/chengxu/svn1.6/bin/svn.exe export \"${fileSvnPath}\" \"${fileSavePath}\" --username ${svn_username} --password ${svn_password}');
INSERT INTO `t_params` VALUES ('10', 'svn导出文件命令linux', 'svn_export_linux_command', 'svn export \"${fileSvnPath}\" \"${fileSavePath}\" --username ${svn_username} --password ${svn_password}');
INSERT INTO `t_params` VALUES ('11', 'git日志查询-本地仓库地址', 'gitLocalPath', '[\"F:\\git\\bj_1602_wbtckt\"]');
INSERT INTO `t_params` VALUES ('12', 'wordSql生成-word文件路径', 'wordSqlPath', '[\"F:\\数据结构\\xxx.doc\"]');
INSERT INTO `t_params` VALUES ('13', 'wordSql生成-模板', 'wordSqlTemplet', '#if(model.indexList==null && model.indexStr!=null && model.indexStr.length()>0)\r\n索引未解析!\r\n索引字段:${model.indexStr}\r\n索引参考:UNIQUE INDEX `index_a_b` (`a`,`b`) USING BTREE\r\n#end\r\n\r\n\\#\\#---Create By ${author}  ${newDateStr}  start----------------------------------------------\r\n@repeat\r\nCREATE TABLE IF NOT EXISTS `${model.name}` (\r\n  #for(service.model.Field field : model.getFieldList())\r\n`${field.name}` ${field.type}${field.isPrimaryKey()?\' PRIMARY KEY\':\'\'}${field.isUnique()&&!field.isPrimaryKey()?\' UNIQUE\':\'\'}${field.isNotNull()&&!field.isPrimaryKey()?\' NOT NULL\':\'\'}${field.comment?\" COMMENT \'\"+field.comment+\"\'\":\'\'}${for.last && (model.indexList==null || model.indexList.size()==0)?\'\':\',\'}\r\n  #end\r\n  #if(model.indexList!=null && model.indexList.size()>0)\r\nUNIQUE INDEX `index_${utils.StringUtils::join(model.indexList,\'_\')}` (`${stringUtils.join(model.indexList,\'`,`\')}`) USING BTREE\r\n  #end\r\n) CHARACTER SET utf8;\r\n\\#\\#---Create By ${author}  ${newDateStr}  end------------------------------------------------\r\n');
INSERT INTO `t_params` VALUES ('14', 'wordSql生成-模板参数', 'wordSqlTempletParam', 'author:likaihao\r\nnewDateStr:{:stringHandlePubStore.getNewDateStr()}');
INSERT INTO `t_params` VALUES ('16', 'word接口生成-word地址', 'wordInterPath', '[\"F:\\接口文档\\xxx.doc\"]');
INSERT INTO `t_params` VALUES ('17', 'word接口生成-模板', 'wordInterTemplet', '//model\r\n#for(service.model.Model model : inter.getModelList())\r\npublic class ${model==inter.getResponseModel()?responseClassName:model.getName()}{\r\n    #for(service.model.Field field : model.getFieldList())\r\n    public ${field.getType()} ${field.getName()}; ${field.getComment()?\"//\"+field.getComment():\"\"}\r\n    #end\r\n}\r\n\r\n#end\r\n\r\n//bpinterface\r\npublic ${responseClassName} ${methodName}(${macroGet(\"searchParams\").trim()});\r\n\r\n//action\r\npublic static void ${methodName}(${macroGet(\"searchParams\").trim()}){\r\n    ${responseClassName} response = new ${bpName}().${methodName}(${commonUtils.join(inter.getRequestModel().getAllFieldName(),\',\')});\r\n}\r\n\r\n//路由\r\n${inter.getMethod()}     ${inter.getUrl()}        ${actionName}.${methodName}\r\n\r\n\r\n\r\n#macro searchParams()\r\n    #for(service.model.Field field : inter.getRequestModel().getFieldList())${field.getType()} ${field.getName()}${!for.last?\", \":\"\"}#end\r\n#end\r\n\r\n');
INSERT INTO `t_params` VALUES ('18', 'word接口生成-模板参数', 'wordInterTempletParam', 'actionName:XxxAction\r\nbpName:XxxBP\r\nmethodName:xxxMethod\r\nresponseClassName:XxxResponse\r\ncommonUtils:{:new work.codegenerate.base.CommonUtils()}');
INSERT INTO `t_params` VALUES ('19', '模板文本编辑-保存的模板内容', 'templetEditorStr', 'xxx');
INSERT INTO `t_params` VALUES ('20', '禅道添加任务-模板', 'zentaoAddTaskTemplet', '------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"module\"\r\n\r\n0\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"assignedTo[]\"\r\n\r\n${assignedTo}\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"type\"\r\n\r\ndevel\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"name\"\r\n\r\n${name}\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"desc\"\r\n\r\n${desc}\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"pri\"\r\n\r\n${pri}\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"estimate\"\r\n\r\n${estimate}\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"estStarted\"\r\n\r\n${estStarted}\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"deadline\"\r\n\r\n${deadline}\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"mailto[]\"\r\n\r\n\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"files[]\"\r\n\r\n\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"labels[]\"\r\n\r\n\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"files[]\"\r\n\r\n\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"labels[]\"\r\n\r\n\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ\r\nContent-Disposition: form-data; name=\"after\"\r\n\r\ntoTaskList\r\n------WebKitFormBoundaryWTfPj14iPlGgHUbZ--');

-- ----------------------------
-- Table structure for t_query_templet
-- ----------------------------
DROP TABLE IF EXISTS `t_query_templet`;
CREATE TABLE `t_query_templet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `pathPattern` varchar(255) DEFAULT NULL,
  `noPathPattern` varchar(255) DEFAULT NULL,
  `fileNamePattern` varchar(255) DEFAULT NULL,
  `noFileNamePattern` varchar(255) DEFAULT NULL,
  `encoding` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_query_templet
-- ----------------------------
INSERT INTO `t_query_templet` VALUES ('1', 'workspace_bus', 'F:\\git\\bj_1602_wbtckt', '/app/.*,/conf/.*,/public/.*,/SQL/.*,/configmodify/.*', '', '.*\\.java,.*\\.conf,.*\\.properties,.*\\.js,.*\\.css,.*\\.xml,.*\\.html,.*\\.txt,.*\\.sql,[^.]*', '', 'utf-8');
INSERT INTO `t_query_templet` VALUES ('2', '新笔记', 'F:\\git\\note', '', null, '', '', 'utf-8');

-- ----------------------------
-- Table structure for t_redis_info
-- ----------------------------
DROP TABLE IF EXISTS `t_redis_info`;
CREATE TABLE `t_redis_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_redis_info
-- ----------------------------
INSERT INTO `t_redis_info` VALUES ('1', '开发环境(192.168.3.39:6379)', '192.168.3.39', '6379', 'bus365_0502');

-- ----------------------------
-- Table structure for t_remote_command
-- ----------------------------
DROP TABLE IF EXISTS `t_remote_command`;
CREATE TABLE `t_remote_command` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commandGroupId` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `command` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=217 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_remote_command
-- ----------------------------
INSERT INTO `t_remote_command` VALUES ('4', '1', '打包-局部打包', 'tar -xzvf {:remoteProject.path}/{:gzFileName} -C {:remoteProject.path}\r\nrm -f {:remoteProject.path}/{:gzFileName}');
INSERT INTO `t_remote_command` VALUES ('5', '1', '打包-全部打包', 'play stop {:remoteProject.path}\r\nplay clean {:remoteProject.path}\r\nfind {:remoteProject.path} -regex \"{:remoteProject.path}/app/[^/]*\" -type d -exec rm -r {} \\;\r\ntar -xzvf {:remoteProject.path}/{:gzFileName} -C {:remoteProject.path}\r\nrm -f {:remoteProject.path}/{:gzFileName}\r\nplay start {:remoteProject.path}');
INSERT INTO `t_remote_command` VALUES ('6', '1', 'play-项目启动', 'play start');
INSERT INTO `t_remote_command` VALUES ('7', '1', 'play-项目清理', 'play clean');
INSERT INTO `t_remote_command` VALUES ('8', '1', 'play-项目停止', 'play stop');
INSERT INTO `t_remote_command` VALUES ('9', '1', 'play-查看实时日志', 'tail -n 50 -f logs/application.log');
INSERT INTO `t_remote_command` VALUES ('10', '1', 'play-访问项目', 'curl -m 1  http://{:remoteProject.ip}:{:remoteProject.port}');

-- ----------------------------
-- Table structure for t_remote_command_group
-- ----------------------------
DROP TABLE IF EXISTS `t_remote_command_group`;
CREATE TABLE `t_remote_command_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_remote_command_group
-- ----------------------------
INSERT INTO `t_remote_command_group` VALUES ('1', 'play');

-- ----------------------------
-- Table structure for t_remote_project
-- ----------------------------
DROP TABLE IF EXISTS `t_remote_project`;
CREATE TABLE `t_remote_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `commandGroupId` int(11) DEFAULT NULL,
  `command` text,
  `localProject` varchar(255) DEFAULT NULL,
  `istest` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=689 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_remote_project
-- ----------------------------
INSERT INTO `t_remote_project` VALUES ('7', '网站-开发环境', '开发环境_bus365_子站', '192.168.3.39', 'root', 'bus365_0502', '/home/nmdbus365', '7081', '1', '', 'bus365', '1');

-- ----------------------------
-- Table structure for t_string_handle_templet
-- ----------------------------
DROP TABLE IF EXISTS `t_string_handle_templet`;
CREATE TABLE `t_string_handle_templet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `code` text,
  `text` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_string_handle_templet
-- ----------------------------
INSERT INTO `t_string_handle_templet` VALUES ('7', '替换/变形', '其它-&换行', 're1:&\r\nre2:\\r\\n\r\nmatchCase:{:new Boolean(false)} //是否区分大小写\r\n_newValue:{:stringHandlePubStore.replace(_value,re1,re2,matchCase)}', null);
INSERT INTO `t_string_handle_templet` VALUES ('8', '替换/变形', '其它-,换行', 're1:,\r\nre2:\\r\\n\r\nmatchCase:{:new Boolean(false)} //是否区分大小写\r\n_newValue:{:stringHandlePubStore.replace(_value,re1,re2,matchCase)}', null);
INSERT INTO `t_string_handle_templet` VALUES ('9', '替换/变形', 'java-声明字符串变量', 're1:(\\\\|\")\r\nre2:\\\\$1\r\nmatchCase:{:new Boolean(false)} //是否区分大小写\r\n_newValue:{:stringHandlePubStore.replace(_value,re1,re2,matchCase)}\r\n\r\nre1:\\r\\n\r\nre2:\\\\r\\\\n\r\n_newValue:{:stringHandlePubStore.replace(_newValue,re1,re2,matchCase)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('11', '替换/变形', '其它-在浏览器打开多个页面', '//复制到文本里\r\nhttp://jl.bus365.com\r\nhttp://nm.bus365.com\r\nhttp://gs.bus365.com\r\nhttp://ah.bus365.com\r\nhttp://jx.bus365.com\r\nhttp://hb.bus365.com\r\nhttp://hlj.bus365.com\r\nhttp://tj.bus365.com\r\nhttp://www.bus365.com\r\nhttp://hbz.bus365.com\r\nhttp://nx.bus365.com\r\nhttp://qh.bus365.com\r\n_newValue:{:stringHandlePubStore.getJSByHrefList(_value)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('12', '替换/变形', 'java-字符串转义', 're1:(\\\\|\")\r\nre2:\\\\$1\r\nmatchCase:{:new Boolean(false)} //是否区分大小写\r\n_newValue:{:stringHandlePubStore.replace(_value,re1,re2,matchCase)}\r\n//str = str.replaceAll(\"(\\\\\\\\|\\\")\", \"\\\\\\\\$1\");', '');
INSERT INTO `t_string_handle_templet` VALUES ('13', '替换/变形', '其它-换行字符串连接(\'\',\'\')', 're1:[\\r\\n]+\r\nre2:\",\"\r\nmatchCase:{:new Boolean(false)} //是否区分大小写\r\n_newValue:{:stringHandlePubStore.replace(_value,re1,re2,matchCase)}\r\n_newValue:{:\"\\\"\"+_newValue+\"\\\"\"}', null);
INSERT INTO `t_string_handle_templet` VALUES ('14', '替换/变形', '其它-换行字符串连接(\',\')', 're1:[\\r\\n]+\r\nre2:\',\'\r\nmatchCase:{:new Boolean(false)} //是否区分大小写\r\n_newValue:{:stringHandlePubStore.replace(_value,re1,re2,matchCase)}\r\n_newValue:{:\"\'\"+_newValue+\"\'\"}', null);
INSERT INTO `t_string_handle_templet` VALUES ('15', '排序/去重', '其它-排序-去重', '_newValue:{:stringHandlePubStore.sort_distinct(_value)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('16', '排序/去重', '其它-排序', '_newValue:{:stringHandlePubStore.sort(_value)}', null);
INSERT INTO `t_string_handle_templet` VALUES ('17', '替换/变形', '其它-正则提取(最后一组)', 're:你好,\"(.*?)\"\r\n_newValue:{:stringUtils.join(regexUtils.getSubstrByRegexReturnList(_value,re),\'\\n\')}', '');
INSERT INTO `t_string_handle_templet` VALUES ('19', '加密/解密', '加密解密-md5', '_newValue:{:algorithmUtils.getMD5Value(_value)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('20', '加密/解密', '加密解密-rsa公钥解密', 'publicKey:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+cRpab4AYu/kIUke5rB7/SPNBeEdNmrzUkbT3yeUg/5WiEPEzfEnLWLxVhoA7H4ek6bkbdkOYzrQ0rTypzJc3fTUJVEZsljxUcXuOmOOP6jJbDYmWh2AKSl7QR555ciiXICwAFy4bF7aB7GPjZJ8WY5TdSWHwNeQOz7845fMtPQIDAQAB\r\n_newValue:{:stringUtils.byteToString(algorithmUtils.decryptByPublicKey(algorithmUtils.decodeBase64(_value), publicKey))}', '');
INSERT INTO `t_string_handle_templet` VALUES ('21', '加密/解密', '加密解密-rsa私钥加密', 'privateKey:MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL5xGlpvgBi7+QhSR7msHv9I80F4R02avNSRtPfJ5SD/laIQ8TN8SctYvFWGgDsfh6TpuRt2Q5jOtDStPKnMlzd9NQlURmyWPFRxe46Y44/qMlsNiZaHYApKXtBHnnlyKJcgLAAXLhsXtoHsY+NknxZjlN1JYfA15A7Pvzjl8y09AgMBAAECgYBA9qwatXZo+ZZV9FtrRv+77dKtPq50Fpjbmmi1LjXvL8CHbGgbPAagS0DTm/IaOQM4ilIOwCUG91wZrA7JjXT3bh1JjxD3Bcbhb6qOXNfDKTIDUGKOol9ru9YEaEaIGAatLPNHQvXjFlpUiLSTQZXVhUFhgWr1tOAKiBijunhcAQJBAOGm7kkHiUGbmbzCRASAnOwQvQ+oQd3aTyEigx+aBMBwUn0wdz1Y1PQxKt8CTYYcxRKMHS9+98KrEvaXbV2kwP0CQQDYDec8OjEqmoJKr+DTEXafiDZ+EJyn5UzrMlAy7lQBqY0ab1NHtFE4e6RtPJ8wQdXibfI5iDJNgXPyCWIOMPFBAkASSjWGNdYqOzJ62kQpGUOe3rx0SG8OTGOdi+6U4ScBchk9jHAdyNV59YJEESu3cx2GI1U6RFBQtw0Sb7Oto20BAkA/ResXZ09o07EeoF1uUunzlJeo2cKXJP8ezjBQih2OExKg0EOKd1NmAEfKOfvAzTcPRfWvRaS9sBC9rGPMFidBAkEAucoo+Y/gyirErYSYKcz0JbvnKM4zDcFXGNqKscoLLRzvjmZL4qkSfJQJBFf1D6J9Dl25lyAmV12/2rLd1xF/Bg==\r\n_newValue:{:algorithmUtils.encodeBase64(algorithmUtils.encryptByPrivateKey(stringUtils.stringToByte(_value), privateKey))}', '');
INSERT INTO `t_string_handle_templet` VALUES ('22', '加密/解密', '加密解密-sha1', '_newValue:{:algorithmUtils.getSHA1Value(_value)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('23', '加密/解密', '加密解密-生成新密钥', 'map:{:algorithmUtils.createPublicAndPrivateKey()}\r\nmd5Key:{:algorithmUtils.getMD5Value(map.privateKey+map.publicKey)}\r\n_newValue:{:\"publicKey:\"+map.publicKey+\"\\r\\nprivateKey:\"+map.privateKey+\"\\r\\nmd5Key:\"+md5Key}', '无');
INSERT INTO `t_string_handle_templet` VALUES ('24', '格式化', '格式化-json格式化', '_newValue:{:jsonUtils.getFormatJsonStr(_value)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('25', '格式化', '格式化-xml格式化', '_newValue:{:xmlUtils.getFormatXml(_value)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('26', '编码/解码', '编码解码-string(base64)', '_newValue:{:stringUtils.byteToString(algorithmUtils.decodeBase64(_value))}', '');
INSERT INTO `t_string_handle_templet` VALUES ('27', '编码/解码', '编码解码-stringToUnicode', '_newValue:{:stringUtils.stringToUnicode(_value)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('28', '编码/解码', '编码解码-unicodeToString', '_newValue:{:stringUtils.unicodeToString(_value)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('29', '编码/解码', '编码解码-url编码-分段', '_newValue:{:stringUtils.urlEncoding_sub(_value)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('30', '编码/解码', '编码解码-url编码', '_newValue:{:stringUtils.urlEncoding(_value)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('31', '编码/解码', '编码解码-url解码-分段', '_newValue:{:stringUtils.urlDecoding_sub(_value)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('32', '编码/解码', '编码解码-url解码', '_newValue:{:stringUtils.urlDecoding(_value)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('33', '编码/解码', '编码解码-打印所有编码的字符串', '_newValue:{:stringHandlePubStore.printAllEncodingStr(_value)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('36', '替换/变形', 'java-从参数声明中获取参数名称', 're1:,\r\nre2:\\r\\n\r\nmatchCase:{:new Boolean(false)} //是否区分大小写\r\n_newValue:{:stringHandlePubStore.replace(_value,re1,re2,matchCase)}\r\n\r\nre:(?:^|\\n).*?\\s+(\\S*?)\\s*(?=\\n|$)\r\n_newValue:{:stringUtils.join(regexUtils.getSubstrByRegexReturnList(_newValue,re),\',\')}\r\n', 'boolean ismock,String callback, String netname,String netaddress,Long departcityid,String departcityname,String reachstationname,String departdate, Integer skb,String  errorText');
INSERT INTO `t_string_handle_templet` VALUES ('38', '替换/变形', '其它-将\\t替换为空格', 'num:4 //\\t最大占用的空格数\r\n_newValue:{:stringHandlePubStore.ltToSpace(_value,4)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('41', '格式化', '格式化-json格式化-字符串中的json', '_newValue:{:jsonUtils.getFormatJsonStr2(_value)}', '{\\\"paymode\\\":{\\\"appId\\\":\\\"wx0d4fd8a16d82c19d\\\",\\\"timeStamp\\\":\\\"1479802612\\\",\\\"nonceStr\\\":\\\"b4jzc2iuq7\\\",\\\"signType\\\":null,\\\"packageValue\\\":\\\"Sign=WXpay\\\",\\\"codeUrl\\\":null,\\\"paySign\\\":null,\\\"sign\\\":\\\"E60CCF845BB3B5CCD072ECEE97CC042A\\\",\\\"appid\\\":null,\\\"timestamp\\\":null,\\\"noncestr\\\":null,\\\"partnerid\\\":null,\\\"prepayid\\\":null,\\\"partnerId\\\":\\\"1240533402\\\",\\\"prepayId\\\":\\\"wx20161122161652b858c24d370184413168\\\"}}');
INSERT INTO `t_string_handle_templet` VALUES ('42', '排序/去重', '其它-获取重复值', '_newValue:{:stringHandlePubStore.getDouble(_value)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('43', '替换/变形', 'url-键值对生成url参数', '_newValue:{:stringHandlePubStore.getUrlParamByStr(_value)}', 'startcode:110000000000\r\nreachcode:110000000000\r\nstartaddress:鼓楼大街\r\norigin:39.951893,116.412605\r\nreachaddress:天方饭店\r\ndestination:39.961405,116.362731\r\nvehicletypeid:1000011\r\npassengername:崔亚坤\r\npassengerphone:15601225929\r\ndeparttime:2016-12-24 18:00\r\nterminalid:1000011\r\nflightno:');
INSERT INTO `t_string_handle_templet` VALUES ('44', '编码/解码', '编码解码-base64(string)', '_newValue:{:algorithmUtils.encodeBase64(_value.getBytes())}', '');
INSERT INTO `t_string_handle_templet` VALUES ('45', '替换/变形', 'java-从参数声明中获取model字段', 're1:, ?\r\nre2:{:\";\\r\\npublic \"}\r\nmatchCase:{:new Boolean(false)} //是否区分大小写\r\n_newValue:{:stringHandlePubStore.replace(_value,re1,re2,matchCase)}\r\n_newValue:{:\"public \"+_newValue+\";\"}', 'String citycode, String routenamealias, String departdate,String reachname, String businesscode, String stationcode, Integer departtype');
INSERT INTO `t_string_handle_templet` VALUES ('46', '替换/变形', 'java-获取字段名称(通过model声明)', 're:public .+? (.*?);\r\n_newValue:{:stringUtils.join(regexUtils.getSubstrByRegexReturnList(_value,re),\'\\n\')}', '    @Id\r\n	/**Id */\r\n	public Long id;\r\n	/**机构代码 */\r\n	public String orgcode;\r\n	/**机构名称 */\r\n	public String orgname;\r\n	/**班次代码 */\r\n	public String schedulecode;\r\n	/**发车日期 */\r\n	public Date departdate;\r\n	/**发车时间 按实际设置的发车时间+约小时数换算出来 生成班次计划时处理*/\r\n	public String departtime;\r\n	/**线路代码 */\r\n	public String routecode;\r\n	/**线路名称 */\r\n	public String routename;\r\n	/**站点代码 */\r\n	public String stationcode;\r\n	/**站点名称 */\r\n	public String stationname;\r\n	/**约公里数 */\r\n	public Double rangekm;\r\n	/**约小时数 */\r\n	public Double hour;\r\n	/**线路价格 */\r\n	public Double lineprice;\r\n	/**站点顺序 */\r\n	public Integer stationorder;\r\n	/**站点标识 参照字典表35*/\r\n	public Integer stationflag;\r\n	/**是可用 1-可用0-不可用 参照字典表*/\r\n	public Integer isactive;\r\n	/**操作员代码 */\r\n	public String usercode;\r\n	/**操作员名称 */\r\n	public String username;\r\n	/**更新时间 */\r\n	@Temporal(TemporalType.TIMESTAMP)\r\n	public Date updatetime;\r\n	/**创建时间 */\r\n	@Temporal(TemporalType.TIMESTAMP)\r\n	public Date createtime;\r\n	\r\n	\r\n	\r\n	/**\r\n	 * 业务代码\r\n	 */\r\n	public String businesscode;\r\n	/**\r\n	 * 业务名称\r\n	 */\r\n	public String businessname;');
INSERT INTO `t_string_handle_templet` VALUES ('47', '排序/去重', '其它-获取没有重复的值', '_newValue:{:stringHandlePubStore.getNotDouble(_value)}', '61040162666\r\n61040162665\r\n61550162386\r\n61570162385\r\n61030162664\r\n61320162663\r\n61790162379\r\n61690162378\r\n61410162662\r\n61650162377\r\n61661162376\r\n61800162372\r\n61970162371\r\n61060162654\r\n61860162370\r\n61031162652\r\n61250162651\r\n61381162649\r\n61501162648\r\n61630162369\r\n61690162367\r\n61861162365\r\n61691162364\r\n61550162362\r\n61031162645\r\n61410162644\r\n61480162641\r\n61870162357\r\n61090162637\r\n61810162354\r\n61791162353\r\n61860162352\r\n61170162636\r\n61141162634\r\n61230162633\r\n61371162632\r\n61190162631\r\n61520162343\r\n61870162342\r\n61520162341\r\n61980162340\r\n61870162339\r\n61270162629\r\n61900162338\r\n61241162626\r\n61930162337\r\n61220162623\r\n61520162341\r\n61520162343\r\n61550162362\r\n61630162369\r\n61650162377\r\n61661162376\r\n61690162367\r\n61690162378\r\n61691162364\r\n61790162379\r\n61791162353\r\n61800162372\r\n61810162354\r\n61860162352\r\n61860162370\r\n61861162365\r\n61870162339\r\n61870162342\r\n61870162357\r\n61900162338\r\n61930162337\r\n61970162371\r\n61980162340\r\n61030162664\r\n61031162645\r\n61031162652\r\n61060162654\r\n61090162637\r\n61141162634\r\n61170162636\r\n61190162631\r\n61220162623\r\n61230162633\r\n61241162626\r\n61250162651\r\n61270162629\r\n61320162663\r\n61371162632\r\n61381162649\r\n61410162644\r\n61410162662\r\n61480162641\r\n61501162648');
INSERT INTO `t_string_handle_templet` VALUES ('48', '加密/解密', '加密解密-aes加密', 'desKey:lkhdemo_sssweb2\r\n_newValue:{:algorithmUtils.encryptAES(_value,desKey)}', '');
INSERT INTO `t_string_handle_templet` VALUES ('49', '加密/解密', '加密解密-aes解密', 'desKey:lkhdemo_sssweb2\r\n_newValue:{:algorithmUtils.decryptAES(_value,desKey)}', '');
-- ------@2017-01@end--------





-- ------@2017-06@start--------
-- @repeat{select count(*) from information_schema.`COLUMNS` WHERE TABLE_SCHEMA = '@@dbname' AND TABLE_NAME = 't_remote_command' AND COLUMN_NAME = 'func';}

INSERT INTO `t_params` (`id`, `remark`, `name`, `value`) VALUES ('21', 'git日志查询-git执行文件地址', 'gitExePath', 'F:\\chengxu\\Git\\bin\\sh.exe');
INSERT INTO `t_params` (`id`, `remark`, `name`, `value`) VALUES ('22', 'git日志查询-忽略的文件名称', 'gitLogIgorName', 'resource.properties,application.conf,kafka.properties,log4j.properties,.gitignore,.project');
INSERT INTO `t_params` (`id`, `remark`, `name`, `value`) VALUES ('23', 'notepad++执行文件地址', 'notepadPath', 'E:\\chengxu\\Notepad++\\notepad++.exe');
INSERT INTO `t_params` (`id`, `remark`, `name`, `value`) VALUES ('24', 'SecureCRT执行文件地址', 'SecureCRTExePath', 'F:\\chengxu\\SecureCRTSecureFX_HH_x86_7.0.0.326\\App\\VanDyke Clients\\SecureCRT.exe');

ALTER TABLE `t_remote_command` add `func` varchar(255) COMMENT '业务功能';

delete from `t_remote_command` where commandGroupId=1;

INSERT INTO `t_remote_command` (`id`, `commandGroupId`, `name`, `command`, `func`) VALUES ('4', '1', '打包-局部打包', 'tar -xzvf {:remoteProject.path}/{:gzFileName} -C {:remoteProject.path}\r\nrm -f {:remoteProject.path}/{:gzFileName}', 'ssh-pack');
INSERT INTO `t_remote_command` (`id`, `commandGroupId`, `name`, `command`, `func`) VALUES ('5', '1', '打包-全部打包', 'play stop {:remoteProject.path}\r\nplay clean {:remoteProject.path}\r\nfind {:remoteProject.path} -regex \"{:remoteProject.path}/app/[^/]*\" -type d -exec rm -r {} \\;\r\ntar -xzvf {:remoteProject.path}/{:gzFileName} -C {:remoteProject.path}\r\nrm -f {:remoteProject.path}/{:gzFileName}\r\nplay start {:remoteProject.path}', 'ssh-pack');
INSERT INTO `t_remote_command` (`id`, `commandGroupId`, `name`, `command`, `func`) VALUES ('6', '1', 'play-启动项目', 'play start', 'ssh-client,ssh-console');
INSERT INTO `t_remote_command` (`id`, `commandGroupId`, `name`, `command`, `func`) VALUES ('7', '1', 'play-清理项目', 'play clean', 'ssh-client,ssh-console');
INSERT INTO `t_remote_command` (`id`, `commandGroupId`, `name`, `command`, `func`) VALUES ('8', '1', 'play-停止项目', 'play stop', 'ssh-client,ssh-console');
INSERT INTO `t_remote_command` (`id`, `commandGroupId`, `name`, `command`, `func`) VALUES ('9', '1', 'play-查看实时日志', 'tail -n 50 -f logs/application.log', 'ssh-client');
INSERT INTO `t_remote_command` (`id`, `commandGroupId`, `name`, `command`, `func`) VALUES ('10', '1', 'play-查看最后200行日志', 'tail -n 200 logs/application.log', 'ssh-console');
INSERT INTO `t_remote_command` (`id`, `commandGroupId`, `name`, `command`, `func`) VALUES ('11', '1', 'play-访问项目', 'curl -m 1  http://{:remoteProject.ip}:{:remoteProject.port}', 'ssh-client,ssh-console');
INSERT INTO `t_remote_command` (`id`, `commandGroupId`, `name`, `command`, `func`) VALUES ('12', '1', 'play-status', 'play status', 'ssh-client,ssh-console');
-- ------@2017-06@end--------

-- ------@2017-07@start--------
-- @repeat{SELECT  COUNT(*) FROM `t_params` WHERE `name`='onLinePackage';}
INSERT INTO `t_params` (`id`, `remark`, `name`, `value`) VALUES ('25', '是否允许正式环境打包', 'onLinePackage', '0');
-- ------@2017-07@end--------

-- ------@2017-08-11@start--------
-- @repeat{SELECT  COUNT(*) FROM `t_params` WHERE `name`='fortress_sid';}
INSERT INTO `t_params` (`id`, `remark`, `name`, `value`) VALUES ('26', '堡垒机sid', 'fortress_sid', 'xxx');
-- ------@2017-08-11@end--------




