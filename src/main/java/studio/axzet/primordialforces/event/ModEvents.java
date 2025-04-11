package studio.axzet.primordialforces.event;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.item.ModItems;
import studio.axzet.primordialforces.utils.ElementalEssenceType;

import java.util.Map;
import java.util.function.Predicate;

@EventBusSubscriber(modid = PrimordialForces.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    private static float DROP_CHANCE = 0.3f;

    private static final Map<Predicate<LivingEntity>, ElementalEssenceType> ELIGIBLE_ENTITIES = Map.of(
            ModEvents::isEarthEligibleEntity, ElementalEssenceType.EARTH,
            ModEvents::isFireEligibleEntity, ElementalEssenceType.FIRE,
            ModEvents::isVoidEligibleEntity, ElementalEssenceType.VOID
    );

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        RandomSource random = entity.level().random;

        if (random.nextFloat() >= DROP_CHANCE) return;

        ELIGIBLE_ENTITIES.forEach((predicate, essenceType) -> {
            if (predicate.test(entity)) {
                addElementalEssenceDrop(event, essenceType);
            }
        });
    }

    private static boolean isVoidEligibleEntity(Object entity) {
        return entity instanceof Zombie
                || entity instanceof Skeleton
                || entity instanceof Witch
                || entity instanceof Stray
                || entity instanceof Zoglin
                ;
    }

    private static boolean isEarthEligibleEntity(Object entity) {
        return entity instanceof Spider
                || entity instanceof IronGolem
                || entity instanceof Husk
                || entity instanceof Bogged
                ;
    }

    private static boolean isFireEligibleEntity(Object entity) {
        return entity instanceof Blaze
                || entity instanceof Strider
                || entity instanceof MagmaCube
                ;
    }

    private static void addElementalEssenceDrop(LivingDropsEvent event, ElementalEssenceType element) {
        ItemEntity drop = new ItemEntity(
                event.getEntity().level(),
                event.getEntity().getX(),
                event.getEntity().getY(),
                event.getEntity().getZ(),
                element.createEssence()
        );
        event.getDrops().add(drop);
    }
}
