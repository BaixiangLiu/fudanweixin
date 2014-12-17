package edu.fudan.weixin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.CDATA;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.EncodeHelper;

/**
 * 微信XML消息与MongoDB的数据对象的互相转换
 * 
 * @author wking
 * 
 */
public class WeixinMessageHelper {	

	/**
	 * 将微信XML转换化DBO
	 * 
	 * @param xml
	 *            MESSAGE XML
	 * @return DBO
	 */
	public static BasicDBObject xml2dbo(Document xml) {
		if (xml == null)
			return null;
		List<Element> params = xml.getRootElement().getChildren();
		if (params == null || params.size() <= 0)
			return null;
		//log.info(xml2str(xml));
		BasicDBObject dbo = new BasicDBObject();
		for (Element e : params) {

			List<Content> cs = e.getContent();
			if (cs != null && cs.size() > 0) {
				for (Content c : cs) {
					if (c != null) {
						Object o = null;
						switch (c.getCType()) {
						case Text:
						case CDATA:
							o = c.getValue();
							break;
						default:;
						}
						if (!CommonUtil.isEmpty(o)) {
							if (e.getName().equalsIgnoreCase("createtime"))
								o = Integer.parseInt(o.toString());
							dbo.append(e.getName(), o);
						}
					}
				}
			}
		}
		return dbo;
	}

	/**
	 * 从InputStream中的XML文档生成DBO
	 * 
	 * @param input
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static BasicDBObject stream2dbo(InputStream input)
			throws JDOMException, IOException {
		return xml2dbo(new SAXBuilder().build(input));
	}

	/**
	 * 将DBO转换为xml消息
	 * 
	 * @param dbo
	 * @return
	 */
	public static Document dbo2xml(Map<String, Object> message) {
		return new Document(msg2xml(message, "xml"));
	}

	/**
	 * 将Message转换为xml节点
	 * 
	 * @param message
	 * @param rootname
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Element msg2xml(Object message, String rootname) {
		Element root = new Element(rootname);
		if (message instanceof Map)

			for (Entry<String, Object> entry : ((Map<String, Object>) message)
					.entrySet()) {
				if (!CommonUtil.isEmpty(entry.getValue())) {

					setValue(root, entry.getValue(), entry.getKey());

				}
			}
		if (message instanceof List)
			for (Object o : (List) message) {
				if (!CommonUtil.isEmpty(o)) {
					setValue(root, o, "item");
				}
			}

		return root;
	}

	private static void setValue(Element e, Object v, String name) {
		if (v instanceof Map)
			e.addContent(msg2xml(v, name));
		else if (v instanceof List)
			e.addContent(msg2xml(v, name));
		else {
			Element n = new Element(name);
			if (v instanceof Number || v instanceof Boolean)
				n.addContent(String.valueOf(v));
			else {
				n.addContent(new CDATA(String.valueOf(v)));

			}
			e.addContent(n);
		}
	}

	/**
	 * XML Document对象转换为pretty样式的字符串，当doc为null时返回空字符串
	 * 
	 * @param doc
	 * @return
	 */
	public static String xml2str(Document doc) {
		if (doc == null)
			return "";
		return new XMLOutputter(Format.getPrettyFormat().setOmitDeclaration(true)).outputString(doc);
	}

	/**
	 * 将XML Document对象写到输出流
	 * 
	 * @param doc
	 * @param out
	 * @throws IOException
	 */
	public static void xml2stream(Document doc, OutputStream out)
			throws IOException {
		if (doc != null)
			new XMLOutputter(Format.getPrettyFormat().setOmitDeclaration(true))
					.output(doc, out);
	}

	/**
	 * 检测请求是否来自微信服务器
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checksum(String signature, String timestamp,
			String nonce) {
		try {
			String[] strs = new String[] {
					Config.getInstance().get("weixin.token"), timestamp, nonce };
			Arrays.sort(strs);
			StringBuilder sb = new StringBuilder();
			for (String s : strs) {
				sb.append(s);
			}
			String hash = EncodeHelper.digest(sb.toString(), "SHA1");
			if (hash.equalsIgnoreCase(signature))
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 将消息转换为JSON字串，用于向微信服务器接口提交请求
	 * 
	 * @param message
	 * @return
	 */
	public static String msg2jsonstr(Map<String, Object> message) {
		return JSON.serialize(message);
	}

	/**
	 * 将为JSON字串格式的消息转换成DBO对象，用于存储到数据库
	 * 
	 * @param JSON
	 *            String
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonString2dbo(String jsonstr) {
		return (Map<String, Object>) JSON.parse(jsonstr);
	}

	
	
}
