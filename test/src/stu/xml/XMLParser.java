package stu.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLParser {
	protected Document document;

	protected Node rootNode;

	protected DocumentBuilder getDoumentBuilder()
			throws ParserConfigurationException {
		DocumentBuilderFactory documnetFactory = DocumentBuilderFactory
				.newInstance();
		return documnetFactory.newDocumentBuilder();
	}

	public void parseBytes(byte[] buf) throws SAXException, IOException,
			ParserConfigurationException {
		ByteArrayInputStream in = new ByteArrayInputStream(buf);
		parseStream(in);
	}


	public void parseStream(InputStream in) throws SAXException, IOException,
			ParserConfigurationException {
		try {

			document = getDoumentBuilder().parse(in);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (Exception e) {
				;
			}
		}
		rootNode = document.getDocumentElement();
	}

	public void parseFile(File file) throws SAXException, IOException,
			ParserConfigurationException {
		document = getDoumentBuilder().parse(file);
		rootNode = document.getDocumentElement();
	}

	/**
	 * 
	 */
	public static String getAttributeValue(Node node, String name) {

		NamedNodeMap nodeAttributes = node.getAttributes();
		for (int i = 0; i < nodeAttributes.getLength(); i++) {
			Node tempNode = nodeAttributes.item(i);
			if (name.equals(tempNode.getNodeName()))
				return tempNode.getNodeValue();
		}
		return "";
	}

	/**
	 * 
	 */
	public static Node getChildNode(Node node, String NodeName) {
		Node resultNode = null;
		Pattern pattern;
		Matcher matcher;
		String nodeName = "";
		String attributeName = "";
		String attributeValue = "";
		pattern = Pattern.compile("(/|^\\b)(\\w+)(?=\\[|\\b)");
		matcher = pattern.matcher(NodeName);
		if (matcher.find()) {
			nodeName = matcher.group();
			nodeName = nodeName.replace('/', ' ');
			nodeName = nodeName.trim();
		}
		pattern = Pattern.compile("(\\w+)(?==)");
		matcher = pattern.matcher(NodeName);
		if (matcher.find())
			attributeName = matcher.group();

		pattern = Pattern.compile("(\\w+)(?=')");
		matcher = pattern.matcher(NodeName);
		if (matcher.find())
			attributeValue = matcher.group();
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			node = nodeList.item(i);
			if (nodeName.equals(node.getNodeName())) {

				if (attributeName.trim().length() > 0) {

					if (getAttributeValue(node, attributeName).equals(
							attributeValue))
						return node;
				} else {
					return node;
				}
			}
		}
		return resultNode;
	}

	/**
	 * 
	 */
	public Node getNode(String path) {
		Node node = null;
		Node tempNode = rootNode;
		List pathList = parsePath(path);
		for (int i = 0; i < pathList.size(); i++) {

			tempNode = getChildNode(tempNode, (String) pathList.get(i));
			node = tempNode;
			if (tempNode == null)
				break;
		}
		return node;
	}

	/**
	 *  
	 */
	public static String getValue(Node node, String elementName) {
		Node childNode = getChildNode(node, elementName);
		if (childNode != null) {
			// return getText((Element)childNode);
			if (childNode.getFirstChild() == null)
				return null;
			return childNode.getFirstChild().getNodeValue();
		} else
			return null;
	}

	/**
	 * 
	 */
	private List parsePath(String path) {
		List resultList = new ArrayList();
		if (path == null || path.trim().equals(""))
			return resultList;
		// Pattern p =
		// Pattern.compile("(/|^\\b)([A-Za-z0-9\\[\\]=']+)(?=/|\\b$)");
		Pattern p = Pattern
				.compile("(/|^\\b)([_A-Za-z0-9\\[\\]=']+)(?=/|\\b$)");
		Matcher m = p.matcher(path);
		while (m.find()) {
			resultList.add(m.group());
		}
		return resultList;
	}

	public Node getRootNode() {
		return this.rootNode;
	}

	public static Element getChildElement(Element parent, String childName) {
		NodeList children = parent.getChildNodes();
		int size = children.getLength();

		for (int i = 0; i < size; i++) {
			Node node = children.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;

				if (childName.equals(element.getNodeName())) {
					return element;
				}
			}
		}

		return null;
	}

	public static List getChildElements(Element parent, String childName) {
		NodeList children = parent.getChildNodes();
		List list = new ArrayList();
		int size = children.getLength();

		for (int i = 0; i < size; i++) {
			Node node = children.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				if (childName.equals(element.getNodeName())) {
					list.add(element);
				}
			}
		}

		return list;
	}

	public static String getChildText(Element parent, String childName) {
		Element child = getChildElement(parent, childName);
		if (child == null) {
			return null;
		}
		return getText(child);
	}

	public static String getText(Element node) {
		StringBuffer sb = new StringBuffer();
		NodeList list = node.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {
			Node child = list.item(i);
			switch (child.getNodeType()) {
			case Node.CDATA_SECTION_NODE:
			case Node.TEXT_NODE:
				sb.append(child.getNodeValue());
			}
		}
		return sb.toString();
	}

	public static void main(String args[]) throws Exception {
		File file = new File("D:\\fxccconfig.xml");
		XMLParser parser = new XMLParser();
		parser.parseFile(file);

	}
}
