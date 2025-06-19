package studio.axzet.primordialforces.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.entity.custom.EntBruteEntity;

public class EntBruteRenderer extends GeoEntityRenderer<EntBruteEntity> {
    public EntBruteRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "ent_brute")));
    }

    @Override
    public ResourceLocation getTextureLocation(EntBruteEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "textures/entity/ent_brute.png");
    }
}
