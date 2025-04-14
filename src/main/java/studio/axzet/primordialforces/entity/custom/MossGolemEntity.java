package studio.axzet.primordialforces.entity.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class MossGolemEntity extends Monster implements GeoEntity {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    // Definición de animaciones
    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation ATTACK_ANIMATION = RawAnimation.begin().thenPlay("attack1");
    private static final RawAnimation DEATH_ANIMATION = RawAnimation.begin().thenPlay("death");

    public MossGolemEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 20.0)
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.ATTACK_DAMAGE, 6.0f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.ARMOR, 2.0)
                .add(Attributes.MOVEMENT_SPEED, 0.1f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(8, new MeleeAttackGoal(this, 1, false) {
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

        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Creeper.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true));

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "move_controller", 5, this::movePredicate));
        controllers.add(new AnimationController<>(this, "attack_controller", 0, this::attackPredicate)
                .triggerableAnim("attack1", ATTACK_ANIMATION)
                .triggerableAnim("death", DEATH_ANIMATION)
        );
    }

    private PlayState attackPredicate(AnimationState<MossGolemEntity> mossGolemEntityAnimationState) {
        return PlayState.STOP;
    }

    private PlayState movePredicate(AnimationState<MossGolemEntity> mossGolemEntityAnimationState) {
        if (mossGolemEntityAnimationState.isMoving()) {
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
    public void die(DamageSource damageSource) {
        if (!this.isDeadOrDying()) {
            triggerAnim("attack_controller", "death");
        }
        super.die(damageSource);
    }
}
