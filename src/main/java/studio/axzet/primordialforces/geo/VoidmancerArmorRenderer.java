package studio.axzet.primordialforces.geo;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.item.custom.VoidmancerArmorItem;

public class VoidmancerArmorRenderer extends GeoArmorRenderer<VoidmancerArmorItem> {
    public VoidmancerArmorRenderer() {
        super(new VoidmancerArmorModel());
    }
}
