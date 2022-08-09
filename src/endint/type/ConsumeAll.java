package endint.type;

import arc.Core;
import arc.func.Prov;
import arc.scene.ui.Button;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.Strings;
import endint.math.ModMath;
import endint.world.blocks.crafting.MultiCrafter;
import mindustry.gen.Building;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.ui.Bar;
import mindustry.ui.ItemImage;
import mindustry.ui.ReqImage;
import mindustry.world.Block;
import mindustry.world.consumers.ConsumePower;
import mindustry.world.meta.BlockStatus;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;
import mindustry.world.meta.Stats;

import static endint.content.ModStats.recipeStat;

public class ConsumeAll {

    public Recipe[] recipes;
    public Prov<ConsumePower> power;

    protected Recipe tempRecipe;

    public ConsumeAll(Recipe... recipes) {
        this.recipes = recipes;
    }

    public void init(Block block){
        ConsumePower cons = new ConsumePower();
        power = () -> cons;
        block.hasConsumers = true;
        boolean i = false, l = false, p = false, pi = false, po = false;
        float ml = 0;
        int mi = 0;
        for (Recipe recipe : recipes) {
            for (ItemStack itemStack : recipe.itemsIn) {
                if(mi < itemStack.amount) mi = itemStack.amount;
                block.itemFilter[itemStack.item.id] = true;
                i = true;
            }
            for (LiquidStack liquidStack : recipe.liquidsIn) {
                if(ml < liquidStack.amount) ml = liquidStack.amount;
                block.liquidFilter[liquidStack.liquid.id] = true;
                l = true;
            }
            for (ItemStack itemStack : recipe.itemsOut) {
                if(mi < itemStack.amount) mi = itemStack.amount;
                i = true;
            }
            for (LiquidStack liquidStack : recipe.liquidsOut) {
                if(ml < liquidStack.amount) ml = liquidStack.amount;
                l = true;
            }
            if(recipe.powerIn != 0) pi = p = true;
            if(recipe.powerOut != 0) po = p = true;
        }

        if(i) block.itemCapacity = mi * 2;
        if(l) block.liquidCapacity = ml * 3;
        block.hasItems = i;
        block.hasLiquids = l;
        block.acceptsItems = i;
        block.hasPower = p;
        block.consumesPower = pi;
        block.outputsPower = po;

        block.consume(power.get());

        setBars(block);
    }

    public void setBars(Block block){
        for (Recipe recipe : recipes) {
            for (LiquidStack liquidStack : recipe.liquidsIn) {
                addLiquidBar(block, liquidStack.liquid);
            }
            for (LiquidStack liquidStack : recipe.liquidsOut) {
                addLiquidBar(block, liquidStack.liquid);
            }
            for (ItemStack itemStack : recipe.itemsIn) {
                addItemBar(block, itemStack.item);
            }
            for (ItemStack itemStack : recipe.itemsOut) {
                addItemBar(block, itemStack.item);
            }
        }
        if(block.consumesPower) addConsPowerBar(block);
        if(block.outputsPower) addOutPowerBar(block);
    }

    void addItemBar(Block block, Item item){
        block.addBar("item-" + item, entity -> new Bar(
                () -> Core.bundle.get("item." + item.name + ".name"),
                () -> Pal.items,
                () -> (float) entity.items.get(item) / block.itemCapacity)
        );
    }

    void addLiquidBar(Block block, Liquid liquid){
        block.addBar("item-" + liquid, entity -> new Bar(
                () -> Core.bundle.get("liquid." + liquid.name + ".name"),
                liquid::barColor,
                () -> entity.liquids.get(liquid) / block.liquidCapacity)
        );
    }

    void addConsPowerBar(Block block){
        block.addBar("powerin", entity -> new Bar(
                () -> Core.bundle.get("powerin"),
                () -> Pal.powerBar,
                entity::efficiency)
        );
    }

    void addOutPowerBar(Block block){
        block.addBar("powerout", entity -> new Bar(
                () -> Core.bundle.get("powerout"),
                () -> Pal.powerBar,
                () -> entity.power != null ? entity.efficiency() : 0f)
        );
    }

