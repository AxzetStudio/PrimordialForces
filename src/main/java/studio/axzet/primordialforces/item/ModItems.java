package studio.axzet.primordialforces.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.item.custom.EarthArmorItem;
import studio.axzet.primordialforces.item.custom.EarthHelmetArmorItem;
import studio.axzet.primordialforces.item.custom.VoidmancerArmorItem;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PrimordialForces.MOD_ID);

    private static final ResourceLocation EMPTY_SLOT_HELMET = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet");
    private static final ResourceLocation EMPTY_SLOT_CHESTPLATE = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate");
    private static final ResourceLocation EMPTY_SLOT_LEGGINGS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings");
    private static final ResourceLocation EMPTY_SLOT_BOOTS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots");
    private static final ResourceLocation EMPTY_SLOT_SHIELD = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_shield");
    private static final ResourceLocation EMPTY_SLOT_SWORD = ResourceLocation.withDefaultNamespace("item/empty_slot_sword");
    private static final ResourceLocation EMPTY_ARCADIUM_CORE_SLOT = ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "item/empty_slot_arcadium_core");

    //region BLACK OPAL
    public static final DeferredItem<Item> BLACK_OPAL = ITEMS.registerSimpleItem("black_opal");
    public static final DeferredItem<Item> RAW_BLACK_OPAL = ITEMS.registerItem("raw_black_opal", Item::new, new Item.Properties());
    //endregion
    //region ARCADIUM
    public static final DeferredItem<Item> ARCADIUM = ITEMS.registerSimpleItem("arcadium");
    public static final DeferredItem<Item> RAW_ARCADIUM = ITEMS.registerSimpleItem("raw_arcadium");
    public static final DeferredItem<Item> ARCADIUM_CONDUIT = ITEMS.registerSimpleItem("arcadium_conduit");
    public static final DeferredItem<Item> ARCADIUM_CORE = ITEMS.registerSimpleItem("arcadium_core");
    //endregion
    //region ESSENCES
    public static final DeferredItem<Item> VOID_ESSENCE = ITEMS.registerSimpleItem("void_essence");
    public static final DeferredItem<Item> EARTH_ESSENCE = ITEMS.registerSimpleItem("earth_essence");
    public static final DeferredItem<Item> FIRE_ESSENCE = ITEMS.registerSimpleItem("fire_essence");
    public static final DeferredItem<Item> WATER_ESSENCE = ITEMS.registerSimpleItem("water_essence");
    public static final DeferredItem<Item> AIR_ESSENCE = ITEMS.registerSimpleItem("air_essence");
    //endregion
    //region SHARDS
    public static final DeferredItem<Item> VOID_SHARD = ITEMS.registerSimpleItem("void_shard");
    public static final DeferredItem<Item> EARTH_SHARD = ITEMS.registerSimpleItem("earth_shard");
    //endregion
    //region RUNES
    public static final DeferredItem<SmithingTemplateItem> EARTH_RUNE = ITEMS.register(
            "earth_rune",
            () -> new SmithingTemplateItem(
                    Component.translatable("item.primordialforces.rune.applies_to"),
                    Component.translatable("item.primordialforces.rune.ingredients"),
                    Component.translatable("item.primordialforces.earth_rune.upgrade"),
                    Component.translatable("item.primordialforces.rune.base_slot"),
                    Component.translatable("item.primordialforces.rune.additional_slot"),
                    List.of(EMPTY_SLOT_HELMET, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_BOOTS, EMPTY_SLOT_SWORD, EMPTY_SLOT_SHIELD),
                    List.of(EMPTY_ARCADIUM_CORE_SLOT)
            )
    );
    //endregion

    //region WEAPONS
    public static final DeferredItem<Item> EARTH_SWORD = ITEMS.register("earth_sword",
            () -> new SwordItem(ModToolTiers.PRIMORDIAL_TIER, new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.PRIMORDIAL_TIER, 3, -2.4f)).stacksTo(1))
            );

    public static final DeferredItem<Item> EARTH_SHIELD = ITEMS.register("earth_shield",
            () -> new ShieldItem(new Item.Properties().durability(500))
            );
    //endregion
    //region VOID ARMOR
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
    //endregion
    //region EARTH ARMOR
    public static final DeferredItem<Item> EARTH_HELMET = ITEMS.register("earth_helmet",
            () -> new EarthHelmetArmorItem(ModArmorMaterials.EARTH_ARCADIUM, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(16)))
    );

    public static final DeferredItem<Item> EARTH_CHESTPLATE = ITEMS.register("earth_chestplate",
            () -> new EarthArmorItem(ModArmorMaterials.EARTH_ARCADIUM, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(16)))
    );

    public static final DeferredItem<Item> EARTH_LEGGINGS = ITEMS.register("earth_leggings",
            () -> new EarthArmorItem(ModArmorMaterials.EARTH_ARCADIUM, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(16)))
    );

    public static final DeferredItem<Item> EARTH_BOOTS = ITEMS.register("earth_boots",
            () -> new EarthArmorItem(ModArmorMaterials.EARTH_ARCADIUM, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(16)))
    );
    //endregion
    //region BLACK OPAL ARMOR
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
    //endregion



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
