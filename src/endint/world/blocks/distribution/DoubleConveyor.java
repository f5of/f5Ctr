package endint.world.blocks.distribution;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.itemSize;

public class DoubleConveyor extends Block{
    public float maxSpeed = 1f / 60f;

    public int capacity = 3;

    TextureRegion reg;

    public DoubleConveyor(String name){
        super(name);
        rotate = true;
        update = true;
        group = BlockGroup.transportation;
        hasItems = true;
        priority = TargetPriority.transport;
        conveyorPlacement = true;
        underBullets = true;

        itemCapacity = capacity;

        unloadable = false;
        noUpdateDisabled = false;
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.itemsMoved, maxSpeed, StatUnit.itemsSecond);
    }

    public class DoubleConveyorBuild extends Building{
        public Seq<ConveyorItem> conveyorItems = new Seq<ConveyorItem>();

        protected Building tempBuild;
        protected ConveyorItem tempItem;

        public float getMaxSpeed(){
            return maxSpeed;
        }

        public float getCapacity(){
            return capacity;
        }

        @Override
        public void updateTile(){
            conveyorItems.each(ConveyorItem::update);
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            if(source.left() == this || source.front() == this){
                return conveyorItems.count(i -> i.place == 0) < capacity;
            }else if(source.right() == this || source.back() == this){
                return conveyorItems.count(i -> i.place == 1) < capacity;
            }
            return false;
        }

        @Override
        public void handleItem(Building source, Item item){
            if(source.left() == this || source.front() == this){
                conveyorItems.add(new ConveyorItem(item, (short)0));
            }else if(source.right() == this || source.back() == this){
                conveyorItems.add(new ConveyorItem(item, (short)1));
            }
        }

        public void dumpItem(){
            tempItem = conveyorItems.find(i -> i.progress >= 1f);

            tempBuild = front();

            if(tempBuild == null) return;

            if(tempBuild.acceptItem(this, tempItem.item))
                conveyorItems.remove(tempItem);
            tempBuild.handleItem(this, tempItem.item);
        }

        @Override
        public void draw(){
            super.draw();
            conveyorItems.each(ConveyorItem::draw);
        }

        class ConveyorItem{
            public Item item;
            public float progress = 0f;
            public short place;

            protected ConveyorItem tempItem;

            public ConveyorItem(Item item, short place){
                this.item = item;
                this.place = place;
            }

            public void update(){
                if(progress >= 1f){
                    owner().dumpItem();
                    return;
                }

                tempItem = null;

                owner().conveyorItems.each(i -> {
                    if(i.place == place)
                        if(i != this && Math.abs(i.progress - progress) <= progress / owner().getCapacity() && i.progress > progress){
                            tempItem = i;
                            Log.info(progress);
                        }
                });

                if(tempItem == null)
                    progress += owner().getMaxSpeed();
            }

            public void drawItem(){
                float cos = Mathf.cosDeg(360 - owner().rotation);
                float sin = Mathf.sinDeg(360 - owner().rotation);


//                Tmp.v1.trns(360-owner().rotation,progress)

                float len = 0.5f + (place == 0 ? -progress : progress);
                Vec2 vec = Tmp.v1.set(sin * len, cos * len);

                Draw.rect(item.fullIcon, owner().x + 4 + vec.x * 8, vec.y * 8 + owner().y + 4, itemSize, itemSize);
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
