package studio.axzet.efs.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.axzet.efs.EchoesOfTheFifthSun;
import studio.axzet.efs.block.custom.ArcadiumInfuserBlock;
import studio.axzet.efs.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(EchoesOfTheFifthSun.MOD_ID);

    //region ARCADIUM
    public static final DeferredBlock<Block> ARCADIUM_DEEPSLATE_ORE = registerBlock("arcadium_deepslate_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<Block> ARCADIUM_BLOCK = registerBlock("arcadium_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops())
    );

    public static final DeferredBlock<Block> ARCADIUM_PORTAL_FRAME = registerBlock("arcadium_portal_frame",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops())
    );

    // INFUSER
    public static final DeferredBlock<Block> ARCADIUM_INFUSER = registerBlock("arcadium_infuser",
            () -> new ArcadiumInfuserBlock(BlockBehaviour.Properties.of().noOcclusion())
    );
    //endregion

    private static <T extends Block>DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
