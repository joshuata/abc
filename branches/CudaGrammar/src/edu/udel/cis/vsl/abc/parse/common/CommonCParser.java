package edu.udel.cis.vsl.abc.parse.common;

import java.util.Map;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;

import edu.udel.cis.vsl.abc.err.IF.ABCRuntimeException;
import edu.udel.cis.vsl.abc.parse.IF.CParser;
import edu.udel.cis.vsl.abc.parse.IF.Parse.RuleKind;
import edu.udel.cis.vsl.abc.parse.IF.ParseException;
import edu.udel.cis.vsl.abc.parse.IF.RuntimeParseException;
import edu.udel.cis.vsl.abc.token.IF.CToken;
import edu.udel.cis.vsl.abc.token.IF.CTokenSource;
import edu.udel.cis.vsl.abc.token.IF.Macro;
import edu.udel.cis.vsl.abc.token.IF.Source;
import edu.udel.cis.vsl.abc.token.IF.SyntaxException;
import edu.udel.cis.vsl.abc.token.IF.TokenFactory;
import edu.udel.cis.vsl.abc.token.common.CommonCToken;

public class CommonCParser implements CParser {

	/* ****************************** Fields ****************************** */

	/**
	 * Which rule is the start rule for parsing?
	 */
	private RuleKind rule;

	/**
	 * The token source provides the sequence of tokens which are to be parsed.
	 */
	private CTokenSource tokenSource;

	/**
	 * The ANTLR token stream, which is formed from the token source, and which
	 * is used as input to the ANTLR parer.
	 */
	private TokenStream stream;

	/**
	 * The ANTLR-generated parser used by this CParser to do the real work.
	 */
	private CivlCParser parser;

	/**
	 * The factory used for producing tokens and related objects.
	 */
	private TokenFactory tokenFactory;

	/* *************************** Constructors *************************** */

	public CommonCParser(CTokenSource tokenSource) {
		this.rule = RuleKind.TRANSLATION_UNIT;
		this.tokenSource = tokenSource;
		this.tokenFactory = tokenSource.getTokenFactory();
		this.stream = new CommonTokenStream(tokenSource);
		this.parser = new CivlCParser(stream);
	}

	public CommonCParser(RuleKind rule, CTokenSource tokenSource,
			int startTokenIndex, int lastTokenIndex) {
		this.rule = rule;
		this.tokenSource = new SubTokenSource(tokenSource, startTokenIndex,
				lastTokenIndex);
		this.tokenFactory = tokenSource.getTokenFactory();
		this.stream = new CommonTokenStream(this.tokenSource);
		this.parser = new CivlCParser(stream);
	}

	/* ********************** Static Helper Methods *********************** */

	/**
	 * <p>
	 * Sets some fields of the tokens that occur in the tree.
	 * </p>
	 * 
	 * <p>
	 * I know ANTLR is supposed to do this but I don't think it does it right.
	 * First, the tokenIndexes are not always what I expect. For some reason,
	 * ANTLR's CommonTokenStream sets the index of the last token (EOF) to be
	 * one higher than it should be, so there is a gap in the indexes between
	 * the penultimate token and the last token. I introduced my own "index"
	 * field to CToken (which extends CommonToken) and set it myself correctly.
	 * </p>
	 * 
	 * <p>
	 * Second, ANTLR is supposed to find the range of tokens spanned by each
	 * node in the tree (by examining all descendants of the node). However:
	 * first, the code that does this uses ANTLR's tokenIndex, and I want to do
	 * it using my index. Second, the ANTLR code is only correct under the
	 * assumption that the token indices are non-decreasing as child index
	 * increases, i.e., the token index of child i is less than or equal to that
	 * of child i+1, for all i, for all nodes. (Hence it only has to examine the
	 * first and last child.) There is no reason that assumption has to hold. So
	 * I compute this correctly (and using CToken indexes) and re-set the
	 * "tokenStartIndex" and "tokenStopIndex" fields of each tree node.
	 * </p>
	 * 
	 * @param tree
	 *            a tree resulting from executing an ANTLR parser
	 */
	private static void postProcessTree(CommonTree tree) {
		initPostProcess(tree);
		completePostProcess(tree);
	}

	/**
	 * Marks all nodes as "not yet visited"---indicating by the magic number
	 * -999 for tokenStartIndex and tokenStopIndex.
	 * 
	 * @param tree
	 */
	private static void initPostProcess(CommonTree tree) {
		int numChildren = tree.getChildCount();

		tree.setTokenStartIndex(-999);
		tree.setTokenStopIndex(-999);
		for (int i = 0; i < numChildren; i++)
			initPostProcess((CommonTree) tree.getChild(i));
	}

