package studio.axzet.primordialforces.portal;

import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.block.ModBlocks;
import studio.axzet.primordialforces.item.ModItems;

public class ModPortals {
    private static void voidDimensionPortal() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(ModBlocks.ARCADIUM_PORTAL_FRAME.get())
                .lightWithItem(ModItems.VOID_SHARD.get())
                .forcedSize(4, 5)
                .onlyLightInOverworld()
                .destDimID(ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "thevoid"))
                .registerPortal()
        ;
    }

    private static void portalTest() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(ModBlocks.BLACK_OPAL_BLOCK.get())
                .lightWithItem(Items.DIAMOND)
                .forcedSize(4, 5)
                .onlyLightInOverworld()
                .destDimID(ResourceLocation.parse("the_end"))
                .tintColor(249, 248, 69)
                .registerPortal()
        ;
    }

    public static void createPortals() {
        //portalTest();
        voidDimensionPortal();
    }
}
