package stu.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

public class PropertiesTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {

		String propertiesPaht = "./src/resource/log4j.properties"; 
		Properties pro = new Properties();
		pro.load(new FileInputStream(propertiesPaht));
		
//		printPropertiesAll(pro);
		
		printPropertiesByIterator(pro);
		
	}

	/**
	 * print properties elements by Iterator interface
	 * @param pro
	 */
	private static void printPropertiesByIterator(Properties pro) {
		
		Iterator<?> it = (Iterator<?>) pro.propertyNames();
		while(it.hasNext()){
			String key = (String) it.next();
			String val = pro.getProperty(key);
			System.out.println(key + "@@@" + val);
		}
	}

	/**
	 * print all properties elements
	 * @param pro
	 */
	private static void printPropertiesAll(Properties pro) {
		Enumeration<?> en = pro.propertyNames();
		while(en.hasMoreElements())
		{
			String key = (String) en.nextElement();
			String val = pro.getProperty(key);
			System.out.println(key + "=====" + val);
		}
		
	}

}
