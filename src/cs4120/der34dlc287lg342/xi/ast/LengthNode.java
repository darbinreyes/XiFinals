package cs4120.der34dlc287lg342.xi.ast;

import java.util.ArrayList;

import cs4120.der34dlc287lg342.xi.ir.Binop;
import cs4120.der34dlc287lg342.xi.ir.Const;
import cs4120.der34dlc287lg342.xi.ir.Expr;
import cs4120.der34dlc287lg342.xi.ir.Mem;
import cs4120.der34dlc287lg342.xi.ir.context.IRContextStack;
import cs4120.der34dlc287lg342.xi.ir.context.InvalidIRContextException;
import cs4120.der34dlc287lg342.xi.ir.context.TempRegister;
import cs4120.der34dlc287lg342.xi.ir.translate.IRTranslation;
import cs4120.der34dlc287lg342.xi.ir.translate.IRTranslationExpr;
import cs4120.der34dlc287lg342.xi.typechecker.ContextList;
import cs4120.der34dlc287lg342.xi.typechecker.XiPrimitiveType;
import cs4120.der34dlc287lg342.xi.typechecker.XiType;

import edu.cornell.cs.cs4120.util.VisualizableTreeNode;
import edu.cornell.cs.cs4120.xi.AbstractSyntaxNode;
import edu.cornell.cs.cs4120.xi.CompilationException;
import edu.cornell.cs.cs4120.xi.Position;

public class LengthNode extends ExpressionNode {

	protected Position position;
	protected AbstractSyntaxTree args;
	protected ArrayList<VisualizableTreeNode> children;
	public LengthNode(AbstractSyntaxNode args, Position position){
		this.args = (AbstractSyntaxTree) args;
		this.position = position;
		children = new ArrayList<VisualizableTreeNode>();
		children.add(args);
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
		return "LENGTH";
	}

	@Override
	public XiType typecheck(ContextList stack) throws CompilationException{
		XiType t_ = ((AbstractSyntaxTree)args).typecheck(stack);
		
		if (!(t_ instanceof XiPrimitiveType))
			throw new CompilationException("Cannot apply length to nonprimitive type ["+t_+"]", args.position());
		
		XiPrimitiveType t = (XiPrimitiveType)t_;
		if (t.isArrayType()){
			type = XiPrimitiveType.INT;
			return type;
		}
		else
			throw new CompilationException("Cannot apply length to non-array types", position());
	}
	
	@Override
	public AbstractSyntaxTree foldConstants(){
		// rhs can be a constant
		AbstractSyntaxTree list = ((AbstractSyntaxTree)args).foldConstants();
		args = resolve_const(0,list,args);
		
		// check for list's dimension
//		if (args instanceof ListNode){
//			IntegerLiteralNode i = new IntegerLiteralNode(((ListNode)args).length(), position);
//			i.type = type;
//			return null; // take care of side effects, do this at MIR stage
//		} else if (args instanceof AbstractSyntaxTree && ((AbstractSyntaxTree)args).type != null){
//			AbstractSyntaxTree tree = (AbstractSyntaxTree)args;
//			ArrayList<VisualizableTreeNode> arr = ((XiPrimitiveType)tree.type).dimension;
//			if (arr.isEmpty()){
//				return null;
//			} else {
//				VisualizableTreeNode end = arr.get(arr.size()-1);
//				if (end instanceof IntegerLiteralNode){
//					AbstractSyntaxTree i = (AbstractSyntaxTree) end;
//					if (i.type == null) i.type = type;
//					return i;
//				}
//			}
//		}
		
		return null;
	}
	
	@Override
	public IRTranslation to_ir(IRContextStack stack) throws InvalidIRContextException{
		/*
		 * Mem(Sub(base_addr, Const(8)))
		 */
		IRTranslation tr = args.to_ir(stack);
		Expr base_addr = tr.expr();
		Expr length = new Mem(new Binop(Binop.MINUS, base_addr, new Const(8)));
		return new IRTranslationExpr(TempRegister.size_of(base_addr));
	}
}
