package studio.axzet.primordialforces.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record ArcadiumInfuserRecipe(Ingredient core, Ingredient essence, ItemStack output) implements Recipe<ArcadiumInfuserRecipeInput> {

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(core);
        list.add(essence);
        return list;
    }

    @Override
    public boolean matches(@NotNull ArcadiumInfuserRecipeInput arcadiumInfuserRecipeInput, Level level) {
        if (level.isClientSide) {
            return false;
        }

        return core.test(arcadiumInfuserRecipeInput.getItem(0)) && essence.test(arcadiumInfuserRecipeInput.getItem(1));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull ArcadiumInfuserRecipeInput arcadiumInfuserRecipeInput, @NotNull HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.ARCADIUM_INFUSER_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipes.ARCADIUM_INFUSER_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ArcadiumInfuserRecipe> {

        public static final MapCodec<ArcadiumInfuserRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("core").forGetter(ArcadiumInfuserRecipe::core),
                Ingredient.CODEC_NONEMPTY.fieldOf("essence").forGetter(ArcadiumInfuserRecipe::essence),
                ItemStack.CODEC.fieldOf("result").forGetter(ArcadiumInfuserRecipe::output)
        ).apply(inst, ArcadiumInfuserRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ArcadiumInfuserRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, ArcadiumInfuserRecipe::core,
                        Ingredient.CONTENTS_STREAM_CODEC, ArcadiumInfuserRecipe::essence,
                        ItemStack.STREAM_CODEC, ArcadiumInfuserRecipe::output,
                        ArcadiumInfuserRecipe::new
                );

        @Override
        public @NotNull MapCodec<ArcadiumInfuserRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, ArcadiumInfuserRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
