package cs4120.der34dlc287lg342.xi.ast;

import java.util.ArrayList;

import cs4120.der34dlc287lg342.xi.typechecker.ContextList;
import cs4120.der34dlc287lg342.xi.typechecker.XiFunctionType;
import cs4120.der34dlc287lg342.xi.typechecker.XiPrimitiveType;
import cs4120.der34dlc287lg342.xi.typechecker.XiType;

import edu.cornell.cs.cs4120.util.VisualizableTreeNode;
import edu.cornell.cs.cs4120.xi.AbstractSyntaxNode;
import edu.cornell.cs.cs4120.xi.CompilationException;
import edu.cornell.cs.cs4120.xi.Position;

public class FuncCallNode extends ExpressionNode {

	protected Position position;
	protected AbstractSyntaxNode id;
	protected ArrayList<VisualizableTreeNode> children, args;
	public FuncCallNode(IdNode id, ArrayList<VisualizableTreeNode> args, Position position){
		this.id = id;
		this.args = args;
		this.position = position;
		children = new ArrayList<VisualizableTreeNode>();
		children.add(id);
		children.addAll(args);
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
		return "CALL("+args.size()+")";
	}

	@Override
	public XiType typecheck(ContextList stack) throws CompilationException{
		IdNode id = (IdNode)this.id;
		XiType t = id.typecheck(stack);
		if (!(t instanceof XiFunctionType))
			throw new CompilationException("Attempt to call a nonfunction object", position());
		XiFunctionType func = (XiFunctionType)t;
		// make sure that args agree
		
		if (func.args.size() != args.size()){
			throw new CompilationException("Incorrect number of arguments applied to the function '"+id.id+"': expected "+args.size() + " but got "+func.args.size()+" instead", position());
		}
		int i = 0;
		for (VisualizableTreeNode child : args){
			if (child instanceof ExpressionNode){
				ExpressionNode expr = (ExpressionNode)child;
				XiType arg_type = expr.typecheck(stack);
				XiPrimitiveType expected = func.args.get(i++);
				if (!arg_type.equals(expected))
					throw new CompilationException("Invalid argument("+(i-1)+") of function '"+id.id+"': expected "+expected+", but got "+arg_type+" instead", position());
			} else {
				throw new CompilationException("Expected an expression type but got "+child.label()+" instead", position());
			}
		}
		
		type = ((XiFunctionType)t).returns().coerce();
		return type;
	}
	
	@Override
	public AbstractSyntaxTree foldConstants(){
		((AbstractSyntaxTree)id).foldConstants();
		
		int i = 0;
		for (VisualizableTreeNode arg : args){
			if (arg != null){
				AbstractSyntaxTree tree = ((AbstractSyntaxTree)arg).foldConstants();
				if (tree != null){
					args.set(i, tree);
					children.set(i+1, tree); // children is id + args
				}
			}
			i++;
		}
		
		// see if the funcdeclnode is trivial or not
		if (type != null && type instanceof XiPrimitiveType){
			// funcdecl with only one child, a return const, will be inlined
			// create a delegate class XiConstants that does a first pass over functions
			// this is not a large optimization problem so not really necessary
		}
		
		return null;
	}
}
