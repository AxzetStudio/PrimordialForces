package studio.axzet.primordialforces.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import studio.axzet.primordialforces.entity.custom.EntBruteEntity;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class EntBruteAttackGoal extends Goal {
    private final EntBruteEntity brute;
    private int attackTime = 0;
    private final double speedModifier;
    private final boolean followingTargetEvenIfNotSeen;
    private int seeTime;
    private final int attackTimeStart = 20;
    private long lastCanUseCheck;
    private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;

    public EntBruteAttackGoal(EntBruteEntity mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        this.brute = mob;
        this.speedModifier = speedModifier;
        this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
    }

    @Override
    public boolean canUse() {
        long gameTime = this.brute.level().getGameTime();
        if (gameTime - this.lastCanUseCheck < COOLDOWN_BETWEEN_CAN_USE_CHECKS) {
            return false;
        } else {
            this.lastCanUseCheck = gameTime;
            LivingEntity target = this.brute.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else {
                return this.followingTargetEvenIfNotSeen || this.brute.getSensing().hasLineOfSight(target);
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.brute.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if (!this.followingTargetEvenIfNotSeen && !this.brute.getSensing().hasLineOfSight(target)) {
            return false;
        } else {
            return !(target instanceof Player) || !target.isSpectator() && !((Player)target).isCreative();
        }
    }

    @Override
    public void start() {
        this.brute.setAggressive(true);
        this.attackTime = 0;
    }

    @Override
    public void stop() {
        this.brute.setAggressive(false);
        this.brute.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.brute.getTarget();
        if (target == null) {
            return;
        }

        if (this.brute.isAttacking()) {
            this.brute.getNavigation().stop();
            return;
        }

        double distanceToTargetSqr = this.brute.distanceToSqr(target.getX(), target.getY(), target.getZ());
        boolean hasLineOfSight = this.brute.getSensing().hasLineOfSight(target);

        if (this.brute.tickCount % 40 == 0) {
            System.out.println("EntBrute Goal - Target: " + target.getName().getString() + 
                             ", Distancia: " + String.format("%.2f", Math.sqrt(distanceToTargetSqr)) +
                             ", LineOfSight: " + hasLineOfSight + 
                             ", seeTime: " + seeTime + 
                             ", attackTime: " + attackTime +
                             ", canAttack: " + this.brute.canAttack());
        }

        if (hasLineOfSight != seeTime > 0) {
            this.seeTime = 0;
        }

        if (hasLineOfSight) {
            ++this.seeTime;
        } else {
            --this.seeTime;
        }

        if (distanceToTargetSqr <= 16 && this.seeTime >= 10) {
            this.brute.getNavigation().stop();
            ++this.attackTime;
        } else {
            this.brute.getNavigation().moveTo(target, this.speedModifier);
            this.attackTime = 0;
        }

        if (attackTime >= this.attackTimeStart && this.brute.canAttack() && !this.brute.isAttacking()) {
            System.out.println("EntBrute Goal - Iniciando ataque contra: " + target.getName().getString());
            this.brute.startAttack(target);
            this.attackTime = 0;
        }
    }
}
