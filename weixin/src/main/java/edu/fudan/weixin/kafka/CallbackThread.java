package edu.fudan.weixin.kafka;

import java.util.Map;
import java.util.Set;

import edu.fudan.eservice.common.utils.ThreadPoolHelper;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

public class CallbackThread implements Runnable {

	Map<String, Set<ConsumeCallback>> callbacks;
	private KafkaStream<String, String> records;

	@Override
	public void run() {
		ConsumerIterator<String, String> i = records.iterator();
		while (i.hasNext()) {
			final MessageAndMetadata<String, String> mm = i.next();
			String topic = mm.topic();
			final Set<ConsumeCallback> cbs = callbacks.get(topic);
			if (cbs != null) {
				ThreadPoolHelper.getInstance().getSchPool()
						.submit(new Thread() {
							public void run() {
								for (ConsumeCallback cb : cbs)
									cb.process(mm);
							}
						});
			}
		}
	}

	public CallbackThread( final Map<String, Set<ConsumeCallback>> callbacks,
			KafkaStream<String, String> records) {
		super();
		this.callbacks = callbacks;
		this.records = records;
	}

}
