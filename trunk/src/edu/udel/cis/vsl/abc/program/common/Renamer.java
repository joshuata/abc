package edu.udel.cis.vsl.abc.program.common;

import java.util.Map;

import edu.udel.cis.vsl.abc.ast.entity.IF.Entity;
import edu.udel.cis.vsl.abc.ast.node.IF.ASTNode;
import edu.udel.cis.vsl.abc.ast.node.IF.IdentifierNode;

public class Renamer {

	private Map<Entity, String> map;

	public Renamer(Map<Entity, String> map) {
		this.map = map;
	}

	public void renameFrom(ASTNode node) {
		if (node instanceof IdentifierNode) {
			IdentifierNode inode = (IdentifierNode) node;
			Entity entity = inode.getEntity();
			String newName = map.get(entity);

			if (newName != null) {
				inode.setName(newName);
			}
		} else {
			for (ASTNode child : node.children())
				renameFrom(child);
		}
	}
}
