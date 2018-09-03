package edu.org;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PraseXML {

	/**
	 * 解析xml
	 * @author xy
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		PraseXML p = new PraseXML();
		p.paraseXML();
		p.paraseXML2();
	}

	public DocumentBuilder init() throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory bdf = DocumentBuilderFactory.newInstance();
		return bdf.newDocumentBuilder();
	}
	
	/**
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * 
	 */
	public void paraseXML2() throws SAXException, IOException, ParserConfigurationException{
		Document document = init().parse(new File("D:\\YTWorkSpace\\test\\src\\edu\\org\\user.xml"));
		NodeList list = document.getElementsByTagName("name");
		System.out.println("Name result :");
		for(int i=0; i<list.getLength(); i++){
			Element element = (Element)list.item(i);
			String name = element.getNodeValue();
			System.out.println(name);
		}
	}
	
	/**
	 * 解析整个文档
	 * @throws Exception
	 */
	public void paraseXML() throws Exception {
		Document document = init().parse(new File("D:\\YTWorkSpace\\test\\src\\edu\\org\\user.xml"));

		NodeList list = document.getElementsByTagName("usa");
		System.out.println("脚本之家测试结果：");
		for (int i = 0; i < list.getLength(); i++) {
			Element element = (Element) list.item(i);
			String name = element.getElementsByTagName("name").item(0)
					.getFirstChild().getNodeValue();
			System.out.println(name);
			String pass = element.getElementsByTagName("pass").item(0)
					.getFirstChild().getNodeValue();
			System.out.println(pass);
			System.out.println("------------------");
		}
	}

}
