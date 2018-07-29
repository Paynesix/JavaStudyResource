/*
 * Created on 2008-3-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package stu.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 把xml文件载入内存properties对象中
 */
public class Configer {	
	static Properties config = new Properties();
	static StackRouter router = new StackRouter();
	static StackRouter logrouter = new StackRouter();
	static boolean isLoaded = false;
	
	/**
	 * 解析xml文件中的节点node
	 * @param node 节点
	 * @param head 头节点
	 * @param map  properties全局对象
	 */
	static private void printNode(Node node, String head, Properties map) {
		boolean hasSubElement = false;
		int nodeType = node.getNodeType();
		if (nodeType == Node.TEXT_NODE) {
			if (node.getParentNode().getChildNodes().getLength() > 1)
				return;
		}
		//	if (node.getNodeValue() == null || node.getNodeValue().trim().equals(""))
		//		return;
		if (node.getNodeType() == Node.ELEMENT_NODE)
			hasSubElement = true;

		switch (nodeType) {
		case Node.ELEMENT_NODE:
			break;
		case Node.TEXT_NODE:
			String value = node.getNodeValue();
			if (value.endsWith("\n"))
				value = value.substring(0, value.length() - 3);
			map.put(head, value.trim());
			break;
		case Node.CDATA_SECTION_NODE:
			CDATASection cData = (CDATASection) node;
			map.put(head, cData.getData().trim());

			break;
		case Node.COMMENT_NODE:
			break;
		}

		if (hasSubElement) {
			NodeList nodeList = node.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node subNode = nodeList.item(i);
				String subHead = "";
				subHead = head;
				if (subNode.getNodeType() == Node.ELEMENT_NODE) {
					String id = ((Element) subNode).getAttribute("id");	//取属性
					String nname;
					if (null != id && (!"".equals(id))) {
						nname = id + '@' + subNode.getNodeName();
					} else {
						nname = subNode.getNodeName();
					}
					if (head.equals(""))
						subHead = nname;
					else
						subHead = head + "." + nname;
				}
				printNode(nodeList.item(i), subHead, map);
			}
		}
	}
	
	/**
	 * xml文件流导入
	 * @param is
	 * @throws Exception
	 */
	static private void loadXmlFromStream(InputStream is) throws Exception {
		if (is == null)
			throw new Exception("Cannot find  configuration file");
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db;
		db = dbf.newDocumentBuilder();
		Document document = db.parse(is);
		is.close();
		Node rootNode = document.getDocumentElement();
		printNode(rootNode, "", config);
	}
	
	/**
	 * properties文件流导入
	 * @param url
	 * @throws Exception
	 */
	public synchronized static void loadprop(String url) throws Exception {
		InputStream is = Tools.getResource(url);
		config.load(is);
	}
	
	/**
	 * xml文件导入
	 * @param url
	 * @throws Exception
	 */
	public synchronized static void loadxml(String url) throws Exception {
		InputStream is = Tools.getResource(url);
		loadXmlFromStream(is);
	}	
	
	/**
	 * xml文件导入
	 * @throws Exception
	 */
	private static void loadxml() throws Exception {
		File file = new File(System.getProperty("user.dir") + File.separator + "config.xml");
		InputStream is = new FileInputStream(file);
		loadXmlFromStream(is);
	}
	
	//指定路径
	public synchronized static void loadXmlFile(String cfgPath) throws Exception {
		if (isLoaded)
			return;
		//有指定路径，并且不是以文件夹分隔符结束时，在路径后增加斜杠
		if (StringUtils.isNotEmpty(cfgPath) && !cfgPath.endsWith(File.separator))
			cfgPath = cfgPath + File.separator;
		File file = new File(cfgPath + "fxccconfig.xml");
		InputStream is = new FileInputStream(file);
		loadXmlFromStream(is);
		isLoaded = true;
	}	
	
	//没有任何配置时log的产生方法与平时一致
	public static Log getLogger() {
		Throwable ab = new Throwable();
		StackTraceElement[] stacks = ab.getStackTrace();
		
//		if ("com.mx.sql.SqlExecuter".equals(stacks[1].getClassName()))
//			System.out.println(Tools.throwable2String(ab));
		
		String logname = logrouter.route(stacks);
		if (null == logname) {
			logname = stacks[1].getClassName();
		}
			
		Log log = LogFactory.getLog(logname); 
		
		return log;
	}
	
	/**
	 * 根据name获取properties中的属性值
	 * @param name
	 * @return
	 */
	public static String absConfig(String name) {
		String ret = null;
		synchronized(config) {
			String s = config.getProperty(name);
			if (null != s)
				ret = new String(s);
		}
		return ret;
	}
	
	/**
	 * 获取配置文件中配置属性
	 * @param name
	 * @return
	 */
	public static String getConfig(String name) {
		Throwable ab = new Throwable();
		StackTraceElement[] stacks = ab.getStackTrace();
		String configname = router.route(stacks);
		
//		if (null == configname) {
//			String cname = stacks[1].getClassName();
//			int i = cname.lastIndexOf('.');
//			cname = cname.substring(i+1);
//			configname = cname;
//		}

		String s = null;
		if (null != configname)
			s = absConfig(configname+"."+name);
		if (null == s)
			s = absConfig(name);
		
		return s;
	}
	
	/**
	 * 获取配置文件中配置属性
	 * @param name
	 * @return
	 */
	public static String config(String name) {
		System.out.println("================111========");
		Throwable ab = new Throwable();
		StackTraceElement[] stacks = ab.getStackTrace();
		String configname = router.route(stacks);
		
//		if (null == configname) {
//			String cname = stacks[1].getClassName();
//			int i = cname.lastIndexOf('.');
//			cname = cname.substring(i+1);
//			configname = cname;
//		}

		String s = null;
		if (null != configname)
			s = absConfig(configname+"."+name);
		if (null == s)
			s = absConfig(name);
		
		System.out.println("================ssss========"+s);
		return s;
	}

	/**
	 * 日志表
	 * @param file
	 * @throws Exception
	 */
	public static void loadRouteTable(String file) throws Exception {
		router.loadTable(file);
	}

	/**
	 * 日志表
	 * @param file
	 * @throws Exception
	 */
	public static void loadLogRouteTable(String file) throws Exception {
		logrouter.loadTable(file);
	}
	
	public Configer() {}
	
	public static void main(String[] args){
		try {
			Configer.loadXmlFile("d:");
			PrintStream out = new PrintStream(new File("d:/fxccconfig.properties"));
			config.list(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
