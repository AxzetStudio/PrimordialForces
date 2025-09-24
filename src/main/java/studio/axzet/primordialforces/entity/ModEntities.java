package studio.axzet.primordialforces.entity;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.entity.custom.*;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, PrimordialForces.MOD_ID);

    public static final Supplier<EntityType<WoodGuardianEntity>> WOOD_GUARDIAN = ENTITY_TYPES.register("wood_guardian",
            () -> EntityType.Builder.of(WoodGuardianEntity::new, MobCategory.CREATURE).sized(1.5f, 4.5f) .build("wood_guardian")
    );

    public static final Supplier<EntityType<PoisonCloudEntity>> POISON_CLOUD = ENTITY_TYPES.register("poison_cloud",
            () -> EntityType.Builder.<PoisonCloudEntity>of(PoisonCloudEntity::new, MobCategory.MISC).sized(1, 0.5f).fireImmune().build("poison_cloud")
            );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
