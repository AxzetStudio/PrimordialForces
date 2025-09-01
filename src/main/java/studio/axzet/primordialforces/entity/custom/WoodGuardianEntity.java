package studio.axzet.primordialforces.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import studio.axzet.primordialforces.entity.goals.WoodGuardianAttackGoal;
import net.minecraft.world.level.block.Blocks;

import java.util.UUID;

public class WoodGuardianEntity extends AbstractGolem implements NeutralMob, GeoEntity {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    // Add this constant for anger duration
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.woodguardian.idle");
    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("animation.woodguardian.walk");
    private static final RawAnimation SMASH_ANIMATION = RawAnimation.begin().thenPlay("animation.woodguardian.earthsmash");

    // Data synchronization
    private static final EntityDataAccessor<Boolean> DATA_IS_ATTACKING = SynchedEntityData.defineId(WoodGuardianEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TICK = SynchedEntityData.defineId(WoodGuardianEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(WoodGuardianEntity.class, EntityDataSerializers.INT);

    // Attack variables
    private int attackTick = 0;
    private boolean isAttacking = false;
    private LivingEntity attackTarget = null;
    private boolean hitDealt = false;
    private int attackCooldown = 0;

    // Anger variables for NeutralMob
    private UUID persistentAngerTarget;

    public WoodGuardianEntity(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_ATTACKING, false);
        builder.define(DATA_ATTACK_TICK, 0);
        builder.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.ATTACK_DAMAGE, 10.0f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.ARMOR, 3.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.25f)
                ;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new WoodGuardianAttackGoal(this, 1.0f, false));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9f, 32.0f));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        // Target selection goals for neutral mob behavior
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public void tick() {
        super.tick();

        // Reduce attack cooldown
        if (attackCooldown > 0) {
            attackCooldown--;
        }

        if (isAttacking) {
            attackTick++;

            // Sync data with client
            setAttackingSynced(true);
            setAttackTickSynced(attackTick);

            // Deal damage at 1 second (20 ticks)
            if (attackTick == 20 && !hitDealt && attackTarget != null) {
                performAreaAttack();
                hitDealt = true;
            }

            // End attack after 3 seconds (60 ticks)
            if (attackTick >= 60) {
                endAttack();
            }
        }

        // Update anger if not on client side
        if (!this.level().isClientSide) {
            this.updatePersistentAnger((net.minecraft.server.level.ServerLevel)this.level(), true);
        }
    }

    public void startAttack(LivingEntity target) {
        if (!isAttacking && attackCooldown <= 0) {
            this.isAttacking = true;
            this.attackTarget = target;
            this.attackTick = 0;
            this.hitDealt = false;
            this.attackCooldown = 100; // 5 seconds cooldown

            // Sync immediately
            setAttackingSynced(true);
            setAttackTickSynced(0);

            this.getNavigation().stop();
        }
    }

    public void performAreaAttack() {
        double radius = 5.0;

        spawnAttackParticles();

        this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(radius))
                .forEach(player -> {
                    if (player != null && this.distanceToSqr(player) <= 25.0) { // 5 block radius squared
                        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                        player.hurt(this.damageSources().mobAttack(this), damage);
                        
                        // Play attack sound
                        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), 
                                             SoundEvents.PLAYER_ATTACK_STRONG, this.getSoundSource(), 1.0f, 1.0f);
                    }
                });
    }
    
    private void spawnAttackParticles() {
        if (this.level() instanceof ServerLevel serverLevel) {
            double radius = 5.0;
            int particleCount = 80; // Más partículas para mejor efecto
            
            // Partículas de tierra en círculo
            for (int i = 0; i < particleCount; i++) {
                double angle = (2.0 * Math.PI * i) / particleCount;
                double particleX = this.getX() + Math.cos(angle) * radius;
                double particleZ = this.getZ() + Math.sin(angle) * radius;
                double particleY = this.getY() + 0.1;
                
                // Partículas de tierra que se rompe
                serverLevel.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DIRT.defaultBlockState()),
                    particleX, particleY, particleZ,
                    5, // cantidad por posición
                    0.3, 0.1, 0.3, // spread X, Y, Z
                    0.2 // velocidad hacia arriba
                );
                
                // Mezclar con césped para variedad
                if (i % 3 == 0) {
                    serverLevel.sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, Blocks.GRASS_BLOCK.defaultBlockState()),
                        particleX, particleY, particleZ,
                        3,
                        0.4, 0.2, 0.4,
                        0.15
                    );
                }
            }
            
            // Partículas adicionales alrededor del área para mayor densidad
            for (int j = 0; j < 40; j++) {
                double randomAngle = this.random.nextDouble() * 2 * Math.PI;
                double randomRadius = this.random.nextDouble() * radius;
                double particleX = this.getX() + Math.cos(randomAngle) * randomRadius;
                double particleZ = this.getZ() + Math.sin(randomAngle) * randomRadius;
                double particleY = this.getY() + 0.1;
                
                // Partículas de tierra dispersas
                serverLevel.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.COARSE_DIRT.defaultBlockState()),
                    particleX, particleY, particleZ,
                    2,
                    0.5, 0.3, 0.5,
                    0.25 // velocidad más alta para efecto de "explosión"
                );
            }
            
            // Efecto central más intenso
            serverLevel.sendParticles(
                new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ROOTED_DIRT.defaultBlockState()),
                this.getX(), this.getY(), this.getZ(),
                15,
                1.0, 0.5, 1.0,
                0.3
            );
            
            // Pequeño humo de polvo para ambiente
            serverLevel.sendParticles(ParticleTypes.POOF,
                this.getX(), this.getY() + 0.5, this.getZ(),
                8,
                2.0, 0.5, 2.0,
                0.05);
        }
    }

    private void endAttack() {
        this.isAttacking = false;
        this.attackTarget = null;
        this.attackTick = 0;
        this.hitDealt = false;

        // Sync end of attack
        setAttackingSynced(false);
        setAttackTickSynced(0);
    }

    public boolean canAttack() {
        return attackCooldown <= 0 && !isAttacking;
    }

    public boolean isAttackingSynced() {
        return this.entityData.get(DATA_IS_ATTACKING);
    }

    public int getAttackTickSynced() {
        return this.entityData.get(DATA_ATTACK_TICK);
    }

    private void setAttackingSynced(boolean attacking) {
        this.entityData.set(DATA_IS_ATTACKING, attacking);
    }

    private void setAttackTickSynced(int tick) {
        this.entityData.set(DATA_ATTACK_TICK, tick);
    }

    // NeutralMob implementation
    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int angerTime) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, angerTime);
    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.persistentAngerTarget = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.addPersistentAngerSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.readPersistentAngerSaveData(this.level(), compound);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "woodguardian_controller", 5, this::predicate));
    }

    private PlayState predicate(AnimationState<WoodGuardianEntity> state) {
        boolean isAttackingSynced = isAttackingSynced();
        
        if (isAttackingSynced) {
            return state.setAndContinue(SMASH_ANIMATION);
        }
        
        if (state.isMoving()) {
            return state.setAndContinue(WALK_ANIMATION);
        }

        return state.setAndContinue(IDLE_ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
