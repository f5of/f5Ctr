package endint.world.type;

import arc.Core;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import endint.world.type.Recipe;
import endint.world.type.Temperaturec;
import mindustry.gen.Building;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.ItemImage;
import mindustry.ui.ReqImage;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumePower;
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
    public boolean ignore() {return false;}

    @Override
    public void apply(Block block) {
        boolean i = false, l = false, p = false;
        for (Recipe recipe : recipes) {
            for (ItemStack ItemStack : recipe.itemsIn) {
                block.itemFilter[ItemStack.item.id] = true;
                i = true;
            }
            for (LiquidStack liquidStack : recipe.liquidsIn) {
                block.liquidFilter[liquidStack.liquid.id] = true;
                l = true;
            }
            if(recipe.energyIn > 0 || recipe.energyOut > 0) p = true;
        }
        block.hasItems = i;
        block.hasLiquids = l;
        block.acceptsItems = true;
        block.hasPower = p;
    }

    @Override
    public void build(Building build, Table table) {
        table.table(c -> {
            for (Recipe recipe : recipes) {
                for (ItemStack itemStack : recipe.itemsIn) {
                    c.add(new ReqImage(new ItemImage(itemStack.item.uiIcon, itemStack.amount),
                            () -> build.items.has(itemStack.item, itemStack.amount))).padRight(8);
                }
                for (LiquidStack liquidStack : recipe.liquidsIn) {
                    c.add(new ReqImage(new ItemImage(liquidStack.liquid.uiIcon, (int) liquidStack.amount),
                            () -> build.liquids.get(liquidStack.liquid) >= liquidStack.amount)).padRight(8);
                }
                if(recipe.energyIn > 0f)
                c.add(Strings.fixed(recipe.energyIn / recipe.craftTime * 60f, 1) + " " + Core.bundle.get("unit.power-second")).padRight(8);

                c.add(new Image(Core.atlas.find("f5of-arrow"))).padRight(8);

                for (ItemStack itemStack : recipe.itemsOut) {
                    c.add(new ItemImage(itemStack.item.uiIcon, itemStack.amount)).padRight(8);
                }
                for (LiquidStack liquidStack : recipe.liquidsOut) {
                    c.add(new ItemImage(liquidStack.liquid.uiIcon, (int) liquidStack.amount)).padRight(8);
                }
                if(recipe.energyOut > 0f)
                    c.add(Strings.fixed(recipe.energyOut, 1) + " " + Core.bundle.get("unit.power")).padRight(8);
                if(recipe.temperatureProd > 0)
                c.add(recipe.temperatureProd + Core.bundle.get("unit.temperature")).padRight(8);

                c.row();
            }
        }).left();
    }

    Recipe getAvailableRecipe(Building build){
        for (Recipe recipe : recipes) {
            ok = true;
            for (LiquidStack liquidStack : recipe.liquidsIn) {
                if(build.liquids.get(liquidStack.liquid) < liquidStack.amount) ok = false;
            }
            for (ItemStack itemStack : recipe.itemsIn) {
                if(!build.items.has(itemStack.item, itemStack.amount)) ok = false;
            }
            if (ok) {
                if(build.block instanceof GenericCrafter) ((GenericCrafter) build.block).craftTime = recipe.craftTime;
                return recipe;
            }
        }
        return null;
    }

    @Override
    public float efficiency(Building build) {
        tempRecipe = getAvailableRecipe(build);
        if(tempRecipe != null){
            return getAvailablePower(build) < tempRecipe.energyIn / tempRecipe.craftTime ? 0f : 1f;
        }
        return 0f;
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

        if(hasPower(build))
        build.power.graph.transferPower(tempRecipe.energyOut);

        if(build instanceof Temperaturec) ((Temperaturec) build).addTemperature(tempRecipe.temperatureProd);
    }

    @Override
    public void display(Stats stats) {
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
                    if(recipe.energyIn > 0f)
                    c.add(Strings.fixed(recipe.energyIn / recipe.craftTime * 60f, 1) + " " + Core.bundle.get("unit.power-second")).padRight(8);

                    c.add(new Image(Core.atlas.find("f5of-arrow"))).padRight(8);

                    for (ItemStack itemStack : recipe.itemsOut) {
                        c.add(new ItemImage(itemStack.item.uiIcon, itemStack.amount)).padRight(8);
                    }
                    for (LiquidStack liquidStack : recipe.liquidsOut) {
                        c.add(new ItemImage(liquidStack.liquid.uiIcon, (int) liquidStack.amount)).padRight(8);
                    }
                    if(recipe.energyOut > 0f)
                    c.add(Strings.fixed(recipe.energyOut, 1) + " " + Core.bundle.get("unit.power")).padRight(8);
                    if(recipe.temperatureProd > 0)
                    c.add(recipe.temperatureProd + Core.bundle.get("unit.temperature")).padRight(8);

                }).growX().pad(5);
                table.row();
            }
        });
    }

    @Override
    public void update(Building build) {
        tempRecipe = getAvailableRecipe(build);
        if(efficiency(build) > 0f) {
            if(hasPower(build))
            build.power.graph.transferPower(-tempRecipe.energyIn / tempRecipe.craftTime);
        }
        for (ItemStack itemStack : tempRecipe.itemsOut) {
            build.dump(itemStack.item);
        }
        for (LiquidStack liquidStack : tempRecipe.liquidsOut) {
            build.dumpLiquid(liquidStack.liquid);
        }
    }

    public float getAvailablePower(Building build){
        if(!hasPower(build)) return 0;
        return build.power.graph.getPowerProduced() - build.power.graph.getPowerNeeded() + build.power.graph.getBatteryStored();
    }

    public boolean hasPower(Building build){
        return build.power != null;
    }
}
