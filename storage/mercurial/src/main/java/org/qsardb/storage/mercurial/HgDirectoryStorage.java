/*
 * Copyright (c) 2011 University of Tartu
 */
package org.qsardb.storage.mercurial;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.qsardb.storage.directory.*;

public class HgDirectoryStorage extends DirectoryStorage {

	public HgDirectoryStorage(File dir) throws IOException {
		this(dir, false);
	}

	public HgDirectoryStorage(File dir, boolean initialize) throws IOException {
		super(dir);

		File hgDir = new File(dir, ".hg");

		if(!hgDir.isDirectory()){

			if(initialize){
				execute("init");

				return;
			}

			throw new IOException("Path " + dir + " does not denote an existing Mercurial directory");
		}
	}

	@Override
	public void add(String path) throws IOException {
		super.add(path);

		execute("add", path);
	}

	@Override
	public void remove(String path) throws IOException {
		super.remove(path);

		execute("remove", path);
	}

	public String branch() throws IOException {
		HgResult result = execute("branch");

		String output = new String(result.getOutput(), ENCODING);

		return output.trim();
	}

	public List<String> branches() throws IOException {
		List<String> branches = new ArrayList<String>();

		HgResult result = execute("branches");

		String output = new String(result.getOutput(), ENCODING);

		List<String> lines = splitLines(output);
		for(String line : lines){
			Matcher matcher = match("(\\S+)\\s+(.+)", line);

			String branch = matcher.group(1);
			branches.add(branch);
		}

		return branches;
	}

	public void commit(String message) throws IOException {
		execute("commit", "-m", message);
	}

	public List<HgRevision> log() throws IOException {
		return log(null);
	}

	public List<HgRevision> log(String branch) throws IOException {
		List<HgRevision> revisions = new ArrayList<HgRevision>();

		HgResult result;

		if(branch != null){
			result = execute("log", "-b", branch);
		} else

		{
			result = execute("log");
		}

		String output = new String(result.getOutput(), ENCODING);
		if("".equals(output)){
			return revisions;
		}

		List<String> lineBlocks = splitLineBlocks(output);
		for(String lineBlock : lineBlocks){
			Map<String, String> map = parseLineBlock(lineBlock);

			HgRevision revision = new HgRevision();
			revision.setChangeset(map.get("changeset"));
			revision.setBranch(map.get("branch"));
			revision.setSummary(map.get("summary"));

			revisions.add(revision);
		}

		// Oldest (i.e. rev 0) first, newest (i.e. tip) last
		Collections.reverse(revisions);

		return revisions;
	}

	public List<HgStatus> status() throws IOException {
		List<HgStatus> statuses = new ArrayList<HgStatus>();

		HgResult result = execute("status");

		String output = new String(result.getOutput(), ENCODING);
		if("".equals(output)){
			return statuses;
		}

		List<String> lines = splitLines(output);
		for(String line : lines){
			Matcher matcher = match("(\\S+)\\s+(\\S+)", line);

			HgStatus status = new HgStatus();
			status.setCode(HgStatus.Code.forValue(matcher.group(1)));
			status.setPath(matcher.group(2));

			statuses.add(status);
		}

		return statuses;
	}

	public HgResult execute(String... args) throws IOException {
		return HgUtil.execute(getDirectory(), args);
	}

	static
	private Matcher match(String regex, String string){
		Matcher matcher = (Pattern.compile(regex)).matcher(string);

		if(!matcher.matches()){
			throw new IllegalArgumentException();
		}

		return matcher;
	}

	static
	private Map<String, String> parseLineBlock(String string){
		Map<String, String> result = new LinkedHashMap<String, String>();

		List<String> lines = splitLines(string);
		for(String line : lines){
			Matcher matcher = match("(.+)\\:\\s+(.+)", line);

			result.put(matcher.group(1), matcher.group(2));
		}

		return result;
	}

	static
	private List<String> splitLines(String string){
		return Arrays.asList(string.split("\\n"));
	}

	static
	private List<String> splitLineBlocks(String string){
		return Arrays.asList(string.split("\\n\\n"));
	}

	public static final String ENCODING = "US-ASCII";
}