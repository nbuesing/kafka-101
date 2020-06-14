package com.buesing.kafka101.consumer;

import com.beust.jcommander.Parameter;
import lombok.Data;

@Data
public class Options {

    @Parameter(names = "--help", help = true, hidden = true)
    private boolean help;

    @Parameter(names = { "-b", "--bootstrap-servers" }, description = "cluster bootstrap servers")
    private String bootstrapServers = "localhost:19092,localhost:29092,localhost:39092";
}
