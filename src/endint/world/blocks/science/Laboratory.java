package endint.world.blocks.science;

import arc.Core;
import arc.Events;
import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import arc.util.Log;
import endint.EndlessInterstellarVars;
import endint.ResearchController;
import endint.game.Fraction;
import endint.tools.FractionResearchNode;
import endint.world.Fractionc;
import mindustry.Vars;
import mindustry.content.TechTree;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.ui.dialogs.DiscordDialog;
import mindustry.world.Block;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;

public class Laboratory extends Block implements Fractionc {
    public final Fraction fraction;

    public int packetsPerSecond = 1;
    public int maxItemsInPacket = 5;

    public Laboratory(String name, Fraction f) {
        super(name);
        hasPower = true;
        hasItems = true;
        solid = true;
        update = true;
        destructible = true;
        group = BlockGroup.logic;
        flags = EnumSet.of(BlockFlag.factory);
        envEnabled = Env.any;
        configurable = true;
        fraction = f;
    }

    @Override
    public Fraction getFraction() {
        return fraction;
    }

    @Override
    public boolean unlockedNow() {
        return unlocked() && Vars.player.team() == fraction.team;
    }

    public class LaboratoryBuild extends Building {
        @Override
        public boolean acceptItem(Building source, Item item) {
            return items.get(item) < getMaximumAccepted(item);
        }

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
            Core.scene.act();
            Sounds.unlock.play();
            Events.fire(new EventType.ResearchEvent(node.content));
        }
    }
}
