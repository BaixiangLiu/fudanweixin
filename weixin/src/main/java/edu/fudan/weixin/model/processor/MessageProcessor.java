package edu.fudan.weixin.model.processor;

import java.util.Map;


/**
 * 自动处理消息的接口
 * @author wking
 *
 */

public interface MessageProcessor {
	public Map<String,Object> process(Map<String,Object> message);
}
