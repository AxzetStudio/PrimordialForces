package studio.axzet.primordialforces.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class EntWarriorEntity extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");

    public EntWarriorEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 5.0)
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.ATTACK_DAMAGE, 6.0f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.ARMOR, 3.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25f)
                ;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9, 32));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 4.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this,  Player.class, true));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "move_controller", 5, this::movePredicate));
    }

    private PlayState movePredicate(AnimationState<EntWarriorEntity> state) {
        if (state.isMoving()) {
            return state.setAndContinue(WALK_ANIMATION);
        }

        return state.setAndContinue(IDLE_ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}
