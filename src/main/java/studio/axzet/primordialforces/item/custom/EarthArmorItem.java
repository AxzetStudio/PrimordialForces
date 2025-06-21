package studio.axzet.primordialforces.item.custom;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import studio.axzet.primordialforces.PrimordialForces;

public class EarthArmorItem extends ArmorItem {
    public EarthArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        if (type == Type.LEGGINGS) {
            return ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "textures/models/armor/earth_arcadium_layer_2.png");
        }

        return ResourceLocation.fromNamespaceAndPath(PrimordialForces.MOD_ID, "textures/models/armor/earth_arcadium_layer_1.png");
    }
}
