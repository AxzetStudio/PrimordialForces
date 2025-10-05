package studio.axzet.efs.event;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import studio.axzet.efs.EchoesOfTheFifthSun;
import studio.axzet.efs.entity.custom.WoodGuardianEntity;
import studio.axzet.efs.item.ModItems;
import studio.axzet.efs.utils.ElementalEssenceType;

import java.util.Map;
import java.util.function.Predicate;

@EventBusSubscriber(modid = EchoesOfTheFifthSun.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    private static final float VILLAGER_HEART_DROP_CHANCE = 0.4f;

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        RandomSource random = entity.level().random;

        if (random.nextFloat() >= VILLAGER_HEART_DROP_CHANCE) return;

        if (entity instanceof Villager) {
            ItemStack heartStack = new ItemStack(ModItems.HEART.get());
            ItemEntity heartDrop = new ItemEntity(
                    entity.level(),
                    entity.getX(),
                    entity.getY(),
                    entity.getZ(),
                    heartStack
            );

            event.getDrops().add(heartDrop);
        }
    }
}
