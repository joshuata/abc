/**
 * <p>The ast module, providing an Abstract Syntax Tree
 * representation of a program.  This module is large
 * and is therefore divided into several submodules.
 * Each submodule has its own interface.  This package proper contains
 * the module entry point, {@link edu.udel.cis.vsl.abc.ast.IF.ASTs}
 * and an exception class specific to ASTs.</p>
 *
 * This module is decomposed into the following submodules:
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
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.ast.value.IF.Values Values}</li>
 *   </ul>
 * </li>
 * 
 * <li><strong>type</strong> ({@link edu.udel.cis.vsl.abc.ast.type})
 *   <ul>
 *   <li>responsibilities: representation of types</li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.ast.type.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.ast.type.IF.Types Types}</li>
 *   </ul>
 * </li>
 *
 * <li><strong>entity</strong> ({@link edu.udel.cis.vsl.abc.ast.entity})
 *   <ul>
 *   <li>responsibilities: representation of abstract program entities which can
 *   be named by an identifier, including variables, functions, labels, scopes, 
 *   structures, unions, enumerations, enumerators, and typedefs</li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.ast.entity.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.ast.entity.IF.Entities Entities}</li>
 *   </ul>
 * </li>
 * 
 * <li><strong>conversion</strong> ({@link edu.udel.cis.vsl.abc.ast.conversion})
 *   <ul>
 *   <li>responsibilities: representation of C's implicit conversions, such
 *   as the conversion of an array to a pointer to the first element of the
 *   array, and so on</li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.ast.conversion.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.ast.conversion.IF.Conversions Conversions}
 *   </ul>
 * </li>
 *
 * <li><strong>node</strong> ({@link edu.udel.cis.vsl.abc.ast.node})
 *   <ul>
 *   <li>responsibilities: representation of AST nodes</li>
 *   <li>interface: {@link edu.udel.cis.vsl.abc.ast.node.IF}</li>
 *   <li>entry point: {@link edu.udel.cis.vsl.abc.ast.node.IF.Nodes Nodes}</li>
 *   </ul>
 * </li>
 * 
 * </ul>
 */
package edu.udel.cis.vsl.abc.ast.IF;

