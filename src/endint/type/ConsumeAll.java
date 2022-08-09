package endint.type;

import arc.Core;
import arc.scene.ui.Button;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.Strings;
import endint.world.blocks.crafting.MultiCrafter;
import mindustry.gen.Building;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.Bar;
import mindustry.ui.ItemImage;
import mindustry.ui.ReqImage;
import mindustry.world.Block;
import mindustry.world.meta.BlockStatus;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;
import mindustry.world.meta.Stats;

import static endint.content.ModStats.recipeStat;

public class ConsumeAll {

    public Recipe[] recipes;

    protected Recipe tempRecipe;

    public ConsumeAll(Recipe... recipes) {
        this.recipes = recipes;
    }

    public void init(Block block){
        boolean i = false, l = false;
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
                block.addLiquidBar(liquidStack.liquid);
            }
            for (ItemStack itemStack : recipe.itemsOut) {
                if(mi < itemStack.amount) mi = itemStack.amount;
                i = true;
            }
            for (LiquidStack liquidStack : recipe.liquidsOut) {
                if(ml < liquidStack.amount) ml = liquidStack.amount;
                l = true;
            }
        }

        if(i) block.itemCapacity = mi * 2;
        if(l) block.liquidCapacity = ml * 3;
        block.hasItems = i;
        block.hasLiquids = l;
        block.acceptsItems = i;
    }

    public void setBars(Block block){
        for (Recipe recipe : recipes) {
            for (LiquidStack liquidStack : recipe.liquidsIn) {
                block.addLiquidBar(liquidStack.liquid);
            }
            for (LiquidStack liquidStack : recipe.liquidsOut) {
                block.addLiquidBar(liquidStack.liquid);
            }
            for (ItemStack itemStack : recipe.itemsIn) {
                addItemBar(block, itemStack.item);
            }
            for (ItemStack itemStack : recipe.itemsOut) {
                addItemBar(block, itemStack.item);
            }
        }
    }

    void addItemBar(Block block, Item item){
        block.addBar("item-" + item, entity -> new Bar(
                () -> Core.bundle.get("item." + item.name + ".name") + ":",
                () -> Pal.items,
                () -> (float) entity.items.get(item) / block.itemCapacity)
        );
    }

    public void display(MultiCrafter.MultiCrafterBuild build, Table table){
        table.table(a -> {
            tempRecipe = recipes[build.currentRecipe];
            {
                int topSize = (tempRecipe.itemsIn.length + tempRecipe.liquidsIn.length) * 40;
                int downSize = (tempRecipe.itemsOut.length + tempRecipe.liquidsOut.length) * 40;
                a.table(c -> {

                    for (ItemStack itemStack : tempRecipe.itemsIn) {
                        c.add(new ReqImage(new ItemImage(itemStack.item.uiIcon, itemStack.amount),
                                () -> build.items.has(itemStack.item, itemStack.amount))).padRight(8);
                    }
                    for (LiquidStack liquidStack : tempRecipe.liquidsIn) {
                        c.add(new ReqImage(new ItemImage(liquidStack.liquid.uiIcon, (int) liquidStack.amount),
                                () -> build.liquids.get(liquidStack.liquid) >= liquidStack.amount)).padRight(8);
                    }

                    c.row();

                    c.add(new Image(Core.atlas.find("f5of-arrow"))).padRight(8).center();
                    c.add((tempRecipe.craftTime / 60f - (tempRecipe.craftTime / 60f) % 0.1) + " " + Core.bundle.get("unit.seconds"));

                    c.row();

                    for (ItemStack itemStack : tempRecipe.itemsOut) {
                        c.add(new ItemImage(itemStack.item.uiIcon, itemStack.amount)).padRight(8);
                    }
                    for (LiquidStack liquidStack : tempRecipe.liquidsOut) {
                        c.add(new ItemImage(liquidStack.liquid.uiIcon, (int) liquidStack.amount)).padRight(8);
                    }
                });
                a.row();
            }
        }).left();
    }

    public void setStats(Stats stats) {
        stats.add(recipeStat, table -> {
            table.row();
            for (Recipe recipe : recipes) {
                table.table(c -> {
                    c.setBackground(Tex.whiteui);
                    c.setColor(Pal.darkestGray);

                    for (ItemStack itemStack : recipe.itemsIn) {
                        c.add(new ItemImage(itemStack.item.uiIcon, itemStack.amount)).padRight(8);
                    }
                    for (LiquidStack liquidStack : recipe.liquidsIn) {
                        c.add(new ItemImage(liquidStack.liquid.uiIcon, (int) liquidStack.amount)).padRight(8);
                    }

                    c.add(new Image(Core.atlas.find("f5of-arrow"))).padRight(8);

                    for (ItemStack itemStack : recipe.itemsOut) {
                        c.add(new ItemImage(itemStack.item.uiIcon, itemStack.amount)).padRight(8);
                    }
                    for (LiquidStack liquidStack : recipe.liquidsOut) {
                        c.add(new ItemImage(liquidStack.liquid.uiIcon, (int) liquidStack.amount)).padRight(8);
                    }

                    c.add((recipe.craftTime / 60f - (recipe.craftTime / 60f) % 0.1 + 0.1) + " " + Core.bundle.get("unit.seconds"));

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

        return BlockStatus.active;
    }

    public void handleCraft(MultiCrafter.MultiCrafterBuild build){
        tempRecipe = getRecipe(build.currentRecipe);

        for (ItemStack itemStack : tempRecipe.itemsIn) build.items.remove(itemStack.item, itemStack.amount);
        for (LiquidStack liquidStack : tempRecipe.liquidsIn) build.liquids.remove(liquidStack.liquid, liquidStack.amount);
        for (ItemStack itemStack : tempRecipe.itemsOut) build.items.add(itemStack.item, itemStack.amount);
        for (LiquidStack liquidStack : tempRecipe.liquidsOut) build.liquids.add(liquidStack.liquid, liquidStack.amount);
    }

    public void buildConfiguration(MultiCrafter.MultiCrafterBuild build, Table table){
        int ind = 0;
        for (Recipe recipe : recipes) {
            Button btn = new Button();

            int i = ind;
            btn.clicked(() -> {
                build.currentRecipe = i;
                build.deselect();
            });

            ind++;

            for (ItemStack itemStack : recipe.itemsIn) {
                btn.add(new ItemImage(itemStack.item.uiIcon, itemStack.amount)).padRight(8);
            }
            for (LiquidStack liquidStack : recipe.liquidsIn) {
                btn.add(new ItemImage(liquidStack.liquid.uiIcon, (int) liquidStack.amount)).padRight(8);
            }

            btn.add(new Image(Core.atlas.find("f5of-arrow"))).padRight(8);

            for (ItemStack itemStack : recipe.itemsOut) {
                btn.add(new ItemImage(itemStack.item.uiIcon, itemStack.amount)).padRight(8);
            }
            for (LiquidStack liquidStack : recipe.liquidsOut) {
                btn.add(new ItemImage(liquidStack.liquid.uiIcon, (int) liquidStack.amount)).padRight(8);
            }

            btn.add((recipe.craftTime / 60f - (recipe.craftTime / 60f) % 0.1 + 0.1) + " " + Core.bundle.get("unit.seconds"));

            table.add(btn);
            table.row();
        }
    }

    public static class Recipe{
        public LiquidStack[] liquidsIn = {}, liquidsOut = {};
        public ItemStack[] itemsIn = {}, itemsOut = {};
        public int craftTime = 60;
    }
}
