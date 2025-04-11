package studio.axzet.primordialforces.utils;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import studio.axzet.primordialforces.item.ModItems;

import java.util.function.Supplier;

public enum ElementalEssenceType {
    EARTH(ModItems.EARTH_ESSENCE),
    FIRE(ModItems.FIRE_ESSENCE),
    WATER(ModItems.WATER_ESSENCE),
    AIR(ModItems.AIR_ESSENCE),
    VOID(ModItems.VOID_ESSENCE);

    private final Supplier<? extends Item> itemSupplier;

    ElementalEssenceType(Supplier<? extends Item> itemSupplier) {
        this.itemSupplier = itemSupplier;
    }

    public ItemStack createEssence() {
        return new ItemStack(itemSupplier.get());
    }
}
