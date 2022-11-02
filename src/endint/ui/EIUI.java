package endint.ui;

public class EIUI {
    public EIResearchDialog researchDialog;

    public void init(){
        researchDialog = new EIResearchDialog();
        ResearchHudFrag.build();
    }
}
