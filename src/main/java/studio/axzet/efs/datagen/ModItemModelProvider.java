package studio.axzet.efs.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import studio.axzet.efs.EchoesOfTheFifthSun;
import studio.axzet.efs.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, EchoesOfTheFifthSun.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.OBSIDIAN_SHARD.get());
        basicItem(ModItems.HEART.get());

        handheldItem(ModItems.MACUAHUITL.get());
    }
}
