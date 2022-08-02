package classes.world.type;

import arc.Core;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import content.ModStats;
import mindustry.gen.Building;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.ItemImage;
import mindustry.ui.ReqImage;
import mindustry.world.Block;
import mindustry.world.consumers.Consume;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;
import mindustry.world.meta.Stats;

public class MultiConsumer extends Consume {
    public static final StatCat
            recipesStat = new StatCat("recipes");

    public static final Stat
            recipeStat = new Stat("recipe", recipesStat);

    public Recipe[] recipes;

    protected Recipe tempRecipe;
    protected boolean ok;

    public MultiConsumer(Recipe... recipes){
        this.recipes = recipes;
    }

    @Override
    public void apply(Block block) {
        for (Recipe recipe : recipes) {
            for (ItemStack ItemStack : recipe.itemsIn) {
                block.itemFilter[ItemStack.item.id] = true;
            }
            for (LiquidStack liquidStack : recipe.liquidsIn) {
                block.liquidFilter[liquidStack.liquid.id] = true;
            }
        }
        block.hasItems = true;
        block.hasLiquids = true;
        block.acceptsItems = true;
    }

    @Override
    public void build(Building build, Table table) {
        table.table(c -> {
            for (Recipe recipe : recipes) {
                for (ItemStack itemStack : recipe.itemsIn) {
                    c.add(new ReqImage(new ItemImage(itemStack.item.uiIcon, itemStack.amount), () -> build.items.has(itemStack.item, itemStack.amount
                    ))).padRight(8);
                }
                for (LiquidStack liquidStack : recipe.liquidsIn) {
                    c.add(new ReqImage(liquidStack.liquid.uiIcon, () -> build.liquids.get(liquidStack.liquid) > liquidStack.amount)).padRight(8);
                }
                c.add(new Image(Core.atlas.find("f5of-arrow"))).padRight(8);
                for (ItemStack itemStack : recipe.itemsOut) {
                    c.add(new ItemImage(itemStack.item.uiIcon, itemStack.amount)).padRight(8);
                }
                for (LiquidStack liquidStack : recipe.liquidsOut) {
                    c.add(new Image(liquidStack.liquid.uiIcon)).padRight(8);
                }
                c.row();
            }
        }).left();
    }

    Recipe getAvailableRecipe(Building build){
        for (Recipe recipe : recipes) {
            ok = true;
            for (LiquidStack liquidStack : recipe.liquidsIn) {
                if(build.liquids.get(liquidStack.liquid) <= liquidStack.amount) ok = false;
            }
            for (ItemStack itemStack : recipe.itemsIn) {
                if(!build.items.has(itemStack.item, itemStack.amount)) ok = false;
            }
            if(ok) return recipe;
        }
        return null;
    }

    @Override
    public float efficiency(Building build) {
        return getAvailableRecipe(build) != null ? 1f : 0f;
    }

    @Override
    public void trigger(Building build) {
        tempRecipe = getAvailableRecipe(build);
        if(tempRecipe == null) return;

        for (ItemStack itemStack : tempRecipe.itemsIn) {
            build.items.remove(itemStack);
        }
        for (LiquidStack liquidStack : tempRecipe.liquidsIn) {
            build.liquids.remove(liquidStack.liquid, liquidStack.amount);
        }

        for (ItemStack itemStack : tempRecipe.itemsOut) {
            build.items.add(itemStack.item, itemStack.amount);
        }
        for (LiquidStack liquidStack : tempRecipe.liquidsOut) {
            build.liquids.add(liquidStack.liquid, liquidStack.amount);
        }

        if(build instanceof Temperaturec) ((Temperaturec) build).addTemperature(tempRecipe.temperatureProd);
    }

    @Override
    public void display(Stats stats) {
        stats.add(recipeStat, table -> {
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
                }).growX().pad(5);
                table.row();
            }
        });
    }

    @Override
    public void update(Building build) {
        tempRecipe = getAvailableRecipe(build);
        for (ItemStack itemStack : tempRecipe.itemsOut) {
            build.dump(itemStack.item);
        }
        for (LiquidStack liquidStack : tempRecipe.liquidsOut) {
            build.dumpLiquid(liquidStack.liquid);
        }
    }
}
