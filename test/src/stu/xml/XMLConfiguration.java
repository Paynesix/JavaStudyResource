package stu.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLConfiguration {
	private Map valuemap;

	private StringBuffer buffer;

	public XMLConfiguration() {
		valuemap = new HashMap();

	}

	public Set keySet() {
		return valuemap.entrySet();
	}

	public boolean exist(String key) {
		return valuemap.containsKey(key);
	}

	public String get(String key) {
		return (String) valuemap.get(key);
	}

	public void put(String key, String value) {
		valuemap.put(key, value);
	}

	protected void print(String str) {
		buffer.append(str);
	}

	protected void println(String str) {
		buffer.append(str + "\r\n");
	}

	protected void printHead(String version, String languageCode) {
		println("<?xml version=\"" + version + "\" encoding=\"" + languageCode
				+ "\"?>");

	}

	protected String pad(int len) {
		String ret = "";
		while (len > 0) {
			ret += "\t";
			len--;
		}
		return ret;
	}

	protected void printMap(int level, String name, Map map) {
		println(pad(level) + "<" + encode(name) + ">");
		Set keys = map.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			Object value = map.get(key);
			if (value instanceof Map)
				printMap(level + 1, key, (Map) value);
			else {
				print(pad(level + 1));
				print("<" + encode(key) + ">");
				print(encode(String.valueOf(value)));
				println("</" + encode(key) + ">");
			}
		}
		println(pad(level) + "</" + encode(name) + ">");
	}

	public String encode(String string) {
		if (string == null) {
			return "";
		}
		char[] chars = string.toCharArray();
		StringBuffer tmpBuffer = new StringBuffer();

		for (int i = 0; i < chars.length; i++) {
			switch (chars[i]) {
			case '&':
				tmpBuffer.append("&amp;");

				break;

			case '<':
				tmpBuffer.append("&lt;");

				break;

			case '>':
				tmpBuffer.append("&gt;");

				break;

			case '\"':
				tmpBuffer.append("&quot;");

				break;

			default:
				tmpBuffer.append(chars[i]);
			}
		}

		return tmpBuffer.toString();
	}

	public void saveToFile(File file) throws IOException {
		Map nodeMap = new HashMap();
		Set keys = valuemap.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) valuemap.get(key);
			String names[] = key.split("\\.");
			int i = 0;
			Map tmpMap = nodeMap;
			for (i = 0; i < names.length; i++) {
				String name = names[i];
				if (i >= names.length - 1) {
					tmpMap.put(name, value);
				} else {
					Object obj = tmpMap.get(name);
					if (obj == null || !(obj instanceof Map)) {
						tmpMap.put(name, new HashMap());
					}
					tmpMap = (Map) tmpMap.get(name);
				}
			}
		}
		buffer = new StringBuffer();
		printHead("1.0", "GB2312");
		printMap(0, "config", nodeMap);
		if (file.exists())
			file.delete();
		file.createNewFile();
		FileOutputStream out = new FileOutputStream(file);
		ByteArrayInputStream in = new ByteArrayInputStream(buffer.toString()
				.getBytes());
		int len = 0;
		byte[] buf = new byte[1024];
		len = in.read(buf);
		while (len > 0) {
			out.write(buf, 0, len);
			len = in.read(buf);
		}
		out.flush();
		out.close();
		in.close();
	}

	public void loadFromURL(URL url) throws IOException {
		try {
			InputStream is = getResource(url);
			loadFromStream(is);
		} catch (Exception e) {
			throw new IOException("load config error :"
					+ e.getMessage(), e);
		}
	}

	public void loadFromFile(File file) throws IOException {
		try {
			InputStream is = getResource(file);
			loadFromStream(is);
		} catch (Exception e) {
			throw new IOException("load config error :"
					+ e.getMessage(), e);
		}
	}

	public void loadFromResource(String resource) throws IOException {
		try {
			InputStream is = getResource(resource);
			loadFromStream(is);
		} catch (Exception e) {
			throw new IOException("load config error :"
					+ e.getMessage(), e);
		}
	}

	public void loadFromStream(InputStream is) throws Exception {
		valuemap = new HashMap();
		if (is == null)
			throw new IOException("Cannot find  configuration file ");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db;
		db = dbf.newDocumentBuilder();
		Document document = db.parse(is);
		is.close();
		Node rootNode = document.getDocumentElement();
		printNode(rootNode, "", valuemap);

	}

	protected InputStream getResource(String recouce) {
		InputStream is = null;
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();

		try {
			is = classLoader.getResourceAsStream(recouce);
		} catch (Exception e) {
		}

		return is;
	}

	protected InputStream getResource(File file) throws FileNotFoundException {
		InputStream is = new FileInputStream(file);
		return is;
	}

	protected InputStream getResource(URL url) {
		InputStream is = null;
		if (url != null) {
			try {
				is = url.openStream();
			} catch (Exception ex) {
			}
		}
		return is;
	}

	protected void printNode(Node node, String head, Map map) {
		boolean hasSubElement = false;
		int nodeType = node.getNodeType();
		if (nodeType == Node.TEXT_NODE) {
			if (node.getParentNode().getChildNodes().getLength() > 1)
				return;
		}
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
					if (head.equals(""))
						subHead = subNode.getNodeName();
					else
						subHead = head + "." + subNode.getNodeName();
				}
				printNode(nodeList.item(i), subHead, map);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		File file = new File("/D:/temp/Test2.xml");
		XMLConfiguration config = new XMLConfiguration();
		config.loadFromFile(file);
		config.put("user.Name.kuyuer", "ccc");
		config.put("user.age", "1<8>");
		config.saveToFile(file);
		Iterator it = config.valuemap.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			Object value = config.get(String.valueOf(key));
			System.out.println(key + " : " + value);
		}
	}
}
