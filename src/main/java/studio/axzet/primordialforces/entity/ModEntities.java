package studio.axzet.primordialforces.entity;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.entity.custom.EntlingEntity;
import studio.axzet.primordialforces.entity.custom.MossGolemEntity;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, PrimordialForces.MOD_ID);

    public static final Supplier<EntityType<MossGolemEntity>> MOSS_GOLEM = ENTITY_TYPES.register("moss_golem",
            () -> EntityType.Builder.of(MossGolemEntity::new, MobCategory.MONSTER).sized(0.7f, 1.5f).build("moss_golem")
    );

    public static final Supplier<EntityType<EntlingEntity>> ENTLING = ENTITY_TYPES.register("entling",
            () -> EntityType.Builder.of(EntlingEntity::new, MobCategory.MONSTER).sized(1, 1).build("entling")
    );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
