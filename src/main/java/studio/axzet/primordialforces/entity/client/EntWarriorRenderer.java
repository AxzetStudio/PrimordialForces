package studio.axzet.primordialforces.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.entity.custom.EntWarriorEntity;

public class EntWarriorRenderer extends GeoEntityRenderer<EntWarriorEntity> {
    public EntWarriorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "ent_warrior")));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntWarriorEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "textures/entity/ent_warrior.png");
    }
}
