package classes.world.type;

import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;

public class Recipe {
    public LiquidStack[] liquidsIn = {}, liquidsOut = {};
    public ItemStack[] itemsIn = {}, itemsOut = {};
    public float energyIn = 0f, energyOut = 0f;
    public float craftTime = 60f;
    public float temperatureProd = 0f;
}
