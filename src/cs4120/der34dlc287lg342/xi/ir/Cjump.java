package cs4120.der34dlc287lg342.xi.ir;

import cs4120.der34dlc287lg342.xi.ir.context.Label;
import cs4120.der34dlc287lg342.xi.tiles.CjumpTile;
import cs4120.der34dlc287lg342.xi.tiles.EqTile;
import cs4120.der34dlc287lg342.xi.tiles.Tile;

public class Cjump extends Stmt {
	public Expr condition;
	public Label to, iffalse;
	public Cjump(Expr cond, Label iftrue, Label iffalse){
		super();
		this.condition = cond;
		this.to = iftrue;
		this.iffalse = iffalse;
		children.add(cond);
	}
	
	@Override
	public Seq lower(){
		Eseq eseq = condition.lower();
		Seq affects = (Seq) eseq.stmts;
		Seq seq = new Seq();
		add_and_lower(seq, affects);
		seq.add(new Cjump(eseq.expr, to, iffalse));
		return seq;
	}
	
	@Override 
	public Tile munch(){
		Tile tile = null;
		
		if (condition instanceof Binop){
			Binop op = (Binop)condition;
			switch (op.op){
			case Binop.EQ:
			case Binop.LE:
			case Binop.LT:
			case Binop.GE:
			case Binop.GT:
			case Binop.UGE:
			case Binop.NE:
				tile = new EqTile(op.op, op.left.munch(), op.right.munch(), to);
				break;
			default:
				tile = new CjumpTile(condition.munch(), to);
			}
		}
		else if (condition instanceof Expr){
			tile = new CjumpTile(condition.munch(), to);
		}
		return tile;
	}
}
