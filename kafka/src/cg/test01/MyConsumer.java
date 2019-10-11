package cg.test01;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
public class MyConsumer extends Thread{


	//配置相关信息
	private static Properties createConsumerConfig() {   
		// 连接参数 
		Properties props = new Properties();   
		//props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
		//zookeeper.connect=192.168.3.18:2181,192.168.3.19:2181,192.168.3.60:2181
		props.put("bootstrap.servers", " 192.168.168.95:9092");
//		props.put("bootstrap.servers", "192.168.3.18:2181,192.168.3.19:2181,192.168.3.60:2181");
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "60000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
/*
 * •zookeeper.connect：zookeeper连接服务器地址
 •zookeeper.session.timeout.ms：zookeeper的session过期时间，默认5000ms，用于检测消费者是否挂掉
 •zookeeper.sync.time.ms：当consumer reblance时，重试失败时时间间隔
 •group.id：指定消费组
 •auto.commit.enable：当consumer消费一定量的消息之后,将会自动向zookeeper提交offset信息 ，注意offset信息并不是每消费一次消息就向zk提交一次,而是现在本地保存(内存),并定期提交,默认为true
 •auto.commit.interval.ms：自动更新时间。默认60 * 1000
 •conusmer.id：当前consumer的标识,可以设定,也可以有系统生成,主要用来跟踪消息消费情况,便于观察
 •client.id：消费者客户端编号，用于区分不同客户端，默认客户端程序自动产生
 •queued.max.message.chunks：最大取多少块缓存到消费者(默认10)
 •rebalance.max.retries：当有新的consumer加入到group时,将会reblance,此后将会有partitions的消费端迁移到新 的consumer上,如果一个consumer获得了某个partition的消费权限,那么它将会向zk注册 "Partition Owner registry"节点信息,但是有可能此时旧的consumer尚没有释放此节点, 此值用于控制,注册节点的重试次数。
 •fetch.min.bytes：获取消息的最大尺寸,broker不会像consumer输出大于此值的消息chunk每次feth将得到多条消息,此值为总大小,提升此值,将会消耗更多的consumer端内存
 •fetch.wait.max.ms： 当消息的尺寸不足时,server阻塞的时间,如果超时,消息将立即发送给consumer
 •auto.offset.reset：如果zookeeper没有offset值或offset值超出范围。那么就给个初始的offset。有smallest、largest、 anything可选，分别表示给当前最小的offset、当前最大的offset、抛异常。默认largest
 •derializer.class：指定序列化处理类，默认为kafka.serializer.DefaultDecoder,即byte[]

 */
		return props;   
	}

	  // 自动提交偏移量
    public void simpleConsumer() {
        Properties properties = createConsumerConfig();
        // 消费者  
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
       // consumer.subscribe(Arrays.asList("test"));
        consumer.subscribe(Arrays.asList("providecarenterprisespic"));
        try {
            while (true) {
            	 //consumer.poll();
              //  ConsumerRecords<String, String> records = consumer.poll(1000L);
             //   System.out.println(records);
               /* for (ConsumerRecord<String, String> record : records) {
                	System.out.println(record.value());
                }*/
            }
        } finally {
            //consumer.close();
        }
    }

	public static void main(String[] args) {   
		MyConsumer consumerThread = new MyConsumer();   
		
		consumerThread.simpleConsumer();   
	}  


}
