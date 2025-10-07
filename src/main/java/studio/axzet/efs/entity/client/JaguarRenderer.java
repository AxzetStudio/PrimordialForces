package studio.axzet.efs.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import studio.axzet.efs.EchoesOfTheFifthSun;
import studio.axzet.efs.entity.custom.Jaguar;

public class JaguarRenderer extends GeoEntityRenderer<Jaguar> {

    public JaguarRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(EchoesOfTheFifthSun.MOD_ID, "mob/neutral/jaguar")));
    }
}
