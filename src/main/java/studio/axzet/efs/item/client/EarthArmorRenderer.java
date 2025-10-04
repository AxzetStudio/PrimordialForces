package studio.axzet.efs.item.client;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import studio.axzet.efs.EchoesOfTheFifthSun;
import studio.axzet.efs.item.custom.EarthArmorItem;

public class EarthArmorRenderer extends GeoArmorRenderer<EarthArmorItem> {
    public EarthArmorRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(EchoesOfTheFifthSun.MOD_ID, "armor/earth_armor")));
    }

    @Override
    public ResourceLocation getTextureLocation(EarthArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(EchoesOfTheFifthSun.MOD_ID, "textures/entity/woodguardian.png");
    }
}
