package edu.fudan.weixin.subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.eservice.common.utils.ThreadPoolHelper;
/**
 * Singleton container hold the books, the key of books is the uisid, and each entry has a inner hashmap with a key of "openid" and value of openid of wechat.
 * @author wking
 *
 */
public class BooksHolder {

	private static final Map<String, Map<String, Object>> books = new HashMap<String, Map<String, Object>>();
	public static final BooksHolder INSTANCE = new BooksHolder();
	private static ReadWriteLock lock = new ReentrantReadWriteLock();

	protected BooksHolder() {
		super();
		load();
		
		// auto reload every 3 minutes
		ThreadPoolHelper.getInstance().getSchPool()
				.scheduleAtFixedRate(new Runnable() {
					public void run() {
						load();
					}
				}, 3, 3, TimeUnit.MINUTES);
	}

	@SuppressWarnings("unchecked")
	public void load() {
		DBCollection cb = MongoUtil.getInstance().getCollection("Books");
		DBCollection cu = MongoUtil.getInstance().getCollection("Bindings");
		DBCursor cursor = cb.find(new BasicDBObject("book", true));
		lock.writeLock().lock();
		;
		books.clear();
		try {
			while (cursor.hasNext()) {
				DBObject book = cursor.next();
				DBObject user = cu.findOne(new BasicDBObject("openid", book
						.get("openid")));
				if (user != null && user.get("binds") != null) {
					if (user.get("binds") instanceof List) {
						List<DBObject> ls = (List<DBObject>) user.get("binds");
						for (DBObject ob : ls) {
							Object uisid = ob.get("uisid");
							if (!CommonUtil.isEmpty(uisid)) {
								Map<String, Object> bl = books.get(uisid);
								if (bl == null){
									bl = new HashMap<String, Object>();
									bl.put("openid", book.get("openid"));
								}
								bl.put(String.valueOf(book.get("item")),
										book.get("threshold")==null?0:book.get("threshold"));
								books.put(uisid.toString(), bl);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	public Map<String,Object> getBooks(String uid)
	{
		Map<String,Object> ret=null;
		lock.readLock().lock();
		ret= books.get(uid);
		lock.readLock().unlock();
		return ret;
	}
	public String getOpenid(String uid)
	{
		Object o= getItem(uid,"openid");
		return o==null?null:o.toString();
	}
	public Object getItem(String uid,String item)
	{
		Map<String,Object> book=getBooks(uid);
		if(book!=null)
			return book.get(item);
		else
			return null;
	}

}
