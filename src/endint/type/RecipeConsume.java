package endint.type;

import arc.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import endint.gen.*;
import endint.meta.EIStatUnit;
import endint.world.blocks.crafting.*;
import endint.world.blocks.crafting.MultiCrafter.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

import static endint.meta.EIStat.recipeStat;

public class RecipeConsume{

    public Recipe[] recipes;
    public RecipeConsumePower power;

    protected Recipe tempRecipe;

    public RecipeConsume(Recipe... recipes){
        this.recipes = recipes;
    }

    public void init(Block block){
        power = new RecipeConsumePower();
        block.hasConsumers = true;
        boolean i = false, l = false, p = false, pi = false, po = false;
        float ml = 0;
        int mi = 0;
        for(Recipe recipe : recipes){
            for(ItemStack itemStack : recipe.itemsIn){
                if(mi < itemStack.amount) mi = itemStack.amount;
                block.itemFilter[itemStack.item.id] = true;
                i = true;
            }
            for(LiquidStack liquidStack : recipe.liquidsIn){
                if(ml < liquidStack.amount) ml = liquidStack.amount;
                block.liquidFilter[liquidStack.liquid.id] = true;
                l = true;
            }
            for(ItemStack itemStack : recipe.itemsOut){
                if(mi < itemStack.amount) mi = itemStack.amount;
                i = true;
            }
            for(LiquidStack liquidStack : recipe.liquidsOut){
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

        block.consume(power);

        setBars(block);
    }

    public void setBars(Block block){
        for(Recipe recipe : recipes){
            for(LiquidStack liquidStack : recipe.liquidsIn){
                addLiquidBar(block, liquidStack.liquid);
            }
            for(LiquidStack liquidStack : recipe.liquidsOut){
                addLiquidBar(block, liquidStack.liquid);
            }
            for(ItemStack itemStack : recipe.itemsIn){
                addItemBar(block, itemStack.item);
            }
            for(ItemStack itemStack : recipe.itemsOut){
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
                () -> (float)entity.items.get(item) / block.itemCapacity)
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
            a.table(c -> {
                for(ItemStack itemStack : tempRecipe.itemsIn){
                    c.add(new ReqImage(new ItemImage(itemStack.item.uiIcon, itemStack.amount),
                            () -> build.items.has(itemStack.item, itemStack.amount))).padRight(8);
                }
                for(LiquidStack liquidStack : tempRecipe.liquidsIn){
                    c.add(new ReqImage(new ItemImage(liquidStack.liquid.uiIcon, (int)liquidStack.amount),
                            () -> build.liquids.get(liquidStack.liquid) >= liquidStack.amount)).padRight(8);
                }

                if(tempRecipe.powerIn != 0) c.add(Strings.fixed(tempRecipe.powerIn / tempRecipe.craftTime, 2)
                        + " " + StatUnit.powerSecond.localized()).padRight(8);

                c.add(new Image(EITex.arrow)).padRight(8);
                c.add(new Image(EITex.arrow)).padRight(8);
                c.add(Strings.fixed(tempRecipe.craftTime / 60f, 2)
                        + " " + StatUnit.seconds.localized()).padRight(8);
                c.add(tempRecipe.temperatureOut + " " + EIStatUnit.temperature.localized()).padRight(8);

                for(ItemStack itemStack : tempRecipe.itemsOut){
                    c.add(new ItemImage(itemStack.item.uiIcon, itemStack.amount)).padRight(8);
                }
                for(LiquidStack liquidStack : tempRecipe.liquidsOut){
                    c.add(new ItemImage(liquidStack.liquid.uiIcon, (int)liquidStack.amount)).padRight(8);
                }

                if(tempRecipe.powerOut != 0) c.add(Strings.fixed(tempRecipe.powerOut / tempRecipe.craftTime, 2)
                        + " " + StatUnit.powerSecond.localized()).padRight(8);

            });
        }).growX().left();
    }

    void showStatsOnTable(Table table, Recipe recipe){
        for(ItemStack itemStack : recipe.itemsIn){
            table.add(new ItemImage(itemStack.item.uiIcon, itemStack.amount)).padRight(8);
        }
        for(LiquidStack liquidStack : recipe.liquidsIn){
            table.add(new ItemImage(liquidStack.liquid.uiIcon, (int)liquidStack.amount)).padRight(8);
        }

        if(recipe.powerIn != 0) table.add(Strings.fixed(recipe.powerIn / recipe.craftTime, 2)
                + " " + StatUnit.powerSecond.localized()).padRight(8);

        table.add(new Image(EITex.arrow)).padRight(8);
        table.add(Strings.fixed(recipe.craftTime / 60f, 2) + " " + StatUnit.seconds.localized()).padRight(8);
        table.add(recipe.temperatureOut + " " + EIStatUnit.temperature.localized()).padRight(8);

        for(ItemStack itemStack : recipe.itemsOut){
            table.add(new ItemImage(itemStack.item.uiIcon, itemStack.amount)).padRight(8);
        }
        for(LiquidStack liquidStack : recipe.liquidsOut){
            table.add(new ItemImage(liquidStack.liquid.uiIcon, (int)liquidStack.amount)).padRight(8);
        }

        if(recipe.powerOut != 0) table.add(Strings.fixed(recipe.powerOut / recipe.craftTime, 2)
                + " " + StatUnit.powerSecond.localized()).padRight(8);
    }

    public void setStats(Stats stats){
        stats.add(recipeStat, table -> {
            table.row();
            for(Recipe recipe : recipes){
                table.table(c -> {
                    c.setBackground(Tex.whiteui);
                    c.setColor(Pal.darkestGray);

                    showStatsOnTable(c, recipe);
                }).growX().pad(5);
                table.row();
            }
        });
    }

    public BlockStatus status(MultiCrafter.MultiCrafterBuild build){
        tempRecipe = recipes[build.currentRecipe];

        //region input
        for(ItemStack itemStack : tempRecipe.itemsIn){
            if(!build.items.has(itemStack.item, itemStack.amount)){
                return BlockStatus.noInput;
            }
        }
        for(LiquidStack liquidStack : tempRecipe.liquidsIn){
            if(build.liquids.get(liquidStack.liquid) < liquidStack.amount){
                return BlockStatus.noInput;
            }
        }
        if(tempRecipe.powerIn != 0 && (build.power == null || build.power.status == 0)){
            return BlockStatus.noInput;
        }
        //endregion
        //region output
        for(ItemStack itemStack : tempRecipe.itemsOut){
            if(build.block.itemCapacity - build.items.get(itemStack.item) < 0){
                return BlockStatus.noOutput;
            }
        }
        for(LiquidStack liquidStack : tempRecipe.liquidsOut){
            if(build.block.liquidCapacity - build.liquids.get(liquidStack.liquid) < 0){
                return BlockStatus.noOutput;
            }
        }
        //endregion

        return BlockStatus.active;
    }

    public void craft(MultiCrafter.MultiCrafterBuild build){
        tempRecipe = recipes[build.currentRecipe];

        for(ItemStack itemStack : tempRecipe.itemsIn) build.items.remove(itemStack.item, itemStack.amount);
        for(LiquidStack liquidStack : tempRecipe.liquidsIn) build.liquids.remove(liquidStack.liquid, liquidStack.amount);
        for(ItemStack itemStack : tempRecipe.itemsOut) build.items.add(itemStack.item, itemStack.amount);
        for(LiquidStack liquidStack : tempRecipe.liquidsOut) build.liquids.add(liquidStack.liquid, liquidStack.amount);
    }

    public float getEfficiency(MultiCrafter.MultiCrafterBuild build){
        if(status(build) != BlockStatus.active) return 0f;
        //TODO replace "build.power.status" by "build.power.status/powerIn"
        if(build.power != null) return recipes[build.currentRecipe].powerIn != 0 ? build.power.status : 1f;
        return 1f;
    }

    public void buildConfiguration(MultiCrafter.MultiCrafterBuild build, Table table){
        int ind = 0;
        for(Recipe recipe : recipes){
            Button btn = new Button();

            int i = ind;
            btn.clicked(() -> {
                build.currentRecipe = i;
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
        public float temperatureOut = 0f;
    }

    class RecipeConsumePower extends ConsumePower{

        public RecipeConsumePower(){

        }

        @Override
        public float requestedPower(Building entity){
            Recipe recipe = recipes[entity.<MultiCrafterBuild>as().currentRecipe];
            return recipe.powerIn / recipe.craftTime;
        }

        boolean canWork(Building build){
            return
                    build instanceof MultiCrafter.MultiCrafterBuild
                            && ((MultiCrafter)build.block).consume.status((MultiCrafter.MultiCrafterBuild)build)
                            == BlockStatus.active;
        }

        @Override
        public boolean ignore(){
            return false;
        }

        @Override
        public void apply(Block block){
        }

        @Override
        public void display(Stats stats){
        }

        @Override
        public void build(Building build, Table table){
        }

        @Override
        public float efficiency(Building build){
            return canWork(build) ? build.power.status : 0f;
        }
    }
}