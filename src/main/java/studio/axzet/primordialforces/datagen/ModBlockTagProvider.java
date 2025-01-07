package studio.axzet.primordialforces.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PrimordialForces.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.ARCADIUM_BLOCK.get())
                .add(ModBlocks.ARCADIUM_DEEPSLATE_ORE.get())
                .add(ModBlocks.BLACK_OPAL_BLOCK.get())
                .add(ModBlocks.BLACK_OPAL_ORE.get())
                .add(ModBlocks.PRIMORDIAL_CRYSTAL_DEEPSLATE_ORE.get())
                .add(ModBlocks.ARCADIUM_PORTAL_FRAME.get())
        ;

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.ARCADIUM_DEEPSLATE_ORE.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.BLACK_OPAL_ORE.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.ARCADIUM_BLOCK.get())
                .add(ModBlocks.BLACK_OPAL_BLOCK.get())
                .add(ModBlocks.PRIMORDIAL_CRYSTAL_DEEPSLATE_ORE.get())
                .add(ModBlocks.ARCADIUM_PORTAL_FRAME.get())
        ;
    }
}
