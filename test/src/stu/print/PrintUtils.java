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
		
		//String printStr = "��ӡ��������";// ��ȡ��Ҫ��ӡ��Ŀ���ı�
        if (textStream != null) // ����ӡ���ݲ�Ϊ��ʱ
        {
            // ָ����ӡ�����ʽ
            DocFlavor flavor = DocFlavor.INPUT_STREAM.PDF;//SERVICE_FORMATTED.PRINTABLE
            // ��λĬ�ϵĴ�ӡ����
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            // ������ӡ��ҵ
            DocPrintJob job = printService.createPrintJob();
            // ���ô�ӡ����
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            // ����ֽ�Ŵ�С,Ҳ�����½�MediaSize�����Զ����С
            pras.add(MediaSizeName.ISO_A4);
            DocAttributeSet das = new HashDocAttributeSet();
            // ָ����ӡ����
            Doc doc = new SimpleDoc(textStream, flavor, das);
            // ����ʾ��ӡ�Ի���ֱ�ӽ��д�ӡ����
            try {
                job.print(doc, pras); // ����ÿһҳ�ľ����ӡ����
            } catch (PrintException pe) {
                pe.printStackTrace();
            }
        } else {
            // �����ӡ����Ϊ��ʱ����ʾ�û���ӡ��ȡ��
            JOptionPane.showConfirmDialog(null,
                    "Sorry, Printer Job is Empty, Print Cancelled!",
                    "Empty", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE);
        }
    }


}

