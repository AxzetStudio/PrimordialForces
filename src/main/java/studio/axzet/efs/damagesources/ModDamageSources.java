package studio.axzet.efs.damagesources;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import studio.axzet.efs.EchoesOfTheFifthSun;

public class ModDamageSources {
    public static final ResourceKey<DamageType> POISON_CLOUD = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(EchoesOfTheFifthSun.MOD_ID, "poison_cloud"));

    public static DamageSource poisonCloud(Level level, Entity source) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(POISON_CLOUD), source);
    }
}
