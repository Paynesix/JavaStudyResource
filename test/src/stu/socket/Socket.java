package stu.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Socket {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		URL url = new URL("http://www.baidu.com");
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		BufferedWriter bw = new BufferedWriter(new FileWriter("d:/baidu.html"));
		String line;
		while((line = br.readLine()) != null){
			System.out.println(line);
			bw.write(line);
			bw.newLine();
		}
		br.close();
		bw.close();
	}
}
