// generated with ast extension for cup
// version 0.8
// 26/7/2024 13:5:46


package rs.ac.bg.etf.pp1.ast;

public class AddopList extends AddopTerms {

    private AddopTerms AddopTerms;
    private Addop Addop;
    private Term Term;

    public AddopList (AddopTerms AddopTerms, Addop Addop, Term Term) {
        this.AddopTerms=AddopTerms;
        if(AddopTerms!=null) AddopTerms.setParent(this);
        this.Addop=Addop;
        if(Addop!=null) Addop.setParent(this);
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
    }

    public AddopTerms getAddopTerms() {
        return AddopTerms;
    }

    public void setAddopTerms(AddopTerms AddopTerms) {
        this.AddopTerms=AddopTerms;
    }

    public Addop getAddop() {
        return Addop;
    }

    public void setAddop(Addop Addop) {
        this.Addop=Addop;
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AddopTerms!=null) AddopTerms.accept(visitor);
        if(Addop!=null) Addop.accept(visitor);
        if(Term!=null) Term.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AddopTerms!=null) AddopTerms.traverseTopDown(visitor);
        if(Addop!=null) Addop.traverseTopDown(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AddopTerms!=null) AddopTerms.traverseBottomUp(visitor);
        if(Addop!=null) Addop.traverseBottomUp(visitor);
        if(Term!=null) Term.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AddopList(\n");

        if(AddopTerms!=null)
            buffer.append(AddopTerms.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Addop!=null)
            buffer.append(Addop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AddopList]");
        return buffer.toString();
    }
}
