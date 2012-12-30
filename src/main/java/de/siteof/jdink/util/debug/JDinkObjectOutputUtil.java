package de.siteof.jdink.util.debug;

public class JDinkObjectOutputUtil {
	
	private static JDinkObjectOutput objectOutput;
	
	public static void writeObject(String name, Object o) {
		if (objectOutput != null) {
			objectOutput.writeObject(name, o);
		}
	}

	public static JDinkObjectOutput getObjectOutput() {
		return objectOutput;
	}

	public static void setObjectOutput(JDinkObjectOutput objectOutput) {
		JDinkObjectOutputUtil.objectOutput = objectOutput;
	}

}
