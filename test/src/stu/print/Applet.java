package stu.print;

import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import javax.swing.JApplet;

import net.sf.json.JSONObject;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageable;

public class Applet extends JApplet {

	/**
	 * 返回打印状态
	 */
	private String resultState;

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4450259317104397591L;

	@Override
	public void init() {
		System.out.println("method________init()");
	}

	/**
	 * 打印文件
	 * 
	 * @param printService
	 * @param filePath
	 * @param flavor
	 * @param pras
	 * @param sleepTime
	 */
	public String printDoc(String hostAppPath, PrintService printService,
			String fileUrl, DocFlavor flavor,
			HashPrintRequestAttributeSet pras, int sleepTime) {
		if (printService == null) {
			System.out.println("打印机服务对象为空");
			return "打印机服务对象为空,打印失败";
		}
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		PDDocument document;
		try {
			DocPrintJob job = printService.createPrintJob(); // 创建打印作业
			DocAttributeSet das = new HashDocAttributeSet();
			addPrintJobListener(job);// 添加监听
			URL url = new URL(hostAppPath + "print?fileUrl=" + fileUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			// 设置请求参数
			conn.setRequestMethod("POST");
			// 得到输入流
			InputStream in = conn.getInputStream();
			// 从输入流中读取内容放到内存中
			document = PDDocument.load(in);
			PrinterJob printJob = PrinterJob.getPrinterJob();
			Doc doc = new SimpleDoc(new PDPageable(document, printJob), flavor,
					das);
			job.print(doc, pras);
		} catch (Exception e) {
			e.printStackTrace();
			return "出现异常,打印失败";
		}
		return resultState;
	}

	/**
	 * 添加监听
	 * 
	 * @param docJob
	 */
	public void addPrintJobListener(DocPrintJob docJob) {
		docJob.addPrintJobListener(new PrintJobListener() {
			public void printJobRequiresAttention(PrintJobEvent pje) {
				// 调用此方法来通知客户端，发生了一个用户也许能够修复的错误，可以生成此事件的一个错误示例是打印机用完了纸张。
				resultState = "5";
			}

			public void printJobNoMoreEvents(PrintJobEvent pje) {
				// 调用此方法来通知客户端，不需要再提供事件。
				resultState = "1";
			}

			public void printJobFailed(PrintJobEvent pje) {
				// 调用此方法来通知客户端，无法成功完成作业，必须重新提交该作业
				resultState = "4";
			}

			public void printJobCompleted(PrintJobEvent pje) {
				// 调用此方法来通知客户端，作业已成功完成。
				resultState = "0";
			}

			public void printJobCanceled(PrintJobEvent pje) {
				// 调用此方法来通知客户端，作业已被用户或程序取消
				resultState = "2";
			}

			public void printDataTransferCompleted(PrintJobEvent pje) {
				// 调用此方法来通知客户端，已成功地将数据传输到打印服务，客户端可以释放分配给该数据的本地资源。
				resultState = "3";
			}
		});
	}

	/**
	 * 客户端打印
	 * 
	 * @param printName
	 *            服务名
	 * @param fileName
	 *            文件名
	 * @return 成功标识
	 */
	public String print(final String printName, final String fileName,
			final String pdfPath, final String hostAppPath, final String date,
			final String cardType) {
		System.out.println("printName -- " + printName);
		System.out.println("fileName -- " + fileName);
		System.out.println("pdfPath -- " + pdfPath);
		System.out.println("hostAppPath -- " + hostAppPath);
		System.out.println("date -- " + date);
		System.out.println("cardType -- " + cardType);
		String result = AccessController
				.doPrivileged(new PrivilegedAction<String>() {
					public String run() {
						System.out.println("时间: " + System.currentTimeMillis()
								+ "      服务器名:" + printName + "   文件名: "
								+ fileName);
						Map<String, String> map = new LinkedHashMap<String, String>();
						String errorMsg = "";
						String errorCode = "0";
						String success = "0";
						String resultStr = "";
						try {
							Applet at = new Applet();
							/*
							 * PDDocument document = PDDocument.load(new URL(at
							 * .analysisPath(fileName) + ".pdf").openStream());
							 */
							PrinterJob printJob = PrinterJob.getPrinterJob();
							PrintService[] ps = PrintServiceLookup
									.lookupPrintServices(null, null);
							for (int i = 0; i < ps.length; i++) {
								System.out.println("打印机: " + i + " = "
										+ ps[i].getName());
								if (ps[i].getName().equals(printName)) {
									printJob.setPrintService(ps[i]);
									break;
								}
							}

							/**
							 * ------------------- start
							 * ------------------------
							 */
							HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
							// 设置打印类型
							DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
							resultStr = printDoc(hostAppPath, printJob
									.getPrintService(),
									at.analysisPath(fileName + ".pdf", pdfPath,
											date, cardType), flavor, pras, 0);

							/** ------------------- end ------------------------ */
							/*
							 * printJob.setPageable(new PDPageable( ,
							 * printJob)); printJob.print();
							 */
						} catch (Exception e) {
							errorMsg = "未找到打印机或者文件";
							errorCode = "999";
							success = "1";
							e.printStackTrace();
						} finally {
							map.put("resultState", resultStr);
							map.put("errorMsg", errorMsg);
							map.put("errorCode", errorCode);
							map.put("success", success);
							map.put("outEntity", "");
						}

						return JSONObject.fromObject(map).toString();
					}
				});
		return result;
	}

	/**
	 * 得到所有服务名
	 * 
	 * @return 服务名
	 */
	public String getPrintServices() {
		String result = AccessController
				.doPrivileged(new PrivilegedAction<String>() {
					public String run() {
						PrintService[] ps = PrintServiceLookup
								.lookupPrintServices(null, null);
						Map<String, String> map = new LinkedHashMap<String, String>();
						JSONObject obj = new JSONObject();
						for (int i = 0; i < ps.length; i++) {
							map.put("serviceName", ps[i].getName());
							obj.accumulateAll(map);
						}
						System.out.println(obj.toString());
						return obj.toString();
					}
				});
		return result;
	}

	/**
	 * 得到客户主机
	 * 
	 * @return 客户主机
	 */
	public String getCustomHostName() {
		String result = AccessController
				.doPrivileged(new PrivilegedAction<String>() {
					public String run() {
						InetAddress address = null;
						try {
							address = InetAddress.getLocalHost();
						} catch (UnknownHostException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						return address.getHostName();
					}
				});
		return result;
	}

	/**
	 * 根据文件名,解析出文件全路径
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件全路径
	 */
	public String analysisPath(String fileName, String url, String date,
			String cardType) {
		System.out.println("-------------------------开始解析文件名");
		// 初始化,个个文件夹名称
		/*
		 * String year, month, day; // 文件名解析出路径.文件名命名规则 txnno_小版本号_模板名称.pdf //
		 * 2014-4-23 String[] dateTmp = date.split("-"); // 年 year = dateTmp[0];
		 * // 月 month = dateTmp[1]; // 日 day = dateTmp[2];
		 */
		String path = url + "output" + "/" + date + "/"
				+ new Applet().getCardTypeName(cardType) + "/" + fileName;
		System.out.println("文件路径为:-------------------" + path);
		return path;
	}

	private Map<String, String> types = new HashMap<String, String>();

	private String getCardTypeName(String cardType) {
		String result = null;
		// 初始化面函类型
		if (0 == types.size()) {
			types.put("1", "voucher");
			types.put("2", "declare");
			types.put("3", "msgs");
			types.put("4", "docs");
		}
		// 返回类型名称
		for (String key : types.keySet()) {
			if (key.equals(cardType)) {
				result = types.get(key);
				break;
			}
		}
		return result;
	}

	public static void main(String[] args) throws IOException {
	}
}
