package studio.axzet.efs.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import studio.axzet.efs.EchoesOfTheFifthSun;
import studio.axzet.efs.entity.custom.WoodGuardianEntity;

public class WoodGuardianRenderer extends GeoEntityRenderer<WoodGuardianEntity> {

    public WoodGuardianRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(EchoesOfTheFifthSun.MOD_ID, "woodguardian")));
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(WoodGuardianEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(EchoesOfTheFifthSun.MOD_ID, "textures/entity/woodguardian.png");
    }
}
