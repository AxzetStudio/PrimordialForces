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

public record ArcadiumInfuserRecipe(Ingredient inputItem, ItemStack output) implements Recipe<ArcadiumInfuserRecipeInput> {

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        return list;
    }

    @Override
    public boolean matches(ArcadiumInfuserRecipeInput arcadiumInfuserRecipeInput, Level level) {
        if (level.isClientSide) {
            return false;
        }

        return inputItem.test(arcadiumInfuserRecipeInput.getItem(0));
    }

    @Override
    public ItemStack assemble(ArcadiumInfuserRecipeInput arcadiumInfuserRecipeInput, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ARCADIUM_INFUSER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.ARCADIUM_INFUSER_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ArcadiumInfuserRecipe> {

        public static final MapCodec<ArcadiumInfuserRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(ArcadiumInfuserRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(ArcadiumInfuserRecipe::output)
        ).apply(inst, ArcadiumInfuserRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ArcadiumInfuserRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, ArcadiumInfuserRecipe::inputItem,
                        ItemStack.STREAM_CODEC, ArcadiumInfuserRecipe::output,
                        ArcadiumInfuserRecipe::new);

        @Override
        public MapCodec<ArcadiumInfuserRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ArcadiumInfuserRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
