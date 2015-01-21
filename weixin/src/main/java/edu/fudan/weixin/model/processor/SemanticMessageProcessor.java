/**
* @Title: SemanticMessageProcessor.java
* @Description: TODO
* @author: Calvinyang
* @date: Dec 22, 2014 4:37:50 PM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.weixin.model.processor;

import java.util.Map;

import org.fnlp.app.keyword.AbstractExtractor;
import org.fnlp.app.keyword.WordExtract;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;
import edu.fudan.util.exception.LoadModelException;
import edu.fudan.weixin.model.message.JSONMessageBuilder;
import edu.fudan.weixin.model.message.NewsMessageBuilder;
import edu.fudan.weixin.utils.BstSearchHelper;

/**
 * @author: Calvinyang
 * @Description: 无法处理的文本消息处理
 * @date: Dec 22, 2014 4:37:50 PM
 */
public class SemanticMessageProcessor extends LongTermProcessor {

	@Override
	public Map<String, Object> process(Map<String, Object> message) {
		String content = String.valueOf(message.get("Content"));
		if (!CommonUtil.isEmpty(content)) {
			return super.process(message);
		}
		return null;
	}

	@Override
	public JSONMessageBuilder _process(Map<String, Object> msg) {
		String content = String.valueOf(msg.get("Content"));
		// 调用复旦关键词提取
		StringBuffer sb = new StringBuffer();
		try {
			ClassLoader cl = SemanticMessageProcessor.class.getClassLoader();
			StopWords sw = new StopWords(cl.getResource("models/stopwords").getFile());
			CWSTagger tagger = new CWSTagger(cl.getResource("models/seg.m").getPath());
			AbstractExtractor extractor = new WordExtract(tagger, sw);
			Map<String, Integer> map = extractor.extract(content, 3);
			for(String key : map.keySet()) {
				sb.append(key).append(" ");
			}
		} catch (LoadModelException e) {
			e.printStackTrace();
		}
		if (sb.length() == 0) {
			sb.append(content);
		}
		NewsMessageBuilder builder = BstSearchHelper.getNewsForKeywords(sb.toString().trim());
		return builder == null || builder.getCount() == 1 ? null : (JSONMessageBuilder)builder;
	}
	
}
