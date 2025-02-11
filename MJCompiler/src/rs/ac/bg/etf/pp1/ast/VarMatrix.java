// generated with ast extension for cup
// version 0.8
// 26/7/2024 13:5:46


package rs.ac.bg.etf.pp1.ast;

public class VarMatrix extends Var {

    private Arr Arr;

    public VarMatrix (Arr Arr) {
        this.Arr=Arr;
        if(Arr!=null) Arr.setParent(this);
    }

    public Arr getArr() {
        return Arr;
    }

    public void setArr(Arr Arr) {
        this.Arr=Arr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Arr!=null) Arr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Arr!=null) Arr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Arr!=null) Arr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarMatrix(\n");

        if(Arr!=null)
            buffer.append(Arr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarMatrix]");
        return buffer.toString();
    }
}
