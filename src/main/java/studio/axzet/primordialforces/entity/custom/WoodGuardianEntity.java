package studio.axzet.primordialforces.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import studio.axzet.primordialforces.entity.goals.WoodGuardianAttackGoal;
import net.minecraft.world.level.block.Blocks;

import java.util.UUID;

public class WoodGuardianEntity extends Monster implements GeoEntity {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.woodguardian.idle");
    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("animation.woodguardian.walk");
    private static final RawAnimation SMASH_ANIMATION = RawAnimation.begin().thenPlay("animation.woodguardian.earthsmash");
    private static final RawAnimation POISON_ANIMATION = RawAnimation.begin().thenPlay("animation.woodguardian.poison");

    // Data synchronization
    private static final EntityDataAccessor<Boolean> DATA_IS_ATTACKING = SynchedEntityData.defineId(WoodGuardianEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TICK = SynchedEntityData.defineId(WoodGuardianEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(WoodGuardianEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TYPE = SynchedEntityData.defineId(WoodGuardianEntity.class, EntityDataSerializers.INT); // 0 = earthsmash, 1 = poison

    // Attack variables
    private int attackTick = 0;
    private boolean isAttacking = false;
    private LivingEntity attackTarget = null;
    private boolean hitDealt = false;
    private int attackCooldown = 0;
    private int attackType = 0; // 0 = earthsmash, 1 = poison

    // Earth Smash
    private final double earthSmashRadius = 9.0;

    // Poison Cloud
    private final float poisonCloudRadius = 9.0f;

    // Anger variables for NeutralMob
    private UUID persistentAngerTarget;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    public WoodGuardianEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_ATTACKING, false);
        builder.define(DATA_ATTACK_TICK, 0);
        builder.define(DATA_REMAINING_ANGER_TIME, 0);
        builder.define(DATA_ATTACK_TYPE, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.MAX_HEALTH, 450)
                .add(Attributes.ATTACK_DAMAGE, 20.0f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.ARMOR, 20.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.25f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2.0f)
                ;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new WoodGuardianAttackGoal(this, 1.5f, false));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 1.0f, 64.0f));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 32.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
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

            if (attackType == 0) { // Earthsmash attack
                // Deal damage at 1 second (20 ticks)
                if (attackTick == 20 && !hitDealt && attackTarget != null) {
                    performAreaAttack();
                    hitDealt = true;
                }
                // End attack after 3 seconds (60 ticks)
                if (attackTick >= 60) {
                    endAttack();
                }
            } else if (attackType == 1) { // Poison attack
                // Spawn poison cloud at 2.5 seconds (50 ticks)
                if (attackTick == 50 && !hitDealt && attackTarget != null) {
                    spawnPoisonCloud();
                    hitDealt = true;
                }
                // End attack after 3 seconds (60 ticks)
                if (attackTick >= 60) {
                    endAttack();
                }
            }
        }
    }

    public void startAttack(LivingEntity target) {
        if (!isAttacking && attackCooldown <= 0) {
            this.isAttacking = true;
            this.attackTarget = target;
            this.attackTick = 0;
            this.hitDealt = false;
            this.attackCooldown = 100; // 5 seconds cooldown

            // Randomly choose attack type
            this.attackType = this.random.nextInt(2); // 0 or 1
            
            // Sync immediately
            setAttackingSynced(true);
            setAttackTickSynced(0);
            setAttackTypeSynced(this.attackType);

            this.getNavigation().stop();
        }
    }

    private void spawnPoisonCloud() {
        if (!this.level().isClientSide) {
            ThornsFieldEntity poisonCloud = new ThornsFieldEntity(this.level(),
                    this.getX(),
                    this.getY(),
                    this.getZ());
            
            poisonCloud.setOwner(this);
            poisonCloud.setRadius(poisonCloudRadius);
            poisonCloud.setDuration(200); // 10 seconds duration
            poisonCloud.setWaitTime(0); // No wait time
            
            this.level().addFreshEntity(poisonCloud);
            
            // Play sound
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), 
                                 SoundEvents.GRASS_HIT, this.getSoundSource(), 1.0f, 0.8f);
        }
    }

    public void performAreaAttack() {
        double radius = earthSmashRadius;

        spawnAttackParticles();

        this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(radius))
                .forEach(player -> {
                    if (player != null && this.distanceToSqr(player) <= (radius * radius)) {
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
            double radius = earthSmashRadius;
            int particleCount = 80;

            for (int i = 0; i < particleCount; i++) {
                double angle = (2.0 * Math.PI * i) / particleCount;
                double particleX = this.getX() + Math.cos(angle) * radius;
                double particleZ = this.getZ() + Math.sin(angle) * radius;
                double particleY = this.getY() + 0.1;

                serverLevel.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DIRT.defaultBlockState()),
                    particleX, particleY, particleZ,
                    5,
                    0.3, 0.1, 0.3,
                    0.2
                );

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

            for (int j = 0; j < 40; j++) {
                double randomAngle = this.random.nextDouble() * 2 * Math.PI;
                double randomRadius = this.random.nextDouble() * radius;
                double particleX = this.getX() + Math.cos(randomAngle) * randomRadius;
                double particleZ = this.getZ() + Math.sin(randomAngle) * randomRadius;
                double particleY = this.getY() + 0.1;

                serverLevel.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.COARSE_DIRT.defaultBlockState()),
                    particleX, particleY, particleZ,
                    2,
                    0.5, 0.3, 0.5,
                    0.25
                );
            }
            
            serverLevel.sendParticles(
                new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ROOTED_DIRT.defaultBlockState()),
                this.getX(), this.getY(), this.getZ(),
                15,
                1.0, 0.5, 1.0,
                0.3
            );
            
            serverLevel.sendParticles(ParticleTypes.POOF,
                this.getX(), this.getY() + 0.5, this.getZ(),
                8,
                2.0, 0.5, 2.0,
                0.05
            );
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

    public int getAttackTypeSynced() {
        return this.entityData.get(DATA_ATTACK_TYPE);
    }

    private void setAttackTypeSynced(int type) {
        this.entityData.set(DATA_ATTACK_TYPE, type);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "woodguardian_controller", 5, this::predicate));
    }

    private PlayState predicate(AnimationState<WoodGuardianEntity> state) {
        boolean isAttackingSynced = isAttackingSynced();
        int attackTypeSynced = getAttackTypeSynced();
        
        if (isAttackingSynced) {
            if (attackTypeSynced == 0) {
                return state.setAndContinue(SMASH_ANIMATION);
            } else {
                return state.setAndContinue(POISON_ANIMATION);
            }
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
