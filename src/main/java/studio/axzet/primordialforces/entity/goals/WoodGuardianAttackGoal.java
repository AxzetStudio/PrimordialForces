package studio.axzet.primordialforces.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import studio.axzet.primordialforces.entity.custom.WoodGuardianEntity;

public class WoodGuardianAttackGoal extends Goal {
    private final WoodGuardianEntity guardian;
    private int attackTime = 0;
    private final double speedModifier;
    private final boolean followingTargetEvenIfNotSeen;
    private int seeTime;
    private final int attackTimeStart = 20; // 1 second before attacking
    private long lastCanUseCheck;
    private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;

    public WoodGuardianAttackGoal(WoodGuardianEntity guardian, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        this.guardian = guardian;
        this.speedModifier = speedModifier;
        this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
        this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        long gameTime = this.guardian.level().getGameTime();
        if (gameTime - this.lastCanUseCheck < COOLDOWN_BETWEEN_CAN_USE_CHECKS) {
            return false;
        } else {
            this.lastCanUseCheck = gameTime;
            LivingEntity target = this.guardian.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else {
                return this.followingTargetEvenIfNotSeen || this.guardian.getSensing().hasLineOfSight(target);
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.guardian.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if (!this.followingTargetEvenIfNotSeen && !this.guardian.getSensing().hasLineOfSight(target)) {
            return false;
        } else {
            return !(target instanceof Player) || !target.isSpectator() && !((Player)target).isCreative();
        }
    }

    @Override
    public void start() {
        this.guardian.setAggressive(true);
        this.attackTime = 0;
    }

    @Override
    public void stop() {
        this.guardian.setAggressive(false);
        this.guardian.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.guardian.getTarget();
        if (target == null) {
            return;
        }

        if (this.guardian.isAttackingSynced()) {
            this.guardian.getNavigation().stop();
            return;
        }

        double distanceToTargetSqr = this.guardian.distanceToSqr(target.getX(), target.getY(), target.getZ());
        boolean hasLineOfSight = this.guardian.getSensing().hasLineOfSight(target);

        if (hasLineOfSight != seeTime > 0) {
            this.seeTime = 0;
        }

        if (hasLineOfSight) {
            ++this.seeTime;
        } else {
            --this.seeTime;
        }

        // Attack when close enough (6 blocks range)
        if (distanceToTargetSqr <= 36 && this.seeTime >= 10) {
            this.guardian.getNavigation().stop();
            ++this.attackTime;
        } else {
            this.guardian.getNavigation().moveTo(target, this.speedModifier);
            this.attackTime = 0;
        }

        if (attackTime >= this.attackTimeStart && this.guardian.canAttack() && !this.guardian.isAttackingSynced()) {
            this.guardian.startAttack(target);
            this.attackTime = 0;
        }
    }
}