package endint.world.blocks.core;

import arc.Events;
import arc.scene.ui.layout.Table;
import endint.EndlessInterstellarVars;
import endint.ResearchController;
import endint.game.Fraction;
import endint.tools.FractionResearchNode;
import mindustry.content.TechTree;
import mindustry.game.EventType;
import mindustry.gen.Sounds;
import mindustry.type.Item;
import mindustry.type.ItemStack;

public class FallenCore extends Core{
    public float powerProduction = 1f;
    public int packetsPerSecond = 1;
    public int maxItemsInPacket = 5;

    public FallenCore(String name, Fraction f) {
        super(name, f);
    }

    public class FallenCoreBuild extends CoreBuild {
        long counter = 0;
        @Override
        public void updateTile() {
            if (counter == 0) counter = System.currentTimeMillis();
            if (System.currentTimeMillis() - counter >= 1000f / packetsPerSecond) {
                FractionResearchNode node = ResearchController.researching.get(fraction);
                int itemsConsumed = 0;
                if (node != null) {
                    int sumReq = 0;
                    int sumFin = 0;
                    for (int i = 0; i < node.node.requirements.length; i++) {
                        Item item = node.node.requirements[i].item;
                        int amount = node.node.requirements[i].amount;
                        int req = node.node.requirements[i].amount;
                        int fin = node.node.finishedRequirements[i].amount;
                        sumReq += req;
                        sumFin += fin;
                        if ((req - fin) != 0 && itemsConsumed < maxItemsInPacket) {
                            int itemsForConsume = Math.min(Math.min(items.get(item), amount), maxItemsInPacket - itemsConsumed);
                            if (items.get(item) > 0) {
                                items.remove(ItemStack.with(item, itemsForConsume));
                                node.node.finishedRequirements[i].amount += itemsForConsume;
                                itemsConsumed += itemsForConsume;
                            }
                        }
                    }
                    if (sumReq - sumFin == 0) {
                        unlock(node.node);
                    }
                }
                counter = System.currentTimeMillis();
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            EndlessInterstellarVars.ui.researchDialog.show(this);
        }

        void unlock(TechTree.TechNode node){
            node.content.unlock();
            ResearchController.researching.remove(fraction);
            arc.Core.scene.act();
            Sounds.unlock.play();
            Events.fire(new EventType.ResearchEvent(node.content));
        }

        @Override
        public float getPowerProduction() {
            return powerProduction;
        }
    }
}
