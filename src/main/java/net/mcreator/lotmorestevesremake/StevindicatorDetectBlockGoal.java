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

import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.World;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.tags.BlockTags;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.CreatureEntity;

import javax.annotation.Nullable;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class StevindicatorDetectBlockGoal extends MoveToBlockGoal {
	private final MobEntity entity;
	private int breakingTime;

	public StevindicatorDetectBlockGoal(CreatureEntity creature, double speed, int yMax) {
		super(creature, speed, 64, yMax);
		this.entity = creature;
	}

	/**
	* Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	* method as well.
	*/
	public boolean shouldExecute() {
		if (!net.minecraftforge.common.ForgeHooks.canEntityDestroy(this.entity.world, this.destinationBlock, this.entity)
				|| this.entity.getAttackTarget() != null) {
			return false;
		} else if (this.runDelay > 0) {
			--this.runDelay;
			return false;
		} else if (this.func_220729_m()) {
			this.runDelay = 20;
			return true;
		} else {
			this.runDelay = this.getRunDelay(this.creature);
			return false;
		}
	}

	private boolean func_220729_m() {
		return this.destinationBlock != null && this.shouldMoveTo(this.creature.world, this.destinationBlock) ? true : this.searchForDestination();
	}

	/**
	* Reset the task's internal state. Called when this task is interrupted by another one
	*/
	public void resetTask() {
		super.resetTask();
		this.entity.fallDistance = 1.0F;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() || this.entity.getAttackTarget() != null;
	}

	/**
	* Execute a one shot task or start executing a continuous task
	*/
	public void startExecuting() {
		super.startExecuting();
		this.breakingTime = 0;
	}

	public void playBreakingSound(IWorld worldIn, BlockPos pos) {
	}

	public void playBrokenSound(World worldIn, BlockPos pos) {
	}

	/**
	* Keep ticking a continuous task that has already been started
	*/
	public void tick() {
		super.tick();
		World world = this.entity.world;
		BlockPos blockpos = this.entity.getPosition();
		BlockPos blockpos1 = this.findTarget(blockpos, world);
		Random random = this.entity.getRNG();
		if (blockpos1 != null) {
			if (this.getIsAboveDestination()) {
				if (!this.entity.isSwingInProgress)
					this.entity.swingArm(Hand.MAIN_HAND);
				else if (this.entity.swingProgress > 0.4f && !this.entity.world.isRemote() && random.nextInt(4) == 0)
					world.destroyBlock(blockpos1, true, this.entity);
				return;
			}
		}
	}

	@Nullable
	private BlockPos findTarget(BlockPos pos, IBlockReader worldIn) {
		if (BlockTags.getCollection().getTagByID(new ResourceLocation("lotmorestevesremake:house_related_blocks"))
				.contains(worldIn.getBlockState(pos).getBlock())) {
			return pos;
		} else {
			BlockPos[] ablockpos = new BlockPos[]{pos.down(), pos.west(), pos.east(), pos.north(), pos.south(), pos.down().down()};
			for (BlockPos blockpos : ablockpos) {
				if (BlockTags.getCollection().getTagByID(new ResourceLocation("lotmorestevesremake:house_related_blocks"))
						.contains(worldIn.getBlockState(pos).getBlock())) {
					return blockpos;
				}
			}
			return null;
		}
	}

	/**
	 * protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
		IChunk ichunk = worldIn.getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.FULL, false);
		if (ichunk == null) {
			return false;
		} else {
			return ichunk.getBlockState(pos).isIn(this.block) && ichunk.getBlockState(pos.up()).isAir() && ichunk.getBlockState(pos.up(2)).isAir();
		}
	}
	* Return true to set given position as destination
	*/
	protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
		IChunk ichunk = worldIn.getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.FULL, false);
		if (ichunk == null) {
			return false;
		} else {
			return BlockTags.getCollection().getTagByID(new ResourceLocation("lotmorestevesremake:house_related_blocks")).contains(
					ichunk.getBlockState(pos).getBlock()) && ichunk.getBlockState(pos.up()).isAir() && ichunk.getBlockState(pos.up(2)).isAir();
		}
	}
}
