package studio.axzet.primordialforces.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.block.custom.ArcadiumInfuserBlock;
import studio.axzet.primordialforces.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PrimordialForces.MOD_ID);

    public static final DeferredBlock<Block> BLACK_OPAL_ORE = registerBlock("black_opal_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops())
            );

    public static final DeferredBlock<Block> BLACK_OPAL_BLOCK = registerBlock("black_opal_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops())
            );

    public static final DeferredBlock<Block> ARCADIUM_DEEPSLATE_ORE = registerBlock("arcadium_deepslate_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops())
            );

    public static final DeferredBlock<Block> ARCADIUM_BLOCK = registerBlock("arcadium_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops())
            );

    // Infuser
    public static final DeferredBlock<Block> ARCADIUM_INFUSER = registerBlock("arcadium_infuser",
            () -> new ArcadiumInfuserBlock(BlockBehaviour.Properties.of().noOcclusion())
            );

    // PRIMORDIAL CRYSTAL
    public static final DeferredBlock<Block> PRIMORDIAL_CRYSTAL_DEEPSLATE_ORE = registerBlock("primordial_crystal_deepslate_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops())
            );

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