    public void display(MultiCrafter.MultiCrafterBuild build, Table table){
        table.table(a -> {
            tempRecipe = recipes[build.currentRecipe];
            {
                a.table(c -> {
                    for (ItemStack itemStack : tempRecipe.itemsIn) {
                        c.add(new ReqImage(new ItemImage(itemStack.item.uiIcon, itemStack.amount),
                                () -> build.items.has(itemStack.item, itemStack.amount))).padRight(8);
                    }
                    for (LiquidStack liquidStack : tempRecipe.liquidsIn) {
                        c.add(new ReqImage(new ItemImage(liquidStack.liquid.uiIcon, (int) liquidStack.amount),
                                () -> build.liquids.get(liquidStack.liquid) >= liquidStack.amount)).padRight(8);
                    }

                    if(tempRecipe.powerIn != 0) table.add(ModMath.fixFloat(tempRecipe.powerIn / tempRecipe.craftTime, 2)
                            + " " + Core.bundle.get("unit.power-second")).padRight(8);

                    //c.row();

                    c.add(new Image(Core.atlas.find("f5of-arrow"))).padRight(8);
                    c.add(ModMath.fixFloat(tempRecipe.craftTime / 60f, 2)
                            + " " + Core.bundle.get("unit.seconds")).padRight(8);
                    c.add(tempRecipe.temperatureProd + " " + Core.bundle.get("unit.temperature")).padRight(8);

                    //c.row();

                    for (ItemStack itemStack : tempRecipe.itemsOut) {
                        c.add(new ItemImage(itemStack.item.uiIcon, itemStack.amount)).padRight(8);
                    }
                    for (LiquidStack liquidStack : tempRecipe.liquidsOut) {
                        c.add(new ItemImage(liquidStack.liquid.uiIcon, (int) liquidStack.amount)).padRight(8);
                    }

                    if(tempRecipe.powerOut != 0) table.add(ModMath.fixFloat(tempRecipe.powerOut / tempRecipe.craftTime, 2)
                            + " " + Core.bundle.get("unit.power-second")).padRight(8);
                });
                a.row();
            }
        }).left();
    }

    void showStatsOnTable(Table table, Recipe recipe){
        for (ItemStack itemStack : recipe.itemsIn) {
            table.add(new ItemImage(itemStack.item.uiIcon, itemStack.amount)).padRight(8);
        }
        for (LiquidStack liquidStack : recipe.liquidsIn) {
            table.add(new ItemImage(liquidStack.liquid.uiIcon, (int) liquidStack.amount)).padRight(8);
        }

        if(recipe.powerIn != 0) table.add(ModMath.fixFloat(recipe.powerIn / recipe.craftTime, 2)
                + " " + Core.bundle.get("unit.power-second")).padRight(8);

        table.add(new Image(Core.atlas.find("f5of-arrow"))).padRight(8);
        table.add(ModMath.fixFloat(recipe.craftTime / 60f, 2) + " " + Core.bundle.get("unit.seconds")).padRight(8);
        table.add(recipe.temperatureProd + " " + Core.bundle.get("unit.temperature")).padRight(8);

        for (ItemStack itemStack : recipe.itemsOut) {
            table.add(new ItemImage(itemStack.item.uiIcon, itemStack.amount)).padRight(8);
        }
        for (LiquidStack liquidStack : recipe.liquidsOut) {
            table.add(new ItemImage(liquidStack.liquid.uiIcon, (int) liquidStack.amount)).padRight(8);
        }

        if(recipe.powerOut != 0) table.add(ModMath.fixFloat(recipe.powerOut / recipe.craftTime, 2)
                + " " + Core.bundle.get("unit.power-second")).padRight(8);
    }

    public void setStats(Stats stats) {
        stats.add(recipeStat, table -> {
            table.row();
            for (Recipe recipe : recipes) {
                table.table(c -> {
                    c.setBackground(Tex.whiteui);
                    c.setColor(Pal.darkestGray);

                    showStatsOnTable(c, recipe);
                }).growX().pad(5);
                table.row();
            }
        });
    }

