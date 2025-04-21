package studio.axzet.primordialforces.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import studio.axzet.primordialforces.PrimordialForces;
import studio.axzet.primordialforces.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PrimordialForces.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Black Opal
        basicItem(ModItems.RAW_BLACK_OPAL.get());
        basicItem(ModItems.BLACK_OPAL.get());

        // Arcadium
        basicItem(ModItems.RAW_ARCADIUM.get());
        basicItem(ModItems.ARCADIUM.get());
        basicItem(ModItems.ARCADIUM_CONDUIT.get());
        basicItem(ModItems.ARCADIUM_CORE.get());

        // ESSENCES
        basicItem(ModItems.VOID_ESSENCE.get());
        basicItem(ModItems.EARTH_ESSENCE.get());
        basicItem(ModItems.FIRE_ESSENCE.get());
        basicItem(ModItems.WATER_ESSENCE.get());
        basicItem(ModItems.AIR_ESSENCE.get());

        // SHARDS
        basicItem(ModItems.VOID_SHARD.get());
        basicItem(ModItems.EARTH_SHARD.get());

        // RUNES
        basicItem(ModItems.EARTH_RUNE.get());

        // Void Armor
        basicItem(ModItems.VOIDMANCER_HELMET.get());
        basicItem(ModItems.VOIDMANCER_CHESTPLATE.get());
        basicItem(ModItems.VOIDMANCER_LEGGINGS.get());
        basicItem(ModItems.VOIDMANCER_BOOTS.get());

        // Earth Armor
        basicItem(ModItems.EARTH_HELMET.get());
        basicItem(ModItems.EARTH_CHESTPLATE.get());
        basicItem(ModItems.EARTH_LEGGINGS.get());
        basicItem(ModItems.EARTH_BOOTS.get());

        //Black Opal Armor
        basicItem(ModItems.BLACK_OPAL_HELMET.get());
        basicItem(ModItems.BLACK_OPAL_CHESTPLATE.get());
        basicItem(ModItems.BLACK_OPAL_LEGGINGS.get());
        basicItem(ModItems.BLACK_OPAL_BOOTS.get());
    }
}
