package com.kafkabrasil.streams;


import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class ProcessadorSaldo {
    public static void main(String[] args) {
        criarStreams();
    }

    private static Properties configurarStreams() {
        final Properties streamsConfiguration = new Properties();

        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, System.getenv("APPLICATION_ID"));
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, System.getenv("APPLICATION_ID") + "-client");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("BOOTSTRAP_SERVERS"));
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 500);

        return streamsConfiguration;
    }

    private static void criarStreams() {
        final StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> inputStream = builder.stream(System.getenv("INPUT_TOPIC"));
        KStream<String, String> processedStream = inputStream.mapValues(value -> {
            return value + " - Processed";
        });

        processedStream.to(System.getenv("OUTPUT_TOPIC"));

        final Topology topology = builder.build();

        KafkaStreams streams = new KafkaStreams(topology, configurarStreams());
        streams.start();
    }
}
