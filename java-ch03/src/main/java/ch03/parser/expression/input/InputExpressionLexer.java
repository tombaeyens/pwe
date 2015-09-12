// Generated from InputExpression.g4 by ANTLR 4.5.1
package ch03.parser.expression.input;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class InputExpressionLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, ID=13, NUMBER=14, STRING=15, HEX_DIGIT=16, 
		WHITESPACE=17;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "ID", "NUMBER", "STRING", "ESC", "HEX_DIGIT", 
		"WHITESPACE"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "'<'", "'!'", "'&&'", "'||'", "'$'", "'.'", "'+'", 
		"'['", "']'", "','"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, "ID", "NUMBER", "STRING", "HEX_DIGIT", "WHITESPACE"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public InputExpressionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "InputExpression.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\23u\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\b"+
		"\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\7\16D\n\16\f"+
		"\16\16\16G\13\16\3\17\6\17J\n\17\r\17\16\17K\3\17\3\17\7\17P\n\17\f\17"+
		"\16\17S\13\17\5\17U\n\17\3\20\3\20\3\20\7\20Z\n\20\f\20\16\20]\13\20\3"+
		"\20\3\20\3\21\3\21\3\21\6\21d\n\21\r\21\16\21e\5\21h\n\21\3\22\6\22k\n"+
		"\22\r\22\16\22l\3\23\6\23p\n\23\r\23\16\23q\3\23\3\23\2\2\24\3\3\5\4\7"+
		"\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\2#"+
		"\22%\23\3\2\t\5\2C\\aac|\6\2\62;C\\aac|\3\2\62;\6\2\f\f\17\17$$^^\13\2"+
		"$$))\61\61^^ddhhppttvv\5\2\62;CHch\5\2\13\f\17\17\"\"}\2\3\3\2\2\2\2\5"+
		"\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2"+
		"\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33"+
		"\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\3\'\3\2\2\2\5"+
		")\3\2\2\2\7+\3\2\2\2\t-\3\2\2\2\13/\3\2\2\2\r\62\3\2\2\2\17\65\3\2\2\2"+
		"\21\67\3\2\2\2\239\3\2\2\2\25;\3\2\2\2\27=\3\2\2\2\31?\3\2\2\2\33A\3\2"+
		"\2\2\35I\3\2\2\2\37V\3\2\2\2!`\3\2\2\2#j\3\2\2\2%o\3\2\2\2\'(\7*\2\2("+
		"\4\3\2\2\2)*\7+\2\2*\6\3\2\2\2+,\7>\2\2,\b\3\2\2\2-.\7#\2\2.\n\3\2\2\2"+
		"/\60\7(\2\2\60\61\7(\2\2\61\f\3\2\2\2\62\63\7~\2\2\63\64\7~\2\2\64\16"+
		"\3\2\2\2\65\66\7&\2\2\66\20\3\2\2\2\678\7\60\2\28\22\3\2\2\29:\7-\2\2"+
		":\24\3\2\2\2;<\7]\2\2<\26\3\2\2\2=>\7_\2\2>\30\3\2\2\2?@\7.\2\2@\32\3"+
		"\2\2\2AE\t\2\2\2BD\t\3\2\2CB\3\2\2\2DG\3\2\2\2EC\3\2\2\2EF\3\2\2\2F\34"+
		"\3\2\2\2GE\3\2\2\2HJ\t\4\2\2IH\3\2\2\2JK\3\2\2\2KI\3\2\2\2KL\3\2\2\2L"+
		"T\3\2\2\2MQ\7\60\2\2NP\t\4\2\2ON\3\2\2\2PS\3\2\2\2QO\3\2\2\2QR\3\2\2\2"+
		"RU\3\2\2\2SQ\3\2\2\2TM\3\2\2\2TU\3\2\2\2U\36\3\2\2\2V[\7$\2\2WZ\5!\21"+
		"\2XZ\n\5\2\2YW\3\2\2\2YX\3\2\2\2Z]\3\2\2\2[Y\3\2\2\2[\\\3\2\2\2\\^\3\2"+
		"\2\2][\3\2\2\2^_\7$\2\2_ \3\2\2\2`g\7^\2\2ah\t\6\2\2bd\7w\2\2cb\3\2\2"+
		"\2de\3\2\2\2ec\3\2\2\2ef\3\2\2\2fh\3\2\2\2ga\3\2\2\2gc\3\2\2\2h\"\3\2"+
		"\2\2ik\t\7\2\2ji\3\2\2\2kl\3\2\2\2lj\3\2\2\2lm\3\2\2\2m$\3\2\2\2np\t\b"+
		"\2\2on\3\2\2\2pq\3\2\2\2qo\3\2\2\2qr\3\2\2\2rs\3\2\2\2st\b\23\2\2t&\3"+
		"\2\2\2\r\2EKQTY[eglq\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}