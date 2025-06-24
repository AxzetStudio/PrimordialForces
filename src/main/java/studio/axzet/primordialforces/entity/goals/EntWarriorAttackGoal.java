package studio.axzet.primordialforces.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import studio.axzet.primordialforces.entity.custom.EntWarriorEntity;

public class EntWarriorAttackGoal extends Goal {
    private final EntWarriorEntity warrior;
    private int attackTime = 0;
    private final double speedModifier;
    private final boolean followingTargetEvenIfNotSeen;
    private int seeTime;
    private int attackTimeStart = 20;
    private int attackInterval = 60;
    private long lastCanUseCheck;
    private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;

    public EntWarriorAttackGoal(EntWarriorEntity mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        this.warrior = mob;
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
            /*if (this.attackTime % 20 == 0) {
                System.out.println("EntWarrior - Distancia: " + Math.sqrt(distanceToTargetSqr) +
                                 ", Viendo: " + hasLineOfSight +
                                 ", Atacando: " + this.warrior.isAttacking() +
                                 ", Puede atacar: " + this.warrior.canAttack());
            }*/
    }
}