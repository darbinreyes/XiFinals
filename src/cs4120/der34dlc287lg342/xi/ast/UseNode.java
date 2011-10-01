package cs4120.der34dlc287lg342.xi.ast;

import java.util.ArrayList;

import edu.cornell.cs.cs4120.util.VisualizableTreeNode;
import edu.cornell.cs.cs4120.xi.AbstractSyntaxNode;
import edu.cornell.cs.cs4120.xi.Position;

public class UseNode extends AbstractSyntaxTree {
	protected Position position;
	public AbstractSyntaxNode lib;
	protected ArrayList<VisualizableTreeNode> children;
	public UseNode(AbstractSyntaxNode lib, Position position){
		this.lib = lib;
		this.position = position;
		children = new ArrayList<VisualizableTreeNode>();
		children.add(lib);
	}
	
	@Override
	public Position position() {
		return position;
	}

	@Override
	public Iterable<VisualizableTreeNode> children() {
		return children;
	}

	@Override
	public String label() {
		return "USE";
	}

}