package studio.axzet.efs.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import studio.axzet.efs.EchoesOfTheFifthSun;
import studio.axzet.efs.entity.custom.Eagle;

public class EagleRenderer extends GeoEntityRenderer<Eagle> {

    public EagleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(EchoesOfTheFifthSun.MOD_ID, "mob/neutral/eagle")));
    }
}
