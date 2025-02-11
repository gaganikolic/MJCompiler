// generated with ast extension for cup
// version 0.8
// 26/7/2024 13:5:46


package rs.ac.bg.etf.pp1.ast;

public class MatrixDecl extends ArrMatrix {

    private ArrMatrix ArrMatrix;
    private Expr Expr;

    public MatrixDecl (ArrMatrix ArrMatrix, Expr Expr) {
        this.ArrMatrix=ArrMatrix;
        if(ArrMatrix!=null) ArrMatrix.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public ArrMatrix getArrMatrix() {
        return ArrMatrix;
    }

    public void setArrMatrix(ArrMatrix ArrMatrix) {
        this.ArrMatrix=ArrMatrix;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ArrMatrix!=null) ArrMatrix.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ArrMatrix!=null) ArrMatrix.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ArrMatrix!=null) ArrMatrix.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MatrixDecl(\n");

        if(ArrMatrix!=null)
            buffer.append(ArrMatrix.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MatrixDecl]");
        return buffer.toString();
    }
}
