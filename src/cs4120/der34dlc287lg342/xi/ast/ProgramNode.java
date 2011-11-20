package cs4120.der34dlc287lg342.xi.ast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import cs4120.der34dlc287lg342.xi.XiInterfaceParser;
import cs4120.der34dlc287lg342.xi.ir.Arg;
import cs4120.der34dlc287lg342.xi.ir.Binop;
import cs4120.der34dlc287lg342.xi.ir.Call;
import cs4120.der34dlc287lg342.xi.ir.Cjump;
import cs4120.der34dlc287lg342.xi.ir.Const;
import cs4120.der34dlc287lg342.xi.ir.Dseq_ro;
import cs4120.der34dlc287lg342.xi.ir.Exp;
import cs4120.der34dlc287lg342.xi.ir.Func;
import cs4120.der34dlc287lg342.xi.ir.Jump;
import cs4120.der34dlc287lg342.xi.ir.LabelNode;
import cs4120.der34dlc287lg342.xi.ir.Mem;
import cs4120.der34dlc287lg342.xi.ir.Move;
import cs4120.der34dlc287lg342.xi.ir.Name;
import cs4120.der34dlc287lg342.xi.ir.Return_DEPRECATED;
import cs4120.der34dlc287lg342.xi.ir.Seq;
import cs4120.der34dlc287lg342.xi.ir.Stmt;
import cs4120.der34dlc287lg342.xi.ir.Temp;
import cs4120.der34dlc287lg342.xi.ir.context.IRContext;
import cs4120.der34dlc287lg342.xi.ir.context.IRContextStack;
import cs4120.der34dlc287lg342.xi.ir.context.InvalidIRContextException;
import cs4120.der34dlc287lg342.xi.ir.context.Label;
import cs4120.der34dlc287lg342.xi.ir.context.TempRegister;
import cs4120.der34dlc287lg342.xi.ir.translate.IRTranslation;
import cs4120.der34dlc287lg342.xi.ir.translate.IRTranslationStmt;
import cs4120.der34dlc287lg342.xi.typechecker.ContextList;
import cs4120.der34dlc287lg342.xi.typechecker.XiFunctionType;
import cs4120.der34dlc287lg342.xi.typechecker.XiPrimitiveType;
import cs4120.der34dlc287lg342.xi.typechecker.XiType;

import edu.cornell.cs.cs4120.util.VisualizableTreeNode;
import edu.cornell.cs.cs4120.xi.AbstractSyntaxNode;
import edu.cornell.cs.cs4120.xi.CompilationException;
import edu.cornell.cs.cs4120.xi.Position;

public class ProgramNode extends AbstractSyntaxTree {

	public Position position;
	protected ArrayList<VisualizableTreeNode> children = new ArrayList<VisualizableTreeNode>();
	private static Stmt strcat;
	
	public ProgramNode(Position position){
		this.position = position;
	}
	
	public void add(AbstractSyntaxNode expr){
		children.add(expr);
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
		return "PROGRAM";
	}
	
	/** Type checks this program node. A program node type checks if
	 * all its function declaration children type check as XiFunctionTypes.
	 * 
	 * @param stack The current context stack. 
	 */
	@Override
 	public XiType typecheck(ContextList stack) throws CompilationException {
		for( VisualizableTreeNode childTree : children) {
			if( childTree instanceof FuncDeclNode) {
				XiType childType = ((AbstractSyntaxTree)childTree).typecheck(stack);
				
				if(!(childType instanceof XiFunctionType))
					throw new CompilationException("Invalid program",position);
			}
		}
		
		type = XiPrimitiveType.UNIT;
		return type;
	}

	// add function declarations found within interfaces into the global context
	// TODO: refactor into a single pass
	void include(String id, IRContextStack stack) throws IOException, InvalidIRContextException{
		FileReader reader = new FileReader(id+".ixi");
		
		String src = "";
		BufferedReader input =  new BufferedReader(reader);
		String line = null;
		while (( line = input.readLine()) != null){
			src += line.replace("\r", "") + "\n";
		}
		
		XiInterfaceParser parser = new XiInterfaceParser(new StringReader(src), id+".ixi");
		AbstractSyntaxNode declarations;
		try{
			declarations = parser.parse();
		} catch (CompilationException e){
			throw new InvalidIRContextException(e.getMessage());
		}
		for (VisualizableTreeNode child : declarations.children()){
			FuncDeclNode decl = (FuncDeclNode)child;
			stack.add_name(decl);
		}
	}
	
