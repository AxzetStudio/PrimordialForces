package studio.axzet.primordialforces.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record ArcadiumInfuserRecipeInput(ItemStack core, ItemStack essence) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int i) {
        return switch (i) {
            case 0 -> core;
            case 1 -> essence;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
