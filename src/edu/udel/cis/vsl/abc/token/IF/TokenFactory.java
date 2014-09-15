package edu.udel.cis.vsl.abc.token.IF;

import java.util.List;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;

import edu.udel.cis.vsl.abc.token.IF.ExecutionCharacter.CharacterKind;

/**
 * A factory for producing all the objects under the control of the token
 * module. These includes instances of the following types (and their subtypes):
 * <ul>
 * <li>{@link CToken}</li>
 * <li>{@link Formation}</li>
 * <li>{@link ExecutionCharacter}</li>
 * <li>{@link Source}</li>
 * <li>{@link SyntaxException}</li>
 * <li>{@link UnsourcedException}</li>
 * <li>{@link Macro}</li>
 * </ul>
 * 
 * @author siegel
 * 
 */
public interface TokenFactory {

	// Formations (records of history of token creation)...

	MacroExpansion newMacroExpansion(CToken startToken, Macro macro, int index);

	Concatenation newConcatenation(List<CToken> tokens);

	/**
	 * Inclusion record for original source file.
	 * 
	 * @param file
	 *            the file which was included
	 * @return a new inclusion record
	 */
	Inclusion newInclusion(SourceFile file);

	Inclusion newInclusion(SourceFile file, Token includeToken);

	/**
	 * Creates a new formation which represents some code added by the system
	 * itself, as opposed to code that emanated from an actual source file. The
	 * identifier should be a short string indicating what part of the system
	 * created the code. Examples: "The CIVL-MPI Transformer".
	 * 
	 * @param identifier
	 *            short string indicating what part of the system created this
	 *            code; used in messages
	 * @return a new system formation object
	 */
	Formation newSystemFormation(String identifier);

	// Basic token creation...

	CToken newCToken(Token token, Formation formation);

	CToken newCToken(int type, String text, Formation formation);

	// Characters and Strings...

	ExecutionCharacter executionCharacter(CharacterKind kind, int codePoint,
			char[] characters);

	CharacterToken characterToken(CToken token) throws SyntaxException;

	StringToken newStringToken(CToken token) throws SyntaxException;

	StringToken newStringToken(List<CToken> tokens) throws SyntaxException;

	// Source objects...

	Source newSource(CToken token);

	Source newSource(CToken first, CToken last);

	Source join(Source source, CToken token);

	Source join(Source source1, Source source2);

	// Exceptions...

	SyntaxException newSyntaxException(String message, Source source);

	SyntaxException newSyntaxException(String message, CToken token);

	SyntaxException newSyntaxException(UnsourcedException e, Source source);

	SyntaxException newSyntaxException(UnsourcedException e, CToken token);

	UnsourcedException newUnsourcedException(String message);

	// Macros...

	ObjectMacro newObjectMacro(Tree definitionNode, SourceFile file);

	FunctionMacro newFunctionMacro(Tree definitionNode, SourceFile file);

	// TokenSources...

	CTokenSequence getTokenSubsequence(CTokenSource fullSource,
			CToken startToken, CToken stopToken);

	CTokenSequence getEmptyTokenSubsequence(CTokenSource originalSource);

}
