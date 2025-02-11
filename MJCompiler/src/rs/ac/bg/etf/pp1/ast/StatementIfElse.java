// generated with ast extension for cup
// version 0.8
// 26/7/2024 13:5:46


package rs.ac.bg.etf.pp1.ast;

public class StatementIfElse extends Statement {

    private LParenIf LParenIf;
    private Condition Condition;
    private RParenIf RParenIf;
    private Statement Statement;
    private ElseSt ElseSt;
    private Statement Statement1;

    public StatementIfElse (LParenIf LParenIf, Condition Condition, RParenIf RParenIf, Statement Statement, ElseSt ElseSt, Statement Statement1) {
        this.LParenIf=LParenIf;
        if(LParenIf!=null) LParenIf.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.RParenIf=RParenIf;
        if(RParenIf!=null) RParenIf.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.ElseSt=ElseSt;
        if(ElseSt!=null) ElseSt.setParent(this);
        this.Statement1=Statement1;
        if(Statement1!=null) Statement1.setParent(this);
    }

    public LParenIf getLParenIf() {
        return LParenIf;
    }

    public void setLParenIf(LParenIf LParenIf) {
        this.LParenIf=LParenIf;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public RParenIf getRParenIf() {
        return RParenIf;
    }

    public void setRParenIf(RParenIf RParenIf) {
        this.RParenIf=RParenIf;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public ElseSt getElseSt() {
        return ElseSt;
    }

    public void setElseSt(ElseSt ElseSt) {
        this.ElseSt=ElseSt;
    }

    public Statement getStatement1() {
        return Statement1;
    }

    public void setStatement1(Statement Statement1) {
        this.Statement1=Statement1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LParenIf!=null) LParenIf.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
        if(RParenIf!=null) RParenIf.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(ElseSt!=null) ElseSt.accept(visitor);
        if(Statement1!=null) Statement1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LParenIf!=null) LParenIf.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(RParenIf!=null) RParenIf.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(ElseSt!=null) ElseSt.traverseTopDown(visitor);
        if(Statement1!=null) Statement1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LParenIf!=null) LParenIf.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(RParenIf!=null) RParenIf.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(ElseSt!=null) ElseSt.traverseBottomUp(visitor);
        if(Statement1!=null) Statement1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementIfElse(\n");

        if(LParenIf!=null)
            buffer.append(LParenIf.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(RParenIf!=null)
            buffer.append(RParenIf.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ElseSt!=null)
            buffer.append(ElseSt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement1!=null)
            buffer.append(Statement1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementIfElse]");
        return buffer.toString();
    }
}
