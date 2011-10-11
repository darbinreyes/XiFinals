package cs4120.der34dlc287lg342.xi.ast;


import cs4120.der34dlc287lg342.xi.ir.Expr;
import cs4120.der34dlc287lg342.xi.ir.Stmt;
import cs4120.der34dlc287lg342.xi.ir.context.IRContextStack;
import cs4120.der34dlc287lg342.xi.ir.translate.IRTranslation;
import cs4120.der34dlc287lg342.xi.typechecker.ContextList;
import cs4120.der34dlc287lg342.xi.typechecker.XiType;
import edu.cornell.cs.cs4120.util.VisualizableTreeNode;
import edu.cornell.cs.cs4120.xi.CompilationException;
import edu.cornell.cs.cs4120.xi.Position;

public abstract class ExpressionNode extends AbstractSyntaxTree {

	@Override
	public Position position() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<VisualizableTreeNode> children() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String label() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public XiType typecheck(ContextList stack) throws CompilationException{
		return type;
	}

	public IRTranslation to_ir(IRContextStack stack){
		return null;
	}
}
