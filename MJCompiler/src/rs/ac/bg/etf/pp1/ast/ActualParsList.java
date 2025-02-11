// generated with ast extension for cup
// version 0.8
// 26/7/2024 13:5:46


package rs.ac.bg.etf.pp1.ast;

public class ActualParsList extends ActPars {

    private ActParsContinue ActParsContinue;

    public ActualParsList (ActParsContinue ActParsContinue) {
        this.ActParsContinue=ActParsContinue;
        if(ActParsContinue!=null) ActParsContinue.setParent(this);
    }

    public ActParsContinue getActParsContinue() {
        return ActParsContinue;
    }

    public void setActParsContinue(ActParsContinue ActParsContinue) {
        this.ActParsContinue=ActParsContinue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActParsContinue!=null) ActParsContinue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActParsContinue!=null) ActParsContinue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActParsContinue!=null) ActParsContinue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActualParsList(\n");

        if(ActParsContinue!=null)
            buffer.append(ActParsContinue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActualParsList]");
        return buffer.toString();
    }
}