    public Recipe getRecipe(int ind){
        return recipes[ind];
    }

    public BlockStatus canWork(MultiCrafter.MultiCrafterBuild build){
        tempRecipe = getRecipe(build.currentRecipe);

        for (ItemStack itemStack : tempRecipe.itemsIn)
            if(!build.items.has(itemStack.item, itemStack.amount))
                return BlockStatus.noInput;
        for (LiquidStack liquidStack : tempRecipe.liquidsIn)
            if(build.liquids.get(liquidStack.liquid) < liquidStack.amount)
                return BlockStatus.noInput;
        for (ItemStack itemStack : tempRecipe.itemsOut)
            if(build.block.itemCapacity - build.items.get(itemStack.item) < 0)
                return BlockStatus.noOutput;
        for (LiquidStack liquidStack : tempRecipe.liquidsOut)
            if(build.block.liquidCapacity - build.liquids.get(liquidStack.liquid) < 0)
                return BlockStatus.noOutput;
        if(build.power != null) if(build.power.status == 0 && tempRecipe.powerIn != 0) return BlockStatus.noInput;
        if(build.power == null && tempRecipe.powerIn != 0) return BlockStatus.noInput;

        return BlockStatus.active;
    }

    public float getEfficiency(MultiCrafter.MultiCrafterBuild build){
        if(canWork(build) != BlockStatus.active) return 0f;
        if(build.power != null) return getRecipe(build.currentRecipe).powerIn != 0 ? build.power.status : 1f;
        return 1f;
    }

    void setConsumePower(MultiCrafter.MultiCrafterBuild build){
        tempRecipe = getRecipe(build.currentRecipe);
        power.get().powerIn = tempRecipe.powerIn / getRecipe(build.currentRecipe).craftTime;
    }

    public void handleCraft(MultiCrafter.MultiCrafterBuild build){
        tempRecipe = getRecipe(build.currentRecipe);

        for (ItemStack itemStack : tempRecipe.itemsIn) build.items.remove(itemStack.item, itemStack.amount);
        for (LiquidStack liquidStack : tempRecipe.liquidsIn) build.liquids.remove(liquidStack.liquid, liquidStack.amount);
        for (ItemStack itemStack : tempRecipe.itemsOut) build.items.add(itemStack.item, itemStack.amount);
        for (LiquidStack liquidStack : tempRecipe.liquidsOut) build.liquids.add(liquidStack.liquid, liquidStack.amount);

        if(build instanceof Temperaturec) ((Temperaturec) build).addTemperature(tempRecipe.temperatureProd);
    }

    public void buildConfiguration(MultiCrafter.MultiCrafterBuild build, Table table){
        int ind = 0;
        for (Recipe recipe : recipes) {
            Button btn = new Button();

            int i = ind;
            btn.clicked(() -> {
                build.currentRecipe = i;
                setConsumePower(build);
                build.deselect();
            });

            ind++;

            showStatsOnTable(btn, recipe);

            table.add(btn);
            table.row();
        }
    }

    public static class Recipe{
        public LiquidStack[] liquidsIn = {}, liquidsOut = {};
        public ItemStack[] itemsIn = {}, itemsOut = {};
        public float powerIn, powerOut;
        public int craftTime = 60;
        public float temperatureProd = 0f;
    }

    public static class ConsumePower extends mindustry.world.consumers.ConsumePower{
        public float powerIn = 0;

        public ConsumePower(){

        }

        @Override
        public float requestedPower(Building entity) {
            return powerIn;
        }

        boolean canWork(Building build){
            return build instanceof MultiCrafter.MultiCrafterBuild &&
                    build.block instanceof MultiCrafter && ((MultiCrafter) build.block).consume.canWork((MultiCrafter.MultiCrafterBuild) build)
                    == BlockStatus.active;
        }

        @Override
        public boolean ignore() {
            return false;
        }

        @Override
        public void apply(Block block) {
        }

        @Override
        public void display(Stats stats) {
        }

        @Override
        public float efficiency(Building build) {
            return canWork(build) ? build.power.status : 0f;
        }
    }
}
