package studio.axzet.primordialforces.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class EntWarriorEntity extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation ATTACK_ANIMATION = RawAnimation.begin().then("double_slash", Animation.LoopType.PLAY_ONCE);

    // Sincronización de datos entre servidor y cliente
    private static final EntityDataAccessor<Boolean> DATA_IS_ATTACKING = SynchedEntityData.defineId(EntWarriorEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TICK = SynchedEntityData.defineId(EntWarriorEntity.class, EntityDataSerializers.INT);

    // Attack variables (solo en servidor)
    private int attackTick = 0;
    private boolean isAttacking = false;
    private LivingEntity attackTarget = null;
    private boolean firstHitDealt = false;
    private boolean secondHitDealt = false;
    private int attackCooldown = 0;

    public EntWarriorEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_ATTACKING, false);
        builder.define(DATA_ATTACK_TICK, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.ATTACK_DAMAGE, 6.0f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.ARMOR, 3.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                ;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntWarriorAttackGoal(this, 1.2, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this,  Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        // Reducir cooldown de ataque
        if (attackCooldown > 0) {
            attackCooldown--;
        }

        if (isAttacking) {
            attackTick++;

            // Sincronizar datos con el cliente
            setAttackingSynced(true);
            setAttackTickSynced(attackTick);
            
            // Debug del servidor
            if (attackTick % 20 == 0) {
                System.out.println("EntWarrior SERVIDOR - Enviando datos: isAttacking=true, tick=" + attackTick);
            }

            // Primer ataque en el segundo 1 (tick 20)
            if(attackTick == 20 && !firstHitDealt && attackTarget != null) {
                performAttack(attackTarget);
                firstHitDealt = true;
                System.out.println("EntWarrior - Primer ataque ejecutado en tick " + attackTick);
            }

            // Segundo ataque en el segundo 2 (tick 40)
            if (attackTick == 40 && !secondHitDealt && attackTarget != null) {
                performAttack(attackTarget);
                secondHitDealt = true;
                System.out.println("EntWarrior - Segundo ataque ejecutado en tick " + attackTick);
            }

            // Terminar ataque después de 3.2 segundos (64 ticks)
            if (attackTick >= 64) {
                endAttack();
            }
        }
    }

    private void startAttack(LivingEntity target) {
        if (!isAttacking && attackCooldown <= 0) {
            this.isAttacking = true;
            this.attackTarget = target;
            this.attackTick = 0;
            this.firstHitDealt = false;
            this.secondHitDealt = false;
            this.attackCooldown = 80; // 4 segundos de cooldown

            // Sincronizar inmediatamente
            setAttackingSynced(true);
            setAttackTickSynced(0);

            this.getNavigation().stop();

            System.out.println("EntWarrior iniciando ataque contra: " + target.getName().getString());
            System.out.println("EntWarrior - Estado de animación: isAttacking=" + isAttacking);
            System.out.println("EntWarrior SERVIDOR - Iniciando sincronización: isAttacking=true, tick=0");
        }
    }

    private void performAttack(LivingEntity target) {
        if (target != null && this.distanceToSqr(target) <= 16.0) { // Rango de ataque de 4 bloques
            float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
            target.hurt(this.damageSources().mobAttack(this), damage);

            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, this.getSoundSource(), 1.0f, 1.0f);

            System.out.println("EntWarrior golpeando por " + damage + " de daño a " + target.getName().getString() + 
                             " (distancia: " + String.format("%.2f", Math.sqrt(this.distanceToSqr(target))) + " bloques)");
        } else {
            System.out.println("EntWarrior no pudo atacar - Target: " + (target != null ? target.getName().getString() : "null") + 
                             ", Distancia: " + (target != null ? String.format("%.2f", Math.sqrt(this.distanceToSqr(target))) : "N/A") + " bloques");
        }
    }

    private void endAttack() {
        this.isAttacking = false;
        this.attackTarget = null;
        this.attackTick = 0;
        this.firstHitDealt = false;
        this.secondHitDealt = false;

        // Sincronizar fin del ataque
        setAttackingSynced(false);
        setAttackTickSynced(0);

        System.out.println("EntWarrior terminando ataque");
        System.out.println("EntWarrior SERVIDOR - Finalizando sincronización: isAttacking=false, tick=0");
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean canAttack() {
        return attackCooldown <= 0 && !isAttacking;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // Controlador simple para diagnosticar
        controllers.add(new AnimationController<>(this, "ent_warrior_controller", 5, this::predicate));
    }

    private PlayState predicate(AnimationState<EntWarriorEntity> state) {
        // Debug siempre para ver si se ejecuta el controlador
        System.out.println("EntWarrior - Controlador ejecutándose");
        
        // Usar datos sincronizados del servidor
        boolean isAttackingSynced = isAttackingSynced();
        int attackTickSynced = getAttackTickSynced();
        
        System.out.println("EntWarrior - Datos sincronizados - isAttacking: " + isAttackingSynced + ", tick: " + attackTickSynced);
        
        if (isAttackingSynced) {
            System.out.println("EntWarrior - Reproduciendo animación de ataque en tick " + attackTickSynced);
            return state.setAndContinue(ATTACK_ANIMATION);
        }
        
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

    @Override
    public boolean isAggressive() {
        return this.getTarget() != null;
    }

    public static class EntWarriorAttackGoal extends Goal {
        private final EntWarriorEntity warrior;
        private int attackTime = 0;
        private final double speedModifier;
        private final boolean followingTargetEvenIfNotSeen;
        private int seeTime;
        private int attackTimeStart = 20;
        private int attackInterval = 60;
        private long lastCanUseCheck;
        private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;

        public EntWarriorAttackGoal(EntWarriorEntity warrior, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            this.warrior = warrior;
            this.speedModifier = speedModifier;
            this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
        }

        @Override
        public boolean canUse() {
            long gameTime = this.warrior.level().getGameTime();
            if (gameTime - this.lastCanUseCheck < COOLDOWN_BETWEEN_CAN_USE_CHECKS) {
                return false;
            } else {
                this.lastCanUseCheck = gameTime;
                LivingEntity target = this.warrior.getTarget();
                if (target == null) {
                    return false;
                } else if (!target.isAlive()) {
                    return false;
                } else {
                    return this.followingTargetEvenIfNotSeen || this.warrior.getSensing().hasLineOfSight(target);
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = this.warrior.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else if (!this.followingTargetEvenIfNotSeen && !this.warrior.getSensing().hasLineOfSight(target)) {
                return false;
            } else {
                return !(target instanceof Player) || !target.isSpectator() && !((Player)target).isCreative();
            }
        }

        @Override
        public void start() {
            this.warrior.setAggressive(true);
            this.attackTime = 0;
        }

        @Override
        public void stop() {
            this.warrior.setAggressive(false);
            this.warrior.getNavigation().stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.warrior.getTarget();
            if (target == null) {
                return;
            }

            double distanceToTargetSqr = this.warrior.distanceToSqr(target.getX(), target.getY(), target.getZ());
            boolean hasLineOfSight = this.warrior.getSensing().hasLineOfSight(target);
            boolean seeTimeIncreased = false;

            if (hasLineOfSight != this.seeTime > 0) {
                this.seeTime = 0;
            }

            if (hasLineOfSight) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (distanceToTargetSqr <= 16.0 && this.seeTime >= 20) {
                this.warrior.getNavigation().stop();
                ++this.attackTime;
            } else {
                this.warrior.getNavigation().moveTo(target, this.speedModifier);
                this.attackTime = 0;
            }

            if (this.attackTime >= this.attackTimeStart && this.warrior.canAttack()) {
                this.warrior.startAttack(target);
                this.attackTime = 0;
            }

            // Debug info
            if (this.attackTime % 20 == 0) {
                System.out.println("EntWarrior - Distancia: " + Math.sqrt(distanceToTargetSqr) + 
                                 ", Viendo: " + hasLineOfSight + 
                                 ", Atacando: " + this.warrior.isAttacking() + 
                                 ", Puede atacar: " + this.warrior.canAttack());
            }
        }
    }

    // Métodos para sincronizar datos
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
}