	/**
	 * Computes the actual start and stop index of each node in the tree.
	 * 
	 * <p>
	 * If there is no CToken occurring in a node or any of its descendants, the
	 * start and stop index of that node will both be set to -1.
	 * </p>
	 * 
	 * @param tree
	 */
	private static void completePostProcess(CommonTree tree) {
		if (tree.getTokenStartIndex() != -999)
			return;
		else {
			int numChildren = tree.getChildCount();
			CommonToken token = (CommonToken) tree.getToken();
			int min, max;

			if (token instanceof CommonCToken) {
				min = max = ((CommonCToken) token).getIndex();
			} else {
				min = max = -1;
			}
			for (int i = 0; i < numChildren; i++) {
				CommonTree child = (CommonTree) tree.getChild(i);
				int childMin, childMax;

				completePostProcess(child);
				childMin = child.getTokenStartIndex();
				childMax = child.getTokenStopIndex();
				if (childMin >= 0 && (min < 0 || childMin < min))
					min = childMin;
				if (childMax >= 0 && (max < 0 || childMax > max))
					max = childMax;
			}
			tree.setTokenStartIndex(min);
			tree.setTokenStopIndex(max);
		}
	}

	/* ************************* CParser Methods ************************** */

	/**
	 * Returns the ANTLR CommonTree resulting from parsing the input, after some
	 * "post-processing" has been done to the tree to fill in some fields.
	 * 
	 * @return the ANTLR tree that results from parsing
	 * @throws ParseException
	 *             if something goes wrong parsing the input
	 */
	@Override
	public CommonTree getTree() throws ParseException {
		try {
			CommonTree tree;

			switch (rule) {
			case TRANSLATION_UNIT:
				tree = (CommonTree) parser.translationUnit().getTree();
				break;
			case EXTERNAL_DEFINITION:
				tree = (CommonTree) parser.externalDeclaration().getTree();
				break;
			case BLOCK_ITEM:
				tree = (CommonTree) parser.blockItem().getTree();
				break;
			default:
				throw new ABCRuntimeException("Unreachable");
			}
			postProcessTree(tree);
			return tree;
		} catch (RecognitionException e) {
			throw new ParseException(e.getMessage(), e.token);
		} catch (RuntimeParseException e) {
			throw new ParseException(e.getMessage());
		}
	}

	@Override
	public Source source(CommonTree tree) {
		CToken firstToken = null, lastToken = null;
		int start = tree.getTokenStartIndex();
		int stop = tree.getTokenStopIndex();

		if (start >= 0)
			firstToken = tokenSource.getToken(start);
		if (stop >= 0)
			lastToken = tokenSource.getToken(stop);
		if (firstToken == null)
			if (lastToken == null)
				throw new IllegalArgumentException(
						"No tokens associated to tree node " + tree);
			else
				firstToken = lastToken;
		else if (lastToken == null)
			lastToken = firstToken;
		return tokenFactory.newSource(firstToken, lastToken);
	}

	@Override
	public SyntaxException newSyntaxException(String message, CommonTree tree) {
		return tokenFactory.newSyntaxException(message, source(tree));
	}

	@Override
	public CTokenSource getTokenSource() {
		return tokenSource;
	}

}

/**
 * A helper class which represents a subsequence of sequence of tokens through
 * the CTokenSource abstraction.
 * 
 * @author siegel
 * 
 */
class SubTokenSource implements CTokenSource {

	int startTokenIndex;

	int lastTokenIndex;

	int currentTokenIndex;

	CTokenSource rootSource;

	CToken eofToken;

	SubTokenSource(CTokenSource rootSource, int startTokenIndex,
			int lastTokenIndex) {
		this.rootSource = rootSource;
		this.startTokenIndex = startTokenIndex;
		this.lastTokenIndex = lastTokenIndex;
		this.currentTokenIndex = startTokenIndex;
		this.eofToken = rootSource.getTokenFactory().newCToken(CParser.EOF,
				"EOF", null);
	}

	@Override
	public Token nextToken() {
		if (currentTokenIndex > lastTokenIndex) {
			return eofToken;
		} else {
			Token result = rootSource.getToken(currentTokenIndex);

			currentTokenIndex++;
			return result;
		}
	}

	@Override
	public String getSourceName() {
		return rootSource.getSourceName();
	}

	@Override
	public Map<String, Macro> getMacroMap() {
		return rootSource.getMacroMap();
	}

	@Override
	public int getNumTokens() {
		return currentTokenIndex - startTokenIndex;
	}

	@Override
	public CToken getToken(int index) {
		return rootSource.getToken(index);
	}

	@Override
	public TokenFactory getTokenFactory() {
		return rootSource.getTokenFactory();
	}
}
