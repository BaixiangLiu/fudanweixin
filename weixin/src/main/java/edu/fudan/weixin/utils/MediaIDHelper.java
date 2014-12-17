package edu.fudan.weixin.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.EncodeHelper;
import edu.fudan.eservice.common.utils.MongoUtil;

/**
 * 
 * @author dannis
 *
 */

public class MediaIDHelper {

	
	private static Log log = LogFactory.getLog(MediaIDHelper.class);

	/**
	 * 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb） 
     * type 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb） media 
     * form-data中媒体文件标识，有filename、filelength、content-type等信息 
     * 图片（image）:128K，支持JPG格式 语音（voice）：256K，播放长度不超过60s，支持AMR\MP3格式 
     * 视频（video）：1MB，支持MP4格式 缩略图（thumb）：64KB，支持JPG格式 视频文件不支持下载 
     */
	
	public static void uploadMediaFile(File file){
		
		String mediaType = getMediaType(file.getName()); 
		String urlstr = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token="
				+ AccessTokenHelper.getInstance().getToken(AccessTokenHelper.WEIXIN)+"&type="+mediaType;
		try{
			String boundary = "--------------------------"+EncodeHelper.randpass(10);
			byte[] formdata = ("--"+boundary + "\r\n" + "Content-Disposition: form-data; name=\"file\"; filename=\""+
					file.getName() + "\"\r\n"+ "Content-Type:application/octet-stream\r\n\r\n").getBytes();
			byte[] enddata = ("\r\n--" + boundary + "--\r\n").getBytes();
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int filesize = (int) file.length();
			byte[] data = new byte[filesize];
			in.read(data, 0, filesize);
			in.close();
			byte[] content = new byte[formdata.length+data.length+enddata.length];
			System.arraycopy(formdata,0,content,0,formdata.length);
			System.arraycopy(data, 0, content, formdata.length, data.length);
			System.arraycopy(enddata, 0, content, formdata.length+data.length, enddata.length);
			Object ret = JSON.parse(CommonUtil.postWebRequest(urlstr,content,
					"multipart/form-data; charset=utf-8;boundary=" + boundary).toString());
			if (ret instanceof DBObject){
				DBObject dbo =(DBObject)ret;
				if (!dbo.containsField("errcode")){
					dbo.put("filename", file.getName());
					dbo.put("filesize", filesize);
					dbo.put("digest", EncodeHelper.MD5(data));
					log.info("upload mediafile success "+dbo.toString());
					MongoUtil.getInstance().getDB().getCollection("uploadmeida").save(dbo);
				}
				else
					log.error("upload failed! "+dbo.toString());
			}
			else
				log.error("error! "+ret.toString());
			
		}
		catch (Exception e){
			log.error(e.toString());
		}
	}
	
	public static String getMediaType(String filename){
		String ext = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
		if (CommonUtil.isEmpty(ext))
			return "file type error!";
		switch (ext) {
		case "amr":
			return "voice";
		case "mp3":
			return "voice";
		case "mp4":
			return "video";
		case "jpg":{
			if (filename.contains("thumb"))
				return "thumb";
			else
				return "image";	
		}
		default:
			return "file type error!";
		}
	}
	
	public static String getMediaID(String filename){
		
		DBObject dbo = MongoUtil.getInstance().getDB().getCollection("uploadmeida").findOne(new BasicDBObject("filename",filename));
		if (!CommonUtil.isEmpty(dbo))
			return String.valueOf(dbo.get("media_id"));
		else
			log.equals("get meidaId failed! "+dbo.toString());
		return null;
		
	}
	


}
