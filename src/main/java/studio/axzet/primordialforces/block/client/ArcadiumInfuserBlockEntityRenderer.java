package studio.axzet.primordialforces.block.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.block.entity.ArcadiumInfuserBlockEntity;

public class ArcadiumInfuserBlockEntityRenderer extends GeoBlockRenderer<ArcadiumInfuserBlockEntity> {

    public ArcadiumInfuserBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "arcadium_infuser")));
    }

    @Override
    public void render(ArcadiumInfuserBlockEntity animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
