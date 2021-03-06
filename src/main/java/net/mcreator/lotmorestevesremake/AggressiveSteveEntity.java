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

import net.minecraft.world.World;
import net.minecraft.world.IServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.GroundPathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.potion.EffectInstance;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Entity;

import net.mcreator.lotmorestevesremake.world.CanCauseInfectionGameRule;
import net.mcreator.lotmorestevesremake.potion.CursedDiversionPotionEffect;
import net.mcreator.lotmorestevesremake.entity.StevagerEntity;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AggressiveSteveEntity extends MonsterEntity {
	public final SwimmerPathNavigator waterNavigator;
	public final GroundPathNavigator groundNavigator;

	public AggressiveSteveEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
		super(type, worldIn);
		setNoAI(false);
		this.waterNavigator = new SwimmerPathNavigator(this, worldIn);
		this.groundNavigator = new GroundPathNavigator(this, worldIn);
	}

	@Override
	public boolean isOnSameTeam(Entity entityIn) {
		if (this.getTeam() != null)
			return super.isOnSameTeam(entityIn);
		else if (EntityTypeTags.getCollection().getTagByID(new ResourceLocation("lotmorestevesremake:we_are_steves")).contains(entityIn.getType()))
			return true;
		else
			return false;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		if (this.canDestroyBuildings()) {
			this.goalSelector.addGoal(6, new CustomDoorBreakGoal(this, e -> true));
			this.goalSelector.addGoal(6, new StevindicatorDetectBlockGoal(this, 1, (int) 10));
		}
		this.goalSelector.addGoal(6, new SwimGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp());
		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, PlayerEntity.class, false, false));
		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, ServerPlayerEntity.class, false, false));
		this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, MobEntity.class, false, false));
	}

	protected boolean canDestroyBuildings() {
		return true;
	}

	public void livingTick() {
		super.livingTick();
		if (GroundPathHelper.isGroundNavigator(this))
			((GroundPathNavigator) this.getNavigator()).setBreakDoors(true);
	}

	protected void collideWithEntity(Entity entityIn) {
		if (entityIn instanceof MobEntity && !this.isOnSameTeam(entityIn) && this.getRNG().nextInt(5) == 0 && !entityIn.isInvulnerable()) {
			this.setAttackTarget((LivingEntity) entityIn);
		}
		super.collideWithEntity(entityIn);
	}

	public static boolean whenToSpawn(IServerWorld worldIn) {
		return true;
	}

	public static boolean whereToSpawn(IServerWorld worldIn) {
		return (World.OVERWORLD) == (worldIn instanceof World ? (((World) worldIn).getDimensionKey()) : World.OVERWORLD);
	}

	public static boolean rarityForSpawn() {
		return true;
	}

	public static boolean customSpawningConditionInLight(EntityType<? extends MonsterEntity> type, IServerWorld worldIn, SpawnReason reason,
			BlockPos pos, Random randomIn) {
		return canMonsterSpawnInLight(type, worldIn, reason, pos, randomIn) && whereToSpawn(worldIn) && whenToSpawn(worldIn) && rarityForSpawn();
	}

	public static boolean customSpawningCondition(EntityType<? extends MonsterEntity> type, IServerWorld worldIn, SpawnReason reason, BlockPos pos,
			Random randomIn) {
		return canMonsterSpawn(type, worldIn, reason, pos, randomIn) && whereToSpawn(worldIn) && whenToSpawn(worldIn) && rarityForSpawn();
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float amount) {
		if (damagesource.getTrueSource() instanceof LivingEntity && this.isOnSameTeam(damagesource.getTrueSource()))
			return false;
		else if (damagesource == DamageSource.DROWN)
			this.setAir(60);
		if (this.getRidingEntity() != null) {
			if (this.getRidingEntity() instanceof StevagerEntity.CustomEntity) {
				this.getRidingEntity().attackEntityFrom(damagesource, amount);
				return false;
			}
		}
		return super.attackEntityFrom(damagesource, amount);
	}

	public boolean attackEntityAsMob(Entity entityIn) {
		if (world.getWorldInfo().getGameRulesInstance().getBoolean(CanCauseInfectionGameRule.gamerule))
			((LivingEntity) entityIn).addPotionEffect(new EffectInstance(CursedDiversionPotionEffect.potion, (int) 200, (int) 0));
		return super.attackEntityAsMob(entityIn);
	}
}
