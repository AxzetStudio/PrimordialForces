package studio.axzet.efs.item;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.axzet.efs.EchoesOfTheFifthSun;

import java.util.EnumMap;
import java.util.List;


public class ModArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, EchoesOfTheFifthSun.MOD_ID);

    public static final Holder<ArmorMaterial> VOID_ARCADIUM =
            ARMOR_MATERIALS.register("void_arcadium", () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 4);
                        map.put(ArmorItem.Type.LEGGINGS, 8);
                        map.put(ArmorItem.Type.CHESTPLATE, 10);
                        map.put(ArmorItem.Type.HELMET, 4);
                        map.put(ArmorItem.Type.BODY, 11);
                    }), 20, SoundEvents.ARMOR_EQUIP_NETHERITE, () -> Ingredient.of(ModItems.VOID_SHARD.get()),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(EchoesOfTheFifthSun.MOD_ID, "void_arcadium"))),
                    4, 0.1f
            ));

    public static final Holder<ArmorMaterial> EARTH_ARCADIUM =
            ARMOR_MATERIALS.register("earth_arcadium", () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 4);
                        map.put(ArmorItem.Type.LEGGINGS, 8);
                        map.put(ArmorItem.Type.CHESTPLATE, 10);
                        map.put(ArmorItem.Type.HELMET, 4);
                        map.put(ArmorItem.Type.BODY, 11);
                    }), 20, SoundEvents.ARMOR_EQUIP_NETHERITE, () -> Ingredient.of(ModItems.EARTH_RUNE.get()),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(EchoesOfTheFifthSun.MOD_ID, "earth_arcadium"))),
                    4, 1.0f
            ));

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }

}
