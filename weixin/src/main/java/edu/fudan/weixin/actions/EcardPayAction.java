package edu.fudan.weixin.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.SWEcardModel;
import edu.fudan.weixin.model.TACOAuth2Model;
import edu.fudan.weixin.model.WiscomPayModel;

@ParentPackage(value = "servicebase")
@Namespace("/")
public class EcardPayAction extends GuestActionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1752755265539060382L;

	private List binds;
	private String uisid;
	private int amount;

	/**
	 * 一卡通充值入口界面预处理
	 * @return
	 * @throws IOException
	 */
	@Action("ecardpre")
	public String prepaid() throws IOException {
		Object openid = getSession().get("openid");
		if (!CommonUtil.isEmpty(openid)) {
			DBObject user = MongoUtil.getInstance().getDB()
					.getCollection("Bindings")
					.findOne(new BasicDBObject("openid", openid));
			binds = new TACOAuth2Model().fetchUserinfo(user);
			if (binds != null && binds.size() > 0) {
				for (Object b : binds) {
					if (b instanceof Map) {
						Map bm = (Map) b;
						String uid = String.valueOf(bm.get("user_id"));
						bm.put("unpaid", SWEcardModel.unpaid(uid));
					}
				}
			}
		}
		if (binds == null)
			binds = new ArrayList();
		return SUCCESS;
	}

	/**
	 * 生成充值订单
	 * @return
	 * @throws IOException
	 */
	@Action("ecardpay")
	public String pay() throws IOException {
		Object openid = getSession().get("openid");
		HttpServletResponse resp = org.apache.struts2.ServletActionContext
				.getResponse();
		Map<String, Object> ret = null;
		if (!CommonUtil.isEmpty(openid)) {
			DBObject user = MongoUtil.getInstance().getDB()
					.getCollection("Bindings")
					.findOne(new BasicDBObject("openid", openid));
			binds = new TACOAuth2Model().fetchUserinfo(user);
			if (binds != null && binds.size() > 0) {
				boolean found = false;
				for (Object b : binds) {
					if (b instanceof Map) {
						Map bm = (Map) b;
						String uid = String.valueOf(bm.get("user_id"));
						if (CommonUtil.eq(uid, uisid)) {
							found = true;
							ret = SWEcardModel.order(String.valueOf(openid),uid, amount);
						}

					}
				}
				if (!found)
					addActionError("未完成对" + uisid + "的基本信息访问授权");

			} else {
				addActionError(" 尚未对任何账号授权");
			}

		} else {
			addActionError("尚未登录");
		}
		if (ret == null) {
			ret = new HashMap<String, Object>();
			ret.put("retcode", -500);
			ret.put("retmsg", getActionErrors());
		}
		resp.setCharacterEncoding("utf-8");
		JSON.writeJSONStringTo(ret, resp.getWriter());

		return NONE;
	}

	public List getBinds() {
		return binds;
	}

	public void setBinds(List binds) {
		this.binds = binds;
	}

	public String getUisid() {
		return uisid;
	}

	public void setUisid(String uisid) {
		this.uisid = uisid;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
