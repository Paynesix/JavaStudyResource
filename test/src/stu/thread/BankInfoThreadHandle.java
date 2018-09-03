package stu.thread;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankInfoThreadHandle {

	volatile static Set<String> swiftCodeSet = new HashSet<String>();
	// 231498耗时 3线程(本机2核4线程)
	// 278592耗时 2线程
	// 397115耗时 单线程
	// 245657耗时 cpu线程数（4线程）
	public static void main(String[] args) throws Exception {
		CountDownLatch doneSignal = new CountDownLatch( Runtime.getRuntime().availableProcessors());
		BankInfoThreadHandle helper = new BankInfoThreadHandle();
		helper.read(doneSignal,"D:\\gjyw\\uploads\\BICPLUS_V1_DELTA_20180615.txt", Runtime
				.getRuntime().availableProcessors(), '\n', new StringCallback("UTF-8") {
			@Override
			void callback(String data) {
			}
		});
		// 确认所有线程任务完成，开始执行主线程的操作
		try {
			doneSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.print("===================================end");
		
	}

	public void read(final CountDownLatch doneSignal,final String path, int threadCount, final char separator,
			final StringCallback callback) throws IOException {

		if (threadCount < 1) {
			throw new InvalidParameterException(
					"The threadCount can not be less than 1");
		}

		if (path == null || path.isEmpty()) {
			throw new InvalidParameterException(
					"The path can not be null or empty");
		}

		if (callback == null) {
			throw new InvalidParameterException("The callback can not be null");
		}

		final RandomAccessFile randomAccessFile = new RandomAccessFile(path, "r");

		long fileTotalLength = randomAccessFile.length();
		long gap = fileTotalLength / threadCount;
		long checkIndex = 0;
		final long[] beginIndexs = new long[threadCount];
		final long[] endIndexs = new long[threadCount];

		for (int n = 0; n < threadCount; n++) {
			beginIndexs[n] = checkIndex;
			if (n + 1 == threadCount) {
				endIndexs[n] = fileTotalLength;
				break;
			}
			checkIndex += gap;
			long gapToEof = getGapToEof(checkIndex, randomAccessFile, separator);

			checkIndex += gapToEof;
			endIndexs[n] = checkIndex;
		}

		ExecutorService executorService = Executors
				.newFixedThreadPool(threadCount);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					readData(doneSignal,beginIndexs[0], endIndexs[0], path,
							randomAccessFile, separator, callback);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		for (int n = 1; n < threadCount; n++) {
			final long begin = beginIndexs[n];
			final long end = endIndexs[n];
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					try {
						readData(doneSignal,begin, end, path, null, separator, callback);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	private long getGapToEof(long beginIndex,
			RandomAccessFile randomAccessFile, char separator)
			throws IOException {
		randomAccessFile.seek(beginIndex);
		long count = 0;

		while (randomAccessFile.read() != separator) {
			count++;
		}

		count++;

		return count;
	}

	private void readData(CountDownLatch doneSignal,long begin, long end, String path,
			RandomAccessFile randomAccessFile, char separator,
			StringCallback callback) throws FileNotFoundException, IOException, SQLException {
		System.out.println("开始工作" + Thread.currentThread().getName());
		if (randomAccessFile == null) {
			randomAccessFile = new RandomAccessFile(path, "r");
		}

		randomAccessFile.seek(begin);
		StringBuilder builder = new StringBuilder();

		while (true) {
			int read = randomAccessFile.read();
			begin++;
			if (separator == read) {
				if (callback != null) {
					fileResolve(builder.toString());
				}
				builder = new StringBuilder();
			} else {
				builder.append((char) read);
			}

			if (begin >= end) {
				break;
			}
		}
		randomAccessFile.close();
		doneSignal.countDown();
	}

	public static abstract class StringCallback {
		private String charsetName;
		private ExecutorService executorService = Executors
				.newSingleThreadExecutor();

		public StringCallback(String charsetName) {
			this.charsetName = charsetName;
		}

		private void callback0(final String data) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					try {
						callback(new String(data.getBytes("ISO-8859-1"),
								charsetName));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			});
		}

		abstract void callback(String data);
	}

	/**
	 * 解析文件.
	 * 1.swiftcode号加入set集合，判断数据库中是否包含此swiftcode，有则更新，无则新增。
	 * 2.如果set集合已经存在此swiftcode，直接下一行数据
	 * 3.如果银行swiftCode号为空，则直接下一行数据
	 * @param file 解析文件路径
	 * @throws EbillsException 
	 * @throws IOException 
	 * @throws SQLException 
	 */
	private static int fileResolve(String data) throws IOException, SQLException {
		int result = 0;
		try {
			int countInsert = 1;
			int countUpdate = 1;
			String targe = "\\t";
			final int BATCH_SIZE = 1000;
				//修改M、增加A、删除D  其中数据不删除
				if (0==data.indexOf("A") || 0==data.indexOf("M") ) {
					//swifCode
					String swiftCode = find(data, 7, 9, targe).replaceAll("\\t", "").replace("XXX", "");
					if(swiftCodeSet.contains(swiftCode) || "".equals(swiftCode) || swiftCode == null ){
					}else{
						swiftCodeSet.add(swiftCode);
						//银行信息
//						Map<String,Object> bankInfo = getBankInfo(swiftCode);
						//国家名称
						String countryName = find(data, 46, 47, targe).replaceAll("\\t", "");
						//英文名称
						String bankName = find(data, 16, 18, targe).replaceAll("\\t", "") + find(data, 44, 45, targe).replaceAll("\\t", "")	;
						//英文地址1,地址2,地址3按顺序优先选择,如果都没有数据,此数据作废
						String address = find(data, 20, 28, targe).replaceAll("\\t", ",");//地址1
						address = StringStartAndEndTrim(address, ",");
						if("".equals(address) || null == address){
							address = find(data, 28, 36, targe).replaceAll("\\t", ",");//地址2
							address = StringStartAndEndTrim(address, ",");
						}
						if("".equals(address) || null == address){
							address = find(data, 36, 44, targe).replaceAll("\\t", ",");//地址3
							address = StringStartAndEndTrim(address, ",");
						}
						while (address.indexOf(",,") >= 0){
							address=address.replaceAll(",,", ",");
						}
						if(("".equals(address) || null == address) || (bankName == null || "".equals(bankName))){
						}
						
						System.out.println(countryName + " || " + swiftCode + " || " + bankName +" || " + address);
						
					}
				}
				if (countInsert % BATCH_SIZE == 0) {
				}
				if (countUpdate % BATCH_SIZE == 0) {
				}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}
	   /** 
  * 去掉指定字符串的开头和结尾的指定字符 
  * @param stream 原始字符串 
  * @param trim 要删除的字符串 
  * @return 
  */  
 public static String StringStartAndEndTrim(String stream, String trim) {  
 	// null或者空字符串的时候不处理  
 	if (stream == null || stream.length() == 0 || trim == null || trim.length() == 0) {  
 		return stream;  
 	}  
 	// 要删除的字符串结束位置  
 	int end;  
 	// 正规表达式  
 	String regPattern = "[" + trim + "]*+";  
 	Pattern pattern = Pattern.compile(regPattern, Pattern.CASE_INSENSITIVE);  
 	// 去掉原始字符串开头位置的指定字符  
 	Matcher matcher = pattern.matcher(stream);  
 	if (matcher.lookingAt()) {  
 		end = matcher.end();  
 		stream = stream.substring(end);  
 	}  
 	
 	String temp = reverse(stream);
 	matcher = pattern.matcher(temp);  
 	if (matcher.lookingAt()) {  
 		end = matcher.end();  
 		temp = temp.substring(end);  
 	}  
 	stream =  reverse(temp);
 	// 返回处理后的字符串  
 	return stream;  
 }  
 
 /**
  * 反转字符串
  * @param str
  * @return
  */
 public static String reverse(String str){  
     return new StringBuilder(str).reverse().toString();  
 }  
	
	/**
	 * 获取指定范围的值.
	 * 
	 * @param str  字符串
	 * @param start  开始点
	 * @param end 结束点
	 * @param targe 分隔符
	 * @return
	 */
	private static String find(String str, int start, int end, String targe) {
		if (null == str || "" == str) {
			return "";
		}
		int startIndex = getCharacterPosition(str, targe, start);
		int endIndex = getCharacterPosition(str, targe, end);
		String targeStr = str.substring(startIndex, endIndex);
		return targeStr;
	}

	/**
	 * 获取字符在字符串中出现的第n次位置
	 * @param str 字符串
	 * @param tag 获取的符号
	 * @param num 第几次出现
	 * @return
	 */
	private static int getCharacterPosition(String str, String tag, int num) {
		// 这里是获取"tag"符号的位置
		Matcher slashMatcher = Pattern.compile(tag).matcher(str);
		int mIdx = 0;
		while (slashMatcher.find()) {
			mIdx++;
			// 当"/"符号第n次出现的位置
			if (mIdx == num) {
				break;
			}
		}
		return slashMatcher.start();
	}
	
}

