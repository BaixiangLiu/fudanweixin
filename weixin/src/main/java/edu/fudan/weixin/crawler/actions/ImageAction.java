package edu.fudan.weixin.crawler.actions;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.eservice.common.utils.ImageHelper;

@ParentPackage("servicebase")
@Namespace("/crawler")
public class ImageAction extends GuestActionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9120298229890720293L;

	private String id;
	
	private static Log log=LogFactory.getLog(ImageAction.class);
	
	@Action("imgurl")
	public String url()
	{
		if(!CommonUtil.isEmpty(id))
		{
			DBCollection c=MongoUtil.getInstance().getDB().getCollection("CrawlerImages");
			DBObject dbo=c.findOne(new BasicDBObject("id",id));
			if(!CommonUtil.isEmpty(dbo))
			{
				try {
					ServletActionContext.getResponse().getWriter().print(dbo.get("url"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return NONE;
	}
	
	public String execute()
	{
		if(!CommonUtil.isEmpty(id))
		{
			DBCollection c=MongoUtil.getInstance().getDB().getCollection("CrawlerImages");
			DBObject dbo=c.findOne(new BasicDBObject("id",id));
			if(!CommonUtil.isEmpty(dbo))
			{
				HttpServletResponse resp=ServletActionContext.getResponse();
				resp.setContentType("image/gif");
			
				try{
					byte[] bimg=null;
				if(!CommonUtil.isEmpty(dbo.get("image")))
				{
					bimg=(byte[]) dbo.get("image");
				}else
				{
					BufferedImage bi=ImageHelper.readImage(dbo.get("url").toString());
					int width=640;
					double scale = (double)  bi.getWidth() /width;
					BufferedImage to =null;
					if(scale>1){
					int newheight = (int) (bi.getHeight() / scale);
					to= ImageHelper.doThumb(bi, width, newheight);
					
					}else
						to=bi;
					
					ByteArrayOutputStream bout=new ByteArrayOutputStream();
					ImageIO.write(to, "gif", bout);
					bimg=bout.toByteArray();
					dbo.put("image", bimg);
					c.save(dbo);
				
				}
				resp.getOutputStream().write(bimg);
				resp.getOutputStream().flush();
				}catch(Exception e){
					log.error(e);
				}
			}
		}
		return NONE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
