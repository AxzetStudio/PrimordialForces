package studio.axzet.efs.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import studio.axzet.efs.entity.ModEntities;

public class Jaguar extends AbstractHorse implements GeoEntity {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALKING_ANIMATION = RawAnimation.begin().thenLoop("walking");
    private static final RawAnimation RUNNING_ANIMATION = RawAnimation.begin().thenLoop("running");

    public Jaguar(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0f)
                .add(Attributes.MAX_HEALTH, 15.0f)
                .add(Attributes.ATTACK_DAMAGE, 6.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.25f)
                .add(Attributes.JUMP_STRENGTH, 0.55f)
                ;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.MEAT);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return ModEntities.JAGUAR.get().create(serverLevel);
    }

    /* ANIMATIONS */

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "jaguar_controller", 5, this::predicate));
    }

    private PlayState predicate(AnimationState<Jaguar> state) {

        if (state.isMoving() && isVehicle()) {
            return state.setAndContinue(RUNNING_ANIMATION);
        }

        if (state.isMoving()) {
            return state.setAndContinue(WALKING_ANIMATION);
        }

        return state.setAndContinue(IDLE_ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    /* RIDEABLE */

    @Override
    public boolean canUseSlot(EquipmentSlot slot) {
        return true;
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        boolean flag = !this.isBaby() && this.isTamed() && player.isSecondaryUseActive();
        if (!this.isVehicle() && !flag) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (!itemStack.isEmpty()) {
                if (this.isFood(itemStack)) {
                    return this.fedFood(player, itemStack);
                }

                if (!this.isTamed()) {
                    this.makeMad();
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
            }
            return super.mobInteract(player, hand);
        } else {
            return super.mobInteract(player, hand);
        }
    }
}
