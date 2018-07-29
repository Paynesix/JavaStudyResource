package com.gjjs.Commons.FTPUtilTools;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.ebills.util.EbillsCfg;
import com.gjjs.Commons.utils.ApplicationException;


public class FtpFiles {
	private final Log log = LogFactory.getLog(getClass());

	private static FTPClient ftp = new FTPClient();
	private  String FTPServer = "127.0.0.1";
	private  String FTPUser = "test";
	private  String FTPPassWord = "test";
	private  String reciveFilePath = "e:/swiftServer/recive";
	private  String reciveFilePathBuf = "e:/swiftServer/recivebuf";
	private  String sendFilePath = "e:/swiftServer/send";
	private  String sendFilePathBak = "e:/swiftServer/sendBak";
	private  String remotRecPath="Output";
	private  String remotSendPath="Input";
	private  String RECIVE_FILE_EXT ;

	private  String SEND_FILE_EXT ;

	protected void process() {
		try {
			connectFTP();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			boolean result;
			FtpFiles ftpfiles = new FtpFiles();
			result = ftpfiles.connectFTP();
			System.out.println(result);
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
	/**
	 * 获取配置文件中的信息
	 * 烟台银行
	 */
	public String getvalue(String value){
		return EbillsCfg.getProperty(value);
	}
    
	public FtpFiles() throws Exception{
		this.RECIVE_FILE_EXT = getvalue("ftp.RECIVE_FILE_EXT");
		this.SEND_FILE_EXT = getvalue("ftp.SEND_FILE_EXT");
		this.FTPServer = getvalue("ftp.FTPServer");
		this.FTPUser = getvalue("ftp.FTPUser");
		this.FTPPassWord = getvalue("ftp.FTPPassWord");
		this.remotRecPath=getvalue("ftp.remotRecPath");
		this.remotSendPath=getvalue("ftp.remotSendPath");
		if (RECIVE_FILE_EXT == null || RECIVE_FILE_EXT.trim().length() < 1)
			throw new ApplicationException("收报扩展文件名为空");
		if (SEND_FILE_EXT == null || SEND_FILE_EXT.trim().length() < 1)
			throw new ApplicationException("发报扩展文件名为空");
	}
	
	public boolean connectFTP() throws Exception {
		int reply;
		try {
			System.out.println("FTP connecting " + FTPServer + ".......");
			log.info("FTP connecting " + FTPServer + ".......");
			try {
				ftp.connect(FTPServer);
			} catch (Exception eio) {
				System.out.println("FTP SERVER IS CLOSED.....");
				log.error("Exception(first try to connect):FTP SERVER IS CLOSED.....");
				return false;
			}

			reply = ftp.getReplyCode();
			log.info("FTP replyCode:" + reply);
			if (!FTPReply.isPositiveCompletion(reply)) {
				log.error("connect not isPositiveCompletion:"+String.valueOf(reply));
				ftp.disconnect();
				return false;
			}
		} catch (IOException e) {
			log.error("first try IO Exception:"+e);
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					return false;
				}
			}
		}
		try {
			if (!ftp.login(FTPUser, FTPPassWord)) {
				log.error("ftp username or password error!");
				ftp.logout();
				return false;
			}
			  log.info("ftp login success!");
			   ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
				reciveFilePath=getvalue("swift.receivePath");
				sendFilePath = getvalue("swift.sendPath");
				sendFilePathBak=getvalue("swift.sendPathback");;
				// 接收下载报文
				ftp.changeWorkingDirectory("/");
				log.info("wait for recive,FTP current dir：" + ftp.printWorkingDirectory());
				GetFiles(ftp);
				log.info("recive file over!");
				// 发送上传报文
				ftp.changeToParentDirectory();
				log.info("wait for send,ftp current dir：" + ftp.printWorkingDirectory());
				PutFiles(ftp);
				log.info("send file over!");
		
			if (ftp.isConnected()){
				ftp.disconnect();
				log.info("已断开与主机"+FTPServer+"连接..........");
			}
			else
				log.info("已断开主机连接............");
			
			
		} catch (FTPConnectionClosedException e) {
			log.error("FTPConnectionClosedException:"+e);
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					return false;
				}
			}
			return false;
		} catch (IOException e) {
			log.error("SEC IOException:"+e);
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					return false;
				}
			}
			return false;
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 上传文件
	 * @param ftp
	 */
	public void PutFiles(FTPClient ftp) {
		try {
			// ftp服务器INPUT目录
			//System.out.println("上传报文当前目录:"+ftp.printWorkingDirectory());
			System.out.println("remot send :"+remotSendPath);
			ftp.changeWorkingDirectory("/");
			ftp.changeWorkingDirectory(remotSendPath);//input
			System.out.println("上传报文当前目录:"+ftp.printWorkingDirectory());
			log.info("上传报文当前目录:"+ftp.printWorkingDirectory());
			Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
			String curDay = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			// 本地的SEND目录
			File putfiles = new File(sendFilePath);
			File[] sourcefiles = putfiles.listFiles();
			System.out.println("开始本地文件上传,本地文件数目: " + sourcefiles.length);
			log.info("开始本地文件上传,本地文件数目: " + sourcefiles.length);
			// 计算成功上传文件个数
			int uploadFileNum = 0;
			int uploadFileErorNum = 0;
			for (int i = 0; i < sourcefiles.length; i++) {
				if (sourcefiles[i].isFile()) {
					if (getExtension(sourcefiles[i].getName()).equals(
							SEND_FILE_EXT)) {
						// 判断该文件是否已在远程存在
						FTPFile[] getfiles = ftp.listFiles();
						boolean isExists = false;
						for (int p = 0; p < getfiles.length; p++) {
							if (getfiles[p].getName().equals(
									sourcefiles[i].getName())) {
								log.info("远程目录已存在[" + sourcefiles[i].getName()
										+ "]文件，此文件不上传");
								isExists = true;
							}
						}
						if (!isExists) {
							// put up a file to the FTP Server
							File putfile = new File(sendFilePath
									+ File.separator + sourcefiles[i].getName());
							FileInputStream swiftputcopy = new FileInputStream(
									putfile);
							if (ftp.storeFile(sourcefiles[i].getName(),
									swiftputcopy)) {
								uploadFileNum++;
								log.info("上传文件[" + sourcefiles[i].getName()
										+ "]成功");
								swiftputcopy.close();
								// 备份本地文件
								File  destdist = new File(sendFilePathBak
										+ File.separator+File.separator+curDay);
								if(!destdist.exists()){
									destdist.mkdir();
								}
								File destFile = new File(sendFilePathBak
										+ File.separator+File.separator+curDay+File.separator
										+ sourcefiles[i].getName());
								if (Utils.copyFile(putfile, destFile)) {
									log.info("备份本地文件[" + destFile.getName()
											+ "]成功");
									
									if (putfile.delete()){
									    log.info("删除本地文件[" + putfile.getPath()
												+ "]成功");
									} else {
										log.info("删除本地文件[" + putfile.getPath()
												+ "]失败");
									}
								} else {
									log.info("备份本地文件[" + destFile.getName()
											+ "]失败");
								}
							} else {
								uploadFileErorNum++;
								log.info("上传文件[" + sourcefiles[i].getName()
										+ "]失败");
							}
							swiftputcopy.close();
						}
					}

				}
			}
			System.out.println("上传文件成功,共上传[" + uploadFileNum + "]个文件,未成功["
					+ uploadFileErorNum + "]个文件");
			log.info("上传文件成功,共上传[" + uploadFileNum + "]个文件,未成功["
					+ uploadFileErorNum + "]个文件");
		} catch (Exception e) {
			log.error("Put Files find error:"+e);
			e.printStackTrace();
		}
	}

	/***
	 * 下载文件
	 * @param ftp
	 * @throws Exception
	 */
	public void GetFiles(FTPClient ftp) throws Exception {
		try {
			System.out.println("remot recpath:"+remotRecPath);
			ftp.changeWorkingDirectory(remotRecPath);//output
			System.out.println("FTP当前工作目录：" + ftp.printWorkingDirectory());
			FTPFile[] getfiles = ftp.listFiles();
			System.out.println("FTP当前工作目录2：" + ftp.printWorkingDirectory());
			log.info("目前SWIFT主机上共有[" + (getfiles.length ) + "]个报文文件"); //- 2
			System.out.println("FTP待取文件数目: " + (getfiles.length - 2));
			int recvNum = 0;
			// 根据烟台银行要求，下载下来的远程swift主机上的报文文件先不删除，将其按时间文件夹备份，隔5天后删除
			Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
			String curDay = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		    //先创建backup目录，然后再在backup目录下创建对应的时间文件夹
			ftp.makeDirectory("backup");	
			ftp.makeDirectory(ftp.printWorkingDirectory()+"/backup/"+curDay);
			for (int i = 0; i < getfiles.length; i++) {
				FTPFile ftpFile = (FTPFile)getfiles[i];
				if (ftpFile.isFile()) {
					// Download a file from the FTP Server
					if (getExtension(getfiles[i].getName()).equals(
							RECIVE_FILE_EXT)) {
						//收报文件
						File destfiles = new File(reciveFilePath
								+ File.separator + getfiles[i].getName());
						FileOutputStream getcopy = new FileOutputStream(
								destfiles);
						if (ftp.retrieveFile(getfiles[i].getName(), getcopy)){
							recvNum++;
							log.info("已成功下载[" + getfiles[i].getName()
									+ "]文件,开始删除远程文件");
						}else{
							log.info("下载[" + getfiles[i].getName()
									+ "]文件失败");
						}
						getcopy.close();
						
/*						if (ftp.deleteFile(getfiles[i].getName())) {
							log.info("远程[" + getfiles[i].getName() + "]文件删除成功");
						} else {
							log.info("远程[" + getfiles[i].getName()
									+ "]文件删除失败，请及时清理远程文件");
						}*/
						//实现文件备份到当前日期的文件夹下
						log.debug("将报文文件备份到FTP服务器BAKUP目录下以当前日期命名的时间文件夹:"+ftp.printWorkingDirectory()+"/backup/"+curDay+"下：");
						if(ftp.rename(ftp.printWorkingDirectory()+"/"+ftpFile.getName(), ftp.printWorkingDirectory()+"/backup/"+curDay+"/"+ftpFile.getName())){
							log.debug("实现将当前工作目录："+ftp.printWorkingDirectory()+"下的报文文件:[" + ftpFile.getName() + "]拷贝到对应BACKUP目录下的时间文件夹:["+curDay+"]下成功!");
							System.out.println("实现将当前工作目录："+ftp.printWorkingDirectory()+"下的报文文件:[" + ftpFile.getName() + "]拷贝到对应BACKUP目录下的时间文件夹:["+curDay+"]下成功!");
						}
					}
				}
			}
			//找到5天前的文件夹，删除掉此文件夹
			calendar.add(Calendar.DATE, -5);    		//得到前5天
	        String  befor5Day= new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());			
	        System.out.println("befor5Day:"+befor5Day);
	        //先进到FTP跟目录下的BackUP目录下，然后找到对应的时间目录
	        ftp.changeWorkingDirectory(ftp.printWorkingDirectory()+"/backup");
	        if(isExist(ftp, befor5Day)){
	        //if(!ftp.makeDirectory(befor5Day)){
	        	System.out.println("存在5天前的文件夹:"+ftp.printWorkingDirectory()+"/"+befor5Day+",将其删除.");
	        	ftp.changeWorkingDirectory(befor5Day);
				FTPFile[]  delFile = ftp.listFiles();
	        	for(int i=0;i<delFile.length;i++){
	        		System.out.println("删除文件:"+delFile[i].getName());
	        		ftp.deleteFile(delFile[i].getName());
	        	}
	        	System.out.println("删除5天前的文件夹:"+ftp.printWorkingDirectory()+"/"+befor5Day);
	        	ftp.changeToParentDirectory();
	        	ftp.removeDirectory(befor5Day);
	        }
			
			log.info("下载文件成功，共成功下载"+recvNum+"个文件");
			System.out.println("数据下载执行完成!");
		} catch (Exception e) {
			log.error("Get files find error:"+e);
			e.printStackTrace();
		}
	}
	/**
	 * 判断指定路径下的指定文件是否存在
	 * @param path
	 * @param name
	 * @return
	 * @throws IOException 
	 */
	public boolean isExist(FTPClient ftp, String name) throws IOException {
		boolean flag = false;
		FTPFile[] fs = ftp.listFiles();
		if (fs != null) {
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].getName().equals(name)) {
					return true;
				}
			}
		}
		return flag;
	}

	// 获取文件扩展名
	public static String getExtension(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');
			if ((i > 0) && (i < (filename.length() - 1))) {
				return filename.substring(i + 1);
			}
		}
		return "";
	}

}
