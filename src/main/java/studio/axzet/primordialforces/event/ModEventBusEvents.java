package studio.axzet.primordialforces.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.entity.ModEntities;
import studio.axzet.primordialforces.entity.custom.MossGolemEntity;

@EventBusSubscriber(modid = PrimordialForces.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MOSS_GOLEM.get(), MossGolemEntity.createAttributes().build());
    }
}
