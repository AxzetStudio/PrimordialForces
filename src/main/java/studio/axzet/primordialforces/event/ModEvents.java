package studio.axzet.primordialforces.event;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.item.ModItems;

@EventBusSubscriber(modid = PrimordialForces.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    private static float DROP_CHANCE = 0.3f;

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (isVoidEligibleEntity(event.getEntity())) {
            if (event.getEntity().level().random.nextFloat() < DROP_CHANCE) {
                addVoidDrop(event);
            }
        }
    }

    private static boolean isVoidEligibleEntity(Object entity) {
        return entity instanceof Zombie
                || entity instanceof Skeleton
                || entity instanceof Witch
                || entity instanceof Stray
                || entity instanceof Zoglin
                ;
    }

    private static void addVoidDrop(LivingDropsEvent event) {
        ItemStack voidEssence = new ItemStack(ModItems.VOID_ESSENCE.get());
        ItemEntity drop = new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), voidEssence);
        event.getDrops().add(drop);
    }
}
