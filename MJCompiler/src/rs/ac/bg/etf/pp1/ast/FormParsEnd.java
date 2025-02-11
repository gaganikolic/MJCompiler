// generated with ast extension for cup
// version 0.8
// 26/7/2024 13:5:46


package rs.ac.bg.etf.pp1.ast;

public class FormParsEnd extends FormPars {

    private FormParsContinue FormParsContinue;

    public FormParsEnd (FormParsContinue FormParsContinue) {
        this.FormParsContinue=FormParsContinue;
        if(FormParsContinue!=null) FormParsContinue.setParent(this);
    }

    public FormParsContinue getFormParsContinue() {
        return FormParsContinue;
    }

    public void setFormParsContinue(FormParsContinue FormParsContinue) {
        this.FormParsContinue=FormParsContinue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsContinue!=null) FormParsContinue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsContinue!=null) FormParsContinue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsContinue!=null) FormParsContinue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsEnd(\n");

        if(FormParsContinue!=null)
            buffer.append(FormParsContinue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsEnd]");
        return buffer.toString();
    }
}
