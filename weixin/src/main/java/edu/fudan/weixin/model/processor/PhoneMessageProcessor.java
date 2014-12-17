package edu.fudan.weixin.model.processor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.weixin.model.message.JSONMessageBuilder;
import edu.fudan.weixin.utils.AccessTokenHelper;
import edu.fudan.weixin.utils.TACOAuth2Helper;

public class PhoneMessageProcessor extends LongTermProcessor {

	private static final Pattern p=Pattern.compile(
			"^(电话|phone|dh)(.*)?$",
			Pattern.CASE_INSENSITIVE);
	public Map<String, Object> process(Map<String, Object> message) {
		String content = null, msgtype = String.valueOf(message.get("MsgType"));	
			content = String.valueOf(message.get("Content"));

		if (!CommonUtil.isEmpty(content)&&p.matcher(content.trim()).matches()
				|| "event".equalsIgnoreCase(msgtype)
				&& "CLICK"
						.equalsIgnoreCase(String.valueOf(message.get("Event")))
				&& "phoneyp".equalsIgnoreCase(String.valueOf(message
						.get("EventKey")))) 
			return super.process(message);
			else
				return null;
		}
			
	public Map<String, Object> _process(Map<String, Object> message) {
			String qkey=null;
			String 	content = String.valueOf(message.get("Content"));
			if (!CommonUtil.isEmpty(content)){
				Matcher m = p.matcher(content);
				if (m.matches())
					{
				qkey=m.group(2);
					}
			}
			JSONMessageBuilder jb=new JSONMessageBuilder();
			if (CommonUtil.isEmpty(qkey))
			{
				
				jb.setContent("请使用语言或者文字输入 电话、dh或phone开头接着要查询的部门或者号码末位（至少四位）\n如：电话信息办 或 phone3207");
				return jb.getMessage();
			}
			

			Object msg = TACOAuth2Helper.phoneyp(qkey).get("list");
			if(msg==null||msg instanceof BasicDBObject)
			{
				if(msg!=null&&"access_denied".equalsIgnoreCase(((BasicDBObject)msg).getString("error")))
				{
					AccessTokenHelper.getInstance().refetch(AccessTokenHelper.TAC);
					msg = TACOAuth2Helper.phoneyp(qkey).get("list");
						}
			}
			if (msg!=null &&msg instanceof BasicDBList) {
				BasicDBList list = (BasicDBList) msg;
				if(list.size()>0)
				{
				StringBuffer ret=new StringBuffer();
				ret.append("关键字："+qkey);
				for (Object obj : list) {
					if(obj!=null &&obj instanceof BasicDBObject)
					{
						BasicDBObject bo=(BasicDBObject)obj;
						ret.append("\n"+bo.get("departname")+"\n");
						String ph=bo.getString("telephonenumber");
						ret.append(ph);
						String in=bo.getString("internalnumber");
						if(!CommonUtil.isEmpty(in)&&!CommonUtil.eq(ph, in))
						{
							ret.append(" 内线 "+in);
						}
					}
				}
				jb.setContent(ret.toString());
				return jb.getMessage();
				}else
				{
					
					jb.setContent("通过您提供的关键字"+qkey+"我们没有查询到相关的记录");
					return jb.getMessage();
				}
			}else
			{
			
				jb.setContent("TAC服务器没有返回数据，请稍候再试");
				return jb.getMessage();
			}
	}
}
