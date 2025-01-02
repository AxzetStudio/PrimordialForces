package studio.axzet.primordialforces.datagen;

import net.minecraft.data.PackOutput;
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
        basicItem(ModItems.RAW_ARCADIUM.get());
        basicItem(ModItems.RAW_BLACK_OPAL.get());
        basicItem(ModItems.ARCADIUM.get());
        basicItem(ModItems.BLACK_OPAL.get());
        basicItem(ModItems.VOID_SHARD.get());

        // Fuel
        basicItem(ModItems.PRIMORDIAL_CRYSTAL.get());

        basicItem(ModItems.VOIDMANCER_HELMET.get());
        basicItem(ModItems.VOIDMANCER_CHESTPLATE.get());
        basicItem(ModItems.VOIDMANCER_LEGGINGS.get());
        basicItem(ModItems.VOIDMANCER_BOOTS.get());

        //Black Opal Armor
        basicItem(ModItems.BLACK_OPAL_HELMET.get());
        basicItem(ModItems.BLACK_OPAL_CHESTPLATE.get());
        basicItem(ModItems.BLACK_OPAL_LEGGINGS.get());
        basicItem(ModItems.BLACK_OPAL_BOOTS.get());
    }
}
