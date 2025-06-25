package studio.axzet.primordialforces.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
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
import studio.axzet.primordialforces.entity.goals.EntBruteAttackGoal;

public class EntBruteEntity extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    // Animations
    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation ATTACK_ANIMATION = RawAnimation.begin().then("smash", Animation.LoopType.PLAY_ONCE);

    // Data Sync
    private static final EntityDataAccessor<Boolean> IS_ATTACKING_DATA = SynchedEntityData.defineId(EntBruteEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TICK = SynchedEntityData.defineId(EntBruteEntity.class, EntityDataSerializers.INT);

    // Attack Variables
    private int attackTick = 0;
    private boolean isAttacking = false;
    private LivingEntity attackTarget = null;
    private int attackCooldown = 0;
    private boolean attackDealt = false;

    public EntBruteEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_ATTACKING_DATA, false);
        builder.define(DATA_ATTACK_TICK, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 10.0)
                .add(Attributes.MAX_HEALTH, 75)
                .add(Attributes.ATTACK_DAMAGE, 12.0f)
                .add(Attributes.ATTACK_SPEED, 0.1f)
                .add(Attributes.ARMOR, 6.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.23f)
                ;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntBruteAttackGoal(this, 1, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 12.0f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this,  Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        
        // Debug: verificar si tiene target
        /*
        if (this.tickCount % 60 == 0) { // Cada 3 segundos
            LivingEntity target = this.getTarget();
            if (target != null) {
                System.out.println("EntBrute - Target: " + target.getName().getString() + 
                                 ", Distancia: " + String.format("%.2f", Math.sqrt(this.distanceToSqr(target))) +
                                 ", isAttacking: " + isAttacking + ", attackTick: " + attackTick);
            } else {
                System.out.println("EntBrute - Sin target, isAttacking: " + isAttacking + ", attackTick: " + attackTick);
            }
        }
         */
        
        // Reducir cooldown de ataque
        if (attackCooldown > 0) {
            attackCooldown--;
        }

        if (isAttacking) {
            attackTick++;

            // Mantener quieto durante el ataque
            this.getNavigation().stop();
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);

            // Sincronizar datos con el cliente
            setAttackingSynced(true);
            setAttackTickSynced(attackTick);

            // Realizar ataque en área en el tick 60 (3 segundos)
            if (attackTick == 60 && !attackDealt) {
                //System.out.println("EntBrute - Ejecutando ataque en área en tick " + attackTick);
                performAreaAttack();
                attackDealt = true;
            }

            // Terminar ataque después de 5 segundos (100 ticks)
            if (attackTick >= 100) {
                endAttack();
            }
        } else {
            // Solo resetear los datos sincronizados si realmente no está atacando
            if (attackTick > 0) {
                setAttackingSynced(false);
                setAttackTickSynced(0);
                attackTick = 0;
            }
        }
    }

    public void startAttack(LivingEntity target) {
        if (!isAttacking && attackCooldown <= 0) {
            //System.out.println("EntBrute - Iniciando ataque real contra: " + target.getName().getString());
            this.isAttacking = true;
            this.attackTarget = target;
            this.attackTick = 0;
            this.attackDealt = false;
            this.attackCooldown = 120; // 6 segundos de cooldown

            // Sincronizar inmediatamente
            setAttackingSynced(true);
            setAttackTickSynced(0);

            // Detener completamente el movimiento
            this.getNavigation().stop();
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0); // Detener movimiento horizontal
        } /*else {
            System.out.println("EntBrute - No puede iniciar ataque - isAttacking: " + isAttacking + ", cooldown: " + attackCooldown);
        }*/
    }

    public void performAreaAttack() {
        //System.out.println("EntBrute - Realizando ataque en área");
        double radius = 5.0;
        int playersHit = 0;
        this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(radius))
                .forEach(player -> {
                    if (player != null && this.distanceToSqr(player) <= 15.0) { // Radio de 5 bloques
                        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                        player.hurt(this.damageSources().mobAttack(this), damage);
                        //System.out.println("EntBrute - Golpeando a " + player.getName().getString() + " por " + damage + " de daño");
                    }
                });
        //System.out.println("EntBrute - Ataque en área completado");
    }

    private void endAttack() {
        //System.out.println("EntBrute - Terminando ataque - attackTick: " + attackTick);
        this.isAttacking = false;
        this.attackTarget = null;
        this.attackTick = 0;
        this.attackDealt = false;

        setAttackingSynced(false);
        setAttackTickSynced(0);
    }

    public boolean canAttack() {
        return attackCooldown <= 0 && !isAttacking;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "ent_brute_controller", 5, this::predicate));
    }

    private PlayState predicate(AnimationState<EntBruteEntity> state) {
        // Usar datos sincronizados del servidor
        boolean isAttackingSynced = isAttackingSynced();
        int attackTickSynced = getAttackTickSynced();

        if (isAttackingSynced) {
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

    // Sync Methods
    public boolean isAttackingSynced() {
        return this.entityData.get(IS_ATTACKING_DATA);
    }

    public int getAttackTickSynced() {
        return this.entityData.get(DATA_ATTACK_TICK);
    }

    private void setAttackingSynced(boolean attacking) {
        this.entityData.set(IS_ATTACKING_DATA, attacking);
    }

    private void setAttackTickSynced(int tick) {
        this.entityData.set(DATA_ATTACK_TICK, tick);
    }
}
