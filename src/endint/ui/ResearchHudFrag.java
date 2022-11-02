package endint.ui;

import arc.graphics.Color;
import arc.scene.ui.Image;
import arc.scene.ui.Label;
import arc.util.Log;
import endint.ResearchController;
import endint.content.EIFractions;
import endint.game.Fraction;
import endint.tools.FractionResearchNode;
import mindustry.Vars;
import mindustry.content.TechTree;
import mindustry.core.UI;
import mindustry.gen.Tex;
import mindustry.type.ItemStack;

public class ResearchHudFrag {
    public static void build(){
        Vars.ui.hudGroup.fill(t -> {
            t.table(r -> {
                r.update(() -> {
                    r.clear();
                    r.background(Tex.pane);
                    Fraction fraction = EIFractions.getFraction(Vars.player.team());
                    if (fraction == null) return;
                    FractionResearchNode fractionNode = ResearchController.researching.get(fraction);
                    TechTree.TechNode node;
                    if (fractionNode != null) node = fractionNode.node;
                    else node = EIFractions.getFraction(Vars.player.team()).root.get();
                    r.add(new Image(node.content.fullIcon));
                    r.row();
                    if (fractionNode != null) {
                        for (int i = 0; i < node.requirements.length; i++) {
                            ItemStack req = node.requirements[i];
                            ItemStack completed = node.finishedRequirements[i];

                            if (req.amount <= completed.amount) continue;

                            r.table(list -> {
                                list.left();
                                list.image(req.item.uiIcon).size(8 * 3).padRight(3);
                                list.add(req.item.localizedName).color(Color.lightGray);
                                Label label = list.label(() -> " " +
                                        UI.formatAmount(completed.amount) + " / "
                                        + UI.formatAmount(req.amount)).get();

                                label.setColor(Color.lightGray);
                            }).fillX().right();
                            r.row();
                        }
                    }
                });
            });
            t.center().right();
        });
    }
}
