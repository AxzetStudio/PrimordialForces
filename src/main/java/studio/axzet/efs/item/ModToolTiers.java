package studio.axzet.efs.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import studio.axzet.efs.utils.ModTags;

public class ModToolTiers {
    public static final Tier PRIMORDIAL_TIER = new SimpleTier(ModTags.Blocks.INCORRECT_FOR_ARCADIUM_TOOL, 1800, 8.5f, 3.5f, 20,
            () -> Ingredient.of(ModItems.EARTH_RUNE.get())
            );
}