	@Override
	public IRTranslation to_ir(IRContextStack stack) throws InvalidIRContextException{
		/*
		 * 2 passes
		 * first one builds the context with globals
		 * second one addes a sequence of funcdecls
		 */
		
		// First pass: build context
		IRContext global = new IRContext();
		stack.push(global);
		
		for (VisualizableTreeNode child : children()){
			if (child instanceof UseNode){
				UseNode use = (UseNode) child;
				try {
					include(use.lib.id, stack);
				} catch (IOException e) {
					throw new InvalidIRContextException(e.getMessage());
				}
			} else if (child instanceof FuncDeclNode){
				FuncDeclNode func = (FuncDeclNode) child;
				stack.add_name(func);
			}
		}
		
		// Second pass: build translation rules
		Seq seq = new Seq();
		for (VisualizableTreeNode child : children()){
			if (child instanceof FuncDeclNode){
				FuncDeclNode func = (FuncDeclNode) child;
				IRTranslation tr = func.to_ir(stack);
				seq.add(tr.stmt());
			}
		}
		
		// Add in auxiliary functions
		if (stack.dynamic_allocation){
			if (dynamalloc_method == null)
				/*
				"dynamalloc(arr:int[]):int[]{" +
				"	n:int = length(arr) i:int = 1" +
				"   if (n == 0) {return ()}" +
				"	tl:int[] = alloca(n+1)" +
				"	while (i<n){" +
				"		tl[i-1] = arr[i]" +
				"	}" +
				"	i = arr[0]" +
				"	list:int[] = alloca(i+1)" +
				"	while (i > 0){" +
				"		i = i - 1" +
				"		a:int[] = dynamalloc(tl) a':int" +
				"		list[i] = a'" +
				"	}"+
				"	return list;" +
				"} "
				 */
				dynamalloc_method = new Func(Label.dynamalloc,
					new Arg(reg("arr").temp, 0),
					new Move(reg("n"),new Mem(new Binop(Binop.MINUS,reg("arr"),new Const(8)))),
					new Move(reg("i"),new Const(1)),
					new Cjump(new Binop(Binop.NE,reg("n"),new Const(0)),label("213"),label("212")),
					new LabelNode(label("212")),
					new Move(reg("rv"),new Const(0)), // equivalent to null as well
					ret,
					new LabelNode(label("213")),
					new Move(reg(5450),new Call(new Name(Label.alloc),new Binop(Binop.PLUS, reg("n"), new Const(1)))),
					new Move(new Mem(reg(5450)), reg("n")),
					new Move(reg(545), new Binop(Binop.PLUS, reg(5450), new Const(8))),
					new Move(reg("tl"),reg(545)),
					new LabelNode(label("214")),
					new Cjump(new Binop(Binop.GE,reg("i"),reg("n")),label("216"),label("215")),
					new LabelNode(label("215")),
					new Move(reg(532),reg("arr")),
					new Move(reg(533),reg("i")),
					new Cjump(new Binop(Binop.LT,reg(533),new Mem(new Binop(Binop.MINUS,reg(532),new Const(8)))),label("219"),label("220")),
					new LabelNode(label("220")),
					new Exp(new Call(new Name(Label.outOfBounds))),
					new LabelNode(label("219")),
					new Move(reg(530),reg("tl")),
					new Move(reg(531),new Binop(Binop.MINUS,reg("i"),new Const(1))),
					new Cjump(new Binop(Binop.LT,reg(531),new Mem(new Binop(Binop.MINUS,reg(530),new Const(8)))),label("217"),label("218")),
					new LabelNode(label("218")),
					new Exp(new Call(new Name(Label.outOfBounds))),
					new LabelNode(label("217")),
					new Move(new Mem(new Binop(Binop.PLUS,reg(530),new Binop(Binop.LSH,reg(531),new Const(3)))),new Mem(new Binop(Binop.PLUS,reg(532),new Binop(Binop.LSH,reg(533),new Const(3))))),
					new Jump(label("214")),
					new LabelNode(label("216")),
					new Move(reg(534),reg("arr")),
					new Move(reg(535),new Const(0)),
					new Cjump(new Binop(Binop.LT,reg(535),new Mem(new Binop(Binop.MINUS,reg(534),new Const(8)))),label("221"),label("222")),
					new LabelNode(label("222")),
					new Exp(new Call(new Name(Label.outOfBounds))),
					new LabelNode(label("221")),
					new Move(reg("i"),new Mem(new Binop(Binop.PLUS,reg(534),new Binop(Binop.LSH,reg(535),new Const(3))))),
					//new Move(reg(552),new Call(new Name(Label.alloc),reg("i"))),
					new Move(reg(5520),new Call(new Name(Label.alloc),new Binop(Binop.PLUS, reg("i"), new Const(1)))),
					new Move(new Mem(reg(5520)), reg("i")),
					new Move(reg(552), new Binop(Binop.PLUS, reg(5520), new Const(8))),
					new Move(reg("list"),reg(552)),
					new LabelNode(label("223")),
					new Cjump(new Binop(Binop.LE,reg("i"),new Const(0)),label("225"),label("224")),
					new LabelNode(label("224")),
					new Move(reg("i"),new Binop(Binop.MINUS,reg("i"),new Const(1))),
					new Move(reg(553),new Call(new Name(Label.dynamalloc),reg("tl"))),
					new Move(reg("a"),reg(553)),
					new Move(reg(539),reg("list")),
					new Move(reg(540),reg("i")),
					new Cjump(new Binop(Binop.LT,reg(540),new Mem(new Binop(Binop.MINUS,reg(539),new Const(8)))),label("226"),label("227")),
					new LabelNode(label("227")),
					new Exp(new Call(new Name(Label.outOfBounds))),
					new LabelNode(label("226")),
					new Move(new Mem(new Binop(Binop.PLUS,reg(539),new Binop(Binop.LSH,reg(540),new Const(3)))),reg("a")),
					new Jump(label("223")),
					new LabelNode(label("225")),
					new Move(new Temp(TempRegister.RV),reg("list")),
					ret
				);
			seq.add(dynamalloc_method);

		}
		
		if (stack.strdup){
			if (strdup == null){
				/*
				 * SEQ(
			         DECL_REG(REG0), 
			         MOVE(TEMP(p0), TEMP(REG0)), 
			         MOVE(TEMP(t10), MEM(TEMP(p0))), 
			         MOVE(TEMP(t27), CALL(NAME(_I_alloc_i), ADD(AR_LSHIFT(TEMP(t10), 
			           CONST(3)), CONST(8)))), 
			         LABEL(.L9), 
			         MOVE(MEM(ADD(TEMP(t27), AR_LSHIFT(TEMP(t10), 
			           CONST(3)))), MEM(ADD(TEMP(p0), AR_LSHIFT(TEMP(t10), CONST(3))))), 
			         MOVE(TEMP(t10), SUB(TEMP(t10), CONST(1))), 
			         CJUMP(GE(TEMP(t10), CONST(0)), .L9, <none>), 
			         MOVE(TEMP(RV), ADD(TEMP(t27), CONST(8)))
			      )
				 * */
				strdup = new Func(Label.internal_strdup, 
					new Arg(reg("p0").temp, 0),
					new Move(reg("t10"), new Mem(reg("p0"))),
					new Move(reg("t27"), new Call(new Name(Label.alloc), new Binop(Binop.PLUS, new Binop(Binop.LSH, reg("t10"), new Const(3)), new Const(8)))),
					new LabelNode(label(".L9")),
					new Move(new Mem(new Binop(Binop.PLUS, reg("t27"), new Binop(Binop.LSH, reg("t10"), new Const(3)))), 
							 new Mem(new Binop(Binop.PLUS, reg("p0"), new Binop(Binop.LSH, reg("t10"), new Const(3))))),
					new Move(reg("t10"), new Binop(Binop.MINUS, reg("t10"), new Const(1))),
					new Cjump(new Binop(Binop.GE, reg("t10"), new Const(0)), label(".L9"), label(".Lnull")),
					new LabelNode(label(".Lnull")),
					new Move(new Temp(TempRegister.RV), new Binop(Binop.PLUS, reg("t27"), new Const(8)))
				);
			}
			seq.add(strdup);
		}
		
		if (stack.strcat){
			if (strcat == null){
				/* 
				 * i:int = 0
				 * n1:int = length(lhs)
				 * n2:int = n1 + length(rhs)
				 * arr:*[n2]
				 * while (i < n1){
				 *   arr[i] = lhs[i]
				 *   i = i + 1
				 * }
				 * while (i < n2){
				 *   arr[i] = rhs[i-n1]
				 *   i = i + 1
				 * }
				 */
				Temp i = new Temp(new TempRegister()), n1 = new Temp(new TempRegister()), n2 = new Temp(new TempRegister()), arr = new Temp(new TempRegister());
				Temp base = new Temp(new TempRegister()), l = new Temp(new TempRegister()), r = new Temp(new TempRegister());
				LabelNode a = new LabelNode(new Label()), b = new LabelNode(new Label()), c = new LabelNode(new Label());
				LabelNode a_ = new LabelNode(new Label()), b_ = new LabelNode(new Label()), c_ = new LabelNode(new Label());
				strcat = new Func(Label.internal_strcat,
					new Arg(l.temp, 0),
					new Arg(r.temp, 1),
					new Move(i, new Const(0)),
					//new Move(l, lhs), new Move(r, rhs),
					new Move(n1, TempRegister.size_of(l)),
					new Move(n2, new Binop(Binop.PLUS, n1, TempRegister.size_of(r))),
					new Move(base, new Call(new Name(Label.alloc), new Binop(Binop.PLUS, n2, new Const(1)))),
					new Move(new Mem(base), n2),
					new Move(arr, new Binop(Binop.PLUS, base, new Const(8))),
					a, // check cond
					new Cjump(new Binop(Binop.LT, i, n1), b.label, c.label),
					b, // body
					new Move(
						new Mem(new Binop(Binop.PLUS, arr, new Binop(Binop.LSH, i, new Const(3)))), 
						new Mem(new Binop(Binop.PLUS, l, new Binop(Binop.LSH, i, new Const(3))))
					), // arr[i] = lhs[i]
					new Move(i, new Binop(Binop.PLUS, i, new Const(1))),
					new Jump(a.label),
					c, // end
					
					a_, // check cond
					new Cjump(new Binop(Binop.LT, i, n2), b_.label, c_.label),
					b_, // body
					new Move(
						new Mem(new Binop(Binop.PLUS, arr, new Binop(Binop.LSH, i, new Const(3)))), 
						new Mem(new Binop(Binop.PLUS, r, new Binop(Binop.LSH, new Binop(Binop.MINUS, i, n1), new Const(3))))
					), // arr[i] = rhs[i-n1]
					new Move(i, new Binop(Binop.PLUS, i, new Const(1))),
					new Jump(a_.label),
					c_, // end
					new Move(new Temp(TempRegister.RV), arr)
				);
			}
			seq.add(strcat);
		}
		
		for (Entry<Label, byte[]> e : stack.ro_data.entrySet()){
			seq.add(new Dseq_ro(e.getKey(), e.getValue()));
		}
		
		if (stack.abort){
			seq.add(new LabelNode(Label.outofbounds_jump));
			seq.add(new Exp(new Call(new Name(Label.outOfBounds))));
		}
		
		return new IRTranslationStmt(seq);
	}
	
	public static Seq dynamalloc_method = null;
	public static Seq strdup = null;
	
	// temporary context
	public static HashMap<String, Temp> reg_s = new HashMap<String, Temp>();
	public static HashMap<String, Label> label_s = new HashMap<String, Label>();
	public static HashMap<Integer, Temp> reg_i = new HashMap<Integer, Temp>();
	public static Temp reg(String s){
		if (reg_s.containsKey(s))
			return reg_s.get(s);
		
		Temp t = new Temp(new TempRegister(s));
		reg_s.put(s, t);
		return t;
	}
	public static Temp reg(int s){
		if (reg_i.containsKey(s))
			return reg_i.get(s);
		
		Temp t = new Temp(new TempRegister());
		reg_i.put(s, t);
		return t;
	}
	public static Label label(String s){
		if (label_s.containsKey(s))
			return label_s.get(s);
		
		Label t = new Label();
		label_s.put(s, t);
		return t;
	}
	public static Return_DEPRECATED ret = new Return_DEPRECATED();
}
