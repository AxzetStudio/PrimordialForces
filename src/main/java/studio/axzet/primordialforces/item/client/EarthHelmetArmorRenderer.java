package studio.axzet.primordialforces.item.client;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.item.custom.EarthHelmetArmorItem;

public class EarthHelmetArmorRenderer extends GeoArmorRenderer<EarthHelmetArmorItem> {
    public EarthHelmetArmorRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "armor/earth_helmet")));
    }
}
