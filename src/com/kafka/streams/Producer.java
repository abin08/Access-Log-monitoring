/**
 * 
 */
package com.kafka.streams;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Abin K. Antony
 * 21-Mar-2019
 * @version 1.0
 * Copyright (c) 2019 Torry Harris Business Solutions, Inc. All Rights Reserved.
 */
public class Producer {
    public static void main(String[] args) {
	Logger logger = LoggerFactory.getLogger(Producer.class);
	String bootstrapServers = "172.30.66.108:9092";
	String topic = "logscount";
	String message = "hello world";
	Properties properties = new Properties();
	properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
	properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
	properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
	//create the producer
	KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
	//create a producer record
	ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, message);
	
	//send data - asynchronous
	producer.send(record, new Callback() {
	    @Override
	    public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
		// executes every time a record is successfully send or an exception is thrown
		if(exception == null) {
		    //record was successfully sent
		    logger.info("Received new metadata.\n" +
			    	"Topic: " + recordMetadata.topic() + "\n" +
			    	"Partition: " + recordMetadata.partition() + "\n" +
			    	"Offset: " + recordMetadata.offset() + "\n" +
			    	"Timestamp: " + recordMetadata.timestamp());
		} else
		    logger.error("Error while producing", exception);
	    }
	});
	//flush data
	producer.flush();
	//flush and close producer
	producer.close();
    }
}
