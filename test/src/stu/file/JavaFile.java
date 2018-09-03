package stu.file;

import java.io.File;
import java.io.IOException;

/**
 * java file test
 * 
 * @author pactera
 * 
 */
public class JavaFile {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// fileNew();
		fileCreate();
	}

	private static void fileCreate() {
		File f = new File("d:\\");
//		String[] sarr = f.list();
//		if (null != sarr)
//			for (String s : sarr)
//				pl(s);
//		File[] farr = f.listFiles();
//		if (null != farr)
//			for (File s : farr)
//				pl(s.toString());

		File[] sarr1 = File.listRoots();
		if(null != sarr1)
			for(File s:sarr1)
				pl(s.getAbsolutePath());
	}

	private static void fileNew() throws IOException {

		// String path = System.getProperty("user.dir");
		// pl(path);
		// System.setProperty("user.dir", "D:\\MyDownloads\\test");
		String path = System.getProperty("user.dir");
		pl(path);
		File d = new File("new.txt");
		boolean f = d.exists();
		if (f) {
			pl("file is exists");
		} else {
			pl("file is not exists");
			d.createNewFile();
		}
		pl(d.getAbsoluteFile().toString());
		pl(d.getName());
		pl(d.getAbsolutePath());
		pl(d.getCanonicalPath());

	}

	public static void p(String s) {
		System.out.print(s);
	}

	public static void pl(String s) {
		System.out.println(s);
	}
}
