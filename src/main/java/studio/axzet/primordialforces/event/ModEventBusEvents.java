package studio.axzet.primordialforces.event;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.entity.ModEntities;
import studio.axzet.primordialforces.entity.custom.EntWarriorEntity;
import studio.axzet.primordialforces.entity.custom.EntlingEntity;
import studio.axzet.primordialforces.entity.custom.MossGolemEntity;

@EventBusSubscriber(modid = PrimordialForces.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MOSS_GOLEM.get(), MossGolemEntity.createAttributes().build());
        event.put(ModEntities.ENTLING.get(), EntlingEntity.createAttributes().build());
        event.put(ModEntities.ENT_WARRIOR.get(), EntWarriorEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.MOSS_GOLEM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.ENT_WARRIOR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
