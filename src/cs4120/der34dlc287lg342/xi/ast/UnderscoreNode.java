package cs4120.der34dlc287lg342.xi.ast;

import java.util.ArrayList;
import java.util.List;

import cs4120.der34dlc287lg342.xi.typechecker.ContextList;
import cs4120.der34dlc287lg342.xi.typechecker.XiPrimitiveType;
import cs4120.der34dlc287lg342.xi.typechecker.XiType;
import cs4120.der34dlc287lg342.xi.typechecker.XiTypeContext;

import edu.cornell.cs.cs4120.util.VisualizableTreeNode;
import edu.cornell.cs.cs4120.xi.CompilationException;
import edu.cornell.cs.cs4120.xi.Position;

public class UnderscoreNode extends AbstractSyntaxTree {

	protected Position position;
	public UnderscoreNode(Position position){
		this.position = position;
	}
	
	@Override
	public Position position() {
		return position;
	}

	@Override
	public Iterable<VisualizableTreeNode> children() {
		// EMPTY
		return new ArrayList<VisualizableTreeNode>();
	}

	@Override
	public String label() {
		return "UNDERSCORE";
	}
	
	@Override
	public XiType typecheck(ContextList stack) throws CompilationException {
		return XiPrimitiveType.UNIT;
	}

}