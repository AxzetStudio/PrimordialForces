package studio.axzet.efs.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import studio.axzet.efs.block.ModBlocks;
import studio.axzet.efs.item.ModItems;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    protected ModBlockLootTableProvider(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        //region ARCADIUM BLOCKS
        dropSelf(ModBlocks.ARCADIUM_BLOCK.get());
        dropSelf(ModBlocks.ARCADIUM_INFUSER.get());
        dropSelf(ModBlocks.ARCADIUM_PORTAL_FRAME.get());

        this.add(ModBlocks.ARCADIUM_DEEPSLATE_ORE.get(),
                block -> createOreDrop(ModBlocks.ARCADIUM_DEEPSLATE_ORE.get(), ModItems.RAW_ARCADIUM.get())
        );
        //endregion
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
