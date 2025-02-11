package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;

import java.util.LinkedList;
import java.util.List;

import rs.ac.bg.etf.pp1.CounterVisitor.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

public class CodeGenerator extends VisitorAdaptor {
	private int mainPc;
	
	List<List<Integer>> addressToFix = new LinkedList<>();
	List<Integer> addressOfCondIfToFix = new LinkedList<>();
	List<Integer> elseAddress = new LinkedList<>();
	
	int addrBreak = -1;
	int addrContinue = -1;
	int addr = -1;
	
	int startMatrixLoop = 0; 
	int endMatrixLoop = 0;
	
	int printWidth = 0;

	public int getMainPc() {
		return mainPc;
	}

	// Method
	public void visit(MethodNameVoid method) {
		if ("main".equalsIgnoreCase(method.getMethodName())) {
			mainPc = Code.pc;
		}
		method.obj.setAdr(Code.pc);
		SyntaxNode methodNode = method.getParent();

		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);

		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);

		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	}
	public void visit(MethodNameAnyType method) {
		method.obj.setAdr(Code.pc);
		SyntaxNode methodNode = method.getParent();

		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);

		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);

		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	}
	public void visit(MethodDecl methodDecl){
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	// Expr
	public void visit(AddopEndMinus exprNeg) {
		Code.put(Code.neg);
	}
	
	// Factor
	public void visit(FactorNum FactorNum) {
		Code.loadConst(FactorNum.getN1());
	}
	public void visit(FactorChar FactorChar) {
		Code.loadConst(FactorChar.getC1());
	}
	public void visit(FactorBool FactorBool) {
		String b = FactorBool.getB1().toString();
		Code.loadConst(b.equals("true") ? 1 : 0);		
	}
	public void visit(FactorDesignator designator) {
		SyntaxNode parent = designator.getParent();
		
		if(DesignatorStatementAssignop.class != parent.getClass() && DesignatorStatementActPars.class != parent.getClass() && FactorDesignatorActPars.class != parent.getClass()){
			if(designator.getDesignator() instanceof DesignatorIdent) {
				//Promenljiva nije niz
				Code.load(designator.getDesignator().obj);
			} else {
				//Promenljiva je matrica
				if(designator.getDesignator().obj.getType().getElemType() != null) { 
					if(designator.getDesignator().obj.getType().getElemType().getElemType() == Tab.charType) {
						Code.put(Code.baload);
					} else {
						Code.put(Code.aload);
					}
				} else { 
					//Promenljiva je niz
					if(designator.getDesignator().obj.getType() == Tab.charType) {
						Code.put(Code.baload);
					} else {
						Code.put(Code.aload);
					}
				}
			} 
		}
	}
	public void visit(FactorNew factorNew) {
		Code.put(Code.newarray);
		if (factorNew.getType().struct.equals(Tab.charType)) {
			Code.put(0);
		} else {
			Code.put(1);
		}
	}
	public void visit(FactorNewMatrix factorNew) {
		Code.put(Code.newarray);
		if (factorNew.getType().struct.equals(Tab.charType)) {
			Code.put(0);
		} else {
			Code.put(1);
		}
		Code.put(Code.astore);
		Code.put(Code.dup_x2);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.putFalseJump(Code.ne, 0);
		endMatrixLoop = Code.pc - 2;
		Code.put(Code.pop);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.dup_x2);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.putJump(startMatrixLoop);
		Code.fixup(endMatrixLoop);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.put(Code.pop);
	}
	public void visit(MatrixFirstSquare mat) {
		Code.put(Code.dup);
		Code.put(Code.newarray); 
		Code.put(1);
		Code.put(Code.dup2);
		Code.loadConst(0);
		startMatrixLoop = Code.pc;
		Code.put(Code.dup_x2);
	}
	public void visit(ArrayDecl mat) {
		if(mat.getParent() instanceof MatrixDecl)
			Code.put(Code.aload);
	}
	public void visit(FactorDesignatorActPars funcCall) {
		if("ord".equals(funcCall.getDesignator().obj.getName())) {
			// Konverzija iz char u int
		} else if ("chr".equals(funcCall.getDesignator().obj.getName())) {
			// Konverzija iz int u char
		} else if ("len".equals(funcCall.getDesignator().obj.getName())) {
			// Dohvatanje duzine niza
			Code.put(Code.arraylength);
		} else {
			Obj functionObj = funcCall.getDesignator().obj;
			int offset = functionObj.getAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);	
		}
	}
	public void visit(DesignatorStatementActPars procCall){
		if("ord".equals(procCall.getDesignator().obj.getName())) {
			// Konverzija iz char u int
		} else if ("chr".equals(procCall.getDesignator().obj.getName())) {
			// Konverzija iz int u char
		} else if ("len".equals(procCall.getDesignator().obj.getName())) {
			// Dohvatanje duzine niza
			Code.put(Code.arraylength);
		} else {
			Obj functionObj = procCall.getDesignator().obj;
			int offset = functionObj.getAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);
			if(procCall.getDesignator().obj.getType() != Tab.noType){
				Code.put(Code.pop);
			}
		}
	}
		
	
	// Designator
	/*public void visit(DesignatorIdent designator) {
		if(designator.getParent() instanceof StatementRead) {
			Code.load(designator.obj);
			Code.put(Code.dup);
			Code.put(Code.arraylength);
		}
	}
	public void visit(DesignatorExpr designator) {
		if(designator.obj.getType() == Tab.charType) {
			Code.put(Code.baload);
		} else {
			Code.put(Code.aload);
		}
	}*/
	
	// Statement
	public void visit(StatementPrint printStmt) {
		if (printStmt.getExpr().obj.getType() == Tab.intType 
				|| printStmt.getExpr().obj.getType() == SemanticAnalyzer.boolType
				|| (printStmt.getExpr().obj.getType().getElemType() != null && printStmt.getExpr().obj.getType().getElemType().getElemType() == Tab.intType )) {
			Code.loadConst(5);
			Code.put(Code.print);
		} else {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	public void visit(StatementPrintParameter printStmt) {
		Code.loadConst(printWidth);
		if (printStmt.getExpr().obj.getType() == Tab.intType 
				|| printStmt.getExpr().obj.getType() == SemanticAnalyzer.boolType
				|| (printStmt.getExpr().obj.getType().getElemType() != null && printStmt.getExpr().obj.getType().getElemType().getElemType() == Tab.intType )) {
			Code.put(Code.print);
		} else {
			Code.put(Code.bprint);
		}
	}
	public void visit(PrintNum printNum) {
		printWidth = printNum.getNum();
	}
	public void visit(StatementReturn returnNoExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	public void visit(StatementReturnExpr returnExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	public void visit(StatementRead statementRead) {
		if(statementRead.getDesignator() instanceof DesignatorIdent) {
			DesignatorIdent ident = (DesignatorIdent) statementRead.getDesignator();
			Code.put(Code.read);
			Code.store(statementRead.getDesignator().obj);
		} else {
			DesignatorExpr expr = (DesignatorExpr) statementRead.getDesignator();
			if(expr.obj.getType().getElemType() != null) { 
				//Matrica
				if(expr.obj.getType().getElemType().getElemType() == Tab.intType) {
					Code.put(Code.read);
					Code.put(Code.astore);
				} else {
					Code.put(Code.bread);
					Code.put(Code.bastore);
				}
			} else {
				//Niz
				if(expr.obj.getType() == Tab.intType) {
					Code.put(Code.read);
					Code.put(Code.astore);
				} else {
					Code.put(Code.bread);
					Code.put(Code.bastore);
				}
			}
		}
	}
	public void visit(StatementBreak statementBreak) {
		//Treba da se skoci na adresu posle for petlje
		addrBreak = Code.pc + 1;
		Code.putJump(addr);	
	}
	public void visit(StatementContinue statementContineu) {
		Code.putJump(addrContinue);
	}
	
	// DesignatorStatement
	public void visit(DesignatorStatementAssignop assign) {
		if(assign.getDesignator() instanceof DesignatorExpr) {
			//Vrednost se dodeljuje promenljivoj koja je niz
			if(assign.getDesignator().obj.getType() == Tab.intType ||
					(assign.getDesignator().obj.getType().getElemType() != null && assign.getDesignator().obj.getType().getElemType().getElemType() == Tab.intType)) {
				Code.put(Code.astore);
			} else {
				Code.put(Code.bastore);		
			} 
		} else {
			//Vrednost se dodeljuje promenljivoj koja nije niz
			Code.store(assign.getDesignator().obj);
		}
	}
	public void visit(DesignatorName designator) {
		Code.load(designator.obj);
	}
	public void visit(DesignatorStatementIncrement designator) {
		if(designator.getDesignator() instanceof DesignatorIdent) {
			//Promenljiva nije tipa niz
			Code.load(designator.getDesignator().obj);
			Code.loadConst(1);
			Code.put(Code.add);
			Code.store(designator.getDesignator().obj);
		} else {
			//Promenljiva je niz
			Code.put(Code.dup2);
			Code.put(Code.aload);
			Code.loadConst(1);
			Code.put(Code.add);
			Code.put(Code.astore);
		}
	}
	public void visit(DesignatorStatementDecrement designator) {
		if(designator.getDesignator() instanceof DesignatorIdent) {
			//Promenljiva nije niz
			Code.load(designator.getDesignator().obj);
			Code.loadConst(1);
			Code.put(Code.sub);
			Code.store(designator.getDesignator().obj);
		} else {
			//Promenljiva je niz
			Code.put(Code.dup2);
			Code.put(Code.aload);
			Code.loadConst(1);
			Code.put(Code.sub);
			Code.put(Code.astore);
		}
	}
	
	// Addop
	public void visit(AddopList addop) {
		if(addop.getAddop() instanceof Plus) {
			Code.put(Code.add);
		} else {
			Code.put(Code.sub);
		}
	}
	
	// Mulop
	public void visit(MulopList mulop) {
		if(mulop.getMulop() instanceof Star) {
			Code.put(Code.mul);
		} else if(mulop.getMulop() instanceof Slash) {
			Code.put(Code.div);
		} else {
			Code.put(Code.rem);
		} 
	}
	
	// Cond
	public void visit(LParenIf lparen) {
		addressToFix.add(new LinkedList<>());
	}
	public int relOp(CondFactRelop condExpr) {
		int relop = -1;
		if(condExpr.getRelop() instanceof EqualTo) {
			relop = Code.eq;
		} else if(condExpr.getRelop() instanceof NotEqualTo) {
			relop = Code.ne;
		} else if(condExpr.getRelop() instanceof Greater) {
			relop = Code.gt;
		} else if(condExpr.getRelop() instanceof GreaterEqual) {
			relop = Code.ge;
		} else if(condExpr.getRelop() instanceof Less) {
			relop = Code.lt;
		} else {
			relop = Code.le;
		}
		return relop;
	}
	public void visit(CondFactRelop condExpr) {
		int realop = relOp(condExpr);
		
		SyntaxNode parentTwo = condExpr.getParent().getParent();
		if(parentTwo instanceof Condition && parentTwo.getParent() instanceof Conditions) {
			Code.putFalseJump(Code.inverse[realop], 0);
		} else {
			//adresa na koju se skace je nepoznata pa se stavlja 0
			//u ovu granu se ulazi kada ne postoje AND/OR ili kad postoji samo AND deo u uslovu
			Code.putFalseJump(realop, 0); 
		}
		int addr = Code.pc-2;
		//adresa na koju treba upisati pomeraj, podrzana su ugnjezdjavanja if/if else uslova
		addressToFix.get(addressToFix.size() - 1).add(addr); 
		if(parentTwo instanceof StatementForCondFact) {
			Code.putJump(36);
			addressToFix.get(addressToFix.size() - 1).add(Code.pc - 2);
		}
	}
	public void visit(CondFactExpr condExpr) {
		//Kada se proveravaju boolean uslovi
		//Stavljamo na ExprStack 1 kako bi se uporedilo sa boolean promenljivom
		Code.loadConst(1);
		SyntaxNode parentTwo = condExpr.getParent().getParent();
		if(parentTwo instanceof Condition && parentTwo.getParent() instanceof Conditions) {
			Code.putFalseJump(Code.inverse[Code.eq], 0);
		} else {
			//adresa na koju se skace je nepoznata pa se stavlja 0
			//u ovu granu se ulazi kada ne postoje AND/OR ili kad postoji samo AND deo u uslovu
			Code.putFalseJump(Code.eq, 0); 
		}
		int addr = Code.pc-2;
		//adresa na koju treba upisati pomeraj, podrzana su ugnjezdjavanja if/if else uslova
		addressToFix.get(addressToFix.size() - 1).add(addr); 
		if(parentTwo instanceof StatementForCondFact) {
			Code.putJump(36);
			addressToFix.get(addressToFix.size() - 1).add(Code.pc - 2);
		}
	}
	public void visit(OrCond or) {
		List<Integer> fixCondList = addressToFix.get(addressToFix.size() - 1);
		for(int i = 0; i < fixCondList.size() - 1; i++) {
			Code.fixup(fixCondList.get(i));
		}
		addressOfCondIfToFix.add(fixCondList.get(fixCondList.size() - 1));
		
		addressToFix.get(addressToFix.size() - 1).clear();
	}
	public void visit(RParenIf rParenIf) {
		for(int i = 0; i < addressOfCondIfToFix.size(); i++) {
			Code.fixup(addressOfCondIfToFix.get(i));
		}
		addressOfCondIfToFix.clear();
	}
	public void visit(StatementIf statement) {
		//Dolaskom do ovog cvora u sintaksnom stablu dosli smo do kraja IF i mozemo da azuriramo adresu skoka, tj da dopisemo pomeraj
		for(int i = 0; i < addressToFix.get(addressToFix.size() - 1).size(); i++) {
			Code.fixup(addressToFix.get(addressToFix.size() - 1).get(i));
		}
		addressToFix.remove(addressToFix.size() - 1);
	}
	public void visit(ElseSt elseSt) {
		Code.putJump(0);
		int addr = Code.pc - 2;
		//Formiranje prve adrese nakon ELSE bloka
		elseAddress.add(addr);
		
		for(int i = 0; i < addressToFix.get(addressToFix.size() - 1).size(); i++) {
			Code.fixup(addressToFix.get(addressToFix.size() - 1).get(i));
		}
		addressToFix.remove(addressToFix.size() - 1);
	}
	public void visit(StatementIfElse statement) {
		//Ovde se obradjuje ceo statement, i ukoliko je ispunjen uslov u IF delu skace se iza ELSE bloka, ta adresa se postavlja ovde
		Code.fixup(elseAddress.remove(elseAddress.size() - 1));
	}
	
	// For
	int addrSecondSemi = -1; boolean secondDesignatorExists = false;
	public void visit(LSemiFor lsemi) {
		addressToFix.add(new LinkedList<>());
		addr = Code.pc;
		addrContinue = Code.pc;
	}
	public void visit(RSemiFor lsemi) {
		addrSecondSemi = Code.pc;
	}
	public void visit(RParenFor rparen) {
		if(addressOfCondIfToFix.size() > 0) {
			for(int i = 0; i < addressOfCondIfToFix.size(); i++) {
				Code.fixup(addressOfCondIfToFix.get(i));
			}
			addressOfCondIfToFix.clear();
		}
		if(addressToFix.size() > 0) {
			Code.fixup(addressToFix.get(addressToFix.size() - 1).get(addressToFix.get(addressToFix.size() - 1).size() - 1));
			addressToFix.get(addressToFix.size() - 1).remove(addressToFix.get(addressToFix.size() - 1).size() - 1);
		}
	}
	public void visit(StatementFor forSt) {
		if(!secondDesignatorExists) {
			secondDesignatorExists = false;
			Code.putJump(addr);
		} else Code.putJump(addrSecondSemi);
		if(addressToFix.size() > 0) {
			for(int i = 0; i < addressToFix.get(addressToFix.size() - 1).size(); i++) {
				Code.fixup(addressToFix.get(addressToFix.size() - 1).get(i));
			}
			addressToFix.remove(addressToFix.size() - 1);
		}
		
		if(addrBreak != -1) {
			Code.fixup(addrBreak);
			addrBreak = -1;
		} 
		addrSecondSemi = -1;
		addrContinue = -1;
	}
	public void visit(StatementForCondFact forSt) {
		if(!secondDesignatorExists) {
			secondDesignatorExists = false;
			Code.putJump(addr);
		} else Code.putJump(addrSecondSemi);
		if(addressToFix.size() > 0) {
			for(int i = 0; i < addressToFix.get(addressToFix.size() - 1).size(); i++) {
				Code.fixup(addressToFix.get(addressToFix.size() - 1).get(i));
			}
			addressToFix.remove(addressToFix.size() - 1);
		}
		
		if(addrBreak != -1) {
			Code.fixup(addrBreak);;
			addrBreak = -1;
		} 
		addrSecondSemi = -1;
		addrContinue = -1;
	}
	public void visit(ForSt forSt) {
		//addrContinue = Code.pc;
	}
	public void visit(DesignatorStatementListSecond stListSecond) {
		if(!(stListSecond.getDesignatorStatementList() instanceof DesignatorStatementEnd)) {
			secondDesignatorExists = true;
			Code.putJump(addr);
		}
	}
}
