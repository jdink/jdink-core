/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.siteof.jdink.loader;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.functions.JDinkExecutionContext;
import de.siteof.jdink.functions.JDinkFunction;
import de.siteof.jdink.model.JDinkContext;

/**
 * <p>Responsible for loading the contents of a ini file</p>
 * <p>Note: unlike other loaders, this loader does execute the corresponding functions</p>
 */
public class JDinkIniLoader extends AbstractLoader {

	private JDinkContext context;

	private static final Log log	= LogFactory.getLog(JDinkIniLoader.class);


	@Override
	protected void load(InputStream in, boolean deferrable) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in), 4096);
		while(true) {
			String line = br.readLine();
			if (line == null) {
				break;
			}
			line = line.trim();
			if ((!line.startsWith(";")) && (!line.startsWith("//"))) {
				processLine(line);
			}
		}
	}

	public void processLine(String line) {
		String[] values = splitLine(line, ' ');
		if (values.length > 0) {
			String functionName = values[0].toUpperCase();
			Object[] arguments = new Object[values.length-1];
			System.arraycopy(values, 1, arguments, 0, arguments.length);
			//log.debug("function: " + functionName);
			JDinkExecutionContext executionContext = new JDinkExecutionContext();
			executionContext.setContext(context);
			executionContext.setFunctionName(functionName);
			executionContext.setArguments(arguments);
			JDinkFunction function = context.getFunction(executionContext.getFunctionName());
			if (function != null) {
				try {
					function.invoke(executionContext);
				} catch (Throwable e) {
					log.error("error processing line:" + line + " - " + e, e);
					System.exit(-1);
				}
			}
		}
	}

	protected static String[] splitLine(String line, char separator) {
		Collection<String> values = new ArrayList<String>();
		int fromIndex = 0;
		while(fromIndex < line.length()) {
			int beginIndex = fromIndex;
			int i = line.indexOf(separator, fromIndex);
			int j = line.indexOf("\"", fromIndex);
			int k = line.indexOf("\'", fromIndex);
			if ((j == fromIndex) || (k == fromIndex)) {
				char ch = line.charAt(fromIndex);
				while(true) {
					i = line.indexOf("\\" + ch, fromIndex+1);
					j = line.indexOf(ch, fromIndex+1);
					if ((j >= 0) && ((j < i) || (i < 0))) {
						fromIndex = j+1;
						break;
					} else if (i >= 0) {
						fromIndex = i+2;
					} else {
						throw new RuntimeException("end quote missing in line: " + line);
					}
				}
				values.add(line.substring(beginIndex, fromIndex));
			} else if (i >= 0) {
				if (i != fromIndex) {
					values.add(line.substring(beginIndex, i));
				}
				fromIndex = i+1;
			} else {
				values.add(line.substring(beginIndex));
				break;
			}
		}
		return (String[]) values.toArray(new String[0]);
	}

	public JDinkContext getContext() {
		return context;
	}
	public void setContext(JDinkContext context) {
		this.context = context;
	}
}
