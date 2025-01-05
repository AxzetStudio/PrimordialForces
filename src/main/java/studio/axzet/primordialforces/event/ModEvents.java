package studio.axzet.primordialforces.event;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.item.ModItems;

@EventBusSubscriber(modid = PrimordialForces.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    static float dropChance = 0.3f;

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof Zombie zombie) {
            if (zombie.level().random.nextFloat() < dropChance) {
                ItemStack voidEssence = new ItemStack(ModItems.VOID_ESSENCE.get());
                ItemEntity drop = new ItemEntity(zombie.level(), zombie.getX(), zombie.getY(), zombie.getZ(), voidEssence);
                event.getDrops().add(drop);
            }
        }
    }
}
