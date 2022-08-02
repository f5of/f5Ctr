package endint.world.planets;

import arc.func.*;
import arc.graphics.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.noise.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.world.*;

public class SeptangoGenerator extends PlanetGenerator{
    float heightScale = 1f;

    float seaLevel = 0.8f;

    @Override
    public float getHeight(Vec3 p) {
        return noise(p) * heightScale;
    }

    float noise(Vec3 p){
        return Simplex.noise3d(seed, 2, 0.1f, 2f, p.x, p.y, p.z);
    }

    Block getBlock(Vec3 p){
        Block block = Blocks.stone;

        float height = getHeight(p) / heightScale;

        if(height <= 0.5f){
            return Blocks.snow;
        }else if(height > 0.5f){
            return Blocks.ice;
        }
        return block;
    }

    @Override
    public Color getColor(Vec3 p) {
        return getBlock(p).mapColor;
    }

    @Override
    public void generateSector(Sector sector) {
        sector.generateEnemyBase = false;
    }

    @Override
    public boolean allowLanding(Sector sector) {
        return true;
    }

    @Override
    public void addWeather(Sector sector, Rules rules) {
    }

    @Override
    protected void genTile(Vec3 p, TileGen t) {
        Block block = getBlock(p);
        t.floor = block;
        t.block = Blocks.air;
    }

    @Override
    public void generate(Tiles tiles, Sector sec, int seed){
        TileGen gen = new TileGen();
        for(int y = 0; y < tiles.height; y++){
            for(int x = 0; x < tiles.width; x++){
                gen.reset();
                Vec3 p = sec.rect.project(x / (float)tiles.width, y / (float)tiles.height);

                genTile(p, gen);
                tiles.set(x, y, new Tile(x, y, gen.floor, gen.overlay, gen.block));
            }
        }
    }

    @Override
    public void generate(Tiles tiles) {
    }
    @Override
    protected void generate() {
    }

    @Override
    public Schematic getDefaultLoadout() {
        return super.getDefaultLoadout();
    }

    @Override
    public void median(int radius) {
    }
    @Override
    public void median(int radius, double percentile) {
    }
    @Override
    public void median(int radius, double percentile, Block targetFloor) {
    }

    @Override
    public void ores(Seq<Block> ores) {
    }
    @Override
    public void ore(Block dest, Block src, float i, float thresh) {
    }
    @Override
    public void oreAround(Block ore, Block wall, int radius, float scl, float thresh) {
    }
    @Override
    public void wallOre(Block src, Block dest, float scl, float thresh) {
    }
    @Override
    public void cliffs() {
    }
    @Override
    public void terrain(Block dst, float scl, float mag, float cmag) {
    }
    @Override
    public void overlay(Block floor, Block block, float chance, int octaves, float falloff, float scl, float threshold) {
    }
    @Override
    public void tech() {
    }
    @Override
    public void tech(Block floor1, Block floor2, Block wall) {
    }
    @Override
    public void distort(float scl, float mag) {
    }
    @Override
    public void scatter(Block target, Block dst, float chance) {
    }
    @Override
    public void cells(int iterations) {
    }
    @Override
    public void cells(int iterations, int birthLimit, int deathLimit, int cradius) {
    }
    @Override
    public void removeWall(int cx, int cy, int rad, Boolf<Block> pred) {
    }
    @Override
    public void decoration(float chance) {
    }
    @Override
    public void blend(Block floor, Block around, float radius) {
    }
    @Override
    public void brush(Seq<Tile> path, int rad) {
    }
    @Override
    public void erase(int cx, int cy, int rad) {
    }
    @Override
    public void trimDark() {
    }
    @Override
    public void inverseFloodFill(Tile start) {
    }
}
