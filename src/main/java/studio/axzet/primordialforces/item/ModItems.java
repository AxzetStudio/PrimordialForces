package studio.axzet.primordialforces.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.axzet.primordialforces.PrimordialForces;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PrimordialForces.MOD_ID);

    public static final DeferredItem<Item> BLACK_OPAL = ITEMS.registerSimpleItem("black_opal");
    public static final DeferredItem<Item> RAW_BLACK_OPAL = ITEMS.registerItem("raw_black_opal", Item::new, new Item.Properties());
    public static final DeferredItem<Item> ARCADIUM = ITEMS.registerSimpleItem("arcadium");
    public static final DeferredItem<Item> RAW_ARCADIUM = ITEMS.registerSimpleItem("raw_arcadium");
    public static final DeferredItem<Item> VOID_SHARD = ITEMS.registerSimpleItem("void_shard");


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
