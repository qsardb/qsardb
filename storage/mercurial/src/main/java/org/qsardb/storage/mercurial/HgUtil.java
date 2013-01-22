/*
 * Copyright (c) 2011 University of Tartu
 */
package org.qsardb.storage.mercurial;

import java.io.*;

import org.apache.commons.exec.*;

public class HgUtil {

	private HgUtil(){
	}

	static
	public HgResult execute(File dir, String... args) throws IOException {
		Executor executor = new DefaultExecutor();
		executor.setWorkingDirectory(dir);

		CommandLine command = new CommandLine("hg");
		command.addArguments(args, true);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ByteArrayOutputStream error = new ByteArrayOutputStream();

		PumpStreamHandler streamHandler = new PumpStreamHandler(output, error);
		executor.setStreamHandler(streamHandler);

		int result = executor.execute(command);
		if(executor.isFailure(result)){
			throw new IOException("Execution failed with code " + String.valueOf(result));
		}

		return new HgResult(output.toByteArray(), error.toByteArray());
	}
}