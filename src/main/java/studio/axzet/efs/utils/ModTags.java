package studio.axzet.efs.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import studio.axzet.efs.EchoesOfTheFifthSun;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> INCORRECT_FOR_ARCADIUM_TOOL = createTag("incorrect_for_arcadium_tool");
        public static final TagKey<Block> NEEDS_FOR_ARCADIUM_TOOL = createTag("needs_arcadium_tool");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(EchoesOfTheFifthSun.MOD_ID, name));
        }
    }
}
