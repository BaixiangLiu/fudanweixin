package edu.fudan.weixin.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.TACOAuth2Model;
import edu.fudan.weixin.model.WiscomPayModel;

@ParentPackage(value = "servicebase")
@Namespace("/")
public class PayAction extends GuestActionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1752755265539060382L;

	private List binds;
	private String uisid;

	public String execute() throws IOException {
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
						if (binds.size() == 1 || !CommonUtil.isEmpty(uisid)
								&& uisid.equals(uid))
							ServletActionContext.getResponse().sendRedirect(
									WiscomPayModel.formupDirecturl(
											openid.toString(), uid));

					}
				}
			}
		}
		if (binds == null)
			binds = new ArrayList();
		return SUCCESS;
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

}
