package com.buesing.kafka101.producer;

import com.beust.jcommander.Parameter;
import lombok.Data;

import java.time.Duration;

@Data
public class Options {

    @Parameter(names = "--help", help = true, hidden = true)
    private boolean help;

    @Parameter(names = { "-c", "--count" }, description = "number of calls")
    private int count = 1;

    @Parameter(names = { "-p", "--pause" }, description = "pause between calls, in milliseconds")
    private long pause = 20L;

    @Parameter(names = { "-b", "--bootstrap-servers" }, description = "cluster bootstrap servers")
    private String bootstrapServers = "localhost:19092,localhost:29092,localhost:39092";
}
