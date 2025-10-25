package studio.axzet.efs.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import studio.axzet.efs.EchoesOfTheFifthSun;
import studio.axzet.efs.entity.ModEntities;
import studio.axzet.efs.entity.custom.*;

@EventBusSubscriber(modid = EchoesOfTheFifthSun.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.WOOD_GUARDIAN.get(), WoodGuardianEntity.createAttributes().build());
        event.put(ModEntities.JAGUAR.get(), Jaguar.createAttributes().build());
        event.put(ModEntities.EAGLE.get(), Eagle.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        //event.register(ModEntities.MOSS_GOLEM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
