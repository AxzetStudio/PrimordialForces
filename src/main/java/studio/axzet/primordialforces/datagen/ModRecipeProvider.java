package studio.axzet.primordialforces.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.block.ModBlocks;
import studio.axzet.primordialforces.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        List<ItemLike> BLACK_OPAL_SMELTABLES = List.of(ModItems.RAW_BLACK_OPAL);
        List<ItemLike> ARCADIUM_SMELTABLES = List.of(ModItems.RAW_ARCADIUM);

        //region BLACK OPAL RECIPES
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BLACK_OPAL_BLOCK.get())
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', ModItems.BLACK_OPAL.get())
                .unlockedBy("has_black_opal", has(ModItems.BLACK_OPAL.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLACK_OPAL, 9)
                .requires(ModBlocks.BLACK_OPAL_BLOCK.get())
                .unlockedBy("has_black_opal", has(ModItems.BLACK_OPAL.get())).save(recipeOutput);

        oreSmelting(recipeOutput, BLACK_OPAL_SMELTABLES, RecipeCategory.MISC, ModItems.BLACK_OPAL.get(), 0.25f, 200, "black_opal");
        oreBlasting(recipeOutput, BLACK_OPAL_SMELTABLES, RecipeCategory.MISC, ModItems.BLACK_OPAL.get(), 0.25f, 100, "black_opal");
        //endregion

        //region ARCADIUM RECIPES
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ARCADIUM_CONDUIT.get())
                .pattern("III")
                .pattern("GAG")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('G', Items.GLASS)
                .define('A', ModItems.ARCADIUM.get())
                .unlockedBy("has_arcadium", has(ModItems.ARCADIUM.get())).save(recipeOutput);
                ;

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ARCADIUM_CORE.get())
                .pattern("ACA")
                .pattern("CPC")
                .pattern("ACA")
                .define('A', ModItems.ARCADIUM.get())
                .define('C', ModItems.ARCADIUM_CONDUIT.get())
                .define('P', ModItems.PRIMORDIAL_CRYSTAL)
                .unlockedBy("has_arcadium_conduit", has(ModItems.ARCADIUM_CONDUIT.get())).save(recipeOutput);

        oreBlasting(recipeOutput, ARCADIUM_SMELTABLES, RecipeCategory.MISC, ModItems.ARCADIUM.get(), 0.35f, 100, "arcadium");
        //endregion

        // Void Armor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VOIDMANCER_HELMET)
                .pattern("VVV")
                .pattern("V V")
                .pattern("   ")
                .define('V', ModItems.VOID_SHARD.get())
                .unlockedBy("has_void_shard", has(ModItems.VOID_SHARD.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VOIDMANCER_CHESTPLATE)
                .pattern("V V")
                .pattern("VVV")
                .pattern("VVV")
                .define('V', ModItems.VOID_SHARD.get())
                .unlockedBy("has_void_shard", has(ModItems.VOID_SHARD.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VOIDMANCER_LEGGINGS)
                .pattern("VVV")
                .pattern("V V")
                .pattern("V V")
                .define('V', ModItems.VOID_SHARD.get())
                .unlockedBy("has_void_shard", has(ModItems.VOID_SHARD.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VOIDMANCER_BOOTS)
                .pattern("   ")
                .pattern("V V")
                .pattern("V V")
                .define('V', ModItems.VOID_SHARD.get())
                .unlockedBy("has_void_shard", has(ModItems.VOID_SHARD.get())).save(recipeOutput);

        // Black Opal Armor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BLACK_OPAL_HELMET)
                .pattern("BBB")
                .pattern("B B")
                .pattern("   ")
                .define('B', ModItems.BLACK_OPAL.get())
                .unlockedBy("has_black_opal", has(ModItems.BLACK_OPAL.get())).save(recipeOutput);
    }

    protected static void oreSmelting(RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pRecipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pRecipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput pRecipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pRecipeOutput, PrimordialForces.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
