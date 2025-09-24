package studio.axzet.primordialforces.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.entity.custom.WoodGuardianEntity;

public class WoodGuardianRenderer extends GeoEntityRenderer<WoodGuardianEntity> {

    public WoodGuardianRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "woodguardian")));
    }

    @Override
    public ResourceLocation getTextureLocation(WoodGuardianEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "textures/entity/woodguardian.png");
    }
}
