package edu.fudan.weixin.resource.actions;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.TACOAuth2Model;
import edu.fudan.weixin.utils.BindingHelper;
import edu.fudan.weixin.utils.OperateResult;

@ParentPackage("servicebase")
@Namespace("/resource")
@Results({ @Result(type = "json") })
public class LessonAction extends GuestActionBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3863222316997029044L;
	private OperateResult result;
	private DBObject list;
	private String uisid;
	private String term = "";

	@Action("lesson")
	public String execute() {
		Object openid = getSession().get("openid");
		if (CommonUtil.isEmpty(openid)) {
			result = OperateResult.NOPRG;

		} else {
			DBCollection c = MongoUtil.getInstance().getDB()
					.getCollection("Bindings");
			DBObject obj = c.findOne(new BasicDBObject("openid", openid));
		
			if (!CommonUtil.isEmpty(obj)
					&& !CommonUtil.isEmpty(obj.get("binds"))) {
				BindingHelper.removeOthers(obj, uisid);
				TACOAuth2Model m = new TACOAuth2Model();
				list = m.lesson(obj, term);
				result = OperateResult.OK;
			} else {
				result = OperateResult.NOTBIND;
			}
		}
		return SUCCESS;
	}

	public OperateResult getResult() {
		return result;
	}

	public void setResult(OperateResult result) {
		this.result = result;
	}

	public DBObject getList() {
		return list;
	}

	public void setList(DBObject list) {
		this.list = list;
	}

	public String getUisid() {
		return uisid;
	}

	public void setUisid(String uisid) {
		this.uisid = uisid;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

}
