package cs4120.der34dlc287lg342.xi.tiles;

import cs4120.der34dlc287lg342.xi.ir.context.TempRegister;

public class SubTile extends BinopTile {
	public SubTile(Tile left, Tile right){
		super(left, right);
	}
	
	public String att(){
		String asm = before();
		asm += "subq %r14, "+out+"\n";
		return asm;
	}
}
