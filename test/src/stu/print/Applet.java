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
	 * ���ش�ӡ״̬
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
	 * ��ӡ�ļ�
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
			System.out.println("��ӡ���������Ϊ��");
			return "��ӡ���������Ϊ��,��ӡʧ��";
		}
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		PDDocument document;
		try {
			DocPrintJob job = printService.createPrintJob(); // ������ӡ��ҵ
			DocAttributeSet das = new HashDocAttributeSet();
			addPrintJobListener(job);// ��Ӽ���
			URL url = new URL(hostAppPath + "print?fileUrl=" + fileUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			// �����������
			conn.setRequestMethod("POST");
			// �õ�������
			InputStream in = conn.getInputStream();
			// ���������ж�ȡ���ݷŵ��ڴ���
			document = PDDocument.load(in);
			PrinterJob printJob = PrinterJob.getPrinterJob();
			Doc doc = new SimpleDoc(new PDPageable(document, printJob), flavor,
					das);
			job.print(doc, pras);
		} catch (Exception e) {
			e.printStackTrace();
			return "�����쳣,��ӡʧ��";
		}
		return resultState;
	}

	/**
	 * ��Ӽ���
	 * 
	 * @param docJob
	 */
	public void addPrintJobListener(DocPrintJob docJob) {
		docJob.addPrintJobListener(new PrintJobListener() {
			public void printJobRequiresAttention(PrintJobEvent pje) {
				// ���ô˷�����֪ͨ�ͻ��ˣ�������һ���û�Ҳ���ܹ��޸��Ĵ��󣬿������ɴ��¼���һ������ʾ���Ǵ�ӡ��������ֽ�š�
				resultState = "5";
			}

			public void printJobNoMoreEvents(PrintJobEvent pje) {
				// ���ô˷�����֪ͨ�ͻ��ˣ�����Ҫ���ṩ�¼���
				resultState = "1";
			}

			public void printJobFailed(PrintJobEvent pje) {
				// ���ô˷�����֪ͨ�ͻ��ˣ��޷��ɹ������ҵ�����������ύ����ҵ
				resultState = "4";
			}

			public void printJobCompleted(PrintJobEvent pje) {
				// ���ô˷�����֪ͨ�ͻ��ˣ���ҵ�ѳɹ���ɡ�
				resultState = "0";
			}

			public void printJobCanceled(PrintJobEvent pje) {
				// ���ô˷�����֪ͨ�ͻ��ˣ���ҵ�ѱ��û������ȡ��
				resultState = "2";
			}

			public void printDataTransferCompleted(PrintJobEvent pje) {
				// ���ô˷�����֪ͨ�ͻ��ˣ��ѳɹ��ؽ����ݴ��䵽��ӡ���񣬿ͻ��˿����ͷŷ���������ݵı�����Դ��
				resultState = "3";
			}
		});
	}

	/**
	 * �ͻ��˴�ӡ
	 * 
	 * @param printName
	 *            ������
	 * @param fileName
	 *            �ļ���
	 * @return �ɹ���ʶ
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
						System.out.println("ʱ��: " + System.currentTimeMillis()
								+ "      ��������:" + printName + "   �ļ���: "
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
								System.out.println("��ӡ��: " + i + " = "
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
							// ���ô�ӡ����
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
							errorMsg = "δ�ҵ���ӡ�������ļ�";
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
	 * �õ����з�����
	 * 
	 * @return ������
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
	 * �õ��ͻ�����
	 * 
	 * @return �ͻ�����
	 */
	public String getCustomHostName() {
		String result = AccessController
				.doPrivileged(new PrivilegedAction<String>() {
					public String run() {
						InetAddress address = null;
						try {
							address = InetAddress.getLocalHost();
						} catch (UnknownHostException e) {
							// TODO �Զ����ɵ� catch ��
							e.printStackTrace();
						}
						return address.getHostName();
					}
				});
		return result;
	}

	/**
	 * �����ļ���,�������ļ�ȫ·��
	 * 
	 * @param fileName
	 *            �ļ���
	 * @return �ļ�ȫ·��
	 */
	public String analysisPath(String fileName, String url, String date,
			String cardType) {
		System.out.println("-------------------------��ʼ�����ļ���");
		// ��ʼ��,�����ļ�������
		/*
		 * String year, month, day; // �ļ���������·��.�ļ����������� txnno_С�汾��_ģ������.pdf //
		 * 2014-4-23 String[] dateTmp = date.split("-"); // �� year = dateTmp[0];
		 * // �� month = dateTmp[1]; // �� day = dateTmp[2];
		 */
		String path = url + "output" + "/" + date + "/"
				+ new Applet().getCardTypeName(cardType) + "/" + fileName;
		System.out.println("�ļ�·��Ϊ:-------------------" + path);
		return path;
	}

	private Map<String, String> types = new HashMap<String, String>();

	private String getCardTypeName(String cardType) {
		String result = null;
		// ��ʼ���溯����
		if (0 == types.size()) {
			types.put("1", "voucher");
			types.put("2", "declare");
			types.put("3", "msgs");
			types.put("4", "docs");
		}
		// ������������
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
