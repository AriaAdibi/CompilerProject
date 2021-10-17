package codeGenerator;

import java.util.ArrayList;

public class ThreeAddressCode {

	private String				code	= "";
	private ArrayList<String>	labels	= null;
	public String				env		= "";

	public ThreeAddressCode(String code) {
		code = code.replaceAll(" ", "");
		code = code.replaceAll("\t", "");

		if (code.equals("")) return;
		if (code.startsWith("PROC")) {
			this.code = code;
			return;
		}

		String[] parts = code.split(":");

		if (parts.length > 1 || code.contains(":"))
			labels = new ArrayList<String>();

		if (!code.endsWith(":")) this.code = parts[parts.length - 1];

		for (int i = 0; i < parts.length - 1; i++) {
			if (parts[i].contains("ENV")) env = parts[i];
			else labels.add(parts[i]);
		}
	}

	public String getCode() {
		return code;
	}

	public ArrayList<String> getLabels() {
		return labels;
	}
}
