package studio.axzet.primordialforces.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import studio.axzet.primordialforces.block.ModBlocks;
import studio.axzet.primordialforces.item.ModItems;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    protected ModBlockLootTableProvider(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.BLACK_OPAL_BLOCK.get());
        dropSelf(ModBlocks.ARCADIUM_BLOCK.get());
        dropSelf(ModBlocks.ARCADIUM_INFUSER.get());

        this.add(ModBlocks.BLACK_OPAL_ORE.get(),
            block -> createOreDrop(ModBlocks.BLACK_OPAL_ORE.get(), ModItems.RAW_BLACK_OPAL.get())
        );

        this.add(ModBlocks.ARCADIUM_DEEPSLATE_ORE.get(),
                block -> createOreDrop(ModBlocks.ARCADIUM_DEEPSLATE_ORE.get(), ModItems.RAW_ARCADIUM.get())
                );

        this.add(ModBlocks.PRIMORDIAL_CRYSTAL_DEEPSLATE_ORE.get(),
                block -> createOreDrop(ModBlocks.PRIMORDIAL_CRYSTAL_DEEPSLATE_ORE.get(), ModItems.PRIMORDIAL_CRYSTAL.get())
                );
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
