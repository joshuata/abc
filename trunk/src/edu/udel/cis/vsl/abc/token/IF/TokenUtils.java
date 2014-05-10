package edu.udel.cis.vsl.abc.token.IF;

import java.io.File;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenSource;
import org.antlr.runtime.tree.CommonTree;

/**
 * Utility class providing static methods dealing with Token objects.
 * 
 * @author siegel
 * 
 */
public class TokenUtils {

	/**
	 * The maximum number of tokens that will be printed when summarizing a
	 * range of tokens. If the number of tokens exceeds this bound, the ellipsis
	 * will be used in the summary.
	 */
	public final static int summaryBound = 10;

	/**
	 * A utility function to extract the filename, line number, and character
	 * index of a token of any type, and return a string representation of this
	 * in a consistent way.
	 * 
	 * @param token
	 *            any instance of Token
	 * @param abbreviated
	 *            true iff the resulting file name a the shorter one (f1, f2,
	 *            ...) instead of the original one.
	 * @return string explaining where the token came from
	 */
	public static String location(Token token, boolean abbreviated) {
		String filename = getShortFilename(token, abbreviated);
		int line = token.getLine();
		int pos = token.getCharPositionInLine();

		return filename + " " + line + "." + pos;
	}

	/**
	 * Computes a short version of the file name from a token's source file.
	 * 
	 * @param token
	 *            a token
	 * @param abbreviated
	 *            true iff the result is an abbreviated file name, i.e., shorter
	 *            file name, which is calculated by the static hash map.
	 * @return the short file name
	 */
	public static String getShortFilename(Token token, boolean abbreviated) {
		String filename;
		int separatorIndex;

		if (token instanceof CToken) {
			CToken ppToken = (CToken) token;
			File file = ppToken.getSourceFile();

			filename = file.getName();
			if (abbreviated) {
				return ppToken.getFileShortName();
			}
		} else {
			CharStream stream = token.getInputStream();

			if (stream == null)
				filename = "<unknown file>";
			else
				filename = stream.getSourceName();
		}
		separatorIndex = filename.lastIndexOf(File.pathSeparatorChar);
		if (separatorIndex >= 0 && separatorIndex < filename.length() - 1)
			filename = filename.substring(separatorIndex + 1);
		return filename;
	}

	public static String summarizeRangeLocation(CToken first, CToken last,
			boolean abbreviated) {
		String result;
		String filename1 = getShortFilename(first, abbreviated);
		String filename2 = getShortFilename(last, abbreviated);
		int line1 = first.getLine();
		int pos1 = first.getCharPositionInLine();
		String endPosition;
		int line2, pos2;
		CToken next = last.getNext();

		if (next != null) {
			int line3 = next.getLine();
			int pos3 = next.getCharPositionInLine();

			if (pos3 == 0) {
				line2 = line3 - 1;
				if (line2 == last.getLine()) {
					pos2 = last.getCharPositionInLine()
							+ last.getText().length();
				} else {
					pos2 = -1;
				}
			} else {
				line2 = line3;
				pos2 = pos3;
			}
		} else {
			line2 = last.getLine();
			pos2 = last.getCharPositionInLine() + last.getText().length();
		}
		if (pos2 >= 0) {
			endPosition = line2 + "." + pos2;
		} else {
			endPosition = line2 + ".EOL";
		}
		if (filename1.equals(filename2)) {
			if (line1 == line2) {
				if (pos1 == pos2)
					result = filename1 + ":" + line1 + "." + pos1;
				else
					result = filename1 + ":" + line1 + "." + pos1 + "-" + pos2;
			} else {
				result = filename1 + ":" + line1 + "." + pos1 + "-"
						+ endPosition;
			}
		} else {
			result = filename1 + ":" + line1 + "." + pos1 + "-" + filename2
					+ ":" + endPosition;
		}
		return result;
	}

	public static String summarizeRange(CToken first, CToken last,
			boolean abbreviated) {
		String result = summarizeRangeLocation(first, last, abbreviated);
		String excerpt = "";
		int tokenCount = 0;
		CToken token = first;

		while (token != null && token != last && tokenCount < summaryBound - 1) {
			excerpt += token.getText();
			token = token.getNext();
			tokenCount++;
		}
		if (token != null) {
			if (token != last)
				excerpt += " ... ";
			excerpt += last.getText();
		}
		excerpt = quoteText(excerpt);
		result = result + " " + excerpt;
		return result;
	}

	/**
	 * A utility function to return the text of a token surrounded by double
	 * quotes, with newlines, returns and tabs replaced by escape sequences.
	 * 
	 * @param token
	 *            any instance of Token
	 * @return the text of the token, nicely formatted, in quotes
	 */
	public static String quotedText(Token token) {
		String txt = token.getText();

		if (txt != null)
			return quoteText(txt);
		return "<no text>";
	}

	private static String quoteText(String text) {
		String txt = text.replaceAll("\n", "\\\\n");

		txt = txt.replaceAll("\r", "\\\\r");
		txt = txt.replaceAll("\t", "\\\\t");
		return "\"" + txt + "\"";
	}

	public static TokenSource makeTokenSourceFromList(CToken first) {
		return new ListTokenSource(first);
	}

	/**
	 * Given a CommonTree node, forms a token source from the children of that
	 * node. Adds an EOF token to the end of the source.
	 * 
	 * 
	 * @param node
	 */
	public static TokenSource makeTokenSourceFromChildren(CommonTree node) {
		return new NodeTokenSource(node);
	}

}

/**
 * A simple TokenSource formed from a linked list of PreprocessorTokens, given
 * the first element in the list. The token source appends an infinite number of
 * EOFs after the last token in the list.
 * 
 * @author siegel
 * 
 */
class ListTokenSource implements TokenSource {

	private CToken current;

	ListTokenSource(CToken first) {
		this.current = first;
	}

	@Override
	public Token nextToken() {
		Token result = current;

		if (result == null)
			result = Token.EOF_TOKEN;

		else
			current = current.getNext();
		return result;
	}

	@Override
	public String getSourceName() {
		if (current == null)
			return "unknown";

		CharStream stream = current.getInputStream();

		if (stream == null)
			return "unknown";

		String name = stream.getSourceName();

		if (name == null)
			return "unknown";

		return name;
	}

}

/**
 * A simple TokenSource formed by iterating over the children of a CommonTree
 * node.
 * 
 * @author siegel
 * 
 */
class NodeTokenSource implements TokenSource {

	private CommonTree root;

	/**
	 * Index of the next child that will be returned by a call to nextToken().
	 */
	private int position = 0;

	private int numChildren;

	NodeTokenSource(CommonTree root) {
		this.root = root;
		this.numChildren = root.getChildCount();
	}

	@Override
	public Token nextToken() {
		if (position >= numChildren)
			return Token.EOF_TOKEN;
		else {
			Token result = ((CommonTree) root.getChild(position)).getToken();

			position++;
			return result;
		}
	}

	@Override
	public String getSourceName() {
		Token token = root.getToken();

		if (token == null)
			return "unknown";

		CharStream stream = token.getInputStream();

		if (stream == null)
			return "unknown";

		String name = stream.getSourceName();

		if (name == null)
			return "unknown";

		return name;
	}
}
