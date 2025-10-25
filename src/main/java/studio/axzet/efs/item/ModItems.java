package studio.axzet.efs.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.axzet.efs.EchoesOfTheFifthSun;
import studio.axzet.efs.item.custom.EarthArmorItem;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EchoesOfTheFifthSun.MOD_ID);

    private static final ResourceLocation EMPTY_SLOT_HELMET = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet");
    private static final ResourceLocation EMPTY_SLOT_CHESTPLATE = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate");
    private static final ResourceLocation EMPTY_SLOT_LEGGINGS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings");
    private static final ResourceLocation EMPTY_SLOT_BOOTS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots");
    private static final ResourceLocation EMPTY_SLOT_SHIELD = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_shield");
    private static final ResourceLocation EMPTY_SLOT_SWORD = ResourceLocation.withDefaultNamespace("item/empty_slot_sword");
    private static final ResourceLocation EMPTY_ARCADIUM_CORE_SLOT = ResourceLocation.fromNamespaceAndPath(EchoesOfTheFifthSun.MOD_ID, "item/empty_slot_arcadium_core");

    public static final DeferredItem<Item> OBSIDIAN_SHARD = ITEMS.registerSimpleItem("obsidian_shard");

    public static final DeferredItem<Item> HEART = ITEMS.registerSimpleItem("heart");
    public static final DeferredItem<Item> AXOLOTL_TOTEM = ITEMS.registerSimpleItem("axolotl_totem");

    //region Weapons
    public static final DeferredItem<Item> MACUAHUITL = ITEMS.register("macuahuitl",
            () -> new SwordItem(Tiers.WOOD, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.WOOD, 3, -2.4f)))
    );
    //endregion

    //region RUNES
    /*public static final DeferredItem<SmithingTemplateItem> EARTH_RUNE = ITEMS.register(
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
    );*/
    //endregion

    //region WEAPONS
    /*public static final DeferredItem<Item> EARTH_SWORD = ITEMS.register("earth_sword",
            () -> new SwordItem(ModToolTiers.PRIMORDIAL_TIER, new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.PRIMORDIAL_TIER, 3, -2.4f)).stacksTo(1))
            );

    public static final DeferredItem<Item> EARTH_SHIELD = ITEMS.register("earth_shield",
            () -> new ShieldItem(new Item.Properties().durability(500))
            );*/
    //endregion
    //region EARTH ARMOR
    /*public static final DeferredItem<Item> EARTH_HELMET = ITEMS.register("earth_helmet",
            () -> new EarthArmorItem(ModArmorMaterials.EARTH_ARCADIUM, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(16)))
    );*/
    //endregion

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
