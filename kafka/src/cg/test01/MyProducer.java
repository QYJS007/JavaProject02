package cg.test01;

import java.util.Properties;

import java.util.concurrent.Executors;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;


import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;


public class MyProducer {

	public static void main(String[] args) {
		Properties props = new Properties();   

		props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
		props.put("acks", "all");
		props.put("retries", 0); // 消息发送请求失败重试次数
		props.put("batch.size", 2000);
		props.put("linger.ms", 1); // 消息逗留在缓冲区的时间，等待更多的消息进入缓冲区一起发送，减少请求发送次数
		props.put("buffer.memory", 33554432); // 内存缓冲区的总量
		// 如果发送到不同分区，并且不想采用默认的Utils.abs(key.hashCode) % numPartitions分区方式，则需要自己自定义分区逻辑
		//  props.put("partitioner.class", "com.snow.kafka.producer.SimplePartitioner");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		// 1、创建生产这对象
		Producer<String, String> producer = new KafkaProducer<String, String>(props);

		
		for(int i=0;i<100;i++){
            ProducerRecord<String,String> r = new ProducerRecord<String,String>("message","key-"+i,"value-"+i);
            producer.send(r);
            System.out.println(r);
        }

        producer.close();


		//2、生成消息     ProducerRecord
		// KeyedMessage<String, String> data = new KeyedMessage<String, String>("mykafka","test-kafka");
		/*int a =0;
		String key = Integer.toString(a++);
		String value = "times: " + key;
		ProducerRecord<String, String> record = new ProducerRecord<String, String>("test",key,value);*/

		/*
		 * try {   
			int i =1; 
			while(i < 100){    
				//3.发送消息
				producer.send(record);   
				//2.同步 RecordMetadata recordMetadata = producer.send(record).get();
				
				//3.y异步
				producer.send(record,new Callback() {
					@Override
					public void onCompletion(RecordMetadata arg0, Exception e) {
						if (e != null) {
							e.printStackTrace();
						}
						
					}
				});
				//System.out.println(recordMetadata);
				
				//Thread.currentThread().sleep(1000);
				System.out.println(record);
				i++;
			} 
		} catch (Exception e) {   
			e.printStackTrace();   
		} 
		 */
	}


	/*public void shoew (){
		Producer<String, String> producer = new KafkaProducer<String, String>(props);
		ProducerRecord<String, String> record = 
				new ProducerRecord<String, String>("CustomerCountry","Precision Products", "France");
		try {
			producer.send(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

}
