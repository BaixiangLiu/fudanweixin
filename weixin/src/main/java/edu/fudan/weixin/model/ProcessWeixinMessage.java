package edu.fudan.weixin.model;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.mongodb.BasicDBObject;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.processor.MessageProcessor;
import edu.fudan.weixin.utils.WeixinMessageHelper;

/**
 * 微信收到消息的处理入口
 * 
 * @author wking
 * 
 */
public class ProcessWeixinMessage {
	private static Log log = LogFactory.getLog(ProcessWeixinMessage.class);

	private static HashMap<String, MessageProcessor> procs = init();

	public static HashMap<String, MessageProcessor> init() {
		HashMap<String, MessageProcessor> ret = new HashMap<String, MessageProcessor>();
		String[] processors = Config.getInstance().get("weixin.processors")
				.split("[;；,，]");
		for (String pstr : processors) {
			try {
				ret.put(pstr, (MessageProcessor) (Class
						.forName("edu.fudan.weixin.model.processor." + pstr)
						.newInstance()));

			} catch (Exception e) {
				// e.printStackTrace();
				log.error(e);
			}
		}
		return ret;
	}

	public static void process(HttpServletRequest request,
			HttpServletResponse response) {

		try {

			String signature = request.getParameter("signature");
			String timestamp = request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");
			String enctype=request.getParameter("encrypt_type");
			String msgsig=request.getParameter("msg_signature");
			
			/*
			log.info("signature:"+signature);
			log.info("timestamp:"+timestamp);
			log.info("nonce:"+nonce);
			log.info("enctype:"+enctype);
			log.info("msgsig:"+msgsig);
			*/
			
			BasicDBObject obj=null;
			WXBizMsgCrypt crypt=null;
			Config conf=Config.getInstance();
			if(enctype!=null &&enctype.equalsIgnoreCase("aes"))
			{
				BufferedReader rd=request.getReader();
				StringBuffer reqsb=new StringBuffer();
				String ln;
				while((ln=rd.readLine())!=null)
				{
					reqsb.append(ln);
				}
				crypt=new WXBizMsgCrypt(conf.get("weixin.token"),conf.get("weixin.aeskey"),conf.get("weixin.appid"));
				String decstr=crypt.decryptMsg(msgsig, timestamp, nonce, reqsb.toString());
				obj=WeixinMessageHelper.xml2dbo(new SAXBuilder().build(new StringReader(decstr)));
				//log.info(decstr);
			}else if(WeixinMessageHelper.checksum(signature, timestamp, nonce)||"127.0.0.1".equals(request.getRemoteAddr()))
			{
				obj=WeixinMessageHelper.stream2dbo(request.getInputStream());
			}
			
			
			

			
			// 无消息内容，认为是接口验证
			if (obj != null) {

				// 将消息存入MongoDB
				MongoUtil.getInstance().getDB().getCollection("Messages")
						.save(obj);
				log.info("Receive:" + obj.toString());
				if ("voice".equalsIgnoreCase(obj.getString("MsgType"))) {
					obj.put("Content", obj.get("Recognition"));
				}

			
				// 这里以后做一个可配置的队列或者栈，将不同的MessageProcesser实现实例化后处理，直到产生消息回复对象，然后直接返回

				Map<String, Object> dbo = null;
				String[] processors = conf
						.get("weixin.processors").split("[;；,，]");
				for (String pstr : processors) {
					try {
						MessageProcessor p = procs.get(pstr);
						if (p == null) {
							p = (MessageProcessor) (Class
									.forName("edu.fudan.weixin.model.processor."
											+ pstr).newInstance());
							procs.put(pstr, p);
						}
						dbo = p.process(obj);

					} catch (Exception e) {
						// e.printStackTrace();
						log.error(e);
					}
					if (dbo != null)
						break;
				}

				if (dbo.size() > 0) {
					dbo.put("ToUserName", obj.get("FromUserName"));
					dbo.put("FromUserName", obj.get("ToUserName"));
					long now=System.currentTimeMillis();
					dbo.put("CreateTime",
							(int) (now/ 1000));
			

					// 回复用户
					response.setCharacterEncoding("utf-8");
					response.setContentType("application/xml");
					Document doc=
							WeixinMessageHelper.dbo2xml(dbo);
					if(crypt==null)
						WeixinMessageHelper.xml2stream(doc, response.getOutputStream());
					else
					{
						String enc=crypt.encryptMsg(new XMLOutputter(Format.getPrettyFormat().setOmitDeclaration(true)).outputString(doc), Long.toString(now), nonce);
						//log.info("SEND:"+enc);
						response.getWriter().write(enc);
						response.flushBuffer();
					}
						
					// 将回复的消息 存入MongoDB
					MongoUtil.getInstance().getDB().getCollection("Messages")
							.save(new BasicDBObject(dbo));
				}
			}

		} catch (Exception e) {
			log.error(e);
		}

	}

}
