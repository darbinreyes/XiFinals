package cs4120.der34dlc287lg342.xi.cfg;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import cs4120.der34dlc287lg342.xi.ir.Call;
import cs4120.der34dlc287lg342.xi.ir.Cjump;
import cs4120.der34dlc287lg342.xi.ir.Const;
import cs4120.der34dlc287lg342.xi.ir.Expr;
import cs4120.der34dlc287lg342.xi.ir.Mem;
import cs4120.der34dlc287lg342.xi.ir.Move;
import cs4120.der34dlc287lg342.xi.ir.Name;
import cs4120.der34dlc287lg342.xi.ir.Stmt;
import cs4120.der34dlc287lg342.xi.ir.Temp;
import cs4120.der34dlc287lg342.xi.ir.context.TempRegister;
import edu.cornell.cs.cs4120.util.VisualizableTreeNode;

public class AvailableCopies {
	public ArrayList<CFG> worklist;
	public Hashtable<CFG, HashSet<Move>> copy_map;
	public Hashtable<CFG, TempRegister> kill_map;
	public HashSet<CFG> kill_mem;
	CFG node;
	
	public AvailableCopies(CFG node){
		this.node = node;
		worklist = new ArrayList<CFG>();
		copy_map = new Hashtable<CFG, HashSet<Move>>();
		kill_map = new Hashtable<CFG, TempRegister>();
		generate_worklist(this.node, new HashSet<CFG>());
	}

	private void generate_worklist(CFG node, HashSet<CFG> seen) {
		if (seen.contains(node)){
			return;
		}
		seen.add(node);
		HashSet<Move> exprs = gen(node);
		
		worklist.add(node);
		copy_map.put(node, kill(exprs, node));
		
		if(node.child1 != null) 
			generate_worklist(node.child1, seen);
		
		if (node.child2 != null) 
			generate_worklist(node.child2, seen);
	}
	
	public void analyze(){
		while (!worklist.isEmpty()) {
			CFG node = worklist.get(0);
			worklist.remove(0);
			boolean changed = false;
			// each iteration, recompute in with a new cache
			Hashtable<CFG, HashSet<Move>> seen = new Hashtable<CFG, HashSet<Move>>();
			seen.put(this.node, new HashSet<Move>());
			
			HashSet<Move> union = out(node, seen);
			changed = !union.equals(node.out_available);
			
			if (changed) {
				node.out_copy = union;
				
				for (CFG next : node.succ()) {
					if (!worklist.contains(next)) {
						worklist.add(next);
					}
				}
			}
		}
	}
	
	public HashSet<Move> out(CFG n, Hashtable<CFG, HashSet<Move>> seen){
		if (seen.containsKey(n)){
			return seen.get(n);
		}
		HashSet<Move> gen = new HashSet<Move>();
		if (copy_map.containsKey(n))
			gen.addAll(copy_map.get(n));
		for (CFG p : n.pred()){
			HashSet<Move> cur = kill(p.out_copy, n);
			gen.addAll(cur);
		}
		seen.put(n, gen);
		
		return gen;
	}
	
	public HashSet<Move> gen(CFG node){
		Stmt stmt = node.ir;
		HashSet<Move> moves = new HashSet<Move>();
		
		if (stmt instanceof Move && ((Move)stmt).dest instanceof Temp && ((Move)stmt).val instanceof Temp){
			// copy
			moves.add((Move) stmt);
			kill_map.put(node, ((Temp)((Move)stmt).dest).temp);
		} else if (stmt instanceof Move && ((Move)stmt).dest instanceof Temp){
			// no copy, but kill
			kill_map.put(node, ((Temp)((Move)stmt).dest).temp);
		}
		
		return moves;
	}
	
	HashSet<Move> kill(HashSet<Move> set, CFG node){
		HashSet<Move> ret = new HashSet<Move>();
		if (kill_map.containsKey(node)){
			TempRegister r = kill_map.get(node);
			for (Move move : set){
//				if (!uses(e, r)){
//					ret.add(e);
//				}
				TempRegister use = ((Temp)move.val).temp;
				if (!r.equals(use)){
					ret.add(move);
				}
			}
		}
		
		return ret;
	}
}
