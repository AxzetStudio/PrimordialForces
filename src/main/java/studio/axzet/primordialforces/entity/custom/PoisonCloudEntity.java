package studio.axzet.primordialforces.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import studio.axzet.primordialforces.entity.ModEntities;

import java.util.List;

public class PoisonCloudEntity extends Entity {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(PoisonCloudEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_DURATION = SynchedEntityData.defineId(PoisonCloudEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_WAIT_TIME = SynchedEntityData.defineId(PoisonCloudEntity.class, EntityDataSerializers.INT);

    private LivingEntity owner;
    private int life;
    private int damageInterval = 20; // Damage every second
    private int damageTimer = 0;

    public PoisonCloudEntity(EntityType<? extends PoisonCloudEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.life = 0;
    }

    public PoisonCloudEntity(Level level, double x, double y, double z) {
        this(ModEntities.POISON_CLOUD.get(), level);
        this.setPos(x, y, z);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    public void setRadius(float radius) {
        this.entityData.set(DATA_RADIUS, radius);
    }

    public float getRadius() {
        return this.entityData.get(DATA_RADIUS);
    }

    public void setDuration(int duration) {
        this.entityData.set(DATA_DURATION, duration);
    }

    public int getDuration() {
        return this.entityData.get(DATA_DURATION);
    }

    public void setWaitTime(int waitTime) {
        this.entityData.set(DATA_WAIT_TIME, waitTime);
    }

    public int getWaitTime() {
        return this.entityData.get(DATA_WAIT_TIME);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_RADIUS, 5.0F);
        builder.define(DATA_DURATION, 200); // 10 seconds
        builder.define(DATA_WAIT_TIME, 0);
    }

    @Override
    public void tick() {
        super.tick();

        boolean shouldDiscard = false;
        if (this.getWaitTime() > 0) {
            this.setWaitTime(this.getWaitTime() - 1);
        }

        if (this.life >= this.getDuration() + this.getWaitTime()) {
            shouldDiscard = true;
        }

        if (!shouldDiscard) {
            boolean isActive = this.life >= this.getWaitTime();
            if (isActive) {
                // Spawn particles
                if (this.level() instanceof ServerLevel serverLevel) {
                    spawnPoisonParticles(serverLevel);
                }

                // Damage entities
                damageTimer++;
                if (damageTimer >= damageInterval) {
                    damageEntitiesInArea();
                    damageTimer = 0;
                }
            }
        }

        if (shouldDiscard) {
            this.discard();
        } else {
            this.life++;
        }
    }

    private void spawnPoisonParticles(ServerLevel level) {
        float radius = this.getRadius();
        int particleCount = (int)(radius * 8);
        
        for (int i = 0; i < particleCount; i++) {
            double angle = this.random.nextDouble() * 2.0 * Math.PI;
            double distance = this.random.nextDouble() * radius;
            double particleX = this.getX() + Math.cos(angle) * distance;
            double particleZ = this.getZ() + Math.sin(angle) * distance;
            double particleY = this.getY() + this.random.nextDouble() * 1.5;

            level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    particleX, particleY, particleZ,
                    1,
                    0.1, 0.1, 0.1,
                    0.02);
            
            if (i % 2 == 0) {
                level.sendParticles(ParticleTypes.SPORE_BLOSSOM_AIR,
                        particleX, particleY + 0.3, particleZ,
                        1,
                        0.15, 0.08, 0.15,
                        0.01);
            }
            
            if (i % 4 == 0) {
                level.sendParticles(ParticleTypes.FALLING_SPORE_BLOSSOM,
                        particleX, particleY + 1.0, particleZ,
                        1,
                        0.2, 0.1, 0.2,
                        0.05);
            }
        }
        
        level.sendParticles(ParticleTypes.COMPOSTER,
                this.getX(), this.getY() + 0.5, this.getZ(),
                8,
                radius * 0.3, 0.2, radius * 0.3,
                0.05);
        
        level.sendParticles(ParticleTypes.ITEM_SLIME,
                this.getX(), this.getY() + 0.2, this.getZ(),
                3,
                radius * 0.4, 0.1, radius * 0.4,
                0.01);
    }

    private void damageEntitiesInArea() {
        if (!this.level().isClientSide) {
            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class,
                    this.getBoundingBox().inflate(this.getRadius()));

            for (LivingEntity entity : entities) {
                if (entity == this.owner) continue; // Don't damage the owner

                double distance = this.distanceTo(entity);
                if (distance <= this.getRadius()) {
                    // Apply poison effect
                    entity.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 2)); // 3 seconds poison level 2

                    // Apply damage
                    float damage = 2.0F; // Base damage
                    entity.hurt(this.damageSources().magic(), damage);

                    if (this.random.nextInt(40) == 0) {
                        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                                SoundEvents.GRASS_BREAK, this.getSoundSource(), 0.3F, 0.8F);
                    }
                }
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.life = compound.getInt("Life");
        this.setRadius(compound.getFloat("Radius"));
        this.setDuration(compound.getInt("Duration"));
        this.setWaitTime(compound.getInt("WaitTime"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Life", this.life);
        compound.putFloat("Radius", this.getRadius());
        compound.putInt("Duration", this.getDuration());
        compound.putInt("WaitTime", this.getWaitTime());
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource damageSource, float amount) {
        return false;
    }
}