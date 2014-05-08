/**
 * Public interface for the ast module, which deals with the Abstract Syntax
 * Tree.  This module is decomposed into the following submodules:
 * 
 * <ul>
 * 
 * <li>main: this package, declaring the interfaces 
 * {@link edu.udel.cis.vsl.abc.ast.IF.AST AST}
 * and {@link edu.udel.cis.vsl.abc.ast.IF.ASTFactory ASTFactory}.
 * </li>
 * 
 * <li><strong>value</strong> ({@link edu.udel.cis.vsl.abc.ast.value})
 *   <ul>
 *   <li>responsibilities: representation of concrete value, such as characters, integers, floating point values</li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.ast.value.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.ast.value.Values Values}</li>
 *   </ul>
 * </li>
 * 
 * <li><strong>type</strong> ({@link edu.udel.cis.vsl.abc.ast.type})
 *   <ul>
 *   <li>responsibilities: representation of types</li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.ast.type.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.ast.type.Types Types}</li>
 *   </ul>
 * </li>
 *
 * <li><strong>entity</strong> ({@link edu.udel.cis.vsl.abc.ast.entity})
 *   <ul>
 *   <li>responsibilities: representation of abstract program entities which can
 *   be named by an identifier, including variables, functions, labels, scopes, 
 *   structures, unions, enumerations, enumerators, and typedefs</li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.ast.entity.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.ast.entity.Entities Entities}</li>
 *   </ul>
 * </li>
 * 
 * <li><strong>conversion</strong> ({@link edu.udel.cis.vsl.abc.ast.conversion})
 *   <ul>
 *   <li>responsibilities: representation of C's implicit conversions, such
 *   as the conversion of an array to a pointer to the first element of the
 *   array, and so on</li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.ast.conversion.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.ast.conversion.Conversions Conversions}
 *   </ul>
 * </li>
 *
 * <li><strong>node</strong> ({@link edu.udel.cis.vsl.abc.ast.node})
 *   <ul>
 *   <li>responsibilities: representation of AST nodes</li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.ast.node.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.ast.node.Nodes Nodes}</li>
 *   </ul>
 * </li>
 * 
 * </ul>
 */
package edu.udel.cis.vsl.abc.ast.IF;

