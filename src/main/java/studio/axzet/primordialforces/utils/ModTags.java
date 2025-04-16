package studio.axzet.primordialforces.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import studio.axzet.primordialforces.PrimordialForces;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> INCORRECT_FOR_ARCADIUM_TOOL = createTag("incorrect_for_arcadium_tool");
        public static final TagKey<Block> NEEDS_FOR_ARCADIUM_TOOL = createTag("needs_arcadium_tool");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, name));
        }
    }
}
