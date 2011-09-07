package com.zluyuer.dt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TextFileUtil {
	
	public static List<String> readLines(String filePath) 
		throws IOException {
		
		return readLines(filePath, "utf-8");
	}

	public static List<String> readLines(String filePath, String encode) 
		throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(filePath)), encode));
		List<String> lines = new ArrayList<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (!line.equals(""))
				lines.add(line);
		}
		reader.close();
		return lines;
	}
}
