/**
 *<p>
 * Welcome to ABC, the ANTLR-Based C front-end.   ABC is a Java
 * program for parsing and manipulating programs written in C
 * and possibly some languages extending C, such as CIVL-C.
 * It can be used to perform a number of tasks, including the following:
 * <ul>
 * <li>preprocess the source file(s)</li>
 * <li>parse the preprocessed source to form an Abstract Syntax Tree representation
 * of a translation unit</li>
 * <li>analyze an AST to determine the type of every expression, the entity
 * associated to every identifier, the scope of every node, and so on</li>
 * <li>merge multiple ASTs into one large AST</li>
 * <li>transform ASTs, by, for example, removing expressions with side-effects,
 * renaming entities, pruning unreachable code, etc.</li>
 * </ul>
 * In addition, it provides a framework for developing your own analyses or
 * transformations on ASTs.
 * </p>
 * 
 * There is a simple command-line interface for ABC (see
 * {@link edu.udel.cis.vsl.abc.ABC ABC}), but
 * most users will want to use ABC through its API.  To use the API, it is
 * helpful to have some understanding of the modular struture of ABC.
 * 
 * The ABC source code is decomposed into modules, each with a well-defined
 * interface and set of responsibilities.  Some of these modules
 * are decomposed further into sub-modules.  The top-level modules are:
 * <ol>
 * 
 * <li><strong>util</strong> ({@link edu.udel.cis.vsl.abc.util})
 *   <ul>
 *   <li>responsibilities: simple general-purpose utility classes that do not use
 *   other parts of ABC</li>
 *   <li>uses: nothing</li>
 *   <li>interface: none; each class can be used directly</li>
 *   <li>entry point: none</li>
 *   </ul>
 * </li>
 * 
 * <li><strong>token</strong> ({@link edu.udel.cis.vsl.abc.token})
 *   <ul>
 *   <li>responsibilities: representing tokens; keeping track of the 
 *   origin and history of each token through macro expansion and inclusion;
 *   representing characters and strings</li>
 *   <li>uses: <strong>util</strong></li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.token.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.token.Tokens Tokens}</li>
 *   </ul>
 * </li>
 * 
 * <li><strong>preproc</strong> ({@link edu.udel.cis.vsl.abc.preproc})
 *   <ul>
 *   <li>responsibilities: preprocessing; generation of post-preprocessor token stream</li>
 *   <li>uses: <strong>util</strong>, <strong>token</strong></li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.preproc.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.preproc.Preprocess Preprocess}</li>
 *   </ul>
 * </li>
 * 
 * <li><strong>config</strong> ({@link edu.udel.cis.vsl.abc.config})
 *   <ul>
 *   <li>responsibilities: representation of configuration parameters for ABC</li>
 *   <li>uses: nothing</li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.config.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.config.Configurations Configurations}</li>
 *   </ul>
 * </li>
 * 
 * <li><strong>parse</strong> ({@link edu.udel.cis.vsl.abc.parse})
 *   <ul>
 *   <li>responsibilities: preprocessing and parsing source files to produce an ANTRL tree representation of a translation unit</li>
 *   <li>uses: <strong>util</strong>, <strong>preproc</strong>, <strong>token</strong>, <strong>config</strong></li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.parse.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.parse.Parse Parse}</li>
 *   </ul>
 * </li>
 * 
 * <li><strong>ast</strong> ({@link edu.udel.cis.vsl.abc.ast})
 *   <ul>
 *   <li>responsibilities: representation and manipulation of an Abstract Syntax Tree and associated entities</li>
 *   <li>uses: <strong>util</strong>, <strong>parse</strong>, <strong>token</strong></li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.ast.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.ast.ASTs ASTs}</li>
 *   <li>submodules
 *     <ul>
 *     <li><strong>value</strong> ({@link edu.udel.cis.vsl.abc.ast.value})
 *       <ul>
 *       <li>responsibilities: representation of concrete value, such as characters, integers, floating point values</li>
 *       <li>interface: {@link edu.udel.cis.vsl.abc.ast.value.IF}</li>
 *       <li>entry point: {@link edu.udel.cis.vsl.abc.ast.value.Values Values}</li>
 *       </ul>
 *     </li>
 *     <li><strong>type</strong> ({@link edu.udel.cis.vsl.abc.ast.type})
 *       <ul>
 *       <li>responsibilities: representation of types</li>
 *       <li>interface: {@link edu.udel.cis.vsl.abc.ast.type.IF}</li>
 *       <li>entry point: {@link edu.udel.cis.vsl.abc.ast.type.Types Types}</li>
 *       </ul>
 *     </li>
 *     <li><strong>entity</strong> ({@link edu.udel.cis.vsl.abc.ast.entity})
 *       <ul>
 *       <li>responsibilities: representation of abstract program entities which can
 *       be named by an identifier, including variables, functions, labels, scopes, 
 *       structures, unions, enumerations, enumerators, and typedefs</li>
 *       <li>interface: {@link edu.udel.cis.vsl.abc.ast.entity.IF}</li>
 *       <li>entry point: {@link edu.udel.cis.vsl.abc.ast.entity.Entities Entities}</li>
 *       </ul>
 *     </li>
 *     <li><strong>conversion</strong> ({@link edu.udel.cis.vsl.abc.ast.conversion})
 *       <ul>
 *       <li>responsibilities: representation of C's implicit conversions, such
 *       as the conversion of an array to a pointer to the first element of the
 *       array, and so on</li>
 *       <li>interface: {@link edu.udel.cis.vsl.abc.ast.conversion.IF}</li>
 *       <li>entry point: {@link edu.udel.cis.vsl.abc.ast.conversion.Conversions Conversions}
 *       </ul>
 *     </li>
 *     <li><strong>node</strong> ({@link edu.udel.cis.vsl.abc.ast.node})
 *       <ul>
 *       <li>responsibilities: representation of AST nodes</li>
 *       <li>interface: {@link edu.udel.cis.vsl.abc.ast.node.IF}</li>
 *       <li>entry point: {@link edu.udel.cis.vsl.abc.ast.node.Nodes Nodes}</li>
 *       </ul>
 *     </li>
 *     </ul>
 *   </li>
 *   </ul>
 * </li>
 * 
 * <li><strong>antlr2ast</strong> ({@link edu.udel.cis.vsl.abc.antlr2ast})
 *   <ul>
 *   <li>responsibilities: translation of ANTLR tree to AST</li>
 *   <li>uses: <strong>preproc</strong>, <strong>parse</strong>, <strong>ast</strong></li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.antlr2ast.Antlr2AST}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.antlr2ast.Antlr2AST}</li>
 *   </ul>
 * </li>
 * 
 * <li><strong>analysis</strong> ({@link edu.udel.cis.vsl.abc.analysis})
 *   <ul>
 *   <li>responsibilities: analyzing AST, creation of entities, resolution of
 *   all identifiers, determination of all scopes, types, and entities</li>
 *   <li>uses: <strong></strong></li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.analysis.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.analysis.Analysis Analysis}</li>
 *   </ul>
 * </li>
 * 
 * <li><strong>transform</strong> ({@link edu.udel.cis.vsl.abc.transform})
 *   <ul>
 *   <li>responsibilities: transformations of an AST</li>
 *   <li>uses: <strong></strong></li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.transform.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.transform.Transform Transform}</li>
 *   </ul>
 * </li>
 * 
 * <li><strong>program</strong> ({@link edu.udel.cis.vsl.abc.program})
 *   <ul>
 *   <li>responsibilities: mutable representation of a program</li>
 *   <li>uses: <strong></strong></li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.program.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.program.Programs Programs}</li>
 *   </ul>
 * </li>
 * 
 * <li>main
 * </li>
 * 
 * </ol>
 */
package edu.udel.cis.vsl.abc;

