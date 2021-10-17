package codeGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class CodeWriter {

	private PrintWriter	writer;
	private String		text;
	private int			pc	= 0;

	public CodeWriter(String fileName) throws IOException {
		writer = new PrintWriter(new FileOutputStream(new File(fileName)),
				true);
		text = "";
	}

	public int write(String code) {
		if (pc != 0) text += "\n";
		text += code;

		return pc++;
	}

	public void replaceLabels(HashMap<String, Integer> codeLocations) {
		for (String label : codeLocations.keySet())
			text = text.replaceAll(label, String.valueOf(codeLocations.get(label)));
	}

	public void print() {
		writer.print(text);
	}

	public void close() {
		writer.close();
	}

}
