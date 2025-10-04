package studio.axzet.efs.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import studio.axzet.efs.EchoesOfTheFifthSun;
import studio.axzet.efs.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, EchoesOfTheFifthSun.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //region ARCADIUM
        //blockWithItem(ModBlocks.ARCADIUM_DEEPSLATE_ORE);
        //endregion
    }

    private void blockWithItem(DeferredBlock<Block> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
