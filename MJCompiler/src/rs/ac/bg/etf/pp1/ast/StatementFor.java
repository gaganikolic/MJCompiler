// generated with ast extension for cup
// version 0.8
// 26/7/2024 13:5:46


package rs.ac.bg.etf.pp1.ast;

public class StatementFor extends Statement {

    private ForSt ForSt;
    private DesignatorStatementList DesignatorStatementList;
    private LSemiFor LSemiFor;
    private RSemiFor RSemiFor;
    private DesignatorStatementListSecond DesignatorStatementListSecond;
    private RParenFor RParenFor;
    private Statement Statement;

    public StatementFor (ForSt ForSt, DesignatorStatementList DesignatorStatementList, LSemiFor LSemiFor, RSemiFor RSemiFor, DesignatorStatementListSecond DesignatorStatementListSecond, RParenFor RParenFor, Statement Statement) {
        this.ForSt=ForSt;
        if(ForSt!=null) ForSt.setParent(this);
        this.DesignatorStatementList=DesignatorStatementList;
        if(DesignatorStatementList!=null) DesignatorStatementList.setParent(this);
        this.LSemiFor=LSemiFor;
        if(LSemiFor!=null) LSemiFor.setParent(this);
        this.RSemiFor=RSemiFor;
        if(RSemiFor!=null) RSemiFor.setParent(this);
        this.DesignatorStatementListSecond=DesignatorStatementListSecond;
        if(DesignatorStatementListSecond!=null) DesignatorStatementListSecond.setParent(this);
        this.RParenFor=RParenFor;
        if(RParenFor!=null) RParenFor.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public ForSt getForSt() {
        return ForSt;
    }

    public void setForSt(ForSt ForSt) {
        this.ForSt=ForSt;
    }

    public DesignatorStatementList getDesignatorStatementList() {
        return DesignatorStatementList;
    }

    public void setDesignatorStatementList(DesignatorStatementList DesignatorStatementList) {
        this.DesignatorStatementList=DesignatorStatementList;
    }

    public LSemiFor getLSemiFor() {
        return LSemiFor;
    }

    public void setLSemiFor(LSemiFor LSemiFor) {
        this.LSemiFor=LSemiFor;
    }

    public RSemiFor getRSemiFor() {
        return RSemiFor;
    }

    public void setRSemiFor(RSemiFor RSemiFor) {
        this.RSemiFor=RSemiFor;
    }

    public DesignatorStatementListSecond getDesignatorStatementListSecond() {
        return DesignatorStatementListSecond;
    }

    public void setDesignatorStatementListSecond(DesignatorStatementListSecond DesignatorStatementListSecond) {
        this.DesignatorStatementListSecond=DesignatorStatementListSecond;
    }

    public RParenFor getRParenFor() {
        return RParenFor;
    }

    public void setRParenFor(RParenFor RParenFor) {
        this.RParenFor=RParenFor;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ForSt!=null) ForSt.accept(visitor);
        if(DesignatorStatementList!=null) DesignatorStatementList.accept(visitor);
        if(LSemiFor!=null) LSemiFor.accept(visitor);
        if(RSemiFor!=null) RSemiFor.accept(visitor);
        if(DesignatorStatementListSecond!=null) DesignatorStatementListSecond.accept(visitor);
        if(RParenFor!=null) RParenFor.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ForSt!=null) ForSt.traverseTopDown(visitor);
        if(DesignatorStatementList!=null) DesignatorStatementList.traverseTopDown(visitor);
        if(LSemiFor!=null) LSemiFor.traverseTopDown(visitor);
        if(RSemiFor!=null) RSemiFor.traverseTopDown(visitor);
        if(DesignatorStatementListSecond!=null) DesignatorStatementListSecond.traverseTopDown(visitor);
        if(RParenFor!=null) RParenFor.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ForSt!=null) ForSt.traverseBottomUp(visitor);
        if(DesignatorStatementList!=null) DesignatorStatementList.traverseBottomUp(visitor);
        if(LSemiFor!=null) LSemiFor.traverseBottomUp(visitor);
        if(RSemiFor!=null) RSemiFor.traverseBottomUp(visitor);
        if(DesignatorStatementListSecond!=null) DesignatorStatementListSecond.traverseBottomUp(visitor);
        if(RParenFor!=null) RParenFor.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementFor(\n");

        if(ForSt!=null)
            buffer.append(ForSt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementList!=null)
            buffer.append(DesignatorStatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(LSemiFor!=null)
            buffer.append(LSemiFor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(RSemiFor!=null)
            buffer.append(RSemiFor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementListSecond!=null)
            buffer.append(DesignatorStatementListSecond.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(RParenFor!=null)
            buffer.append(RParenFor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementFor]");
        return buffer.toString();
    }
}
