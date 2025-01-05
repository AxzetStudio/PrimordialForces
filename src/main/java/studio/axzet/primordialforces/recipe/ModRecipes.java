package studio.axzet.primordialforces.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.axzet.primordialforces.PrimordialForces;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, PrimordialForces.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, PrimordialForces.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ArcadiumInfuserRecipe>> ARCADIUM_INFUSER_SERIALIZER =
            SERIALIZERS.register("arcadium_infusing", ArcadiumInfuserRecipe.Serializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<ArcadiumInfuserRecipe>> ARCADIUM_INFUSER_TYPE =
            TYPES.register("arcadium_infusing", () -> new RecipeType<ArcadiumInfuserRecipe>() {
                @Override
                public String toString() {
                    return "arcadium_infusing";
                }
            });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
