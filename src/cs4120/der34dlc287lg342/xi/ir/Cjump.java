package cs4120.der34dlc287lg342.xi.ir;

import cs4120.der34dlc287lg342.xi.ir.context.Label;
import edu.cornell.cs.cs4120.util.VisualizableTreeNode;

public class Cjump extends Stmt {
	public Expr cond;
	public Label iftrue, iffalse;
	public Cjump(Expr cond, Label iftrue, Label iffalse){
		super();
		this.cond = cond;
		this.iftrue = iftrue;
		this.iffalse = iffalse;
		children.add(cond);
	}
	
	@Override
	public Seq lower(){
		Eseq eseq = cond.lower();
		Seq affects = (Seq) eseq.stmts;
		Seq seq = new Seq();
		for (VisualizableTreeNode c : affects.children){
			Seq s = ((Stmt)c).lower();
			seq.children.addAll(s.children);
		}
		seq.add(new Cjump(eseq.expr, iftrue, iffalse));
		return seq;
	}
}
