package stu.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class FileFind {

	/**
	 * function find string in file author payne
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String fileName = "d:/gjyw/ebills1.properties";
		
//		readerFileByMap("FileFind.java", 3, 5);
		readerFileByAccessMap(fileName);
	}

	public void readerInputStream() {
		File f = new File("d:/gjyw/ebills1.properties");
		byte[] b = new byte[2014];
		InputStream out;
		try {
			out = new FileInputStream(f);
			int n = 0;
			while ((n = out.read(b)) != -1) {
				System.out.write(b, 0, n);
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * reader
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void read(String fileName) throws IOException {
		File f = new File(fileName);
		FileReader fr = new FileReader(f);
		int n = 0;
		while ((n = fr.read()) != -1) {
			System.out.print((char) n);
		}
		fr.close();
	}

	/**
	 * reader file by buffer
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void readerBuffer(String fileName) throws IOException {
		File f = new File(fileName);
		BufferedInputStream bis = new BufferedInputStream(
				new FileInputStream(f));
		byte[] b = new byte[100];
		bis.read(b);
		bis.close();
	}

	/**
	 * reader buffered by channel
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void readerFileByChannle(String fileName) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		FileChannel fc = raf.getChannel();

		ByteBuffer bb = ByteBuffer.allocate(48);

		int bytesRead = fc.read(bb);

		while (bytesRead != -1) {
			System.out.println("Read : " + bytesRead);
			bb.flip();
			while (bb.hasRemaining()) {
				System.out.print((char) bb.get());
			}
			bb.clear();
			bytesRead = fc.read(bb);
		}
		raf.close();
	}

	/**
	 * reader file by map
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public static boolean readerFileByMap(String fileName, int start, int len)
			throws IOException {
		// create a random access object
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		long totalLen = raf.length();
		System.out.println("file total length : " + totalLen);

		// open a file channel
		FileChannel fc = raf.getChannel();
		// read one part to inner ram
		MappedByteBuffer mbb = fc.map(MapMode.READ_WRITE, start, len);

		for (int i = 0; i < len; i++) {
			byte src = mbb.get(i);
			mbb.put(i, (byte) (src - 31));
			System.out.println("change init byte is :" + src);
		}
		mbb.force();
		mbb.clear();
		fc.close();
		raf.close();
		return true;
	}
	
	/**
	 * read file by random access and map
	 * @param fileName
	 * @throws IOException 
	 */
	public static void readerFileByAccessMap(String fileName) throws IOException{
		int length = 0x80000000; // 128MB
		FileChannel fc = new RandomAccessFile(fileName, "rw").getChannel();
		MappedByteBuffer bb = fc.map(MapMode.READ_WRITE, 0, length);
		//write length byte
		for(int i=0; i<length; i++){
			bb.put((byte)'X');
			
		}
		System.out.println("Finished write");
		
		for(int j=length>>2; j<length>>2+6; j++){
			System.out.println((char)bb.get(j));
			
		}
		fc.close();
	}
}
