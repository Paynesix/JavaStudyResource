package stu.print;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.JOptionPane;

public class PrintUtils {
	public static void main(String[] args) {
		FileInputStream textStream = null;
		try {
			textStream = new FileInputStream("D://gjyw//output//2017-08-31//voucher//2017060900190723_001_whshpz.pdf");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//String printStr = "打印测试内容";// 获取需要打印的目标文本
        if (textStream != null) // 当打印内容不为空时
        {
            // 指定打印输出格式
            DocFlavor flavor = DocFlavor.INPUT_STREAM.PDF;//SERVICE_FORMATTED.PRINTABLE
            // 定位默认的打印服务
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            // 创建打印作业
            DocPrintJob job = printService.createPrintJob();
            // 设置打印属性
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            // 设置纸张大小,也可以新建MediaSize类来自定义大小
            pras.add(MediaSizeName.ISO_A4);
            DocAttributeSet das = new HashDocAttributeSet();
            // 指定打印内容
            Doc doc = new SimpleDoc(textStream, flavor, das);
            // 不显示打印对话框，直接进行打印工作
            try {
                job.print(doc, pras); // 进行每一页的具体打印操作
            } catch (PrintException pe) {
                pe.printStackTrace();
            }
        } else {
            // 如果打印内容为空时，提示用户打印将取消
            JOptionPane.showConfirmDialog(null,
                    "Sorry, Printer Job is Empty, Print Cancelled!",
                    "Empty", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE);
        }
    }


}

