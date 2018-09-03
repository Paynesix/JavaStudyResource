package stu.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Properties;

public class BufferWrite {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {

		String fp = System.getProperty("user.dir") + "\\src\\resource\\foo.properties";
		deleteEmptyvalueAttr(fp );
	}

	/**
	 * delete properties empty attribute
	 * @param fp
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private static void deleteEmptyvalueAttr(String fp) throws FileNotFoundException, IOException {

		Properties pro = new Properties();
		pro.load(new FileInputStream(fp));
		
		StringBuffer sb = new StringBuffer();
		
		Iterator it = (Iterator) pro.propertyNames();
		while(it.hasNext()){
			String key = (String) it.next();
			String val = pro.getProperty(key);
			if(!"".equals(val))
				sb.append(key + "=" + val + "\n");
		}
		
		FileOutputStream fos = new FileOutputStream(fp);
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		buf.clear();
		buf.put(sb.toString().getBytes());
		
		fos.write(buf.array());
		
		fos.close();
		
		System.out.println("over .. ..");
	}

}
