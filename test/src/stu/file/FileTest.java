package stu.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * some test for file demo
 * @author pactera
 *
 */
public class FileTest {

	private final static String filePath = ".\\src\\resource\\foo.properties";
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

//		readFileToConsole();
		
//		inputStreamToReader();
		
		fileInfo();
		
	}

	/**
	 * file info
	 * @throws IOException 
	 */
	private static void fileInfo() throws IOException {
		File f = new File("C:\\Users\\pactera\\Desktop\\jd-gui.exe");
		if(f.canExecute())
			System.out.println(f.setExecutable(true));

		InputStream path = FileTest.class.getClass().getResourceAsStream("foo.properties") ;

//		Runtime r = Runtime.getRuntime(); 
//		r.exec("C:\\Users\\pactera\\Desktop\\ETL脚本.bat");
		
		
	}

	/**
	 * inputstream transfrom to reader
	 * @throws IOException 
	 */
	private static void inputStreamToReader() throws IOException {
		InputStream is = new FileInputStream(filePath);
		InputStreamReader isr = new InputStreamReader(new BufferedInputStream(is), "UTF8");
		
		while(isr.ready()){
			System.out.print((char)isr.read());
		}
	}

	private static void readFileToConsole() throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		
		byte[] b = new byte[1024];
		
		while(fis.read() != -1){
			fis.read(b);
			for(int i=0; i<b.length; i++){
				System.out.print((char)b[i]);
			}
		}
		
		fis.close();
		
	}

}
