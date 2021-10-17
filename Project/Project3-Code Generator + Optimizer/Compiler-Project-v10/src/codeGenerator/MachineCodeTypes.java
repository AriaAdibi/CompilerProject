package codeGenerator;

import symbols.Type;

public class MachineCodeTypes {

	public static Character getMachineCodeType(Type type) {
		String name = type.getLexeme();
		switch (name) {
			case "int":
			case "float":
			case "boolean":
			case "char":
				return name.charAt(0);
			default:
				return null;
		}
	}
}
