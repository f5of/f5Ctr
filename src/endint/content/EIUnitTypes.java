package endint.content;

import arc.graphics.Color;
import mindustry.ai.types.BuilderAI;
import mindustry.annotations.Annotations;
import mindustry.content.Fx;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.entities.pattern.ShootSpread;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.weapons.RepairBeamWeapon;

import static mindustry.Vars.tilesize;

public class EIUnitTypes {
    public static UnitType bd1;

    public static void load(){
        bd1 = new UnitType("BD-1"){{
            alwaysUnlocked = true;
            constructor = EntityMapping.nameMap.get("eclipse");
            aiController = BuilderAI::new;

            coreUnitDock = true;
            isEnemy = false;
            envDisabled = 0;

            targetPriority = -2;
            lowAltitude = false;
            mineWalls = false;
            mineFloor = true;
            mineHardnessScaling = false;
            flying = true;
            mineSpeed = 6f;
            mineTier = 1;
            buildSpeed = 1.0f;
            drag = 0.08f;
            speed = 2.75f;
            rotateSpeed = 7f;
            accel = 0.09f;
            itemCapacity = 50;
            health = 200f;
            armor = 0f;
            hitSize = 9f;
            engineSize = 0;
            payloadCapacity = 1f * 1f * tilesize * tilesize;
            pickupUnits = true;
            vulnerableWithPayloads = true;

            fogRadius = 0f;
            targetable = false;
            hittable = false;

            weapons.add(new Weapon("beam-weapon"){{
                top = false;
                shake = 0f;
                shootY = 0f;
                x = 0f;
                reload = 60f;
                recoil = 0f;
                shootSound = Sounds.laser;

                bullet = new LaserBulletType(){{
                    width = 6f;
                    damage = 40f;
                    recoil = 0f;
                    sideAngle = 0f;
                    sideWidth = 0f;
                    sideLength = 0f;
                    healPercent = 0f;
                    collidesTeam = true;
                    length = 60f;
                    colors = new Color[]{new Color(48, 213, 200).mul(0.7f), new Color(48, 213, 200).mul(0.6f), new Color(48, 213, 200).mul(0.8f)};
                }};
            }
                @Override
                public void draw(Unit unit, WeaponMount mount) {

                }
            });
        }};
    }
}
