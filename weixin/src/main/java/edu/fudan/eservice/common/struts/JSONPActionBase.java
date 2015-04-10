package edu.fudan.eservice.common.struts;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import edu.fudan.eservice.common.utils.CommonUtil;

@SuppressWarnings("serial")
public class JSONPActionBase extends GuestActionBase {
	private String callback = null;

	private static Logger log = LoggerFactory.getLogger(JSONPActionBase.class);

	protected List<Map<String, Object>> rs2map(final ResultSet rs) {

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		if (rs != null) {
			try {
				ResultSetMetaData meta = rs.getMetaData();
				int cols = meta.getColumnCount();
				String[] colnames = new String[cols];
				for (int i = 1; i <= cols; i++) {
					colnames[i - 1] = meta.getColumnName(i);
				}
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					for (int i = 1; i <= cols; i++) {
						String value = rs.getString(i);
						if (value != null)
							value = value.trim();
						else
							value = "";
						map.put(colnames[i - 1].toLowerCase(), value);
					}
					ret.add(map);
				}
			} catch (SQLException e) {
				log.error("Parse Resultset", e);
			}
		}
		return ret;
	}

	protected void jsonp(Object obj) {
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().setContentType(
				CommonUtil.isEmpty(callback) ? "application/json"
						: "application/javascript");
		try {
			String result =JSON.toJSONString(
					obj, SerializerFeature.WriteMapNullValue);
			savelog(result);
			org.apache.struts2.ServletActionContext.getResponse().getWriter()
					.print( CommonUtil.isEmpty(callback) ? result: (callback
							+ "("+result+")"));
		} catch (IOException e) {
			log.error("", e);
		}
	}

	protected void savelog(String result) {
		
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

}
