package edu.fudan.weixin;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

import org.junit.Test;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.EncodeHelper;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.TACOAuth2Model;
import edu.fudan.weixin.utils.BindingHelper;

public class NewClient {

	
	public void newclient() throws NoSuchAlgorithmException
	{
		String clientid="ecard";
		String pass=EncodeHelper.randpass(8);
		System.out.println("Password:"+pass);
		KeyGenerator kg=KeyGenerator.getInstance("DESede");
		kg.init(168);
		String key=EncodeHelper.bytes2hex(kg.generateKey().getEncoded());
		DBObject o=new BasicDBObject("clientid",clientid).append("password", EncodeHelper.digest(pass,"SHA")).append("enckey", key);
		MongoUtil.getInstance().getDB().getCollection("Clients").save(o);
		
	}
	
	@Test
	public void balance() throws Exception
	{
		String uid="04538";
		String clientid="ecard";
		String pass="4c9cc8d2a1b6cfa7f6019b0c38c9ee537d100861";
		String enckey="cb2c197c4ca21cad80fd4a85cdb0efb33d620d43d9548f7a";
		DB db=MongoUtil.getInstance().getDB();
		DBObject user=db.getCollection("Bindings").findOne(new BasicDBObject("binds",new BasicDBObject("$elemMatch",new BasicDBObject("uisid",uid))));
		BindingHelper.removeOthers(user, uid);
		BasicDBList l=new TACOAuth2Model().yktxx(user);
		Object balance=((DBObject)l.get(0)).get("card_balance")+"Ԫ";
		Object username=((DBObject)l.get(0)).get("username")+" ("+uid+")";
		
		DBObject data=new BasicDBObject("url",Config.getInstance().get("weixin.context")).append("first", "��ֹ2014��9��16��24ʱ���һ��ͨ���������£�").append("name", username).append("money", balance).append("remark", "������ʱ��ͨ��һ��ͨ��վ��Ȧ������������ֵ��");
		long now=System.currentTimeMillis();
		String userenc=EncodeHelper.bytes2hex(EncodeHelper.encrypt("DESede", (pass+now).getBytes(),EncodeHelper.hex2bytes(enckey),null));
		DBObject head=new BasicDBObject("template","ecard_balance").append("touser", uid).append("timestamp", now).append("clientid", clientid)
				.append("userenc", userenc)
				.append("checksum",EncodeHelper.digest(JSON.serialize(data).replaceAll("[ \\r\\n\\t]", "")+uid+userenc, "SHA"));
		System.out.println(JSON.serialize(head).replaceAll("[ \\r\\n]", ""));
		System.out.println(CommonUtil.postWebRequest(Config.getInstance().get("weixin.context")+"msgpush.act", JSON.serialize(new BasicDBObject("head",head).append("data", data)).getBytes("utf-8"), "application/json"));

		
	}

	public void getst() throws Exception
	{
		String clientid="ecard";
		String pass="4c9cc8d2a1b6cfa7f6019b0c38c9ee537d100861";
		String enckey="cb2c197c4ca21cad80fd4a85cdb0efb33d620d43d9548f7a";
		long msgid=200487780;
		long now=System.currentTimeMillis();
		String userenc=EncodeHelper.bytes2hex(EncodeHelper.encrypt("DESede", (pass+now).getBytes(),EncodeHelper.hex2bytes(enckey),null));
		System.out.println(CommonUtil.getWebContent(Config.getInstance().get("weixin.context")+"pushresult.act?clientid="+clientid+"&userenc="+userenc+"&timestamp="+now+"&msgid="+msgid));
	}
}
