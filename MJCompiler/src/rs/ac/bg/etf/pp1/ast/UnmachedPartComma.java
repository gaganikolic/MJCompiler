// generated with ast extension for cup
// version 0.8
// 26/7/2024 13:5:46


package rs.ac.bg.etf.pp1.ast;

public class UnmachedPartComma extends UnmachedList {

    private UnmachedList UnmachedList;

    public UnmachedPartComma (UnmachedList UnmachedList) {
        this.UnmachedList=UnmachedList;
        if(UnmachedList!=null) UnmachedList.setParent(this);
    }

    public UnmachedList getUnmachedList() {
        return UnmachedList;
    }

    public void setUnmachedList(UnmachedList UnmachedList) {
        this.UnmachedList=UnmachedList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(UnmachedList!=null) UnmachedList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(UnmachedList!=null) UnmachedList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(UnmachedList!=null) UnmachedList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("UnmachedPartComma(\n");

        if(UnmachedList!=null)
            buffer.append(UnmachedList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [UnmachedPartComma]");
        return buffer.toString();
    }
}
