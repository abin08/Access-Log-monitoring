package com.kafka.streams;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;

import com.kafka.models.ApiLogs;
import com.kafka.utils.Property;

/**
 * @author Abin K. Antony 20-Mar-2019
 * @version 1.0
 */
public class StreamProcessor {
    public static void main(String[] args) {
	String inputTopic = Property.getInstance().getProperty("inputTopic");
	String bootstrapServers = Property.getInstance()
		.getProperty("bootstrapServers");

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
	KStream<String, String> input = builder.stream(inputTopic);

	// total count
	input.map(
		new KeyValueMapper<String, String, KeyValue<String, String>>() {
		    @Override
		    public KeyValue<String, String> apply(String key,
			    String value) {
			return new KeyValue<String, String>("total", value);
		    }
		}).groupByKey().count().toStream().to("totalCount",
			Produced.with(Serdes.String(), Serdes.Long()));

	// total count using aggregate
	KStream<String, Long> total = input.map(
		new KeyValueMapper<String, String, KeyValue<String, String>>() {
		    @Override
		    public KeyValue<String, String> apply(String key,
			    String value) {
			return new KeyValue<String, String>("total", value);
		    }
		}).groupByKey(Serialized.with(Serdes.String(), Serdes.String()))
		.aggregate(new Initializer<Long>() {
		    @Override
		    public Long apply() {
			// TODO Auto-generated method stub
			return 0L;
		    }
		}, new Aggregator<String, String, Long>() {
		    @Override
		    public Long apply(String key, String value,
			    Long aggregate) {
			return aggregate+1;
		    }
		}, Materialized.with(Serdes.String(), Serdes.Long())).toStream();
	total.to("totalCount", Produced.with(Serdes.String(), Serdes.Long()));

	KStream<String, ApiLogs> sourceStream = input.map(
		new KeyValueMapper<String, String, KeyValue<String, ApiLogs>>() {
		    @Override
		    public KeyValue<String, ApiLogs> apply(String key,
			    String value) {
			ApiLogs apiLogs = new ApiLogs(value);
			return new KeyValue<String, ApiLogs>(apiLogs.getIp(),
				apiLogs);
		    }
		});

	// find the count of each ip
	sourceStream.mapValues(value -> value.toString())
		.groupByKey()
		.count()
		.toStream()
		.to("uniqueIpCounts", Produced.with(Serdes.String(), Serdes.Long()));
	
	// sum of bytes
	sourceStream.mapValues(value -> value.getBytes())
	.groupByKey(Serialized.with(Serdes.String(), Serdes.Long()))
	.aggregate(new Initializer<Long>() {
	    @Override
	    public Long apply() {
		return 0L;
	    }
	}, new Aggregator<String, Long, Long>() {
	    @Override
	    public Long apply(String key, Long value,
		    Long aggregate) {
		return aggregate+ value;
	    }
	}, Materialized.with(Serdes.String(), Serdes.Long()))
	.toStream()
	.to("ipBytesSum", Produced.with(Serdes.String(), Serdes.Long()));
	
	
	// find the count of status in each ip address
	KStream<String, String> ip_status_stream = sourceStream.map(
		new KeyValueMapper<String, ApiLogs, KeyValue<String, String>>() {
		    @Override
		    public KeyValue<String, String> apply(String key,
			    ApiLogs value) {
			return new KeyValue<String, String>(
				value.getIp() + "_" + value.getStatus(),
				value.toString());
		    }
		});
	ip_status_stream.groupByKey().count().toStream().to("ipStatusCount",
		Produced.with(Serdes.String(), Serdes.Long()));

	// in each hour from each ip how much came
	KStream<String, String> hours_stream = sourceStream.map(
		new KeyValueMapper<String, ApiLogs, KeyValue<String, String>>() {
		    @Override
		    public KeyValue<String, String> apply(String key,
			    ApiLogs value) {
			return new KeyValue<String, String>(
				value.getTimeStamp().substring(0, 15) + "_"
					+ value.getIp(),
				value.toString());
		    }
		});
	hours_stream.groupByKey().count().toStream().to("hoursCount",
		Produced.with(Serdes.String(), Serdes.Long()));
	
	KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);
	streams.cleanUp();
	streams.start();
	Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
