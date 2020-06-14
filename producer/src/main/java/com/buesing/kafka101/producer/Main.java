package com.buesing.kafka101.producer;

import com.beust.jcommander.JCommander;

public class Main {

	public static void main(String[] args) {

		Options options = new Options();

		JCommander jCommander = JCommander.newBuilder()
				.addObject(options)
				.build();

		jCommander.parse(args);

		if (options.isHelp()) {
			jCommander.usage();
			return;
		}

		final Producer producer = new Producer();

		producer.start(options);
	}

}

