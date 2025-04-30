package studio.axzet.primordialforces.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class MossGolemEntity extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    // Definición de animaciones
    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation ATTACK_ANIMATION = RawAnimation.begin().thenPlay("attack1");
    public MossGolemEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 5.0)
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_DAMAGE, 6.0f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.ARMOR, 2.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, false) {
            @Override
            protected void checkAndPerformAttack(LivingEntity target) {
                if (this.canPerformAttack(target)) {
                    triggerAnim("attack_controller", "attack1");
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(target);
                    this.resetAttackCooldown();
                }
            }

            @Override
            protected boolean isTimeToAttack() {
                return this.getTicksUntilNextAttack() <= 0;
            }

            @Override
            protected int getAttackInterval() {
                return 20;
            }
        });
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9, 32));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1, 200));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 4.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this,  Player.class, true));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "move_controller", 5, this::movePredicate));
        controllers.add(new AnimationController<>(this, "attack_controller", 0, this::attackPredicate)
                .triggerableAnim("attack1", ATTACK_ANIMATION)
        );
    }

    private PlayState attackPredicate(AnimationState<MossGolemEntity> mossGolemEntityAnimationState) {
        return PlayState.STOP;
    }

    private PlayState movePredicate(AnimationState<MossGolemEntity> mossGolemEntityAnimationState) {
        Vec3 velocity = this.getDeltaMovement();
        if (velocity.horizontalDistanceSqr() > 1.0E-4) {
            return mossGolemEntityAnimationState.setAndContinue(WALK_ANIMATION);
        }

        return mossGolemEntityAnimationState.setAndContinue(IDLE_ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public int getCurrentSwingDuration() {
        return 20;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.swinging && this.swingTime == this.getCurrentSwingDuration() -1) {
            this.swingTime = 0;
        }
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }
}
