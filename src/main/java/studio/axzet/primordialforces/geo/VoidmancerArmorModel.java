package studio.axzet.primordialforces.geo;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.item.custom.VoidmancerArmorItem;

public class VoidmancerArmorModel extends GeoModel<VoidmancerArmorItem> {
    @Override
    public ResourceLocation getModelResource(VoidmancerArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "geo/voidmancer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VoidmancerArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "textures/armor/voidmancer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VoidmancerArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "animations/voidmancer.animation.json");
    }
}
