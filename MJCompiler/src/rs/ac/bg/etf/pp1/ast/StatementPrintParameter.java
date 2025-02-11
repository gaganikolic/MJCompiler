// generated with ast extension for cup
// version 0.8
// 26/7/2024 13:5:46


package rs.ac.bg.etf.pp1.ast;

public class StatementPrintParameter extends Statement {

    private Expr Expr;
    private PrintNum PrintNum;

    public StatementPrintParameter (Expr Expr, PrintNum PrintNum) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.PrintNum=PrintNum;
        if(PrintNum!=null) PrintNum.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public PrintNum getPrintNum() {
        return PrintNum;
    }

    public void setPrintNum(PrintNum PrintNum) {
        this.PrintNum=PrintNum;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(PrintNum!=null) PrintNum.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(PrintNum!=null) PrintNum.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(PrintNum!=null) PrintNum.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementPrintParameter(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(PrintNum!=null)
            buffer.append(PrintNum.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementPrintParameter]");
        return buffer.toString();
    }
}
