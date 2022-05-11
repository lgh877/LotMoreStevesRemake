/**
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.lotmorestevesremake as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.lotmorestevesremake;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.Difficulty;
import net.minecraft.entity.ai.goal.InteractDoorGoal;
import net.minecraft.entity.MobEntity;
import net.minecraft.block.Block;

import java.util.function.Predicate;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CustomDoorBreakGoal extends InteractDoorGoal {
	private final Predicate<Difficulty> difficultyPredicate;
	protected int breakingTime;
	protected int previousBreakProgress = -1;
	protected int timeToBreak = -1;

	public CustomDoorBreakGoal(MobEntity entity, Predicate<Difficulty> difficultyPredicate) {
		super(entity);
		this.difficultyPredicate = difficultyPredicate;
	}

	public CustomDoorBreakGoal(MobEntity entity, int timeToBreak, Predicate<Difficulty> difficultyPredicate) {
		this(entity, difficultyPredicate);
		this.timeToBreak = timeToBreak;
	}

	protected int func_220697_f() {
		return Math.max(240, this.timeToBreak);
	}

	/**
	* Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	* method as well.
	*/
	public boolean shouldExecute() {
		if (!super.shouldExecute()) {
			return false;
		} else if (!net.minecraftforge.common.ForgeHooks.canEntityDestroy(this.entity.world, this.doorPosition, this.entity)) {
			return false;
		} else {
			return this.func_220696_a(this.entity.world.getDifficulty()) && !this.canDestroy();
		}
	}

	/**
	* Execute a one shot task or start executing a continuous task
	*/
	public void startExecuting() {
		super.startExecuting();
		this.breakingTime = 0;
	}

	/**
	* Returns whether an in-progress EntityAIBase should continue executing
	*/
	public boolean shouldContinueExecuting() {
		return this.breakingTime <= this.func_220697_f() && !this.canDestroy() && this.doorPosition.withinDistance(this.entity.getPositionVec(), 4.0D)
				&& this.func_220696_a(this.entity.world.getDifficulty());
	}

	/**
	* Reset the task's internal state. Called when this task is interrupted by another one
	*/
	public void resetTask() {
		super.resetTask();
		this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.doorPosition, -1);
	}

	public void swingArmSettings() {
		this.entity.swingArm(this.entity.getActiveHand());
	}

	/**
	* Keep ticking a continuous task that has already been started
	*/
	public void tick() {
		super.tick();
		if (this.entity.getRNG().nextInt(20) == 0) {
			this.entity.world.playEvent(1019, this.doorPosition, 0);
			if (!this.entity.isSwingInProgress) {
				this.swingArmSettings();
			}
		}
		++this.breakingTime;
		int i = (int) ((float) this.breakingTime / (float) this.func_220697_f() * 10.0F);
		if (i != this.previousBreakProgress) {
			this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.doorPosition, i);
			this.previousBreakProgress = i;
		}
		if (this.breakingTime == this.func_220697_f() && this.func_220696_a(this.entity.world.getDifficulty())) {
			this.entity.world.removeBlock(this.doorPosition, false);
			this.entity.world.playEvent(1021, this.doorPosition, 0);
			this.entity.world.playEvent(2001, this.doorPosition, Block.getStateId(this.entity.world.getBlockState(this.doorPosition)));
		}
	}

	private boolean func_220696_a(Difficulty difficulty) {
		return this.difficultyPredicate.test(difficulty);
	}
}
