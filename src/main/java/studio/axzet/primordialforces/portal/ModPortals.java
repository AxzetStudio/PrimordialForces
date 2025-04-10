package studio.axzet.primordialforces.portal;

import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.block.ModBlocks;
import studio.axzet.primordialforces.item.ModItems;

public class ModPortals {
    private static void creationForgeDimensionPortal() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(ModBlocks.ARCADIUM_PORTAL_FRAME.get())
                .lightWithItem(ModItems.VOID_SHARD.get())
                .forcedSize(4, 5)
                .onlyLightInOverworld()
                .destDimID(ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "creationforge"))
                .registerPortal()
        ;
    }

    public static void createPortals() {
        //portalTest();
        creationForgeDimensionPortal();
    }
}
