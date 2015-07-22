package edu.fudan.weixin.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import edu.fudan.weixin.entity.payment.PaymentRequest;
import edu.fudan.weixin.entity.payment.PaymentResult;

public class PaymentInfoModel {

	private PaymentRequest pr;

	public PaymentRequest getPaymentRequest() {
		return pr;
	}

	public void setPaymentRequest(PaymentRequest pr) {
		this.pr = pr;
	}

	public PaymentInfoModel(PaymentRequest pi) {
		this.pr = pi;
	}

	public String createURL() throws UnsupportedEncodingException {
		String[] signParameters = pr.getSignParameters();
		String[] otherParameters = pr.getOtherParameters();
		HashMap<String, Object> values = (HashMap<String, Object>) pr
				.getValues();

		StringBuffer params = new StringBuffer("sign=");
		params.append(pr.getSign());
		for (int i = 0; i < signParameters.length; i++) {
			Object value = values.get(signParameters[i]);
			if (value != null) {
				params.append("&");
				params.append(signParameters[i]);
				params.append("=");
				params.append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
			}
		}
		for (int i = 0; i < otherParameters.length; i++) {
			Object value = values.get(otherParameters[i]);
			if (value != null) {
				params.append("&");
				params.append(otherParameters[i]);
				params.append("=");
				params.append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
			}
		}
		return params.toString();
	}

	/**
	 * @param get
	 *            Http Request 模式，true 为 GET，false 为 POST
	 * @param url
	 *            String 形式的连接地址
	 */
	public PaymentResult getResponse(boolean get, String url) throws Exception {

		String urlParameters = this.createURL();
		if (pr.getClass().getName()
				.equals("edu.fudan.weixin.entity.payment.QueryDeal"))
			get = false;

		URL destURL;
		if (get) {
			destURL = new URL(url.concat("?").concat(urlParameters));
		} else {
			destURL = new URL(url);
		}

		HttpURLConnection con = (HttpURLConnection) destURL.openConnection();

		// add request header
		con.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		// optional default is GET
		if (get) {
			con.setRequestMethod("GET");
		} else {
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
		}

		int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		String singleLine = "";
		List<Map> response = new ArrayList<Map>();
		while ((inputLine = in.readLine()) != null) {
			if(inputLine.startsWith("{"))
				response.add((Map) JSON.parse(inputLine));
			else
				singleLine = inputLine;
		}
		in.close();

		if (pr.getReturnType().equalsIgnoreCase("data"))
			return resolveDataResponse(response);
		else
			return resolveSimpleResponse(singleLine);

	}

	private PaymentResult resolveSimpleResponse(String result) {
		PaymentResult pr = new PaymentResult(true);
		pr.setResultCode(result);
		return pr;
	}

	private PaymentResult resolveDataResponse(List result) {
		PaymentResult pr = new PaymentResult(false);
		/*
		 * System.out.println(sb.toString()); String[] results = new
		 * String(sb).split("\n"); for(int i=0; i < results.length; i++ ) { if(i
		 * == 0) pr.setResultSummary((Map)JSON.parse(results[i])); else
		 * pr.addResultDetail((Map)JSON.parse(results[i])); }
		 * System.out.println(sb.toString()); Object result = JSON.parse(new
		 * String(sb));
		 */

		pr.setResultSummary((Map) result.get(0));

		for (int i = 1; i < result.size(); i++)
			pr.addResultDetail((Map) result.get(i));

		pr.setResultCode(pr.getCodeFromSummary());
		return pr;
	}

}
