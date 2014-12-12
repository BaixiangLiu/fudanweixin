package edu.fudan.eservice.common.struts;

import edu.fudan.eservice.common.utils.Config;

import java.io.*;




/**
 * 文件上载Action的抽象类
 * 
 * @author bwang
 * 
 */
public class AbstractFileUploadAction extends GuestActionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4735200035660423323L;
	//禁止使用的扩展名，以免恶意jsp代码执行漏洞
	public static final String[] FORBIDDENEXT=new String[]{"act","ftl","jsp","vm"};
	
	protected File upload = null;

	protected String uploadFileName = null;//源文件名

	protected String uploadContentType = null;	
	

	
/**
	 * 列出/upload下指定目录里的所有文件名
	 * @param dir 相对目录，以"/"开头
	 * @return
	 */
	protected String[] listFiles(String dir)
	{
		String ffp=dir;
		if(!ffp.startsWith("/"))
		{
			ffp=Config.getInstance().get("mms.path")+ffp;
		}
		File listDir=new File(ffp);
		if(listDir.isDirectory());
		return listDir.list();
		
		
	}


	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

}
