package com.gjjs.Commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;

import com.gjjs.Commons.Commons;
import com.gjjs.OtInterface.YtAutoSkReciveInfo;
import com.gjjs.OtInterface.YtAutoSkSendInfo;

public class XMLUtils {


	public static  String parseXML(Object[] inObjs) {
		String ret = "";
		Object inObj=inObjs[0];
		if (inObj == null) {
			return "";
		}		
		Document document = DocumentHelper.createDocument();
		Element Service = document.addElement("Service");
		Element Service_Header = Service.addElement("Service_Header");
		Element body = Service.addElement("Service_Body");
		Element bodyrequest = body.addElement("request");
		Element bodyresponse = body.addElement("response");
		
		if(inObjs.length>=1){
			YtAutoSkReciveInfo service=(YtAutoSkReciveInfo)inObjs[0];
			Element service_sn  =  Service_Header.addElement("service_sn");
			service_sn.addText(service.getService_sn()+"");
			Element service_id  =  Service_Header.addElement("service_id");
			service_id.addText(service.getServiceId()+"");
			Element requester_id = Service_Header.addElement("requester_id");
			requester_id.addText(service.getRequester_id()+"");
			Element branch_id = Service_Header.addElement("branch_id");
			branch_id.addText(service.getBranch_id()+"");
			Element service_time = Service_Header.addElement("service_time");
			service_time.addText(service.getService_time()+"");
			Element coreCustNo = bodyrequest.addElement("coreCustNo");
			coreCustNo.addText(service.getCoreCustNo()+"");
			Element startDate = bodyrequest.addElement("startDate");
			startDate.addText(service.getStartDate()+"");
			Element endDate = bodyrequest.addElement("endDate");
			endDate.addText(service.getEndDate()+"");
			Element acctNo = bodyrequest.addElement("acctNo");
			acctNo.addText(service.getAcctNo()+"");
			Element typeNo = bodyrequest.addElement("typeNo");
			typeNo.addText(service.getTypeNo()+"");
		}	
		Element service_response = Service_Header.addElement("service_response");
		if (inObjs.length>=2){
			String str=(String)inObjs[1];
			String[] arraystr=str.split(";");
			if(arraystr.length>0){
				Element status =  service_response.addElement("status");
				status.addText(arraystr[0]+"");
				Element code = service_response.addElement("code");
				code.addText(arraystr[1]+"");
				Element desc = service_response.addElement("desc");
				desc.addText(arraystr[2]+"");
			}
		}
		
		if(inObjs.length>=3){
			List list = (List)inObjs[2];
			Element totalCount = bodyresponse.addElement("totalCount");
			totalCount.addText(list.size()+"");
			if(list!=null){
				for(int i=0;i<list.size();i++){
					YtAutoSkSendInfo send =(YtAutoSkSendInfo)list.get(i);
					Element elist = bodyresponse.addElement("list");
					elist.addAttribute("loop_num", i+1+"");
					Element payDate = elist.addElement("payDate");
					payDate.addText(send.getPayDate()+"");
					Element expDate = elist.addElement("expDate");
					expDate.addText(send.getExpDate()+"");
					Element listypeNo = elist.addElement("typeNo");
					listypeNo.addText(send.getTypeNo()+"");
					Element listacctNo = elist.addElement("acctNo");
					listacctNo.addText(send.getAcctNo()+"");
					Element ccy = elist.addElement("ccy");
					try {
						ccy.addText(Commons.getCurSignCode(send.getCcy(), ""));
					} catch (Exception e) {
						e.printStackTrace();
					}
					Element payAmt = elist.addElement("payAmt");
					java.math.BigDecimal bd = new java.math.BigDecimal(send.getPayAmt());
					payAmt.addText(bd.toPlainString());
					
				}
			}
		}
		 try {
			ByteArrayOutputStream wrstr= new ByteArrayOutputStream();
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setTrimText(false);
			format.setEncoding("UTF-8");
			format.setNewLineAfterDeclaration(false);
			format.setIndent(false);
			format.setNewlines(false);
			org.dom4j.io.XMLWriter wr = new  org.dom4j.io.XMLWriter(wrstr,format);
			wr.write(document);		
			ret = new String(wrstr.toByteArray(),"UTF-8");
			wrstr.close();
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	};
	
	
	

}
