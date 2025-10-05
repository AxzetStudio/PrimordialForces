package studio.axzet.efs.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import studio.axzet.efs.EchoesOfTheFifthSun;
import studio.axzet.efs.block.ModBlocks;
import studio.axzet.efs.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        //List<ItemLike> ARCADIUM_SMELTABLES = List.of(ModItems.RAW_ARCADIUM);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.OBSIDIAN_SHARD.get())
                .pattern("  O")
                .pattern(" O ")
                .pattern("O  ")
                .define('O', Items.OBSIDIAN)
                .unlockedBy("has_obsidian", has(Items.OBSIDIAN)).save(recipeOutput);
                ;

                ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MACUAHUITL.get())
                        .pattern(" OM")
                        .pattern("OMO")
                        .pattern("PO ")
                        .define('O', ModItems.OBSIDIAN_SHARD.get())
                        .define('M', ItemTags.PLANKS)
                        .define('P', Items.STICK)
                        .unlockedBy("has_obsidian_shard", has(ModItems.OBSIDIAN_SHARD.get())).save(recipeOutput);
                        ;
        //oreBlasting(recipeOutput, ARCADIUM_SMELTABLES, RecipeCategory.MISC, ModItems.ARCADIUM.get(), 0.35f, 100, "arcadium");
        //endregion

        //region EARTH EQUIPMENT
        /*smithing(
                recipeOutput,
                Ingredient.of(ModItems.EARTH_RUNE.get()),
                Ingredient.of(Items.NETHERITE_HELMET),
                Ingredient.of(ModItems.ARCADIUM_CORE.get()),
                RecipeCategory.COMBAT,
                ModItems.EARTH_HELMET.get(),
                "has_earth_rune",
                ModItems.EARTH_RUNE.get(),
                "earth_helmet_smithing"
        );*/
        //endregion
    }

    protected  static void smithing(RecipeOutput pRecipeOutput, Ingredient template, Ingredient base, Ingredient addition, RecipeCategory pCategory, Item result, String unlocks, ItemLike unlocksItem, String recipeName) {
        SmithingTransformRecipeBuilder.smithing(
                template,
                base,
                addition,
                pCategory,
                result
        )
                .unlocks(unlocks, has(unlocksItem))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(EchoesOfTheFifthSun.MOD_ID, recipeName));
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
                    .save(pRecipeOutput, EchoesOfTheFifthSun.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
