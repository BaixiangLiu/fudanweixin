package edu.fudan.eservice.common.utils;




import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
/**
 * 获取MongoDB的操作对象，MongoDB的Java Driver自带连接池，此单件对象用于返回操作用的MongoClient或者认证过的DB对象
 * <pre>
 *  //获取MongoClient对象
 * 	MongoClient client=MongoUtil.getInstance().getClient();
 *  //获取配置文件config.properties中默认的mongo.db并使用mongo.user/mongo.pwd认证
 *  DB db=MongoUtil.getInstance().getDB();
 * @author wking
 *
 */
public class MongoUtil {
	
	private static MongoUtil mu=null;
	private static Log log=LogFactory.getLog(MongoUtil.class);
	private MongoClient client;
	private Properties props;
	protected MongoUtil() {
		props=new Properties();
		try {
			props.load(this.getClass().getResourceAsStream("/dbconnect.properties"));
			String user=props.getProperty("mongo.user");
			if(!CommonUtil.isEmpty(user))
				client=new MongoClient(new ServerAddress(props.getProperty("mongo.ip","localhost"),Integer.parseInt(props.getProperty("mongo.port","27017"))),
						Arrays.asList(MongoCredential.createCredential(user, props.getProperty("mongo.db"), props.getProperty("mongo.pwd").toCharArray())));

			else
			client=new MongoClient(new ServerAddress(String.valueOf(props.get("mongo.ip")),Integer.parseInt(String.valueOf(props.get("mongo.port")))));
		
		} catch (Exception e) {
			log.error("MongoDB Connect Fail");
		}
		
	}
	/**
	 * 单件模式获取MongoUtil实例
	 * @return
	 */
	 
	public static synchronized MongoUtil getInstance()
	{
		if(mu==null)
		{
			mu=new MongoUtil();
		}
		return mu;
	}
	/**
	 * 获取MongoClient
	 * @return
	 */
	public MongoClient getClient()
	{
		return client;
	}
	/**
	 * 获取默认的DB对象并完成认证
	 * @return
	 */
	public DB getDB()
	{
		
		DB ret= client.getDB(String.valueOf(props.get("mongo.db")));
			return ret;
	}

	/**
	 * 
	* @Title: getCollection
	* @Description: 获取一个集合
	* @param collectionName
	* @return
	 */
	public DBCollection getCollection(String collectionName) {
		return getDB().getCollection(collectionName);
	}
}
