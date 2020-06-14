package com.buesing.kafka101.consumer;

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

		final Consumer consumer = new Consumer();

		consumer.start(options);
	}

}

