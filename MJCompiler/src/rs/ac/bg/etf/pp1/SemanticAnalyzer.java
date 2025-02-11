package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {
	public static final Struct boolType = new Struct(Struct.Bool);
	
	List<Obj> consts = new ArrayList<>();
	List<Obj> vars = new ArrayList<>();
	List<Obj> forSquare = new ArrayList<>();
	List<Struct> actParsList = new ArrayList<>();
	
	Obj currentMethod = null;
	Struct declType = null;

	boolean returnFound = false;
	boolean errorDetected = false;
	boolean mainExists = false;
	boolean forFound = false;
    
	int nVars = 0;
	int nMetoda = 0;
    
	public boolean passed() {
		return !errorDetected;
	}

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}
	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	// Program
	public void visit(ProgramName programName) {
		programName.obj = Tab.insert(Obj.Prog, programName.getProgramName(), Tab.noType);
		Tab.openScope();
		log.info("Pocetak programa " + programName.getProgramName() + ".");
	}
	public void visit(Program program) {
		if (!mainExists) {
			report_error("GRESKA: U programu ne postoji MAIN metoda!", null);
		}
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgramName().obj);
		Tab.closeScope();
	}
	
	// Type
	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTn());
		if (typeNode == Tab.noObj) {
			report_error("GRESKA: Nije pronadjen tip " + type.getTn() + " u tabeli simbola! ", null);
			type.struct = Tab.noType;
			declType = typeNode.getType();
		} else {
			if (Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();
			} else {
				report_error("GRESKA: Ime " + type.getTn() + " ne predstavlja tip!", type);
				type.struct = Tab.noType;
			}
		}
	}

	// Const
	public void visit(ConstNum num) {
		num.obj = new Obj(Obj.Elem, num.getConstName(), Tab.intType);
		num.obj.setAdr(num.getN1());
		consts.add(num.obj);
	}
	public void visit(ConstChar chr) {
		chr.obj = new Obj(Obj.Elem, chr.getConstName(), Tab.charType);
		chr.obj.setAdr(chr.getC1());
		consts.add(chr.obj);
	}
	public void visit(ConstBool bool) {
		bool.obj = new Obj(Obj.Elem, bool.getConstName(), boolType);
		if(bool.getB1()) bool.obj.setAdr(1);
		else bool.obj.setAdr(0);
		consts.add(bool.obj);
	}
	public void visit(ConstDeclarationList constDecl) {
		Struct kind = constDecl.getType().struct;
		for (int j = 0; j < consts.size(); j++) {
			if (Tab.currentScope.findSymbol(consts.get(j).getName()) != null) {
				report_error("GRESKA: Vec postoji identifikator " + consts.get(j).getName(), null);
			} 
			if (kind.getKind() != consts.get(j).getType().getKind()) {
				report_error("GRESKA: Tip konstante nije odgovarajuci", constDecl);
			} else {
				report_info("Deklarisana je konstanta " + consts.get(j).getName(), constDecl);
				Obj con = Tab.insert(Obj.Con, consts.get(j).getName(), constDecl.getType().struct);
				con.setAdr(consts.get(j).getAdr());
			}
		}
		consts.removeAll(consts);
	}

	// Var
	public void visit(VarName vn) {
		vn.obj = new Obj(Obj.NO_VALUE, vn.getName(), Tab.noType);
	}
	public void visit(Arr arr) {
		arr.obj = new Obj(Obj.Var, arr.getVarName().obj.getName(), new Struct(Struct.Array));
	}
	public void visit(VarIdent varIdent) {
		varIdent.obj = new Obj(Obj.Var, varIdent.getVarName().obj.getName(), Tab.noType);
		vars.add(varIdent.obj);
	}
	public void visit(VarArr varArr) {
		varArr.obj = varArr.getArr().obj;
		vars.add(varArr.obj);
	}
	public void visit(VarMatrix varMatrix) {
		varMatrix.obj = varMatrix.getArr().obj;
		varMatrix.obj.getType().setElementType(new Struct(Struct.Array));
		vars.add(varMatrix.obj);
	}
	public void visit(VarDeclarationList varList) {
		Obj con = null;
		for (int j = 0; j < vars.size(); j++) {
			if (Tab.currentScope.findSymbol(vars.get(j).getName()) != null)
				report_error("GRESKA: Vec postoji identifikator " + vars.get(j).getName(), null);
			else {
				report_info("Deklarisana je promenljiva " + vars.get(j).getName(), varList);
				if(vars.get(j).getType().getElemType() != null) {
					if(vars.get(j).getType().getElemType().getKind() == Struct.Array) {
						con = Tab.insert(Obj.Var, vars.get(j).getName(), new Struct(Struct.Array, new Struct(Struct.Array, varList.getType().struct)));
					}
				} else if(vars.get(j).getType().getKind() == Struct.Array) {
					con = Tab.insert(Obj.Var, vars.get(j).getName(), new Struct(Struct.Array, varList.getType().struct));
				} 
				else {
					con = Tab.insert(Obj.Var, vars.get(j).getName(), varList.getType().struct);
				}
			}
		}
		vars.removeAll(vars);
	}

	// Method
	public void visit(MethodNameAnyType methodName) {
		currentMethod = Tab.insert(Obj.Meth, methodName.getMethodName(), methodName.getType().struct);
		methodName.obj = currentMethod;
		Tab.openScope();
		report_info("Obradjuje se funkcija " + methodName.getMethodName(), methodName);
	}
	public void visit(MethodNameVoid methodName) {
		currentMethod = Tab.insert(Obj.Meth, methodName.getMethodName(), Tab.noType);
		methodName.obj = currentMethod;
		Tab.openScope();
		report_info("Obradjuje se funkcija " + methodName.getMethodName(), methodName);
	}
	public void visit(MethodDecl methodDecl) {
		if (!returnFound && currentMethod.getType() != Tab.noType) {
			report_error("GRESKA: funkcija " + currentMethod.getName() + " nema return iskaz!", methodDecl);
		}
		if (doesMainExists(methodDecl)) {
			mainExists = true;
		}
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();

		returnFound = false;
		currentMethod = null;
	}
	public boolean doesMainExists(MethodDecl methodDecl) {
		return (methodDecl.getMethodName() instanceof MethodNameVoid
				&& methodDecl.getMethodPars() instanceof NoMethodParameters
				&& "main".equals(((MethodNameVoid) methodDecl.getMethodName()).getMethodName()));
	}

	// Term
	public void visit(Term term) {
		term.struct = term.getMulopFactors().struct;
	}

	// FormPars
	public void visit(FormParsArr arr) {
		report_info("Deklarisan je parametar funkcije (niz) " + arr.getVarName(), arr);
		Obj fun = Tab.find(currentMethod.getName());
		fun.setFpPos(fun.getFpPos() + 1);
		Tab.insert(Obj.Var, arr.getVarName(), new Struct(Struct.Array, arr.getType().struct));
	}
	public void visit(FormParsVar var) {
		report_info("Deklarisan je parametar funkcije " + var.getVarName(), var);
		Obj fun = Tab.find(currentMethod.getName());
		fun.setFpPos(fun.getFpPos() + 1);
		Tab.insert(Obj.Var, var.getVarName(), var.getType().struct);
	}

	// Factor
	public void visit(FactorNum num) {
		num.struct = Tab.intType;
	}
	public void visit(FactorChar ch) {
		ch.struct = Tab.charType;
	}
	public void visit(FactorBool bool) {
		bool.struct = boolType;
	}
	public void visit(FactorDesignator factorDesignator) {
		if (factorDesignator.getDesignator().obj != null)
			factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
		else
			factorDesignator.struct = Tab.nullType;
	}
	public void visit(FactorDesignatorActPars designatorFun) {
		Obj func = designatorFun.getDesignator().obj;
		if (Obj.Meth == func.getKind()) {
			if(Tab.noType == func.getType()) {
				report_error("GRESKA: Funkcija sa povratnom vrednoscu VOID se ne moze koristiti u izrazima dodele", designatorFun);
			} else  {
				List<Obj> pars = new ArrayList<>(func.getLocalSymbols());
				if(actParsList.size() != func.getFpPos() && 
						(("len".equals(func.getName()) && actParsList.size() != func.getLocalSymbols().size()))
						) {
					report_error("GRESKA: Neodgovarajuci broj parametara pozvane funkcije", designatorFun);
				} else if("len".equals(func.getName())) {
					if(actParsList.get(0).getElemType() == null) {
						report_error("GRESKA: Nepoklapanje tipa parametara pozvane funkcije" , designatorFun);
					}
					else if(actParsList.get(0).getKind() != pars.get(0).getType().getKind()) {
						report_error("GRESKA: Nepoklapanje tipa parametara pozvane funkcije" , designatorFun);
					}
					report_info("Pronadjen poziv funkcije " + func.getName(), designatorFun);
					designatorFun.struct = func.getType();
				} else {
					for(int i = 0; i < actParsList.size(); i++) {
						if(!actParsList.get(i).compatibleWith(pars.get(i).getType())) {
							report_error("GRESKA: Nepoklapanje tipa parametara pozvane funkcije" , designatorFun);
						}
					}
					report_info("Pronadjen poziv funkcije " + func.getName(), designatorFun);
					designatorFun.struct = func.getType();
				}
			}
		} else {
			report_error("GRESKA: Na liniji " + designatorFun.getLine() + " : ime " + func.getName() + " nije funkcija!",
					null);
			designatorFun.struct = Tab.noType;
		}
		actParsList.clear();
	}
	public void visit(FactorNew factorNew) {
		Struct defType = declType;
		if (factorNew.getExpr().obj.getType().getKind() == Struct.Int) {
			factorNew.struct = new Struct(Struct.Array, factorNew.getType().struct);
		} else {
			report_error("GRESKA: Kreiranje niza, Expr nije tipa int", factorNew);
			factorNew.struct = Tab.noType;
		}
	}
	public void visit(FactorNewMatrix factorNew) {
		if(matrixBadExpr) {
			factorNew.struct = Tab.noType;
			matrixBadExpr = false;
		} else {
			if (factorNew.getExpr().obj.getType().getKind() == Struct.Int) {
				factorNew.struct = new Struct(Struct.Array, new Struct(Struct.Array, factorNew.getType().struct));
			} else {
				report_error("GRESKA: Kreiranje matrice, Expr 2 nije tipa int", factorNew);
				factorNew.struct = Tab.noType;
			}
		}
	}
	boolean matrixBadExpr = false;
	public void visit(MatrixFirstSquare factorNew) {
		if(factorNew.getExpr().obj.getType().getKind() != Struct.Int) {
			report_error("GRESKA: Kreiranje matrice, Expr 1 nije tipa int", factorNew);
			matrixBadExpr = true;
		}
	}
	public void visit(FactorExpr expr) {
		expr.struct = expr.getExpr().obj.getType();
	}
	public void visit(FactorRange range) {
		if (range.getExpr().obj.getType().getKind() != Struct.Int) {
			report_error("GRESKA: Parametar funkcije RANGE nije tipa int", range);
		}
		range.struct = new Struct(Struct.Array, range.getExpr().obj.getType());
	}

	// Designator
	public void visit(DesignatorIdent designator) {
		Obj obj = Tab.find(designator.getName());
		if (obj == Tab.noObj) {
			report_error("GRESKA: Ime " + designator.getName() + " nije deklarisano! ", designator);
		}
		designator.obj = obj;
	}
	public void visit(DesignatorExpr designatorExp) {
		Obj desCheck = Tab.find(designatorExp.getDesignatorName().getName());
		if (desCheck == Tab.noObj) {
			report_error("GRESKA: Ime " + designatorExp.getDesignatorName().getName() + " nije deklarisano! ", designatorExp);
		} else {
			if (!(desCheck.getType().getKind() == Struct.Array)) {
				report_error("GRESKA: Ime " + designatorExp.getDesignatorName().getName() + " nije deklarisano kao niz", designatorExp);
			} else if (designatorExp.getArrMatrix() instanceof ArrayDecl) {
				//Niz
				ArrayDecl arr = (ArrayDecl) designatorExp.getArrMatrix();
				if(arr.getExpr().obj.getType() != Tab.intType) {
					report_error("GRESKA: Expr nije int", designatorExp);
				}
			} else if (designatorExp.getArrMatrix() instanceof MatrixDecl) {
				//Matrica
				MatrixDecl arr = (MatrixDecl) designatorExp.getArrMatrix();
				ArrayDecl arr2 = (ArrayDecl) arr.getArrMatrix();
				if(arr.getExpr().obj.getType() != Tab.intType || arr2.getExpr().obj.getType() != Tab.intType) {
					report_error("GRESKA: Expr nije int", designatorExp);
				}
			} 
			designatorExp.getDesignatorName().obj = Tab.noObj;
			//ako je matrica drugacije treba
			if(desCheck.getType().getElemType() != null && desCheck.getType().getElemType().getKind() == Struct.Array) {
				designatorExp.obj = new Obj(Obj.Elem, "", new Struct(Struct.Array, desCheck.getType().getElemType()));
				report_info("Kreiran/promenjen element matrice " + desCheck.getName(), null);
				designatorExp.getDesignatorName().obj = desCheck;
			} else {
				designatorExp.obj = new Obj(Obj.Elem, "", desCheck.getType().getElemType());
				report_info("Kreiran/promenjen element niza " + desCheck.getName(), null);
				designatorExp.getDesignatorName().obj = desCheck;
			}
		}
	}
	public void visit(DesignatorName name) {
		Obj declaredName = Tab.find(name.getName());

		if (declaredName == Tab.noObj) {
			name.obj = Tab.noObj;
			report_error("GRESKA: Ident " + declaredName.getName() + " nije prethodno deklarisan", name);
		} else {
			name.obj = declaredName;
		}
	}
	public void visit(DesignatorStatementActPars dsFun) {
		Obj func = dsFun.getDesignator().obj;
		if (Obj.Meth == func.getKind()) {
			List<Obj> pars = new ArrayList<>(func.getLocalSymbols());
			if(actParsList.size() != func.getFpPos() && 
					(("len".equals(func.getName()) || "len".equals(func.getName()) || "len".equals(func.getName()) && actParsList.size() != func.getLocalSymbols().size()))
					) {
				report_error("GRESKA: Neodgovarajuci broj parametara pozvane funkcije", dsFun);
			} else if("len".equals(func.getName())) {
				if(actParsList.get(0).getElemType() == null) {
					report_error("GRESKA: nepoklapanje tipa parametara pozvane funkcije" , dsFun);
				} else if((actParsList.get(0).getElemType().getKind() != pars.get(0).getKind()) && actParsList.get(0).getElemType() == null) {
					report_error("GRESKA: nepoklapanje tipa parametara pozvane funkcije" , dsFun);
				}
			} else {
				for(int i = 0; i < actParsList.size(); i++) {
					if(!actParsList.get(i).compatibleWith(pars.get(i).getType())) {
						report_error("GRESKA: nepoklapanje tipa parametara pozvane funkcije" , dsFun);
					}
				}
				report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + dsFun.getLine(), null);
			}
		} else {
			report_error("GRESKA: na liniji " + dsFun.getLine() + ": funkcija ne postoji!", null);
		}
		actParsList.clear();
	}

	// Statements
	public void visit(StatementReturnExpr returnExpr) {
		returnFound = true;
		Struct currMethType = currentMethod.getType();
		Struct s = returnExpr.getExpr().obj.getType();
		if (!currMethType.compatibleWith(s)) {
			report_error("GRESKA: Tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
		}
		report_info("Dobra povratna vrednost funkcije", returnExpr);
	}
	public void visit(PrintNum num) {
		num.struct = Tab.intType;
	}
	public void visit(StatementPrint ps) {
		int kind = -1;
		if(ps.getExpr().obj.getType() != null) kind = ps.getExpr().obj.getType().getKind();
		if (kind == -1 || kind != Struct.Int && kind != Struct.Char && kind != Struct.Bool && kind != Struct.Array) {
			report_error("GRESKA: Neodgovarajuci operand instrukcije PRINT", ps);
		}
		report_info("Obradjena funkcija PRINT", ps);
	}
	public void visit(StatementPrintParameter ps) {
		if (ps.getPrintNum().struct != Tab.intType) {
			report_error("GRESKA: Drugi parametar funkcije PRINT nije tipa int", ps);
		}
		int kind = ps.getExpr().obj.getType().getKind();
		if (kind != Struct.Int && kind != Struct.Char && kind != Struct.Bool) {
			report_error("GRESKA: Neodgovarajuci operand instrukcije PRINT", ps);
		}
		report_info("Obradjena funkcija PRINT", ps);
	}
	public void visit(StatementRead sr) {
		if (sr.getDesignator() instanceof DesignatorIdent) {
			checkReadIdent(sr);
		} else {
			checkReadExpr(sr);
		}
	}
	public void checkReadIdent(StatementRead sr) {
		if (sr.getDesignator().obj.getKind() != Obj.Var) {
			report_error("GRESKA: Greska prilikom obrade funkcije READ, nije prosledjena promenljiva", sr);
		} else {
			Struct type = sr.getDesignator().obj.getType();
			//if(type.getElemType() != null) 
			if (type != Tab.intType && type != Tab.charType && type != boolType) {
				report_error("GRESKA: Promenljiva prosledjena funkciji READ nije odgovarajuceg tipa", sr);
			}
		}
		report_info("Funkciji READ je prosledjena promenljiva " + sr.getDesignator().obj.getName(), sr);
	}
	public void checkReadExpr(StatementRead sr) {
		Struct type = sr.getDesignator().obj.getType();
		Struct typeElem = null;
		if(sr.getDesignator().obj.getType().getElemType() != null) {
			typeElem = sr.getDesignator().obj.getType().getElemType().getElemType();
		}
		if(typeElem == null) {
			if (type != Tab.intType && type != Tab.charType && type != boolType) {
				report_error("GRESKA: Promenljiva prosledjena funkciji READ nije odgovarajuceg tipa (niz)", sr);
			} 
		} else {
			if (typeElem != Tab.intType && typeElem != Tab.charType && typeElem != boolType) {
					report_error("GRESKA: Promenljiva prosledjena funkciji READ nije odgovarajuceg tipa (matrica)", sr);
				}
		}
		report_info("Funkciji READ je prosledjen novi element", null);
	}
	public void visit(UnmatchedOne statement) {
		Obj d1 = statement.getDesignator().obj;
		Obj d2 = statement.getDesignator1().obj;
		if (d2.getType().getKind() != Struct.Array) {
			report_error("GRESKA: Designator sa desne strane znaka = mora biti tipa Array,", statement);
		}
		if (d1.getType().getKind() != Struct.Array) {
			report_error("GRESKA: Designator nakon znaka * mora biti tipa Array", statement);
		}
		if(!d1.getType().compatibleWith(d2.getType())) {
			report_error("GRESKA: Tipovi nisu kompatibilni", statement);
		}
	}
	public void visit(UnmatchedMoreThanOne statement) {
		Obj d1 = statement.getDesignator().obj;
		Obj d2 = statement.getDesignator1().obj;
		if (d2.getType().getKind() != Struct.Array) {
			report_error("GRESKA: Designator sa desne strane znaka = mora biti tipa Array,", statement);
		}
		if (d1.getType().getKind() != Struct.Array) {
			report_error("GRESKA: Designator nakon znaka * mora biti tipa Array", statement);
		}
		if(!d1.getType().compatibleWith(d2.getType())) {
			report_error("GRESKA: Tipovi nisu kompatibilni", statement);
		}
		
		for(int i = 0; i < forSquare.size(); i++) {
			if(!forSquare.get(i).getType().compatibleWith(d2.getType())) {
				report_error("GRESKA: " + forSquare.get(i).getName() + " nije tipa " + d2.getKind(), statement);
			}
		}
		forSquare.removeAll(forSquare);
	}
	public void visit(UnmachedEnd part) {
		Obj obj = part.getDesignator().obj;
		if(obj.getKind() != Obj.Var && obj.getKind() != Obj.Fld && obj.getKind() != Obj.Elem) {
			report_error("GRESKA: Designator nije promenljiva niti element niza", part);
		}
		forSquare.add(obj);
	}
	public void visit(UnmachedPartDesignator part) {
		Obj obj = part.getDesignator().obj;
		if(obj.getKind() == Obj.Var && obj.getKind() == Obj.Fld && obj.getKind() == Obj.Elem) {
			report_error("GRESKA: Designator nije promenljiva niti element niza", part);
		}
		forSquare.add(obj);
	}
	
	// Expr
	public void visit(ExprPositive expr) {
		if(expr.getAddopTerms().struct != null && expr.getAddopTerms().struct.getElemType() != null ) {
			if(expr.getAddopTerms().struct.getElemType().getElemType() != null) {
				expr.obj = new Obj(Obj.Elem, "", expr.getAddopTerms().struct.getElemType().getElemType());
			} else {
				expr.obj = new Obj(Obj.Elem, "", expr.getAddopTerms().struct);
			}
		} else {
			expr.obj = new Obj(Obj.Elem, "", expr.getAddopTerms().struct);
		}
	}
	/*public void visit(ExprNegativ expr) {
		if (!expr.getAddopTerms().struct.equals(Tab.intType)) {
			report_error("Greska na liniji " + expr.getLine() + ", tip parametra uz znak minus mora biti int", null);
			expr.obj = Tab.noObj;
		} else {
			expr.obj = new Obj(Obj.Elem, "", expr.getAddopTerms().struct);
		}
	}*/

	// DesignatorStatements
	public void visit(DesignatorStatementAssignop assignment) {
		Obj obj = assignment.getDesignator().obj;
		if (obj.getKind() == Obj.Var || obj.getKind() == Obj.Fld || obj.getKind() == Obj.Elem) {
			if (assignment.getDesignator().obj.getType().compatibleWith(assignment.getExpr().obj.getType())) {
				report_info("Designator i expr su odgovarajuceg tipa", assignment);
			} else if(assignment.getDesignator().obj.getType().getElemType() != null && assignment.getDesignator().obj.getType().getElemType().getElemType() != null) {
					if(assignment.getDesignator().obj.getType().getElemType().getElemType().compatibleWith(assignment.getExpr().obj.getType())) {
						report_info("Designator i expr su odgovarajuceg tipa", assignment);
					}
			} else {
				report_error("GRESKA: Designator i expr nisu istog tipa", assignment);
			}
		} else {
			report_error("GRESKA: Designator nije odgovarajuceg tipa", assignment);
		}
	}
	public void visit(DesignatorStatementIncrement desInc) {
		if (desInc.getDesignator() instanceof DesignatorExpr) {
			if (!desInc.getDesignator().obj.getType().equals(Tab.intType) && !desInc.getDesignator().obj.getType().getElemType().getElemType().equals(Tab.intType))
				report_error("GRESKA: Promenljiva je tipa niz", desInc);
		} else {
			if (desInc.getDesignator().obj.getKind() != Obj.Var) {
				report_error("GRESKA: Operand nije promenljiva", desInc);
			} else {
				if (!desInc.getDesignator().obj.getType().equals(Tab.intType)) {
					report_error("GRESKA: Promenljiva nije celobrojnog tipa", desInc);
				}
			}
		}
	}
	public void visit(DesignatorStatementDecrement desDec) {
		if (desDec.getDesignator() instanceof DesignatorExpr) {
			if (!desDec.getDesignator().obj.getType().equals(Tab.intType) && !desDec.getDesignator().obj.getType().getElemType().getElemType().equals(Tab.intType))
				report_error("GRESKA: Promenljiva je tipa niz", desDec);
		} else {
			if (desDec.getDesignator().obj.getKind() != Obj.Var) {
				report_error("GRESKA: Operand nije promenljiva", desDec);
			} else {
				if (!desDec.getDesignator().obj.getType().equals(Tab.intType)) {
					report_error("GRESKA: Promenljiva nije celobrojnog tipa", desDec);
				}
			}
		}
	}
	public void visit(DesignatorStatementFor ds) {
		Obj d1 = ds.getDesignator().obj;
		Obj d2 = ds.getDesignator1().obj;
		Struct expr = ds.getExpr().obj.getType();

		if (d1.getType().getKind() != Struct.Array || d2.getType().getKind() != Struct.Array || d2.getKind() != d1.getKind()) {
			report_error("GRESKA: Operandi nisu istog tipa", ds);
		} else if (expr.getKind() != d2.getKind()) {
			report_error("GRESKA: Expr i niz nisu istog tipa", ds);
		}
	}
	public void visit(DesignatorStatementForCondition ds) {
		Obj d1 = ds.getDesignator().obj;
		Obj d2 = ds.getDesignator1().obj;
		Struct expr = ds.getExpr().obj.getType();

		if (d1.getType().getKind() != Struct.Array || d2.getType().getKind() != Struct.Array || d2.getKind() != d1.getKind()) {
			report_error("GRESKA: Operandi nisu istog tipa", ds);
		} else if (expr.getKind() != d2.getKind()) {
			report_error("GRESKA: Expr i niz nisu istog tipa", ds);
		}
	}
	
	// Addop
	public void visit(AddopList addop) {
		Struct t1 = addop.getAddopTerms().struct;
		Struct t2 = addop.getTerm().struct;
		
		if(t1.getElemType() != null) {
			//Levi operand je matrica
			if (t1.getElemType().getElemType().compatibleWith(t2) && t1.getElemType().getElemType() == Tab.intType) {
				addop.struct = addop.getAddopTerms().struct;
			} else {
				report_error("GRESKA: Greska u sabiranju, tipovi operanada su razliciti ", addop);
				addop.struct = Tab.noType;
			}
			
		} else if(t2.getElemType() != null) {
			//Desni operand je matrica
			if (t2.getElemType().getElemType().compatibleWith(t1) && t2.getElemType().getElemType() == Tab.intType) {
				addop.struct = addop.getAddopTerms().struct;
			} else {
				report_error("GRESKA: Greska u sabiranju, tipovi operanada su razliciti ", addop);
				addop.struct = Tab.noType;
			}
		}else {
			if (t1.compatibleWith(t2) && t1 == Tab.intType) {
				addop.struct = addop.getAddopTerms().struct;
			} else {
				report_error("GRESKA: Greska u sabiranju, tipovi operanada su razliciti ", addop);
				addop.struct = Tab.noType;
			}
		}
	}
	public void visit(AddopEnd addop) {
		addop.struct = addop.getTerm().struct;	
	}
	public void visit(AddopEndMinus addop) {
		if(!addop.getTerm().struct.equals(Tab.intType)) {
			report_error("GRESKA: Tip parametra uz znak minus mora biti int", addop);
			addop.struct = Tab.noType;
		} else {
			addop.struct = addop.getTerm().struct;
		}
	}

	// Conditions
	public void visit(CondFactExpr cond) {
		if (cond.getExpr().obj.getType().getKind() != Struct.Bool) {
			report_error("GRESKA: Uslov u if naredbi nije bool tipa", cond);
		}
	}
	public void visit(CondFactRelop cond) {
		Expr expr = cond.getExpr();
		Expr expr1 = cond.getExpr1();

		if (expr.obj.getType().isRefType() != expr1.obj.getType().isRefType()) {
			report_error("GRESKA: Operandi u uslovu IF naredbe nisu istog tipa", cond);
		} else if (expr.obj.getType().isRefType()) {
			if (!(expr1.obj.getType() == Tab.nullType) && !(expr.obj.getType() == Tab.nullType)
					&& expr1.obj.getType().getElemType().getKind() != expr.obj.getType().getElemType().getKind()) {
				report_error("GRESKA: Operandi u uslovu IF naredbe nisu istog tipa", cond);
			} else if (!(cond.getRelop() instanceof EqualTo || cond.getRelop() instanceof NotEqualTo)) {
				report_error("GRESKA: Greska u IF naredbi, nizovi se mogu porediti samo sa != i ==", cond);
			}
		} else {
			if (expr.obj.getType().getKind() != expr1.obj.getType().getKind()) {
				report_error("GRESKA: Operandi u uslovu IF naredbe nisu istog tipa", cond);
			} else if (expr.obj.getType().getKind() == Struct.Bool
					&& !(cond.getRelop() instanceof EqualTo || cond.getRelop() instanceof NotEqualTo)) {
				report_error("GRESKA: Greska u IF naredbi, tip bool se moze porediti samo sa != i ==", cond);
			}
		}
	}

	// Mulop
	public void visit(MulopEnd factor) {
		factor.struct = factor.getFactor().struct;
	}
	public void visit(MulopList mul) {
		Struct f1 = mul.getMulopFactors().struct;
		Struct f2 = mul.getFactor().struct;
		
		if(f1.getElemType() != null) {
			//Levi operand je matrica
			if (f1.getElemType().getElemType().equals(f2) && f1.getElemType().getElemType() == Tab.intType) {
				mul.struct = f1;
			} else {
				report_error("GRESKA: Operandi nisu odgovarajuci tipovi", mul);
				mul.struct = Tab.noType;
			}
		} else if(f2.getElemType() != null) {
			//Desni operand je matrica
			if (f2.getElemType().getElemType().equals(f1) && f2.getElemType().getElemType() == Tab.intType) {
				mul.struct = f1;
			} else {
				report_error("GRESKA: Operandi nisu odgovarajuci tipovi", mul);
				mul.struct = Tab.noType;
			}
		} else {
			if (f1.equals(f2) && f1 == Tab.intType) {
				mul.struct = f1;
			} else {
				report_error("GRESKA: Operandi nisu odgovarajuci tipovi", mul);
				mul.struct = Tab.noType;
			}
		}
	}

	// ActPars
	public void visit(ActParsList acts) {
		Struct kind = acts.getExpr().obj.getType();
		actParsList.add(kind);
	}
	public void visit(ActParsEnd acts) {
		Struct kind = acts.getExpr().obj.getType();
		actParsList.add(kind);
	}
		
	// For
	public void visit(ForSt forSt) {
		forFound = true;
	}	
	public void visit(StatementContinue sc) {
		if(!forFound) {
			report_error("GRESKA: CONTINUE naredba nije unutar FOR bloka", sc);
		}
	}
	public void visit(StatementBreak sb) {
		if(!forFound) {
			report_error("GRESKA: BREAK naredba nije unutar FOR bloka", sb);
		}
	}
	
}
