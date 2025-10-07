package studio.axzet.efs.entity;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.axzet.efs.EchoesOfTheFifthSun;
import studio.axzet.efs.entity.custom.*;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, EchoesOfTheFifthSun.MOD_ID);

    public static final Supplier<EntityType<WoodGuardianEntity>> WOOD_GUARDIAN = ENTITY_TYPES.register("wood_guardian",
            () -> EntityType.Builder.of(WoodGuardianEntity::new, MobCategory.CREATURE).sized(1.5f, 4.5f) .build("wood_guardian")
    );

    public static final Supplier<EntityType<PoisonCloudEntity>> POISON_CLOUD = ENTITY_TYPES.register("poison_cloud",
            () -> EntityType.Builder.<PoisonCloudEntity>of(PoisonCloudEntity::new, MobCategory.MISC).sized(1, 0.5f).fireImmune().build("poison_cloud")
            );

    public static final Supplier<EntityType<ThornsFieldEntity>> THORNS_FIELD = ENTITY_TYPES.register("thorns_field",
            () -> EntityType.Builder.<ThornsFieldEntity>of(ThornsFieldEntity::new, MobCategory.AMBIENT).sized(1, 0.5f).fireImmune().build("thorns_field")
    );

    //region
    public static final Supplier<EntityType<Jaguar>> JAGUAR = ENTITY_TYPES.register("jaguar",
            () -> EntityType.Builder.of(Jaguar::new, MobCategory.CREATURE).sized(0.7f, 1f).build("jaguar")
    );
    //endregion

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
