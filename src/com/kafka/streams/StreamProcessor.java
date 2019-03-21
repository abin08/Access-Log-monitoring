package com.kafka.streams;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KGroupedTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.json.JSONObject;
import org.json.JSONString;

import com.kafka.pojo.Logs;
import com.kafka.utils.Property;

/**
 * @author Abin K. Antony 20-Mar-2019
 * @version 1.0
 */
public class StreamProcessor {
    private static String inputTopic;
    private static String bootstrapServers;
    private static String outputTopic;


    public static void main(String[] args) {
	inputTopic = Property.getInstance().getProperty("inputTopic");
	bootstrapServers = Property.getInstance()
		.getProperty("bootstrapServers");
	outputTopic = Property.getInstance().getProperty("outputTopic");
	
	Properties streamsConfiguration = new Properties();
	streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG,
		"kafka-stream-poc1");
	streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
		bootstrapServers);
	streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
		Serdes.String().getClass());
	streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
		Serdes.String().getClass());
	final StreamsBuilder builder = new StreamsBuilder();
	KStream<String, String> source = builder.stream(inputTopic);
		
	final KStream<String, String> event = 
		source.map(new KeyValueMapper<String, String, KeyValue<String,String>>() {
		    @Override
		    public KeyValue<String, String> apply(String key, String value) {
			Logs logs = getLogs(value);
			return new KeyValue<String, String>(logs.getIp(), logs.toString());
		    }
		});
	
	event.flatMapValues(value -> Arrays.asList(value.split(" ")))
	     .groupBy((key, value) -> value)
	     .count(Materialized.<String, Long, KeyValueStore<Bytes,byte[]>>as("counts-store"))
	     .toStream()
	     .to(outputTopic, Produced.with(Serdes.String(), Serdes.Long()));
		
	final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);
	streams.cleanUp();
	streams.start();
	Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
	
    }
    
    static Logs getLogs(String text) {
   	List<String> list = Arrays.asList(text.split(" "));
   	Logs logs = new Logs();
   	logs.setIp(list.get(0));
   	logs.setStatus(list.get(list.size()-2));
   	return logs;
       }
}
