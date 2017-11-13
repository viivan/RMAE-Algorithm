public class Cooperation {
	List<File> wrdl = getWRDL();
	Map<String, Object> memoryPool = cache(wrdl);
	public Response cooperate (Request request) {
		String url = request.getRequestURI();
		Executor executor = getRouter(url);//执行器
		if (executor == NULL) {
			return NULL;
		}
		Profile profile = analyze(request);//请求参数
		Config config = profile.lookup(memoryPool);//配置片段
		if (config == NULL) {
			return NULL;
		}
		List<Microservice> services = new ArrayList<Microservice>();//微服务序列
		for (Microservice m : config.getMicroservices()) {
			if (microserviceRepository.contains(m)) {
				services.add(m);
			} else {
				inform(registry);
				registry.update(wrdl);
				cache(wrdl);
			}
		}
		Response response = executor.doMicroservices(services);
		return response;
	}
	public List<File> getWRDL() {
		List<File> files = new ArrayList<File>();
		String[] paths = new String[]{"browse.xml","delete.xml","insert.xml","update.xml","import.xml","export.xml","other.xml"};
		for (String path : paths) {
			files.add(new File(path));
		}
		return files;
	}
	public Map<String, Map> cache(List<File> wrdl) {
		Map<String, Map> memoryPool = new HashMap<String, Map>();
		for(File f : wrdl) {
			switch (f.getName();) {
				case "browse.xml":
					memoryPool.put("browse", doBrowse(f));
					break;
				case "delete.xml":
					memoryPool.put("delete", doDelete(f));
					break;
				case "insert.xml":
					memoryPool.put("insert", doInsert(f));
					break;
				case "update.xml":
					memoryPool.put("update", doUpdate(f));
					break;
				case "import.xml":
					memoryPool.put("import", doImport(f));
					break;
				case "export.xml":
					memoryPool.put("export", doExport(f));
					break;
				case "other.xml":
					memoryPool.put("other", doOther(f));
					break;
			}
		}
		return memoryPool;
	}
	public Executor getRouter(String url) {
		String[] s = url.split("/");
		String tail = s[s.length-1];
		Class clazz= Class.forName(tail.substring(0,tail.length()-4));
		Executor executor = (Executor)clazz.newInstance();
		return executor;
	}
	public Profile analyze(Request request) {
		String url = request.getRequestURI();
		String method = request.getMethod();
		Profile profile = new Profile(url，method);
		return profile;
	}
	public void inform(Registry registry) {
		publishMessage(registry, information);
	}
	public Map<String, BrowseBean1> doBrowse(File f) {
		Map<String, BrowseBean1> map1 = new HashMap<String, BrowseBean1>();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(f);
		List list = document.selectNodes("/BrowseElements/element/@flag");
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Attribute attribute = (Attribute) iter.next();
			String flag = attribute.getValue();
			List listTemp1 = document.selectNodes("/BrowseElements/element[@flag='"
					+ flag + "']/@pageSize");
			Iterator iterTemp1 = listTemp1.iterator();
			int pageSize = 0;
			while (iterTemp1.hasNext()) {
				Attribute attribute1 = (Attribute) iterTemp1.next();
				pageSize = Integer.parseInt(attribute1.getValue());
			}
			BrowseBean1 browseBean1 = new BrowseBean1();
			browseBean1.setPageSize(pageSize);
			List listTemp2 = document.selectNodes("/BrowseElements/element[@flag='"
					+ flag + "']/sql/@value");
			Iterator iterTemp2 = listTemp2.iterator();
			LinkedHashMap<String,BrowseBean2> map2 = new LinkedHashMap<String,BrowseBean2>();
			while (iterTemp2.hasNext()) {
				Attribute attribute2 = (Attribute) iterTemp2.next();
				String value = attribute2.getValue();
				BrowseBean2 browseBean2 = new BrowseBean2();
				List listTemp3 = document.selectNodes("/BrowseElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/@key");
				Iterator iterTemp3 = listTemp3.iterator();
				while (iterTemp3.hasNext()) {
					Attribute attribute3 = (Attribute) iterTemp3.next();
					String key = attribute3.getValue();
					browseBean2.setKey(key);
				}
				List listTemp4 = document.selectNodes("/BrowseElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/struct");
				Iterator iterTemp4 = listTemp4.iterator();
				ArrayList<StructBean> struct = new ArrayList<StructBean>();
				while (iterTemp4.hasNext()) {
					Element element = (Element) iterTemp4.next();
					StructBean structBean = new StructBean();
					structBean.setStructValue(element.getText());
					structBean.setSession(element.attributeValue("session"));
					structBean.setRequest(element.attributeValue("request"));
					struct.add(structBean);
				}
				browseBean2.setStruct(struct);
				List listTemp5 = document.selectNodes("/BrowseElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/param");
				Iterator iterTemp5 = listTemp5.iterator();
				ArrayList<ParamBean> param = new ArrayList<ParamBean>();
				while (iterTemp5.hasNext()) {
					Element element = (Element) iterTemp5.next();
					ParamBean paramBean = new ParamBean();
					paramBean.setParamValue(element.getText());
					paramBean.setNotNull(element.attributeValue("notNull"));
					paramBean.setSession(element.attributeValue("session"));
					paramBean.setRequest(element.attributeValue("request"));
					paramBean.setDrop(element.attributeValue("drop"));
					paramBean.setTimeStart(element.attributeValue("timeStart"));
					paramBean.setTimeEnd(element.attributeValue("timeEnd"));
					param.add(paramBean);
				}
				browseBean2.setParam(param);
				List listTemp6 = document.selectNodes("/BrowseElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/title");
				Iterator iterTemp6 = listTemp6.iterator();
				ArrayList<String> title = new ArrayList<String>();
				while (iterTemp6.hasNext()) {
					Element element = (Element) iterTemp6.next();
					title.add(element.getText());
				}
				browseBean2.setTitle(title);
				List listTemp7 = document.selectNodes("/BrowseElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/output");
				Iterator iterTemp7 = listTemp7.iterator();
				ArrayList<String> output = new ArrayList<String>();
				while (iterTemp7.hasNext()) {
					Element element = (Element) iterTemp7.next();
					output.add(element.getText());
				}
				browseBean2.setOutput(output);
				map2.put(value, browseBean2);
			}
			browseBean1.setMap(map2);
			map1.put(flag, browseBean1);
		}
		return map1;
	}
	public Map<String, BrowseBean1> doDelete(File f) {
		Map<String, BrowseBean1> map1 = new HashMap<String, BrowseBean1>();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(f);
		List list = document.selectNodes("/DeleteElements/element/@flag");
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Attribute attribute = (Attribute) iter.next();
			String flag = attribute.getValue();
			List listTemp1 = document.selectNodes("/DeleteElements/element[@flag='"
					+ flag + "']/sql/@value");
			Iterator iterTemp1 = listTemp1.iterator();
			LinkedHashMap<String, DeleteBean2> map2 = new LinkedHashMap<String, DeleteBean2>();
			while (iterTemp1.hasNext()) {
				Attribute attribute1 = (Attribute) iterTemp1.next();
				String value = attribute1.getValue();
				List listTemp2 = document.selectNodes("/DeleteElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/struct");
				Iterator iterTemp2 = listTemp2.iterator();
				ArrayList<StructBean> struct = new ArrayList<StructBean>();
				while (iterTemp2.hasNext()) {
					Element element = (Element) iterTemp2.next();
					StructBean structBean = new StructBean();
					structBean.setStructValue(element.getText());
					structBean.setSession(element.attributeValue("session"));
					structBean.setRequest(element.attributeValue("request"));
					struct.add(structBean);
				}
				DeleteBean2 deleteBean2 = new DeleteBean2();
				deleteBean2.setStruct(struct);
				List listTemp3 = document.selectNodes("/DeleteElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/param");
				Iterator iterTemp3 = listTemp3.iterator();
				ArrayList<ParamBean> param = new ArrayList<ParamBean>();
				while (iterTemp3.hasNext()) {
					Element element = (Element) iterTemp3.next();
					ParamBean paramBean = new ParamBean();
					paramBean.setParamValue(element.getText());
					paramBean.setNotNull(element.attributeValue("notNull"));
					paramBean.setNeedSplit(element.attributeValue("needSplit"));
					paramBean.setSession(element.attributeValue("session"));
					paramBean.setRequest(element.attributeValue("request"));
					param.add(paramBean);
				}
				deleteBean2.setParam(param);
				map2.put(value, deleteBean2);
			}
			DeleteBean1 deleteBean1 = new DeleteBean1();
			deleteBean1.setMap(map2);
			List listTemp4 = document.selectNodes("/DeleteElements/element[@flag='"
					+ flag + "']/next");
			Iterator iterTemp4 = listTemp4.iterator();
			String next = null;
			while (iterTemp4.hasNext()) {
				Element element = (Element) iterTemp4.next();
				next = element.getText();
			}
			deleteBean1.setNext(next);
			map1.put(flag, deleteBean1);
		}
		return map1;
	}
	public Map<String, BrowseBean1> doInsert(File f) {
		Map<String, BrowseBean1> map1 = new HashMap<String, BrowseBean1>();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(f);
		List list = document.selectNodes("/InsertElements/element/@flag");
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Attribute attribute = (Attribute) iter.next();
			String flag = attribute.getValue();
			List listTemp1 = document.selectNodes("/InsertElements/element[@flag='"
					+ flag + "']/sql/@value");
			Iterator iterTemp1 = listTemp1.iterator();
			LinkedHashMap<String, InsertBean2> map2 = new LinkedHashMap<String, InsertBean2>();
			while (iterTemp1.hasNext()) {
				Attribute attribute1 = (Attribute) iterTemp1.next();
				String value = attribute1.getValue();
				List listTemp2 = document.selectNodes("/InsertElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/struct");
				Iterator iterTemp2 = listTemp2.iterator();
				ArrayList<StructBean> struct = new ArrayList<StructBean>();
				while (iterTemp2.hasNext()) {
					Element element = (Element) iterTemp2.next();
					StructBean structBean = new StructBean();
					structBean.setStructValue(element.getText());
					structBean.setSession(element.attributeValue("session"));
					structBean.setRequest(element.attributeValue("request"));
					struct.add(structBean);
				}
				InsertBean2 insertBean2 = new InsertBean2();
				insertBean2.setStruct(struct);
				List listTemp3 = document.selectNodes("/InsertElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/param");
				Iterator iterTemp3 = listTemp3.iterator();
				ArrayList<ParamBean> param = new ArrayList<ParamBean>();
				while (iterTemp3.hasNext()) {
					Element element = (Element) iterTemp3.next();
					ParamBean paramBean = new ParamBean();
					paramBean.setParamValue(element.getText());
					paramBean.setNotNull(element.attributeValue("notNull"));
					paramBean.setNeedSplit(element.attributeValue("needSplit"));
					paramBean.setSession(element.attributeValue("session"));
					paramBean.setRequest(element.attributeValue("request"));
					param.add(paramBean);
				}
				insertBean2.setParam(param);
				map2.put(value, insertBean2);
			}
			InsertBean1 insertBean1 = new InsertBean1();
			insertBean1.setMap(map2);
			List listTemp4 = document.selectNodes("/InsertElements/element[@flag='"
					+ flag + "']/next");
			Iterator iterTemp4 = listTemp4.iterator();
			String next = null;
			while (iterTemp4.hasNext()) {
				Element element = (Element) iterTemp4.next();
				next = element.getText();
			}
			insertBean1.setNext(next);
			map1.put(flag, insertBean1);
		}
		return map1;
	}
	public Map<String, BrowseBean1> doUpdate(File f) {
		Map<String, BrowseBean1> map1 = new HashMap<String, BrowseBean1>();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(f);
		List list = document.selectNodes("/UpdateElements/element/@flag");
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Attribute attribute = (Attribute) iter.next();
			String flag = attribute.getValue();
			List listTemp1 = document.selectNodes("/UpdateElements/element[@flag='"
					+ flag + "']/sql/@value");
			Iterator iterTemp1 = listTemp1.iterator();
			LinkedHashMap<String, UpdateBean2> map2 = new LinkedHashMap<String, UpdateBean2>();
			while (iterTemp1.hasNext()) {
				Attribute attribute1 = (Attribute) iterTemp1.next();
				String value = attribute1.getValue();
				List listTemp2 = document.selectNodes("/UpdateElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/struct");
				Iterator iterTemp2 = listTemp2.iterator();
				ArrayList<StructBean> struct = new ArrayList<StructBean>();
				while (iterTemp2.hasNext()) {
					Element element = (Element) iterTemp2.next();
					StructBean structBean = new StructBean();
					structBean.setStructValue(element.getText());
					structBean.setSession(element.attributeValue("session"));
					structBean.setRequest(element.attributeValue("request"));
					struct.add(structBean);
				}
				UpdateBean2 updateBean2 = new UpdateBean2();
				updateBean2.setStruct(struct);
				List listTemp3 = document.selectNodes("/UpdateElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/param");
				Iterator iterTemp3 = listTemp3.iterator();
				ArrayList<ParamBean> param = new ArrayList<ParamBean>();
				while (iterTemp3.hasNext()) {
					Element element = (Element) iterTemp3.next();
					ParamBean paramBean = new ParamBean();
					paramBean.setParamValue(element.getText());
					paramBean.setNotNull(element.attributeValue("notNull"));
					paramBean.setNeedSplit(element.attributeValue("needSplit"));
					paramBean.setSession(element.attributeValue("session"));
					paramBean.setRequest(element.attributeValue("request"));
					param.add(paramBean);
				}
				updateBean2.setParam(param);
				map2.put(value, updateBean2);
			}
			UpdateBean1 updateBean1 = new UpdateBean1();
			updateBean1.setMap(map2);
			List listTemp4 = document.selectNodes("/UpdateElements/element[@flag='"
					+ flag + "']/next");
			Iterator iterTemp4 = listTemp4.iterator();
			String next = null;
			while (iterTemp4.hasNext()) {
				Element element = (Element) iterTemp4.next();
				next = element.getText();
			}
			updateBean1.setNext(next);
			map1.put(flag, updateBean1);
		}
		return map1;
	}
	public Map<String, BrowseBean1> doImport(File f) {
		Map<String, BrowseBean1> map1 = new HashMap<String, BrowseBean1>();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(f);
		List list = document.selectNodes("/ImportElements/element/@flag");
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Attribute attribute = (Attribute) iter.next();
			String flag = attribute.getValue();
			List listTemp1 = document.selectNodes("/ImportElements/element[@flag='"
					+ flag + "']/@sql");
			Iterator iterTemp1 = listTemp1.iterator();
			String sql = new String();
			while (iterTemp1.hasNext()) {
				Attribute attribute1 = (Attribute) iterTemp1.next();
				sql = attribute1.getValue();
			}
			ImportBean1 importBean1 = new ImportBean1();
			importBean1.setSql(sql);
			List listTemp2 = document.selectNodes("/ImportElements/element[@flag='"
					+ flag + "']/input");
			Iterator iterTemp2 = listTemp2.iterator();
			ArrayList<ImportBean2> beanArray = new ArrayList<ImportBean2>();
			while (iterTemp2.hasNext()) {
				Element element = (Element) iterTemp2.next();
				ImportBean2 importBean2 = new ImportBean2();
				importBean2.setTitle(element.attributeValue("title"));
				importBean2.setTargetFlag(element.attributeValue("targetFlag"));
				importBean2.setNotNull(element.attributeValue("notNull"));
				importBean2.setNoRepeat(element.attributeValue("noRepeat"));
				importBean2.setDataType(element.attributeValue("dataType"));
				importBean2.setLength(element.attributeValue("length"));
				importBean2.setMaxLength(element.attributeValue("maxLength"));
				beanArray.add(importBean2);
			}
			importBean1.setBeanArray(beanArray);
			map1.put(flag, importBean1);
		}
		return map1;
	}
	public Map<String, BrowseBean1> doExport(File f) {
		Map<String, BrowseBean1> map1 = new HashMap<String, BrowseBean1>();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(f);
		List list = document.selectNodes("/ExportElements/element/@flag");
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Attribute attribute = (Attribute) iter.next();
			String flag = attribute.getValue();
			List listTemp1 = document.selectNodes("/ExportElements/element[@flag='"
					+ flag + "']/sql/@value");
			Iterator iterTemp1 = listTemp1.iterator();
			HashMap<String,ExportBean> map2 = new HashMap<String,ExportBean>();
			while (iterTemp1.hasNext()) {
				ExportBean exportBean = new ExportBean();
				Attribute attribute1 = (Attribute) iterTemp1.next();
				String value = attribute1.getValue();
				List listTemp2 = document.selectNodes("/ExportElements/element[@flag='"
						+ flag + "']/sql[@value='" + value + "']/param");
				Iterator iterTemp2 = listTemp2.iterator();
				ArrayList<ParamBean> param = new ArrayList<ParamBean>();
				while (iterTemp2.hasNext()) {
					Element element = (Element) iterTemp2.next();
					ParamBean paramBean = new ParamBean();
					paramBean.setParamValue(element.getText());
					paramBean.setNotNull(element.attributeValue("notNull"));
					paramBean.setSession(element.attributeValue("session"));
					paramBean.setRequest(element.attributeValue("request"));
					paramBean.setDrop(element.attributeValue("drop"));
					paramBean.setTimeStart(element.attributeValue("timeStart"));
					paramBean.setTimeEnd(element.attributeValue("timeEnd"));
					param.add(paramBean);
				}
				exportBean.setParam(param);
				List listTemp3 = document.selectNodes("/ExportElements/element[@flag='"
						+ flag + "']/sql[@value='" + value + "']/output");
				Iterator iterTemp3 = listTemp3.iterator();
				ArrayList<OutputBean> output = new ArrayList<OutputBean>();
				while (iterTemp3.hasNext()) {
					Element element = (Element) iterTemp3.next();
					OutputBean outputBean = new OutputBean();
					outputBean.setColumn(element.attributeValue("column"));
					outputBean.setTitle(element.attributeValue("title"));
					output.add(outputBean);
				}
				exportBean.setOutput(output);
				map2.put(value, exportBean);
			}
			map1.put(flag, map2);
		}
		return map1;
	}
	public Map<String, BrowseBean1> doOther(File f) {
		Map<String, BrowseBean1> map1 = new HashMap<String, BrowseBean1>();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(f);
		List list = document.selectNodes("/QueryElements/element/@flag");
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Attribute attribute = (Attribute) iter.next();
			String flag = attribute.getValue();
			List listTemp1 = document.selectNodes("/QueryElements/element[@flag='"
					+ flag + "']/@pageSize");
			Iterator iterTemp1 = listTemp1.iterator();
			int pageSize = 0;
			while (iterTemp1.hasNext()) {
				Attribute attribute1 = (Attribute) iterTemp1.next();
				pageSize = Integer.parseInt(attribute1.getValue());
			}
			QueryBean1 queryBean1 = new QueryBean1();
			queryBean1.setPageSize(pageSize);
			List listTemp2 = document.selectNodes("/QueryElements/element[@flag='"
					+ flag + "']/sql/@value");
			Iterator iterTemp2 = listTemp2.iterator();
			LinkedHashMap<String,QueryBean2> map2 = new LinkedHashMap<String,QueryBean2>();
			while (iterTemp2.hasNext()) {
				Attribute attribute2 = (Attribute) iterTemp2.next();
				String value = attribute2.getValue();
				QueryBean2 queryBean2 = new QueryBean2();
				List listTemp3 = document.selectNodes("/QueryElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/@key");
				Iterator iterTemp3 = listTemp3.iterator();
				while (iterTemp3.hasNext()) {
					Attribute attribute3 = (Attribute) iterTemp3.next();
					String key = attribute3.getValue();
					queryBean2.setKey(key);
				}
				List listTemp4 = document.selectNodes("/QueryElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/struct");
				Iterator iterTemp4 = listTemp4.iterator();
				ArrayList<StructBean> struct = new ArrayList<StructBean>();
				while (iterTemp4.hasNext()) {
					Element element = (Element) iterTemp4.next();
					StructBean structBean = new StructBean();
					structBean.setStructValue(element.getText());
					structBean.setSession(element.attributeValue("session"));
					structBean.setRequest(element.attributeValue("request"));
					struct.add(structBean);
				}
				queryBean2.setStruct(struct);
				List listTemp5 = document.selectNodes("/QueryElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/param");
				Iterator iterTemp5 = listTemp5.iterator();
				ArrayList<ParamBean> param = new ArrayList<ParamBean>();
				while (iterTemp5.hasNext()) {
					Element element = (Element) iterTemp5.next();
					ParamBean paramBean = new ParamBean();
					paramBean.setParamValue(element.getText());
					paramBean.setNotNull(element.attributeValue("notNull"));
					paramBean.setSession(element.attributeValue("session"));
					paramBean.setRequest(element.attributeValue("request"));
					param.add(paramBean);
				}
				queryBean2.setParam(param);
				List listTemp6 = document.selectNodes("/QueryElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/title");
				Iterator iterTemp6 = listTemp6.iterator();
				ArrayList<String> title = new ArrayList<String>();
				while (iterTemp6.hasNext()) {
					Element element = (Element) iterTemp6.next();
					title.add(element.getText());
				}
				queryBean2.setTitle(title);
				List listTemp7 = document.selectNodes("/QueryElements/element[@flag='"
						+ flag + "']/sql[@value='"+value+"']/output");
				Iterator iterTemp7 = listTemp7.iterator();
				ArrayList<String> output = new ArrayList<String>();
				while (iterTemp7.hasNext()) {
					Element element = (Element) iterTemp7.next();
					output.add(element.getText());
				}
				queryBean2.setOutput(output);
				map2.put(value, queryBean2);
			}
			queryBean1.setMap(map2);
			List listTemp8 = document.selectNodes("/QueryElements/element[@flag='"
					+ flag + "']/next");
			Iterator iterTemp8 = listTemp8.iterator();
			HashMap<String,String> next = new HashMap<String,String>();
			while (iterTemp8.hasNext()) {
				Element element = (Element) iterTemp8.next();
				next.put(element.attributeValue("tag"), element.getText());
			}
			queryBean1.setNext(next);
			map1.put(flag, queryBean1);
		}
		return map1;
	}
}