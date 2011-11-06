package cs4120.der34dlc287lg342.xi.ir.translate;

import java.util.ArrayList;

import cs4120.der34dlc287lg342.xi.ir.*;
import cs4120.der34dlc287lg342.xi.ir.context.Label;
import edu.cornell.cs.cs4120.util.VisualizableTreeNode;

public class LowerCjump {
	public static Func translate(Func seq){
		Seq s = translate((Seq)seq);
		Func func = new Func(seq.name);
		func.children.addAll(s.children);
		return func;
	}
	
	public static Seq translate(Seq seq){
		Seq ret = new Seq();
		// Go through the seq
		ArrayList<VisualizableTreeNode> children = (ArrayList<VisualizableTreeNode>)seq.children();
		for (int i = 0; i < children.size(); i++){
			Stmt child = (Stmt)children.get(i);
			if (child instanceof Func){
				ret.add(LowerCjump.translate((Func) child));
			} else if (child instanceof Cjump && i < children.size()-1){
				// get the label of the Cjump to reorder blocks
				Cjump cjump = (Cjump)child;
				
				Label iffalse = cjump.iffalse, iftrue = cjump.to;
				ret.add(child);
				// peek at the next label
				if (children.get(i+1) instanceof LabelNode){
					LabelNode label = (LabelNode) children.get(i+1);
					
					// Case 1: label is already false, in which case discard it and do nothing
					if (label.label.equals(iffalse)){
						// pass
						//i++;
					} else if (label.label.equals(iftrue)){
						// Case 2: switch label and negate the condition
						if (cjump.condition instanceof Binop){
							Binop op = (Binop)cjump.condition;
							switch (op.op){
							case Binop.LT:
								op.op = Binop.GE;
								break;
							case Binop.LE:
								op.op = Binop.GT;
								break;
							case Binop.GT:
								op.op = Binop.LE;
								break;
							case Binop.GE:
								op.op = Binop.LT;
								break;
							case Binop.EQ:
								op.op = Binop.NE;
								break;
							case Binop.NE:
								op.op = Binop.EQ;
								break;
							default:
								cjump.condition = new Binop(Binop.XOR, cjump.condition, new Const(1));
							}
						} else {
							cjump.condition = new Binop(Binop.XOR, cjump.condition, new Const(1));
						}
						//ret.add(new LabelNode(iffalse));
						//i++;
						cjump.iffalse = iftrue;
						cjump.to = iffalse;
					}
				} else if (cjump.condition instanceof Binop && ((Binop)cjump.condition).op != Binop.UGE) {
					// Case 3: Create a new false label lf and rewrite the cjump as
					// cjump(expr, a, b, iftrue)
					// jump(iffalse)
					ret.add(new Jump(iffalse));
				}
			} else {
				ret.add(child);
			}
		}
		return ret;
	}
}
