package studio.axzet.primordialforces.entity.client;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.entity.custom.MossGolemEntity;

public class MossGolemModel<T extends MossGolemEntity> extends GeoModel<MossGolemEntity> {
    @Override
    public ResourceLocation getModelResource(MossGolemEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "geo/entity/moss_golem.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MossGolemEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "textures/entity/moss_golem.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MossGolemEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "animations/entity/moss_golem.animation.json");
    }
}
