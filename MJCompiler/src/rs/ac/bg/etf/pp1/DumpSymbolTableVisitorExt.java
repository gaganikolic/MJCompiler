package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

public class DumpSymbolTableVisitorExt extends DumpSymbolTableVisitor {

	@Override
	public void visitStructNode(Struct structToVisit) {
		switch (structToVisit.getKind()) {
		case Struct.None:
			output.append("notype");
			break;
		case Struct.Int:
			output.append("int");
			break;
		case Struct.Char:
			output.append("char");
			break;
		case 5:
			output.append("bool");
			break;
		case Struct.Array:
			if(structToVisit.getElemType().getElemType() != null) {
				output.append("Matrix of ");
				switch (structToVisit.getElemType().getElemType().getKind()) {
				case Struct.None:
					output.append("notype");
					break;
				case Struct.Int:
					output.append("int");
					break;
				case Struct.Char:
					output.append("char");
					break;
				case 5:
					output.append("bool");
					break;
				case Struct.Class:
					output.append("Class");
					break;
				}
			} else {
				output.append("Arr of ");
				switch (structToVisit.getElemType().getKind()) {
				case Struct.None:
					output.append("notype");
					break;
				case Struct.Int:
					output.append("int");
					break;
				case Struct.Char:
					output.append("char");
					break;
				case 5:
					output.append("bool");
					break;
				case Struct.Class:
					output.append("Class");
					break;
				}
			}
			break;
		case Struct.Class:
			output.append("Class [");
			for (Obj obj : structToVisit.getMembers()) {
				obj.accept(this);
			}
			output.append("]");
			break;
		}

	}
}
