package studio.axzet.efs.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import studio.axzet.efs.recipe.ArcadiumInfuserRecipe;
import studio.axzet.efs.recipe.ArcadiumInfuserRecipeInput;
import studio.axzet.efs.recipe.ModRecipes;
import studio.axzet.efs.screen.custom.ArcadiumInfuserMenu;

import java.util.Optional;
import java.util.stream.IntStream;

public class ArcadiumInfuserBlockEntity extends BlockEntity implements MenuProvider, GeoBlockEntity {

    protected static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.arcadium_infuser.idle");
    protected static final RawAnimation INFUSING_ANIMATION = RawAnimation.begin().thenLoop("animation.arcadium_infuser.infusing");

    AnimationController<ArcadiumInfuserBlockEntity> animationController = new AnimationController<>(this, "arcadium_infuser_controller", 5, this::predicate);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final Logger LOGGER = LoggerFactory.getLogger(ArcadiumInfuserBlockEntity.class);

    public final ItemStackHandler itemStackHandler = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int CORE_SLOT = 0;
    private static final int INFUSE_SLOT1 = 1;
    private static final int INFUSE_SLOT2 = 2;
    private static final int INFUSE_SLOT3 = 3;
    private static final int INFUSE_SLOT4 = 4;
    private static final int INFUSE_SLOT5 = 5;
    private static final int INFUSE_SLOT6 = 6;
    private static final int OUTPUT_SLOT = 7;


    private final ContainerData data;
    private int progress = 0;
    private int maxProgress = 144;
    private final int DEFAULT_MAX_PROGRESS = 144;

    private boolean isInfusing = false;

    public ArcadiumInfuserBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ARCADIUM_INFUSER_BE.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> ArcadiumInfuserBlockEntity.this.progress;
                    case 1 -> ArcadiumInfuserBlockEntity.this.maxProgress;
                    default ->  0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> ArcadiumInfuserBlockEntity.this.progress = i1;
                    case 1 -> ArcadiumInfuserBlockEntity.this.maxProgress = i1;
                };
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("blockentity.primordialforces.arcadium_infuser");
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        LOGGER.info("createMenu called for player: " + player.getName().getString());

        return new ArcadiumInfuserMenu(i, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemStackHandler.serializeNBT(registries));
        tag.putInt("arcadium_infuser.progress", progress);
        tag.putInt("arcadium_infuser.max_progress", maxProgress);
        tag.putBoolean("arcadium_infuser.is_infusing", isInfusing);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemStackHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("arcadium_infuser.progress");
        maxProgress = tag.getInt("arcadium_infuser.max_progress");
        isInfusing = tag.getBoolean("arcadium_infuser.is_infusing");
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemStackHandler.getSlots());

        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inv.setItem(i, itemStackHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        boolean wasInfusing = isInfusing;
        
        if(hasRecipe() && isOutputSlotEmptyOrReceivable()) {
            isInfusing = true;
            increaseCraftingProgress();
            setChanged(level, pos, state);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
                isInfusing = false;
            }
        } else {
            isInfusing = false;
          resetProgress();
        }

        if (wasInfusing != isInfusing && !level.isClientSide) {
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = DEFAULT_MAX_PROGRESS;
    }

    private void craftItem() {
        Optional<RecipeHolder<ArcadiumInfuserRecipe>> recipe = getCurrentRecipe();
        ItemStack output = recipe.get().value().output();

        itemStackHandler.extractItem(CORE_SLOT, 1, false);
        IntStream.of(INFUSE_SLOT1, INFUSE_SLOT2, INFUSE_SLOT3, INFUSE_SLOT4, INFUSE_SLOT5, INFUSE_SLOT6)
                .forEach(slot -> itemStackHandler.extractItem(slot, 1, false));

        ItemStack currentOutput = itemStackHandler.getStackInSlot(OUTPUT_SLOT);
        if (currentOutput.isEmpty()) {
            itemStackHandler.setStackInSlot(OUTPUT_SLOT, output.copy());
        } else {
            currentOutput.grow(output.getCount());
        }
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.itemStackHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                this.itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount() < this.itemStackHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<ArcadiumInfuserRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty() || needsInfusingRequirements()) {
            return false;
        }

        ItemStack output = recipe.get().value().getResultItem(null);

        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);
    }

    private boolean needsInfusingRequirements() {
        Optional<RecipeHolder<ArcadiumInfuserRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return true;

        ItemStack core = itemStackHandler.getStackInSlot(CORE_SLOT);
        ItemStack firstInfuser = itemStackHandler.getStackInSlot(INFUSE_SLOT1);

        boolean matchesRecipe = recipe.get().value().core().test(core) &&
                recipe.get().value().infuser().test(firstInfuser);

        boolean infusersMatch = areAllInfuserSlotsEqual();

        return !matchesRecipe || !infusersMatch;
    }

    private boolean areAllInfuserSlotsEqual() {
        ItemStack firstInfuser = itemStackHandler.getStackInSlot(INFUSE_SLOT1);
        if (firstInfuser.isEmpty()) return false;

        return firstInfuser.getItem() == itemStackHandler.getStackInSlot(INFUSE_SLOT2).getItem()
                && firstInfuser.getItem() == itemStackHandler.getStackInSlot(INFUSE_SLOT3).getItem()
                && firstInfuser.getItem() == itemStackHandler.getStackInSlot(INFUSE_SLOT4).getItem()
                && firstInfuser.getItem() == itemStackHandler.getStackInSlot(INFUSE_SLOT5).getItem()
                && firstInfuser.getItem() == itemStackHandler.getStackInSlot(INFUSE_SLOT6).getItem()
                ;
    }

    private Optional<RecipeHolder<ArcadiumInfuserRecipe>> getCurrentRecipe() {
        return this.level.getRecipeManager().getRecipeFor(
                ModRecipes.ARCADIUM_INFUSER_TYPE.get(),
                new ArcadiumInfuserRecipeInput(
                        itemStackHandler.getStackInSlot(CORE_SLOT),
                        itemStackHandler.getStackInSlot(INFUSE_SLOT1)
                ),
                level
        );
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return itemStackHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                itemStackHandler.getStackInSlot(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = itemStackHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ? 64 : itemStackHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(animationController);
    }

    private PlayState predicate(AnimationState<ArcadiumInfuserBlockEntity> state) {
        if (isInfusing) {
            return state.setAndContinue(INFUSING_ANIMATION);
        }

        return state.setAndContinue(IDLE_ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
