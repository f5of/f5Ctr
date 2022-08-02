package content;

import arc.graphics.Color;
import classes.world.planets.SeptangoGenerator;
import mindustry.content.Items;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.graphics.Pal;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.graphics.g3d.SunMesh;
import mindustry.maps.planet.SerpuloPlanetGenerator;
import mindustry.type.Planet;

public class ModPlanets {
    public static Planet modSun, modPlanet;

    public static void load(){
        modSun = new Planet("mod-sun", Planets.sun, 8f){{
            bloom = true;
            accessible = false;

            meshLoader = () -> new SunMesh(
                    this, 4,
                    5, 0.3, 1.7, 1.2, 1,
                    1.1f,
                    Color.valueOf("ffffff"),
                    Color.valueOf("cccccc"),
                    Color.valueOf("aaaaaa"),
                    Color.valueOf("eeeeee"),
                    Color.valueOf("999999"),
                    Color.valueOf("777777")
            );

            orbitRadius = 240f;
            orbitSpacing = 60f;

            accessible = true;
            alwaysUnlocked = true;
        }};

        modPlanet = new Planet("septango", modSun, 1f, 3){{
            generator = new SeptangoGenerator();
            meshLoader = () -> new HexMesh(this, 5);
            cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this, 11, 0.15f, 0.13f, 5, Color.blue.a(0.75f), 2, 0.45f, 0.9f, 0.38f),
                    new HexSkyMesh(this, 1, 0.6f, 0.16f, 5, Color.white.a(0.75f), 2, 0.45f, 1f, 0.41f)
            );

            launchCapacityMultiplier = 0.5f;
            sectorSeed = 2;
            allowWaves = true;
            allowWaveSimulation = true;
            allowSectorInvasion = true;
            allowLaunchSchematics = true;
            enemyCoreSpawnReplace = true;
            allowLaunchLoadout = true;
            ruleSetter = r -> {
                r.waveTeam = Team.crux;
                r.placeRangeCheck = false;
                r.attributes.clear();
                r.showSpawns = false;
            };
            atmosphereColor = Color.valueOf("eeeeee");
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;
            startSector = 15;
            alwaysUnlocked = true;
            landCloudColor = Color.blue.cpy().a(0.5f);
            hiddenItems.addAll(Items.erekirItems).removeAll(Items.serpuloItems);
        }};
    }
}
