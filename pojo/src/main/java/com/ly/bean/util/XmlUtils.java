package com.ly.bean.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

/**
 * xml工具类
 * @author ly
 *
 */
public class XmlUtils {
	/**
	 * 当获取层级为1时，数据存储不会执行，此时使用一个临时的map存储数据信息
	 */
	private static ConcurrentMap<String, List<String>> saveMap = null;

	/**
	 * 格式化xml字符串
	 * 
	 * @param xml 需要格式化的字符串
	 * @return String 返回格式化后的字符串
	 */
	public static String xmlFormatter(String xml) {
		try {
			SAXReader reader = new SAXReader();
			if(xml == null || "".equals(xml)){
				return null;
			}
			StringReader in = new StringReader(xml);
			// 创建文档对象
			Document doc = reader.read(in);
			// 创建输出格式
			OutputFormat formatter = OutputFormat.createPrettyPrint();
			// 设置编码
			formatter.setEncoding("utf-8");
			// 创建字符串输出流
			StringWriter out = new StringWriter();
			// 创建xml格式的字符串输出流
			XMLWriter xmlWriter = new XMLWriter(out, formatter);
			// 将xml格式数据写到 输出流中
			xmlWriter.write(doc);
			xmlWriter.close();
			// 将输出流中的数据返回
			return out.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 *
	 * 将节点信息存储到map集合中
	 *
	 * @param parentMap
	 * @param e
	 * @param maxCount 
	 * @param listMap 存储节点
	 * @param isParent 节点是否是最外层父节点
	 * @param count 计数器，记录xml 数据所处的层次
	 * @param isExecute  当获取层级为1时，判断存储数据 是否执行
	 */
	@SuppressWarnings("unchecked")
	public static void addMap(ConcurrentMap<String, List<String>> parentMap, Element e,
			Integer maxCount,List<Map<String,List<String>>> listMap,Boolean isParent,Integer count,Boolean isExecute) {
		// 去除共享航班
		// 每当进入一层递归，就加一
		count++;
		List<Element> list = e.getChildren();
		String key = null;
		int size = list.size();
		if (size <= 0) {// 判该节点不存在子节点时
			List<Attribute> attributes = e.getAttributes();// 获取该节点所有属性信息
			int size2 = attributes.size();
			for (int i = 0; i < size2; i++) {// 将该属性的值存储到map中
				key = attributes.get(i).getName();
				key = key.substring(0, 1).toLowerCase() + key.substring(1);
				if (parentMap.get(key) != null
						&& !"".equals(attributes.get(i).getValue())) {// 该节点的属性在map中已经存在
					List<String> ml = parentMap.get(key);
					ml.add(attributes.get(i).getValue());
				} else if (!"".equals(attributes.get(i).getValue())) {// 该节点的属性在map中不存在
					ArrayList<String> newList = new ArrayList<String>();
					newList.add(attributes.get(i).getValue());
					parentMap.put(key, newList);
				}

			}
			key = e.getName();
			key = key.substring(0, 1).toLowerCase() + key.substring(1);
			if (parentMap.get(key) != null && !"".equals(e.getText())) {// 该节点的内容信息在map中存在
				List<String> ml = parentMap.get(key);
				ml.add(e.getText());
			} else if (!"".equals(e.getText())) {// 该节点的内容信息在map中不存在
				ArrayList<String> newList = new ArrayList<String>();
				newList.add(e.getText());
				parentMap.put(key, newList);
			}
			// 如果该节点是父节点，且不存在子节点,则只需保存其属性信息
			if (isParent) {
				if(listMap == null){
					listMap = new ArrayList<Map<String,List<String>>>();
				}
				if (parentMap.size() > 0) {
					listMap.add(parentMap);
				}
				parentMap = new ConcurrentHashMap<String, List<String>>();
				isParent = false;
			}

		} else {// 该节点存在子节点
			List<Attribute> attributes = e.getAttributes();
			int size2 = attributes.size();
			for (int i = 0; i < size2; i++) {// 将该节点的属性信息存储到map中
				key = attributes.get(i).getName();
				key = key.substring(0, 1).toLowerCase() + key.substring(1);
				if (parentMap.get(key) != null
						&& !"".equals(attributes.get(i).getValue())) {// 该节点的属性在map中已经存在
					List<String> ml = parentMap.get(key);
					ml.add(attributes.get(i).getValue());
				} else if (!"".equals(attributes.get(i).getValue())) {// 该节点的属性在map中不存在
					ArrayList<String> newList = new ArrayList<String>();
					newList.add(attributes.get(i).getValue());
					parentMap.put(key, newList);
				}
			}
			for (int i = 0; i < list.size(); i++) {// 遍历该节点的所有子节点
				// 如果是最外层父节点，则先将其属性添加到listMap 中，然后再遍历其子节点
				if (i == 0 && isParent) {
					if (parentMap.size() > 0) {
						listMap.add(parentMap);
					}
					parentMap = new ConcurrentHashMap<String, List<String>>();
					isParent = false;
				}

				addMap(parentMap, list.get(i), maxCount,listMap,isParent,count,isExecute);
				if (count == 1 && maxCount == 1) {
					saveMap = parentMap;
				}
				// 当map不为空，且当前层级处于最外层父节点中的第一层节点时，将map添加到listMap中，并格式化parentMap;
				if (parentMap.size() > 0 && count < maxCount) {
					listMap.add(parentMap);
					parentMap = new ConcurrentHashMap<String, List<String>>();
					isExecute = true;
				}
			}
		}
		// 每当退出一层递归 ，计数器就减一
		count--;
	}
	/**
	 * 获取xml节点信息
	 * @param xml xml字符串
	 * @param nodeName 需要获取的节点名称（若传null,则获取该xml的根节点下的所有节点）
	 * @return List<Element> 返回一个节点集合
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> getElement(String xml, String nodeName,List<Element> nodeList) {
		if(xml == null || "".equals(xml)){
			return null;
		}
		// 创建一个新的字符串
		StringReader read = new StringReader(xml);
		// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		InputSource source = new InputSource(read);
		// 创建一个新的SAXBuilder
		SAXBuilder sb = new SAXBuilder();
		try {
			// 通过输入源构造一个Document
			org.jdom.Document doc = sb.build(source);
			// 取得根元素
			Element root = doc.getRootElement();

			// 得到根元素所有子元素的集合
			List<Element> node = root.getChildren();
			// 调用addMap 遍历xml文件中所有节点
			if (nodeName != null && !"".equals(nodeName)) {
				forList(node, nodeName,nodeList);
			} else {
				nodeList = node;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nodeList;
	}
	/**
	 * 重载
	 * @param xml
	 * @param maxCount
	 * @param nodeName
	 * @param voClass
	 * @return
	 */
	public static  <M> List<M> xml2Map(String xml, Integer maxCount,
			String nodeName, Class<M> voClass){
		return XmlUtils.xml2Map(xml, maxCount, nodeName, voClass,false);
	}
	/**
	 * 将xml数据存储到集合中
	 * @param xml xml字符串
	 * @param maxCount 以第几个节点作为返回点
	 * @param nodeName 要获取的节点名称 传null时 默认根节点
	 * @param voClass 要返回的对象类型
	 * @return List 返回指定对象的集合
	 */
	public static  <M> List<M> xml2Map(String xml, Integer maxCount,
			String nodeName, Class<M> voClass,Boolean chooseParent) {
		try {
			List<Element> nodeList = new ArrayList<>();
			List<Map<String, List<String>>> listMap = new ArrayList<>();

			if ("".equals(xml) || xml == null) {
				return new ArrayList<M>();
			}	
			Element et = null;
			Boolean isParent = true;
			Boolean isExecute = false;
			getElement(xml,nodeName,nodeList);
				if (nodeList != null) {
						if(!chooseParent){
							isParent = true;
						}else{
							isParent = false;
						}
					// 遍历父节点集合
					for (Element e : nodeList) {
						isExecute = false;
						// 初始化父节点状态
						et = e;
						addMap(new ConcurrentHashMap<String, List<String>>(), et, maxCount,listMap,isParent,0,isExecute);
						if (!isExecute || listMap == null || listMap.size() <= 0) {
							if (saveMap != null) {
								listMap.add(saveMap);
							}

						}
					}
				}
			
			if (nodeList != null && nodeList.size() > 0) {
				nodeList.clear();
			}
			List<M> entityList = new ArrayList<M>();
			for (Map<String, List<String>> map : listMap) {
				M entity = ConvertUtils.convertEntity(map, voClass,false);
				if (entity != null) {
					entityList.add(entity);
				}
			}

			return entityList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 将指定的节点存储到指定对象的集合中
	 * @param el 指定的节点
	 * @param maxCount 第几个层级的节点作为一个对象
	 * @param nodeName 需要获取的节点名称
	 * @param voClass 需要转换的对象
	 * @return List 返回指定对象的集合
	 */
	@SuppressWarnings("unchecked")
	public static <M> List<M> xml2Map(Element el, Integer maxCount,
			String nodeName, Class<M> voClass) {
		// 清空集合信息
		List<Element> nodeList = new ArrayList<>();
		List<Map<String, List<String>>> listMap = new ArrayList<>();

		if (el == null) {
			return new ArrayList<M>();
		}	
		Element et = null;
		Boolean isParent = true;
		Boolean isExecute = false;
		List<Element> list = el.getChildren();
		if(list.size() <=0){
			return new ArrayList<M>();
		}
		forList(list,nodeName,nodeList);
			if (nodeList != null) {
				// 遍历父节点集合
				for (Element e : nodeList) {
					// 初始化父节点状态
					isParent = true;
					et = e;
					addMap(new ConcurrentHashMap<String, List<String>>(), et, maxCount,listMap,isParent,0,isExecute);
					if (!isExecute || listMap == null || listMap.size() <= 0) {
						if (saveMap != null) {
							listMap.add(saveMap);
						}

					}
				}
			}
		
		if (nodeList != null && nodeList.size() > 0) {
			nodeList.clear();
		}
		List<M> entityList = new ArrayList<M>();
		for (Map<String, List<String>> map : listMap) {
			M entity = ConvertUtils.convertEntity(map, voClass,true);
			if (entity != null) {
				entityList.add(entity);
			}
		}

		return entityList;

	}
	
	
/**
 * 通过指定名称查找父节点的对应节点
 * @param e 开始节点
 * @param nodeName 需要获取的节点名称
 */
	@SuppressWarnings("unchecked")
	public static void forList(List<Element> e, String nodeName,List<Element> nodeList) {
		
		for (Element o : e) {
			if (!o.getName().equals(nodeName)) {
				if (o.getChildren().size() > 0) {
					forList(o.getChildren(), nodeName,nodeList);
				}
			} else {
				nodeList.add(o);
			}

		}

	}
	/**
	 * 通过传入文件路径,获取xml文件的字符串格式
	 * @param filePath
	 * @return String 返回指定格式的字符串
	 */
	public static String getXMLString(String filePath) {
		SAXBuilder sb = new SAXBuilder();
		try {
			org.jdom.Document doc = sb.build(new File(filePath));
			Format format = Format.getPrettyFormat();

			XMLOutputter xmlout = new XMLOutputter(format);

			ByteArrayOutputStream bo = new ByteArrayOutputStream();

			xmlout.output(doc, bo);

			String xmlStr = bo.toString();
			return xmlStr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}

