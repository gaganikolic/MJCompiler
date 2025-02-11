package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;

public class CounterVisitor extends VisitorAdaptor {
	protected int count;

	public int getCount() {
		return count;
	}

	public static class FormParamCounter extends CounterVisitor {
		public void visit(FormParsArr formParamDecl) {
			count++;
		}
		public void visit(FormParsVar formParamDecl) {
			count++;
		}
	}

	public static class VarCounter extends CounterVisitor {
		public void visit(VarName formParamDecl) {
			count++;
		}
		public void visit(ConstNum formParamDecl) {
			count++;
		}
		public void visit(ConstChar formParamDecl) {
			count++;
		}
		public void visit(ConstBool formParamDecl) {
			count++;
		}
	}
}
