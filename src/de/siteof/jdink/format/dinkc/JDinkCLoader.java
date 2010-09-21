package de.siteof.jdink.format.dinkc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.siteof.jdink.io.Resource;
import de.siteof.jdink.loader.AbstractLoader;
import de.siteof.jdink.model.JDinkContext;
import de.siteof.jdink.script.JDinkDefineLabelFunctionCall;
import de.siteof.jdink.script.JDinkGotoLabelFunctionCall;
import de.siteof.jdink.script.JDinkScriptBlock;
import de.siteof.jdink.script.JDinkScriptCall;
import de.siteof.jdink.script.JDinkScriptFile;
import de.siteof.jdink.script.JDinkScriptFunction;
import de.siteof.jdink.script.JDinkScriptFunctionCall;
import de.siteof.jdink.script.JDinkScriptIfFunctionCall;
import de.siteof.jdink.script.JDinkScriptInstanceVariableDeclarationFunctionCall;
import de.siteof.jdink.script.JDinkScriptModificatorFunctionCall;
import de.siteof.jdink.script.JDinkScriptOperatorFunctionCall;
import de.siteof.jdink.script.JDinkScriptReturnFunctionCall;
import de.siteof.jdink.script.JDinkScriptSetModificatorFunctionCall;
import de.siteof.jdink.script.JDinkScriptSetOperatorFunctionCall;
import de.siteof.jdink.script.JDinkScriptVariableGetFunctionCall;
import de.siteof.jdink.script.JDinkScriptVariableSetFunctionCall;

/**
 * <p>Loads DinkC script files</p>
 */
public class JDinkCLoader extends AbstractLoader {

	protected static class ParseContext {

		private static final Log log	= LogFactory.getLog(JDinkCLoader.class);

		private int state = STATE_NONE;
		private JDinkScriptBlock block;
		private JDinkScriptFunction definitionFunction;
		private JDinkScriptFunctionCall call;


		public int getState() {
			return state;
		}
		public void setState(int currentState) {
			this.state = currentState;
		}
		public JDinkScriptFunction getDefinitionFunction() {
			return definitionFunction;
		}
		public void setDefinitionFunction(JDinkScriptFunction definitionFunction) {
			this.definitionFunction = definitionFunction;
		}
		public JDinkScriptFunctionCall getCall() {
			return call;
		}
		public void setCall(JDinkScriptFunctionCall call) {
			if (this.call != null) {
				log.warn("warning: discarding previous call: " + this.call + " (new call: " + call + ")");
			}
			this.call = call;
		}
		public JDinkScriptBlock getBlock() {
			return block;
		}
		public void setBlock(JDinkScriptBlock block) {
			this.block = block;
		}
	}

	public static class SyntaxException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		private final String text;
		private final String line;
		private final int lineNo;
		private final int column;
		private final String token;


