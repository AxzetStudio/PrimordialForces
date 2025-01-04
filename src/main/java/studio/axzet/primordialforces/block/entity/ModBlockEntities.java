package studio.axzet.primordialforces.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.block.ModBlocks;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PrimordialForces.MOD_ID);

    public static final Supplier<BlockEntityType<ArcadiumInfuserBlockEntity>> ARCADIUM_INFUSER_BE =
            BLOCK_ENTITIES.register("arcadium_infuser_be", () -> BlockEntityType.Builder.of(
                    ArcadiumInfuserBlockEntity::new, ModBlocks.ARCADIUM_INFUSER.get()
            ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
