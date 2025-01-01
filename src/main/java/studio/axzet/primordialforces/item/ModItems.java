package studio.axzet.primordialforces.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.item.custom.VoidmancerArmorItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PrimordialForces.MOD_ID);

    public static final DeferredItem<Item> BLACK_OPAL = ITEMS.registerSimpleItem("black_opal");
    public static final DeferredItem<Item> RAW_BLACK_OPAL = ITEMS.registerItem("raw_black_opal", Item::new, new Item.Properties());
    public static final DeferredItem<Item> ARCADIUM = ITEMS.registerSimpleItem("arcadium");
    public static final DeferredItem<Item> RAW_ARCADIUM = ITEMS.registerSimpleItem("raw_arcadium");
    public static final DeferredItem<Item> VOID_SHARD = ITEMS.registerSimpleItem("void_shard");

    // VOIDMANCER ARMOR
    public static final DeferredItem<Item> VOIDMANCER_HELMET = ITEMS.register("voidmancer_helmet",
            () -> new VoidmancerArmorItem(ModArmorMaterials.VOID_ARCADIUM, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(16)))
            );

    public static final DeferredItem<Item> VOIDMANCER_CHESTPLATE = ITEMS.register("voidmancer_chestplate",
            () -> new VoidmancerArmorItem(ModArmorMaterials.VOID_ARCADIUM, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(16)))
    );

    public static final DeferredItem<Item> VOIDMANCER_LEGGINGS = ITEMS.register("voidmancer_leggings",
            () -> new VoidmancerArmorItem(ModArmorMaterials.VOID_ARCADIUM, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(16)))
    );

    public static final DeferredItem<Item> VOIDMANCER_BOOTS = ITEMS.register("voidmancer_boots",
            () -> new VoidmancerArmorItem(ModArmorMaterials.VOID_ARCADIUM, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(16)))
    );

    // BLACK OPAL ARMOR
    public static final DeferredItem<Item> BLACK_OPAL_HELMET = ITEMS.register("black_opal_helmet",
            () -> new ArmorItem(ModArmorMaterials.BLACK_OPAL, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(16)))
            );

    public static final DeferredItem<Item> BLACK_OPAL_CHESTPLATE = ITEMS.register("black_opal_chestplate",
            () -> new ArmorItem(ModArmorMaterials.BLACK_OPAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(16)))
    );

    public static final DeferredItem<Item> BLACK_OPAL_LEGGINGS = ITEMS.register("black_opal_leggings",
            () -> new ArmorItem(ModArmorMaterials.BLACK_OPAL, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(16)))
    );

    public static final DeferredItem<Item> BLACK_OPAL_BOOTS = ITEMS.register("black_opal_boots",
            () -> new ArmorItem(ModArmorMaterials.BLACK_OPAL, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(16)))
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
