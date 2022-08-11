package endint.world.blocks.distribution;

import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.io.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class DoubleConveyor extends Block{
    public float maxSpeed = 1f / Time.toSeconds;

    public int sideCapacity = 3;

    public DoubleConveyor(String name){
        super(name);
        rotate = true;
        update = true;
        group = BlockGroup.transportation;
        hasItems = true;
        priority = TargetPriority.transport;
        conveyorPlacement = true;
        underBullets = true;
        itemCapacity = -1;

        unloadable = false;
        noUpdateDisabled = false;
    }

    @Override
    public void init(){
        super.init();
        if(itemCapacity == -1){
            itemCapacity = sideCapacity * 2;
        }
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.itemsMoved, maxSpeed, StatUnit.itemsSecond);
    }

    public class DoubleConveyorBuild extends Building{
        public final Seq<ConveyorItem> conveyorItems = new Seq<>();

        @Override
        public void updateTile(){
            for(ConveyorItem item : conveyorItems){
                if(item.progress >= 1f){
                    dumpItem(item);
                }else{
                    item.update();
                }
            }
        }

        public byte isRightConveyorItem(Building other){
            if(other.left() == this || other.front() == this){
                return 1;
            }else if(other.right() == this || other.back() == this){
                return 0;
            }
            return -1;
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            byte isRight = isRightConveyorItem(source);
            if(isRight == -1) return false;
            return conveyorItems.count(i -> i.isRight == (isRight == 1)) < sideCapacity;
        }

        @Override
        public void handleItem(Building source, Item item){
//            int rotation = Math.abs(relativeTo(source) - this.rotation);
            byte isRight = isRightConveyorItem(source);
            if(isRight == -1) return;
            conveyorItems.add(new ConveyorItem(item, isRight == 1));
        }

        public void dumpItem(ConveyorItem conveyorItem){

            Building tempBuild = front();

            if(tempBuild == null) return;

            if(tempBuild.acceptItem(this, conveyorItem.item))
                conveyorItems.remove(conveyorItem);
            tempBuild.handleItem(this, conveyorItem.item);
        }

        @Override
        public void draw(){
            super.draw();
            conveyorItems.each(ConveyorItem::draw);
        }

        @Override
        public byte version(){
            return 1;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.i(conveyorItems.size);
            for(ConveyorItem item : conveyorItems){
                write.bool(item.isRight);
                TypeIO.writeString(write, item.item.name);
                write.f(item.progress);
            }
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            if(revision == 0) return;
            conveyorItems.clear();

            int size = read.i();
            for(int i = 0; i < size; i++){
                boolean isRight = read.bool();
                String itemName = TypeIO.readString(read);
                Item item = content.item(itemName);
                float progress = read.f();
                if(item == null){
                    //TODO some extra finding
                    Log.err("[@]Cannot find item with name \"@\"", getClass().getCanonicalName(), itemName);
                    continue;
                }
                ConveyorItem value = new ConveyorItem(item, isRight);
                value.progress = progress;
                conveyorItems.add(value);
            }
        }

        class ConveyorItem{
            public Item item;
            public float progress = 0f;
            public boolean isRight;


            public ConveyorItem(Item item, boolean isRight){
                this.item = item;
                this.isRight = isRight;
            }

            public void update(){
                int saveSideAmount = owner().conveyorItems.count(i -> {
                    if(i.isRight == isRight &&
                    i != this && Math.abs(i.progress - progress) <= progress / DoubleConveyor.this.sideCapacity && i.progress > progress){
                        Log.info(progress);
                        return true;
                    }
                    return false;
                });

                if(saveSideAmount > 0){
                    progress += DoubleConveyor.this.maxSpeed * Time.delta;
                }
            }

            public void drawItem(){
                Tmp.v1
                .trns(owner().rotdeg(), 0.5f + (isRight ? -progress : progress))
                .add(0.5f, 0.5f)
                .scl(tilesize)
                .add(owner());

                Draw.rect(item.fullIcon, Tmp.v1.x, Tmp.v1.y, itemSize, itemSize);
            }

            private DoubleConveyorBuild owner(){
                return DoubleConveyorBuild.this;
            }

            public void draw(){
                drawItem();
            }
        }
    }
}
