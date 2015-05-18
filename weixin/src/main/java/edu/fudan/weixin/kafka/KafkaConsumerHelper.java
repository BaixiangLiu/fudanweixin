package edu.fudan.weixin.kafka;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.consumer.Whitelist;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.Decoder;
import kafka.serializer.StringDecoder;
import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.ThreadPoolHelper;

public class KafkaConsumerHelper {

	private static ConsumerConnector consumer;
	private static KafkaConsumerHelper instance;
	Map<String, Set<ConsumeCallback>> callbacks;

	protected KafkaConsumerHelper() {

		

		callbacks = new HashMap<String, Set<ConsumeCallback>>();

		
	}

	public static synchronized KafkaConsumerHelper getInstance() {
		if (instance == null)
			instance = new KafkaConsumerHelper();
		return instance;
	}

	public ConsumerConnector getConsumer() {
		return consumer;
	}

	public void addCallback(ConsumeCallback callback) {
		if (callback == null)
			return;
		String[] topics = callback.getSubscribeTopics();
		for (String tp : topics) {
			Set<ConsumeCallback> cbs = callbacks.get(tp);
			if (cbs == null) {
				cbs = new HashSet<ConsumeCallback>();
			}
			cbs.add(callback);
			callbacks.put(tp, cbs);
		}
	}

	public  synchronized void start() {
		Config conf= Config.getInstance();
		Properties props = new Properties();
		props.put("zookeeper.connect",conf
				.get("kafka.servers"));
		props.put("group.id", conf.get("kafka.groupid"));
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(
				props));
		String topics = "";
		for (String s : callbacks.keySet()) {
			topics += "," + s;
		}
		if (topics.length() > 0) {
			topics = topics.substring(1);
			Decoder<String> sd = new StringDecoder(null);
			List<KafkaStream<String, String>> streams = consumer
					.createMessageStreamsByFilter(
							new Whitelist(topics),
							Integer.parseInt(conf.get(
									"kafka.threads")), sd, sd);
			if (streams != null) {
				ExecutorService tph = ThreadPoolHelper.getInstance()
						.getSchPool();
				for (KafkaStream<String, String> stream : streams) {
					tph.submit(new CallbackThread(callbacks, stream));
				}
			}
		}
	}

	public void stop() {
		  if (consumer != null) consumer.shutdown();
	}

}
