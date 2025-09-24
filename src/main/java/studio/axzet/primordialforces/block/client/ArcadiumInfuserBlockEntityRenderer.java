package studio.axzet.primordialforces.block.client;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.block.entity.ArcadiumInfuserBlockEntity;

public class ArcadiumInfuserBlockEntityRenderer extends GeoBlockRenderer<ArcadiumInfuserBlockEntity> {

    public ArcadiumInfuserBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "arcadium_infuser")));
    }
}
