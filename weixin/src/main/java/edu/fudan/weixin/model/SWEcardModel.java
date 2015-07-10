package edu.fudan.weixin.model;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.EncodeHelper;

/**
 * 树维一卡通接口
 * @author wking
 *
 */
public class SWEcardModel {
	
	private static final DateFormat DF=new SimpleDateFormat("yyyyMMddHHmmss");
	private static final DateFormat QDF=new SimpleDateFormat("yyyyMMdd");
	private static final String CTYPE="application/x-www-form-urlencoded";
	private static final String HASH="SHA1";
	private static final Logger log=LoggerFactory.getLogger(SWEcardModel.class);
	/**
	 * 生成订单
	 * @param uid
	 * @param amount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> order(String uid,int amount)
	{
		Map<String,Object> ret=new HashMap<String,Object>();
		String nstr=DF.format(new Date());
		Config conf=Config.getInstance();
		String key=conf.get("ykt.key");
		StringBuffer sb=new StringBuffer("stuempno=").append(uid)
				.append("&amount=").append(amount).append("&timestamp=").append(nstr).append("&sign=")
				.append(EncodeHelper.hmac(HASH, uid+amount+nstr, key));
		try {
			StringBuffer retstr=CommonUtil.postWebRequest(conf.get("ykt.orderurl"), sb.toString().getBytes("utf-8"),CTYPE );
			Object retobj=JSON.parse(retstr.toString());
			if(retobj instanceof Map)
			{
				ret=(Map<String,Object>) retobj;
				String retmsg= String.valueOf(ret.get("retcode"))+ret.get("retmsg")+ret.get("payid")+(CommonUtil.isEmpty(ret.get("url"))?"":URLDecoder.decode(ret.get("url").toString(), "utf-8"));
				if(!EncodeHelper.hmac(HASH, retmsg,key).equals(ret.get("sign")))
				{
					ret.put("retcode", -997);
					ret.put("retmsg", "CHECKSUM ERROR:");
				}
				//把返回的PC用的支付URL转为移动端的URL
				ret.put("url",String.valueOf( ret.get("url")).replace("2Fpay", "2FpayApp")+"&flag=0");
				
			}else
			{
				ret.put("retcode", -998);
				ret.put("retmsg", "PARSE ERROR:"+retstr);
			}
		}  catch (Exception e) {
			log.error("POST REQ", e );
			ret.put("retcode", -999);
			ret.put("retmsg", e.getMessage());
		}
		
		return ret;
	}
	
	/**
	 * 查询订单信息
	 * @param uid
	 * @param payid
	 * @param bdate
	 * @param edate
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> query(String uid,String payid,Date bdate,Date edate,int pageno, int pagesize)
	{
		Map<String,Object> ret=new HashMap<String,Object>();
		Config conf=Config.getInstance();
		String key=conf.get("ykt.key");
		String nstr=DF.format(new Date());
		StringBuffer sb=new StringBuffer("stuempno=").append(CommonUtil.isEmpty(uid)?"":uid)
				.append("&payid=").append(CommonUtil.isEmpty(payid)?"":payid)
				.append("&startdate=").append(CommonUtil.isEmpty(bdate)?"":QDF.format(bdate))
				.append("&enddate=").append(CommonUtil.isEmpty(edate)?"":QDF.format(edate))
				.append("&pageno=").append(pageno)
				.append("&pagesize=").append(pagesize)
				.append("&timestamp=").append(nstr)
				.append("&sign=").append(EncodeHelper.hmac(HASH,(CommonUtil.isEmpty(uid)?"":uid)+ (CommonUtil.isEmpty(payid)?"":payid)+(CommonUtil.isEmpty(bdate)?"":QDF.format(bdate))
						+(CommonUtil.isEmpty(edate)?"":QDF.format(edate))+pageno+""+pagesize+nstr
						, key));
		try {
			StringBuffer retstr=CommonUtil.postWebRequest(conf.get("ykt.queryurl"), sb.toString().getBytes("utf-8"),CTYPE );
			Object retobj=JSON.parse(retstr.toString());
			if(retobj instanceof Map)
			{
				ret=(Map<String,Object>)retobj;			
			}else
			{
				ret.put("retcode", -998);
				ret.put("retmsg", "PARSE ERROR:"+retstr);
			}
		}  catch (Exception e) {
			log.error("POST REQ", e );
			ret.put("retcode", -999);
			ret.put("retmsg", e.getMessage());
		}
		
		return ret;
	}
	
	/**
	 * 获取未支付订单
	 * @param uid
	 * @return
	 */
	public static List<Map<String,Object>> unpaid(String uid)
	{
		Map<String,Object> oret=query(uid,null,null,null,1,10);
		if(oret.get("retcode").equals(0))
		{
			Object ords=oret.get("data");
			if(ords instanceof List)
			{
				Iterator<Map<String,Object>> i=((List<Map<String,Object>>)ords).iterator();
				while(i.hasNext())
				{
					Map<String,Object> order=i.next();
					if(order==null)
					{
						i.remove();
					}else
					{
						if(!"待付款".equals(order.get("status")))
							i.remove();
					}
					return (List<Map<String,Object>>)ords;
				}
				
			}
		}
		return new ArrayList<Map<String,Object>>(0);
	}

}