		public SyntaxException(String text, String line, int lineNo, int column, String token) {
			super(text + " [" + lineNo + ":" + column + "]:" + line + " (token:" + token + ")");
			this.text = text;
			this.line = line;
			this.lineNo = lineNo;
			this.column = column;
			this.token = token;
		}
		public int getColumn() {
			return column;
		}
		public String getLine() {
			return line;
		}
		public int getLineNo() {
			return lineNo;
		}
		public String getText() {
			return text;
		}
		public String getToken() {
			return token;
		}
	}

	public static final int STATE_NONE = 0;
	public static final int STATE_DEFINITION_FUNCTION_NAME = 1;
	public static final int STATE_DEFINITION_FUNCTION_ARGUMENTS_BEGIN = 2;
	public static final int STATE_DEFINITION_FUNCTION_ARGUMENTS_BODY = 3;
	public static final int STATE_DEFINITION_FUNCTION_ARGUMENTS_END = 4;
	public static final int STATE_DEFINITION_FUNCTION_BODY_BEGIN = 21;
	public static final int STATE_DEFINITION_FUNCTION_BODY = 22;
	public static final int STATE_BLOCK_BODY = 23;
	public static final int STATE_DEFINITION_FUNCTION_BODY_STEP2 = 24;
	public static final int STATE_CALL_FUNCTION_ARGUMENTS_BEGIN = 31;
	public static final int STATE_CALL_FUNCTION_ARGUMENTS_BODY = 32;
	public static final int STATE_CALL_FUNCTION_ARGUMENTS_END = 33;
	public static final int STATE_CALL_FUNCTION_ARGUMENTS_STRING = 34;
	public static final int STATE_CALL_FUNCTION_ARGUMENTS_SEPARATOR = 35;
	public static final int STATE_CALL_VARIABLE_SET_VALUE = 41;
	public static final int STATE_CALL_OPERATOR_VALUE = 42;
	public static final int STATE_CALL_MODIFICATOR_VALUE = 43;
	public static final int STATE_CALL_RETURN_VALUE = 44;
	public static final int STATE_IF_THEN_STATEMENT_BODY = 45;
	public static final int STATE_IF_THEN_STATEMENT_BODY_STEP2 = 46;
	public static final int STATE_IF_ELSE_OR_END = 47;
	public static final int STATE_CALL_END = 36;
	public static final int STATE_CALL_END_OR_SET_VALUE = 37;
	public static final int STATE_CHOICE_BODY = 51;

	private static final String IF_TOKEN = "if";
	private static final String ELSE_TOKEN = "else";
	private static final String GOTO_TOKEN = "goto";
	private static final String STATEMENT_END_TOKEN = ";";
	private static final String LABEL_DEFINE_TOKEN = ":";
	private static final String ROUND_BRAKET_BEGIN_TOKEN = "(";
	private static final String ROUND_BRAKET_END_TOKEN = ")";
	private static final String ARGUMENT_SEPARATOR_TOKEN = ",";

	private static final String NEGATIVE_NUMBER_PREFIX = "-";

	private static final String QUOTE_BEGIN_STRING = "\"";
	private static final String QUOTE_END_STRING = "\"";

	private JDinkContext context;
	private final String separatingChars;
	private final String operatorChars;

	private ParseContext currentParseContext;
	private final Stack<ParseContext> parseContextStack;

	private String previousToken;
	private StringBuffer tempString;
	private int lineNo = 0;
	private String currentLine;
	private int currentColumn;
	private boolean relaxedParsing = true;

	private final JDinkScriptFile scriptFile;
	private final Map<String, Integer> operatorMap;
	private final Map<String, Integer> modificatorMap;
	private final Map<String, Integer> prefixSetModificatorMap;
	private final Map<String, Integer> postfixSetModificatorMap;
	private final Map<String, Integer> setOperatorMap;

	private JDinkScriptFunctionCall choiceStartScriptFunctionCall;
	private Collection<Object> choiceArguments;
	private String pushedBackToken;

	private static final Log log = LogFactory.getLog(JDinkCLoader.class);

	public JDinkCLoader() {
		separatingChars = " (){}[]<>\"',!:;=+-";
		operatorChars = "!=+-<>";
		currentParseContext = new ParseContext();
		scriptFile = new JDinkScriptFile();
		parseContextStack = new Stack<ParseContext>();
		operatorMap = new HashMap<String, Integer>();
		operatorMap.put("==", new Integer(JDinkScriptOperatorFunctionCall.OP_EQUALS));
		operatorMap.put("!=", new Integer(JDinkScriptOperatorFunctionCall.OP_NOT_EQUALS));
		operatorMap.put("<", new Integer(JDinkScriptOperatorFunctionCall.OP_LESS_THAN));
		operatorMap.put(">", new Integer(JDinkScriptOperatorFunctionCall.OP_GREATER_THAN));
		operatorMap.put("<=", new Integer(JDinkScriptOperatorFunctionCall.OP_LESS_THAN_OR_EQUALS));
		operatorMap.put(">=", new Integer(JDinkScriptOperatorFunctionCall.OP_GREATER_THAN_OR_EQUALS));
		operatorMap.put("+", new Integer(JDinkScriptOperatorFunctionCall.OP_ADD));
		operatorMap.put("-", new Integer(JDinkScriptOperatorFunctionCall.OP_SUBTRACT));
		setOperatorMap = new HashMap<String, Integer>();
		setOperatorMap.put("+=", new Integer(JDinkScriptOperatorFunctionCall.OP_ADD));
		setOperatorMap.put("-=", new Integer(JDinkScriptOperatorFunctionCall.OP_SUBTRACT));
		modificatorMap = new HashMap<String, Integer>();
		modificatorMap.put("-", new Integer(JDinkScriptModificatorFunctionCall.OP_NEG));
		modificatorMap.put("!", new Integer(JDinkScriptModificatorFunctionCall.OP_NOT));
		prefixSetModificatorMap = new HashMap<String, Integer>();
		prefixSetModificatorMap.put("++", new Integer(JDinkScriptSetModificatorFunctionCall.OP_INCREMENT_AND_GET));
		prefixSetModificatorMap.put("--", new Integer(JDinkScriptSetModificatorFunctionCall.OP_DECREMENT_AND_GET));
		postfixSetModificatorMap = new HashMap<String, Integer>();
		postfixSetModificatorMap.put("++", new Integer(JDinkScriptSetModificatorFunctionCall.OP_GET_AND_INCREMENT));
		postfixSetModificatorMap.put("--", new Integer(JDinkScriptSetModificatorFunctionCall.OP_GET_AND_DECREMENT));
	}

	protected void showSyntaxException(SyntaxException e) {
		StringBuffer sb = new StringBuffer();
		sb.append("syntax error: ");
		sb.append(e.getText());
		sb.append("\n");
		sb.append("file: ");
		sb.append(scriptFile.getFileName());
		sb.append(" (line: ");
		sb.append(e.getLineNo());
		sb.append("  column: ");
		sb.append(e.getColumn());
		sb.append(")\n");
		sb.append(e.getLine());
		sb.append("\n");
		for (int i = 1; i < e.getColumn(); i++) {
			sb.append(' ');
		}
		sb.append("^\n");
		log.error(sb);
	}

	@Override
	protected void load(InputStream in, boolean deferrable) throws IOException {
		if (this.getResource() != null) {
			scriptFile.setFileName(this.getResource().getName());
		}
		if (log.isDebugEnabled()) {
			log.debug("LOADING SCRIPT: " + scriptFile.getFileName());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(in), 4096);
		try {
			while(true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				lineNo++;
				//line = line.trim();
				if (!line.startsWith("//")) {
					try {
						processLine(line);
					} catch (SyntaxException e) {
						showSyntaxException(e);
						log.error("error processing file (" + scriptFile.getFileName() + ":" + lineNo + "):" + line + " - " + e, e);
						return;
					} catch (RuntimeException e) {
						log.error("error processing file (" + scriptFile.getFileName() + ":" + lineNo + "):" + line + " - " + e, e);
						return;
					}
				}
			}
			if (currentParseContext.getState() != STATE_NONE) {
				throw new RuntimeException("unexpected end of file (state:" + currentParseContext.getState() + ")");
			}
			if (parseContextStack.size() != 0) {
				throw new RuntimeException("unexpected end of file (stack:" + parseContextStack.size() + ")");
			}
		} catch (Throwable e) {
			log.error("error processing file (" + scriptFile.getFileName() + ")" + " - " + e, e);
		}
		if (log.isDebugEnabled()) {
			log.debug("LOADING SCRIPT DONE: " + scriptFile.getFileName());
		}
	}

	protected void pushParseContext() {
		ParseContext newContext = new ParseContext();
		newContext.setState(currentParseContext.getState());
		pushParseContext(newContext);
	}

	protected void pushParseContext(ParseContext newContext) {
		parseContextStack.push(currentParseContext);
		currentParseContext = newContext;
	}

	protected void popParseContext() {
		currentParseContext = (ParseContext) parseContextStack.pop();
	}

	protected Integer getOperator(String token) {
		return (Integer) operatorMap.get(token);
	}

	protected void processOperators() {
		while ((currentParseContext.getCall() instanceof JDinkScriptOperatorFunctionCall) ||
				(currentParseContext.getCall() instanceof JDinkScriptModificatorFunctionCall)) {
			ParseContext oldContext = currentParseContext;
			this.popParseContext();
			currentParseContext.getCall().addArgument(oldContext.getCall());
		}
	}

	protected JDinkScriptFunctionCall getOperatorCall(String token) {
		Integer op = (Integer) operatorMap.get(token);
		if (op != null) {
			return new JDinkScriptOperatorFunctionCall(op.intValue());
		}
		op = (Integer) setOperatorMap.get(token);
		if (op != null) {
			return new JDinkScriptSetOperatorFunctionCall(op.intValue());
		}
		return null;
	}

	protected JDinkScriptFunctionCall getModificatorCall(String token) {
		Integer op = (Integer) modificatorMap.get(token);
		if (op != null) {
			return new JDinkScriptModificatorFunctionCall(op.intValue());
		}
		return null;
	}

	protected JDinkScriptFunctionCall getPrefixSetModificatorCall(String token) {
		Integer op = (Integer) prefixSetModificatorMap.get(token);
		if (op != null) {
			return new JDinkScriptSetModificatorFunctionCall(op.intValue());
		}
		return null;
	}

	protected JDinkScriptFunctionCall getPostfixSetModificatorCall(String token) {
		Integer op = (Integer) postfixSetModificatorMap.get(token);
		if (op != null) {
			return new JDinkScriptSetModificatorFunctionCall(op.intValue());
		}
		return null;
	}

	protected void startCall(JDinkScriptFunctionCall call) {
		this.pushParseContext();
		currentParseContext.setCall(call);
		currentParseContext.getCall().setLineNo(lineNo);
	}

	protected void continueCall(JDinkScriptFunctionCall call) {
		this.pushParseContext();
		currentParseContext.setCall(call);
	}

	protected JDinkScriptCall getPreviousCall() {
		JDinkScriptCall result = null;
		JDinkScriptBlock block = this.currentParseContext.getBlock();
		if (block != null) {
			JDinkScriptCall[] calls = block.getCalls();
			if ((calls != null) && (calls.length > 0)) {
				result = calls[calls.length - 1];
			}
		}
		return result;
	}

	protected void startBlock() {
		this.pushParseContext();
		currentParseContext.setCall(null);
		currentParseContext.setBlock(new JDinkScriptBlock());
		currentParseContext.getBlock().setLineNo(lineNo);
	}

	protected void processStatementEnd() {
		ParseContext oldParseContext = currentParseContext;
		this.popParseContext();
		while (currentParseContext.getCall() != null) {
			currentParseContext.getCall().addArgument(oldParseContext.getCall());
			oldParseContext = currentParseContext;
			this.popParseContext();
		}
		if (oldParseContext.getCall() != this.getPreviousCall()) {
			currentParseContext.getBlock().addCall(oldParseContext.getCall());
		}
		currentParseContext.setCall(null);
		currentParseContext.setState(STATE_DEFINITION_FUNCTION_BODY);
	}

	protected void processBlockEnd() {
		this.processOperators();
		//this.processStatementEnd();
		ParseContext oldParseContext = currentParseContext;
		this.popParseContext();
		while (currentParseContext.getCall() != null) {
			if (oldParseContext.getCall() != null) {
				currentParseContext.getCall().addArgument(oldParseContext.getCall());
			} else if (oldParseContext.getBlock() != null) {
				currentParseContext.getCall().addArgument(oldParseContext.getBlock());
			} else {
				throw new SyntaxException("no call to add", currentLine, lineNo, currentColumn, null);
			}
			oldParseContext = currentParseContext;
			this.popParseContext();
		}
		if (currentParseContext.getDefinitionFunction() != null) {
			currentParseContext.getDefinitionFunction().setScriptBlock(oldParseContext.getBlock());
			scriptFile.addFunction(currentParseContext.getDefinitionFunction());
			currentParseContext.setDefinitionFunction(null);
			currentParseContext.setState(STATE_NONE);
		} else {
			if (currentParseContext.getCall() != null) {
				throw new SyntaxException("still in function call but found block-end: " + currentParseContext.getCall(), currentLine, lineNo, currentColumn, null);
				//currentParseContext.getCall().addArgument(oldParseContext.getBlock());
			} else {
				if (oldParseContext.getBlock() != null) {
					currentParseContext.getBlock().addCall(oldParseContext.getBlock());
				} else if (oldParseContext.getCall() != null) {
					if (oldParseContext.getCall() != this.getPreviousCall()) {
						currentParseContext.getBlock().addCall(oldParseContext.getCall());
					}
				} else {
					try {
						throw new SyntaxException("warning: no call to add", currentLine, lineNo, currentColumn, null);
					} catch (SyntaxException e) {
						this.showSyntaxException(e);
					}
					//log.warn("warning: no call to be added");
				}
				currentParseContext.setState(STATE_DEFINITION_FUNCTION_BODY);
			}
			//currentParseContext.getBlock().addCall(previousParseContext.getCall());
//			scriptFile.addFunction(currentParseContext.getDefinitionFunction());
//			currentParseContext.setDefinitionFunction(null);
//			currentParseContext.setState(STATE_NONE);
		}
	}


	protected Object getTokenAsArgument(String token) {
		if ("0123456789-+".indexOf(token.charAt(0)) >= 0) {
			int intValue = Integer.parseInt(token);
			return new Integer(intValue);
		} else {
			JDinkScriptFunctionCall call = new JDinkScriptVariableGetFunctionCall(token);
			call.setLineNo(lineNo);
			return call;
		}
	}

	protected void pushTokenBack(String token) {
		if (pushedBackToken != null) {
			throw new RuntimeException("already pushed back: " + pushedBackToken);
		}
		pushedBackToken = token;
	}

	public void processLine(String line) {
		this.currentLine = line;
		int nextColumn = 1;
		StringTokenizer st = new StringTokenizer(line, separatingChars, true);
		while ((pushedBackToken != null) || (st.hasMoreTokens())) {
			String token;
			int column = nextColumn;
			this.currentColumn = column;
			if (pushedBackToken == null) {
				if ((line.length() >= column+1) && (line.substring(column-1, column+1).equals("//"))) {
					break;
				}
			}
			boolean isSeparatingChar = false;
			if (currentParseContext.getState() == STATE_CALL_FUNCTION_ARGUMENTS_STRING) {
				token = st.nextToken("\"");
				nextColumn += token.length();
			} else {
				if (pushedBackToken != null) {
					token = pushedBackToken;
					pushedBackToken = null;
				} else {
					token = st.nextToken(separatingChars);
					nextColumn += token.length();
					token = token.trim();
				}
				if ((NEGATIVE_NUMBER_PREFIX.equals(token)) && (line.length() >= nextColumn) &&
						Character.isDigit(line.charAt(nextColumn - 1))) {
					// this is a negative number
					// get the remaining digits (until the next separating character)
					// and set the token to the number, including "-"
					token = st.nextToken(separatingChars);
					nextColumn += token.length();
					token = NEGATIVE_NUMBER_PREFIX + token.trim();
				}
				isSeparatingChar = (token.length() == 1) && (separatingChars.indexOf(token.charAt(0)) >= 0);
				if ((token.length() == 1) && (operatorChars.indexOf(token.charAt(0)) >= 0) &&
						(line.length() >= nextColumn)) {
					char ch = line.charAt(nextColumn-1);
					if (operatorChars.indexOf(ch) >= 0) {
						String token2 = st.nextToken();
						nextColumn += token2.length();
						token = token + token2;
					}
				}
			}
			if (token.equals("")) {
				continue;
			}
			switch(currentParseContext.getState()) {
				case STATE_NONE:
					if (isSeparatingChar) {
						throw new SyntaxException("function type expected", line, lineNo, column, token);
					}
					currentParseContext.setDefinitionFunction(new JDinkScriptFunction());
					currentParseContext.getDefinitionFunction().setResultType(token);
					currentParseContext.setState(STATE_DEFINITION_FUNCTION_NAME);
					break;
				case STATE_DEFINITION_FUNCTION_NAME:
					if (isSeparatingChar) {
						throw new SyntaxException("function name expected", line, lineNo, column, token);
					}
					currentParseContext.getDefinitionFunction().setName(token);
					currentParseContext.setState(STATE_DEFINITION_FUNCTION_ARGUMENTS_BEGIN);
					break;
				case STATE_DEFINITION_FUNCTION_ARGUMENTS_BEGIN:
					if (!token.equals(ROUND_BRAKET_BEGIN_TOKEN)) {
						throw new SyntaxException("\"(\" expected", line, lineNo, column, token);
					}
					currentParseContext.setState(STATE_DEFINITION_FUNCTION_ARGUMENTS_BODY);
					break;
				case STATE_DEFINITION_FUNCTION_ARGUMENTS_BODY:
					if (token.equals("void")) {
						currentParseContext.setState(STATE_DEFINITION_FUNCTION_ARGUMENTS_END);
						break;
					}
				case STATE_DEFINITION_FUNCTION_ARGUMENTS_END:
					if (!token.equals(ROUND_BRAKET_END_TOKEN)) {
						throw new SyntaxException("\")\" expected", line, lineNo, column, token);
					}
					currentParseContext.setState(STATE_DEFINITION_FUNCTION_BODY_BEGIN);
					break;
				case STATE_DEFINITION_FUNCTION_BODY_BEGIN:
					if (!token.equals("{")) {
						throw new SyntaxException("\"{\" expected", line, lineNo, column, token);
					}
					currentParseContext.setState(STATE_NONE);
					startBlock();
					currentParseContext.setState(STATE_DEFINITION_FUNCTION_BODY);
					break;
				case STATE_BLOCK_BODY:
				case STATE_DEFINITION_FUNCTION_BODY:
					if (token.equals("}")) {
						processBlockEnd();
					} else {
						if (token.equals(";")) {
							this.processStatementEnd();
							break;
						}
						if (isSeparatingChar) {
							throw new SyntaxException("function or type name expected", line, lineNo, column, token);
						}
						previousToken = token;
						currentParseContext.setState(STATE_DEFINITION_FUNCTION_BODY_STEP2);
					}
					break;
				case STATE_IF_THEN_STATEMENT_BODY_STEP2:
				case STATE_DEFINITION_FUNCTION_BODY_STEP2:
					// test previous token
					if (previousToken.equals("return")) {
						//log.debug("generate return-function-call: " + previousToken);
						startCall(new JDinkScriptReturnFunctionCall());
						currentParseContext.setState(STATE_CALL_RETURN_VALUE);
						if (token.equals(";")) {
							this.processStatementEnd();
						} else {
							if (isSeparatingChar) {
								throw new SyntaxException("return value expected", line, lineNo, column, token);
							}
							previousToken = token;
						}
						break;
					} else if (previousToken.equals(IF_TOKEN)) {
						if (log.isDebugEnabled()) {
							log.debug("generate if-function-call: " + previousToken);
						}
						currentParseContext.setState(STATE_IF_THEN_STATEMENT_BODY);
						startCall(new JDinkScriptIfFunctionCall());
						if (token.equals("(")) {
							currentParseContext.setState(STATE_CALL_FUNCTION_ARGUMENTS_BODY);
							break;
						} else {
							throw new SyntaxException("\"(\" expected", line, lineNo, column, token);
						}
					} else if (ELSE_TOKEN.equals(previousToken)) {
						if (log.isDebugEnabled()) {
							log.debug("generate else block for if-function-call: " + previousToken);
						}
						JDinkScriptCall previousCall = this.getPreviousCall();
						if (!(previousCall instanceof JDinkScriptIfFunctionCall)) {
							throw new SyntaxException("else does not follow if statement", line, lineNo, column, token);
						} else {
							JDinkScriptIfFunctionCall ifFunctionCall = (JDinkScriptIfFunctionCall) previousCall;
							if (ifFunctionCall.getArgumentCount() != 2) {
								throw new SyntaxException("else does not follow if-then block", line, lineNo, column, token);
							} else {
								if (token.equals("{")) {
									// TODO
									currentParseContext.setState(STATE_IF_THEN_STATEMENT_BODY);
									// we're starting the if call again
									continueCall(ifFunctionCall);
									currentParseContext.setState(STATE_IF_THEN_STATEMENT_BODY);
									startBlock();
									currentParseContext.setState(STATE_BLOCK_BODY);
									break;
								} else if (isSeparatingChar) {
									throw new SyntaxException("\"{\" expected", line, lineNo, column, token);
								} else {
									// TODO
									currentParseContext.setState(STATE_IF_THEN_STATEMENT_BODY);
									// we're starting the if call again
									continueCall(ifFunctionCall);
									currentParseContext.setState(STATE_IF_THEN_STATEMENT_BODY_STEP2);
									previousToken = token;
									break;
								}
							}
						}
					} else if (GOTO_TOKEN.equals(previousToken)) {
						startCall(new JDinkGotoLabelFunctionCall(token));
						currentParseContext.setState(STATE_CALL_END);
						break;
					}

					// test current token
					if (token.equals(STATEMENT_END_TOKEN)) {
						if (currentParseContext.getState() == STATE_IF_THEN_STATEMENT_BODY_STEP2) {
							throw new SyntaxException("then statement must not be a value", line, lineNo, column, token);
						}
						//log.debug("generate get-argument? (due to \";\"): " + previousToken);
						currentParseContext.getCall().addArgument(getTokenAsArgument(previousToken));
						processStatementEnd();
					} else if (LABEL_DEFINE_TOKEN.equals(token)) {
						startCall(new JDinkDefineLabelFunctionCall(previousToken));
						processStatementEnd();
					} else if (token.equals("(")) {
						if (currentParseContext.getCall() != null) {
							currentParseContext.setState(STATE_DEFINITION_FUNCTION_BODY);
						} else {
							currentParseContext.setState(STATE_CALL_END);
						}
						startCall(new JDinkScriptFunctionCall(previousToken));
						//log.debug("generate function-call: " + previousToken);
						currentParseContext.setState(STATE_CALL_FUNCTION_ARGUMENTS_BODY);
					} else if (token.equals("=")) {
						currentParseContext.setState(STATE_CALL_VARIABLE_SET_VALUE);
						startCall(new JDinkScriptVariableSetFunctionCall(previousToken));
//					} else if (token.equals("==")) {
//						currentParseContext.setState(STATE_CALL_OPERATOR_VALUE);
//						pushParseContext();
//						currentParseContext.setCall(new JDinkScriptOperatorFunctionCall(JDinkScriptOperatorFunctionCall.OP_EQUALS));
//						currentParseContext.getCall().setLineNr(lineNo);
//						currentParseContext.getCall().addArgument(getTokenAsArgument(previousToken));
					} else if (token.equals(ROUND_BRAKET_END_TOKEN)) {
						this.processArgumentEnd();
						ParseContext oldParseContext = currentParseContext;
						if (currentParseContext.getCall() instanceof JDinkScriptIfFunctionCall) {
							if (log.isDebugEnabled()) {
								log.debug("if-function-call: start body");
							}
							currentParseContext.setState(STATE_IF_THEN_STATEMENT_BODY);
							break;
						} else {
							this.popParseContext();
							if (currentParseContext.getCall() != null) {
								currentParseContext.getCall().addArgument(oldParseContext.getCall());
								oldParseContext = currentParseContext;
								//this.popParseContext();
							} else {
								this.pushParseContext(oldParseContext);
								currentParseContext.setState(STATE_CALL_END);
							}
						}
					} else if (isSeparatingChar) {
						if (token.equals(ARGUMENT_SEPARATOR_TOKEN)) {
							if (currentParseContext.getCall() != null) {
								currentParseContext.getCall().addArgument(getTokenAsArgument(previousToken));
							} else {
								throw new SyntaxException("unexpected token", line, lineNo, column, token);
							}
							this.processOperators();
							if (currentParseContext.getState() == STATE_CALL_FUNCTION_ARGUMENTS_SEPARATOR) {
								currentParseContext.setState(STATE_CALL_FUNCTION_ARGUMENTS_BODY);
							} else {
								throw new SyntaxException("unexpected token", line, lineNo, column, token);
							}
							break;
						}
						JDinkScriptFunctionCall opCall = getOperatorCall(token);
						if (opCall != null) {
							startCall(opCall);
							currentParseContext.setState(STATE_CALL_OPERATOR_VALUE);
							if (previousToken != null) {
								currentParseContext.getCall().addArgument(this.getTokenAsArgument(previousToken));
							}
							break;
						}
						JDinkScriptFunctionCall modificatorCall = getModificatorCall(token);
						if (modificatorCall != null) {
							if (previousToken != null) {
								throw new SyntaxException("modificator not expected here", line, lineNo, column, token);
							}
							startCall(modificatorCall);
							currentParseContext.setState(STATE_CALL_MODIFICATOR_VALUE);
							break;
						}
						JDinkScriptFunctionCall postfixSetModificatorCall = getPostfixSetModificatorCall(token);
						if (postfixSetModificatorCall != null) {
							if (previousToken == null) {
								throw new SyntaxException("unexpected token", line, lineNo, column, token);
							}
							startCall(postfixSetModificatorCall);
							currentParseContext.getCall().addArgument(this.getTokenAsArgument(previousToken));
							currentParseContext.setState(STATE_CALL_END);
							break;
						}
						throw new SyntaxException("variable name expected", line, lineNo, column, token);
					} else {
						startCall(new JDinkScriptInstanceVariableDeclarationFunctionCall(previousToken, token));
						previousToken = token;
						currentParseContext.setState(STATE_CALL_END_OR_SET_VALUE);
					}
					break;
				case STATE_IF_THEN_STATEMENT_BODY:
					if (token.equals("{")) {
						currentParseContext.setState(STATE_IF_ELSE_OR_END);
						startBlock();
						currentParseContext.setState(STATE_BLOCK_BODY);
						break;
					}
					if (isSeparatingChar) {
						throw new SyntaxException("then-statement expected", line, lineNo, column, token);
					}
					previousToken = token;
					currentParseContext.setState(STATE_IF_THEN_STATEMENT_BODY_STEP2);
					break;
				case STATE_CALL_RETURN_VALUE:
					if (token.equals(STATEMENT_END_TOKEN)) {
						this.processStatementEnd();
						break;
					}
				case STATE_CALL_MODIFICATOR_VALUE:
				case STATE_CALL_OPERATOR_VALUE:
				case STATE_CALL_VARIABLE_SET_VALUE:
				{
					JDinkScriptFunctionCall modificatorCall = getModificatorCall(token);
					if (modificatorCall != null) {
						startCall(modificatorCall);
						currentParseContext.setState(STATE_CALL_MODIFICATOR_VALUE);
						break;
					}
					if (isSeparatingChar) {
						throw new SyntaxException("value expected", line, lineNo, column, token);
					}
					previousToken = token;
					currentParseContext.setState(STATE_DEFINITION_FUNCTION_BODY_STEP2);
					break;
				}
				case STATE_CALL_FUNCTION_ARGUMENTS_BEGIN:
					if (!token.equals("(")) {
						throw new SyntaxException("\"(\" expected", line, lineNo, column, token);
					}
					currentParseContext.setState(STATE_CALL_FUNCTION_ARGUMENTS_BODY);
					break;
				case STATE_CALL_FUNCTION_ARGUMENTS_BODY:
					if (token.equals(ROUND_BRAKET_END_TOKEN)) {
						if (currentParseContext.getCall() instanceof JDinkScriptIfFunctionCall) {
							throw new SyntaxException("if condition expected", line, lineNo, column, token);
						}
						currentParseContext.setState(STATE_CALL_END);
						if (currentParseContext.getCall().getFunctionName().equals("choice_start")) {
							choiceStartScriptFunctionCall = currentParseContext.getCall();
							choiceArguments = new ArrayList<Object>();
							processStatementEnd();
							currentParseContext.setState(STATE_CHOICE_BODY);
							return;
						}
						if (currentParseContext.getCall().getFunctionName().equals("breakpoint")) {
							log.debug("breakpoint found");
						}
					} else if (token.equals("\"")) {
						tempString = new StringBuffer();
						currentParseContext.setState(STATE_CALL_FUNCTION_ARGUMENTS_STRING);
					} else {
						previousToken = token;
						currentParseContext.setState(STATE_CALL_FUNCTION_ARGUMENTS_SEPARATOR);
					}
					break;
				case STATE_CALL_FUNCTION_ARGUMENTS_SEPARATOR:
					if (token.equals(ARGUMENT_SEPARATOR_TOKEN)) {
						if (currentParseContext.getCall() instanceof JDinkScriptIfFunctionCall) {
							throw new SyntaxException("if has one parameter only", line, lineNo, column, token);
						}
						if (previousToken != null) {
							//log.debug("generate get-argument? (due to \",\"): " + previousToken);
							currentParseContext.getCall().addArgument(getTokenAsArgument(previousToken));
						}
						currentParseContext.setState(STATE_CALL_FUNCTION_ARGUMENTS_BODY);
						break;
					}
				case STATE_CALL_FUNCTION_ARGUMENTS_END:
					if (token.equals("(")) {
						String functionName = previousToken;
						startCall(new JDinkScriptFunctionCall(functionName));
						currentParseContext.setState(STATE_CALL_FUNCTION_ARGUMENTS_BODY);
						break;
					}
					if (previousToken != null) {
						JDinkScriptFunctionCall modificatorCall = getModificatorCall(previousToken);
						if (modificatorCall != null) {
							if (log.isDebugEnabled()) {
								log.debug("generate modificator-call: " + previousToken);
							}
							currentParseContext.setState(STATE_CALL_FUNCTION_ARGUMENTS_SEPARATOR);
							startCall(modificatorCall);
							currentParseContext.setState(STATE_CALL_MODIFICATOR_VALUE);
							previousToken = null;
							pushTokenBack(token);
							break;
						}
						//log.debug("generate get-argument? (due to argument combination - " + token + "): " + previousToken);
						if (previousToken != null) {
							currentParseContext.getCall().addArgument(getTokenAsArgument(previousToken));
							previousToken = null;
						}
					}
					JDinkScriptFunctionCall modificatorCall = getModificatorCall(token);
					if (modificatorCall != null) {
						currentParseContext.setState(STATE_CALL_FUNCTION_ARGUMENTS_SEPARATOR);
						startCall(modificatorCall);
						currentParseContext.setState(STATE_CALL_MODIFICATOR_VALUE);
						break;
					}
					JDinkScriptFunctionCall opCall = getOperatorCall(token);
					if (opCall != null) {
						currentParseContext.setState(STATE_CALL_FUNCTION_ARGUMENTS_SEPARATOR);
						JDinkScriptFunctionCall previousCall = currentParseContext.getCall();
						startCall(opCall);
						currentParseContext.setState(STATE_CALL_OPERATOR_VALUE);
						currentParseContext.getCall().addArgument(previousCall.popArgument());
						break;
					}
					if (!token.equals(ROUND_BRAKET_END_TOKEN)) {
						throw new SyntaxException("\")\" expected", line, lineNo, column, token);
					}
					if (currentParseContext.getCall() instanceof JDinkScriptIfFunctionCall) {
						log.debug("if-function-call: start body");
						//ParseContext oldParseContext = currentParseContext;
						//this.popParseContext();
						//this.pushParseContext(oldParseContext);
						currentParseContext.setState(STATE_IF_THEN_STATEMENT_BODY);
						break;
					}
					ParseContext oldParseContext = currentParseContext;
					this.popParseContext();
					if (currentParseContext.getCall() != null) {
						currentParseContext.getCall().addArgument(oldParseContext.getCall());
						oldParseContext = currentParseContext;
						if (currentParseContext.getState() != STATE_CALL_FUNCTION_ARGUMENTS_SEPARATOR) {
							currentParseContext.setState(STATE_CALL_END);
						}
					} else {
						this.pushParseContext(oldParseContext);
						//currentParseContext.setCall(oldParseContext.getCall());
						currentParseContext.setState(STATE_CALL_END);
						if (oldParseContext.getCall().getFunctionName().equals("choice_start")) {
							choiceStartScriptFunctionCall = oldParseContext.getCall();
							choiceArguments = new ArrayList<Object>();
							processStatementEnd();
							currentParseContext.setState(STATE_CHOICE_BODY);
							return;
						}
					}
					break;
				case STATE_CALL_FUNCTION_ARGUMENTS_STRING:
					if ((token.equals("\"")) && (!tempString.toString().endsWith("\\"))) {
						currentParseContext.getCall().addArgument(tempString.toString());
						currentParseContext.setState(STATE_CALL_FUNCTION_ARGUMENTS_SEPARATOR);
						tempString = null;
						previousToken = null;
					} else {
						tempString.append(token);
					}
					break;
				case STATE_CALL_END_OR_SET_VALUE:
					if (token.equals("=")) {
						processStatementEnd();
						currentParseContext.setState(STATE_CALL_VARIABLE_SET_VALUE);
						startCall(new JDinkScriptVariableSetFunctionCall(previousToken));
						previousToken = null;
						break;
					}
				case STATE_CALL_END:
					if (!token.equals(STATEMENT_END_TOKEN)) {
						try {
							throw new SyntaxException("\";\" expected", line, lineNo, column, token);
						} catch (SyntaxException e) {
							if (this.isRelaxedParsing()) {
								showSyntaxException(e);
								this.processStatementEnd();
								if (!":".equals(token)) {
									pushTokenBack(token);
								}
								break;
							} else {
								throw e;
							}
						}
					}
					if (this.parseContextStack.isEmpty()) {
						log.error("stack is empty");
					}
					processStatementEnd();
					break;
				case STATE_CHOICE_BODY:
					if (token.equals("choice_end")) {
						currentParseContext.setState(STATE_DEFINITION_FUNCTION_BODY);
						while (choiceStartScriptFunctionCall.getArgumentCount() > 0) {
							choiceStartScriptFunctionCall.popArgument();
						}
						choiceStartScriptFunctionCall.addArgument(choiceArguments.toArray(new Object[0]));
						choiceStartScriptFunctionCall = null;
						choiceArguments = null;
//						startCall(new JDinkScriptFunctionCall());
//						currentParseContext.getCall().setFunctionName(token);
//						currentParseContext.setState(STATE_CALL_FUNCTION_ARGUMENTS_BODY);
					} else {
						String choice = line.trim();
						int quoteStartIndex= choice.indexOf(QUOTE_BEGIN_STRING);
						Object condition = null;
						if (quoteStartIndex > 0) {
							String conditionString = choice.substring(0, quoteStartIndex);
							condition = parseCondition(lineNo, conditionString);
							choice = choice.substring(quoteStartIndex);
						}
						if ((choice.length() >= 2) &&
								(choice.startsWith(QUOTE_BEGIN_STRING)) &&
								(choice.endsWith(QUOTE_END_STRING))) {
							choice = choice.substring(
									QUOTE_BEGIN_STRING.length(),
									choice.length() - QUOTE_END_STRING.length());
						}
						if (condition != null) {
							JDinkScriptIfFunctionCall call = new JDinkScriptIfFunctionCall();
							call.setLineNo(this.lineNo);
							call.addArgument(condition);
							call.addArgument(choice);
							choiceArguments.add(call);
						} else {
							choiceArguments.add(choice);
						}
					}
					return;
				default:
					throw new SyntaxException("invalid state: " + currentParseContext.getState(), line, lineNo, column, token);
			}
		}
	}

	private void processArgumentEnd() {
		if (previousToken != null) {
			if (currentParseContext.getCall() != null) {
				currentParseContext.getCall().addArgument(getTokenAsArgument(previousToken));
			} else {
				throw new RuntimeException("no call... not supported yet");
			}
			previousToken = null;
		}
		this.processOperators();
	}


	public Object parseStatement(int lineNo, String s) {
		return this.parseCondition(lineNo, s);
	}

	protected Object parseCondition(int lineNo, String s) {
		s = s.trim();
		if ((s.startsWith(ROUND_BRAKET_BEGIN_TOKEN)) && (s.endsWith(ROUND_BRAKET_END_TOKEN))) {
			s = s.substring(1, s.length() - 1);
		}
		JDinkScriptBlock block = new JDinkScriptBlock();
		JDinkCLoader dinkCLoader = new JDinkCLoader() {
			@Override
			public Resource getResource() {
				return JDinkCLoader.this.getResource();
			}
		};
		dinkCLoader.setContext(context);
		dinkCLoader.lineNo = lineNo;
		ParseContext currentParseContext = new ParseContext();
		currentParseContext.setBlock(block);
		currentParseContext.setState(STATE_IF_THEN_STATEMENT_BODY);
		dinkCLoader.currentParseContext = currentParseContext;
		dinkCLoader.startCall(new JDinkScriptIfFunctionCall());
		dinkCLoader.currentParseContext.setState(STATE_CALL_FUNCTION_ARGUMENTS_BODY);
		dinkCLoader.processLine(s);
		dinkCLoader.processArgumentEnd();
		Object result = dinkCLoader.currentParseContext.getCall().getArgument(0);
		return result;
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
	public JDinkScriptFile getScriptFile() {
		return scriptFile;
	}

	public boolean isRelaxedParsing() {
		return relaxedParsing;
	}

	public void setRelaxedParsing(boolean relaxedParsing) {
		this.relaxedParsing = relaxedParsing;
	}
}
