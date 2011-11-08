package cs4120.der34dlc287lg342.xi.ir.translate;

import java.lang.reflect.Field;
import java.util.ArrayList;

import cs4120.der34dlc287lg342.xi.ir.*;
import edu.cornell.cs.cs4120.util.VisualizableTreeNode;

public class ConstantFolding {
	
	public static Func foldConstants(Func stmts){
		Seq seq = foldConstants((Seq)stmts);
		Func f = new Func(stmts.name);
		f.addAll(seq);
		return f;
	}
	
	public static Seq foldConstants(Seq stmts){
		int i = 0;
		for (VisualizableTreeNode child : stmts.children()){
			Stmt stmt = (Stmt)child;
			
			if (stmt instanceof Func){
				foldConstants((Func)stmt);
			} else {
				ArrayList<VisualizableTreeNode> arr = (ArrayList<VisualizableTreeNode>)stmt.children();
				// introspect stmt
				for (Field field : stmt.getClass().getDeclaredFields()){
					
					field.setAccessible(true);
					Object o;
					try {
						o = field.get(stmt);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					if (o instanceof Expr){
						Expr expr = (Expr)o;
						Expr new_expr = foldConstants(expr);
						if (new_expr != null){
							try {
								field.set(stmt, new_expr);
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
							int j = arr.indexOf(expr);
							if (j > -1)
								arr.set(j, new_expr);
						}
					} 
				}
			}
			
			// check special cases
			if (stmt instanceof Cjump){
				Cjump cjump = (Cjump)stmt;
				if (cjump.condition instanceof Const){
					int v = ((Const)cjump.condition).value;
					if (v == 0){
						// if false 
						// replace with jump(iffalse)
						Jump jump = new Jump(cjump.iffalse);
						stmts.children.set(i, jump);
					}else{
						// if true 
						// replace with jump(true)
						Jump jump = new Jump(cjump.to);
						stmts.children.set(i, jump);
					}
				}
			}
			
			i++;
		}
		return stmts;
	}
	
	public static Expr foldConstants(Expr expr){
		ArrayList<VisualizableTreeNode> arr = (ArrayList<VisualizableTreeNode>)expr.children();
		// introspect expr
		if (expr instanceof Binop)
			return foldConstants((Binop)expr);
		if (expr instanceof Call)
			return foldConstants((Call)expr);
		for (Field field : expr.getClass().getDeclaredFields()){
			field.setAccessible(true);
			Object o;
			try {
				o = field.get(expr);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			
			if (o instanceof Expr){
				Expr this_expr = (Expr)o;
				int i = arr.indexOf(this_expr);
				Expr new_expr = foldConstants(this_expr);
				if (new_expr != null){
					try {
						field.set(expr, new_expr);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					if (i > -1)
						arr.set(i, new_expr);
				}
			} 
		}
		return expr;
	}
	
	public static Expr foldConstants(Call expr){
		ArrayList<VisualizableTreeNode> children = (ArrayList<VisualizableTreeNode>)expr.children();
		for (int i = 0; i < expr.args.size(); i++){
			Expr arg = foldConstants(expr.args.get(i));
			if (arg != null){
				expr.args.set(i, arg);
				children.set(i, arg);
			}
		}
		return expr;
	}
	
	public static Expr foldConstants(Binop expr){
		Expr left = foldConstants(expr.left), right = foldConstants(expr.right);
		if (left instanceof Const && right instanceof Const){
			int l = ((Const)left).value, r = ((Const)right).value;
			switch (expr.op){
			case Binop.PLUS:
				return new Const(l+r);
			case Binop.MINUS:
				return new Const(l-r);
			case Binop.MUL:
				return new Const(l*r);
			case Binop.DIV:
				return new Const(l/r);
			case Binop.MOD:
				return new Const(l%r);
			case Binop.XOR:
				return new Const(l^r);
			case Binop.LSH:
				return new Const(l<<r);
			case Binop.RSH:
				return new Const(l>>r);
			case Binop.EQ:
				return new Const(l==r?1:0);
			case Binop.NE:
				return new Const(l!=r?1:0);
			case Binop.LT:
				return new Const(l<r?1:0);
			case Binop.LE:
				return new Const(l<=r?1:0);
			case Binop.GT:
				return new Const(l>r?1:0);
			case Binop.GE:
				return new Const(l>=r?1:0);
			}
		}
		
		boolean left1 = true;
		if (left instanceof Binop && right instanceof Const){
			Expr temp = right;
			right = left;
			left = temp;
			left1 = false;
		}
		
		if (left instanceof Const && right instanceof Binop){
			int l = ((Const)left).value;
			Binop bin  = ((Binop)right);
			if (bin.left instanceof Const || bin.right instanceof Const){
				int r;
				Expr e;
				boolean leftside = true;
				if (bin.left instanceof Const){
					r = ((Const)bin.left).value;
					e = bin.right;
				} else {
					r = ((Const)bin.right).value;
					e = bin.left;
					leftside = false;
				}
				
				// Cases where the operations commute
				if (expr.op == Binop.PLUS && bin.op == Binop.PLUS){
					return new Binop(Binop.PLUS, new Const(l+r), e);
				} else if (expr.op == Binop.MINUS && bin.op == Binop.MINUS){
					// leftside && left1 = true -> l-(r-e) -> (l-r)+e
					// leftside && else -> (r-e)-l -> (r-l)-e
					// else && left1-> l-(e-r) -> (l+r)-e
					// else -> (e-r)-l -> e-(r+l)
					if (leftside){
						if (left1)
							return new Binop(Binop.MINUS, e, new Const(r-l));
						else 
							return new Binop(Binop.MINUS, new Const(r-l), e);
					} else {
						if (left1)
							return new Binop(Binop.MINUS, new Const(l+r), e);
						else
							return new Binop(Binop.MINUS, e, new Const(r+l));
					}
				} else if (expr.op == Binop.MUL && bin.op == Binop.MUL){
					return new Binop(Binop.MUL, new Const(l*r), e);
				} else if (expr.op == Binop.DIV && bin.op == Binop.DIV){
					// leftside = true -> l/(r/e) -> (l/r)*e
					// else -> l/(e/r) -> (l*r)/e
					if (leftside){
						if (left1)
							return new Binop(Binop.MUL, new Const(l/r), e);
						else 
							return new Binop(Binop.DIV, new Const(r/l), e);
					} else {
						if (left1)
							return new Binop(Binop.DIV, new Const(l*r), e);
						else
							return new Binop(Binop.DIV, e, new Const(r*l));
					}
				}
			}
		}
		
		return expr;
	}
}
