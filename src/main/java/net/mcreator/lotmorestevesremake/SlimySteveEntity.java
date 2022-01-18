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

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.potion.EffectInstance;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.IPacket;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.FollowMobGoal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.Pose;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;

import net.mcreator.lotmorestevesremake.potion.CursedDiversionPotionEffect;
import net.mcreator.lotmorestevesremake.item.SlimeBombItem;
import net.mcreator.lotmorestevesremake.entity.BouncySteveEntity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SlimySteveEntity extends AggressiveSteveEntity {
	private static final DataParameter<Integer> DEATH_TICKS = EntityDataManager.createKey(SlimySteveEntity.class, DataSerializers.VARINT);
	public int deathTicks;
	public boolean wasOnGround;

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(DEATH_TICKS, 0);
	}

	protected void onDeathUpdate() {
		this.deathTicks++;
		this.setDeathTicksState(deathTicks);
		if (deathTicks % 5 == 0) {
			this.playSound(SoundEvents.ENTITY_SLIME_DEATH, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			if (world.isRemote) {
				float i = (2 + deathTicks / 40) * this.getWidth() * 4;
				for (int j = 0; j < i * 10; ++j) {
					float f = this.rand.nextFloat() * ((float) Math.PI * 2F);
					float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
					float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1 * (rand.nextFloat() - 0.5f) * 2;
					float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1 * (rand.nextFloat() - 0.5f) * 2;
					this.world.addParticle(ParticleTypes.ITEM_SLIME, this.getPosX() + (double) f2, this.getPosY(), this.getPosZ() + (double) f3, 0.0D,
							rand.nextFloat(), 0.0D);
				}
			}
		}
		if (deathTicks > 80) {
			this.remove();
		}
	}

	protected void jump() {
		Vector3d vector3d = this.getMotion();
		this.setMotion(this.getLookVec().x * 0.5f * jumpStrength(), this.getJumpFactor(), this.getLookVec().z * 0.5f * jumpStrength());
		this.isAirBorne = true;
	}

	protected float jumpStrength() {
		return this.getAttackTarget() == null ? 1 : (float) Math.pow(this.getDistance(this.getAttackTarget()), 0.25);
	}

	public boolean onLivingFall(float distance, float damageMultiplier) {
		if (distance > 2) {
			if (world instanceof ServerWorld) {
				for (int i = 0; i < 3; i++) {
					AbstractArrowEntity entityToSpawn = new SlimeBombItem.ArrowCustomEntity(SlimeBombItem.arrow, this, (World) world);
					entityToSpawn.setDamage(2);
					entityToSpawn.setSilent(true);
					entityToSpawn.setLocationAndAngles(this.getPosX(), this.getPosY() + 0.3, this.getPosZ(), world.getRandom().nextFloat() * 360F, 0);
					entityToSpawn.setMotion((rand.nextFloat() - 0.5f) * 0.8f, rand.nextFloat() + 0.2f, (rand.nextFloat() - 0.5f) * 0.8f);
					world.addEntity(entityToSpawn);
				}
			}
			for (LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(1.5f, 0, 1.5f))) {
				if (!this.isOnSameTeam(livingentity)) {
					livingentity.attackEntityFrom(DamageSource.causeMobDamage(this), this.getAttackDamage() / 3);
					this.applyEnchantments(this, livingentity);
					livingentity.hurtResistantTime = 0;
				}
				if (livingentity != this)
					for (int i = 0; i < 3; i++)
						this.constructKnockBackVector(livingentity);
			}
		}
		return false;
	}

	public void setDeathTicksState(int value) {
		this.dataManager.set(DEATH_TICKS, value);
	}

	public int getDeathTicksState() {
		return this.dataManager.get(DEATH_TICKS);
	}

	public SlimySteveEntity(FMLPlayMessages.SpawnEntity packet, World world) {
		this(BouncySteveEntity.entity, world);
	}

	public SlimySteveEntity(EntityType<? extends MonsterEntity> type, World world) {
		super(type, world);
		experienceValue = 5;
		setNoAI(false);
		jumpMovementFactor = 0.1f;
		stepHeight = 1.5f;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new FollowMobGoal(this, (float) 1, 10, 5));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
		this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setCallsForHelp());
		this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, MobEntity.class, false, false));
		this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, PlayerEntity.class, false, false));
		this.targetSelector.addGoal(7, new NearestAttackableTargetGoal(this, ServerPlayerEntity.class, false, false));
	}

	@Override
	protected void updateArmSwingProgress() {
		int i = 10;
		if (this.isSwingInProgress) {
			++this.swingProgressInt;
			if (this.swingProgressInt >= i) {
				this.swingProgressInt = 0;
				this.isSwingInProgress = false;
			}
		} else {
			this.swingProgressInt = 0;
		}
		this.swingProgress = (float) this.swingProgressInt / (float) i;
	}

	public EntitySize getSize(Pose poseIn) {
		if (this.isAlive()) {
			return super.getSize(poseIn);
		} else {
			return super.getSize(poseIn).scale(1, 0.1f);
		}
	}

	public void livingTick() {
		super.livingTick();
		if (!this.isPassenger() && this.isAlive()) {
			if (rand.nextInt(100) == 0 && (this.onGround) || (rand.nextInt(20) == 0 && (this.isInLava() || this.isInWater()))) {
				this.jump();
				this.playSound(SoundEvents.ENTITY_SLIME_JUMP, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}
			if ((this.isInWater() || this.isInLava() || this.onGround) && !this.wasOnGround) {
				this.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				if (world.isRemote) {
					float i = this.getWidth() * 6;
					for (int j = 0; j < i * 8; ++j) {
						float f = this.rand.nextFloat() * ((float) Math.PI * 2F);
						float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
						float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1;
						float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1;
						this.world.addParticle(ParticleTypes.ITEM_SLIME, this.getPosX() + (double) f2, this.getPosY(), this.getPosZ() + (double) f3,
								0.0D, 0.0D, 0.0D);
					}
				}
				if (rand.nextInt(10) == 0)
					this.jump();
				if (this.isAlive())
					this.swingArm(Hand.MAIN_HAND);
			}
			this.wasOnGround = this.onGround;
			if (this.isInWater() || this.isInLava())
				this.wasOnGround = true;
		}
	}

	protected float getAttackDamage() {
		return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
	}

	public boolean attackEntityFrom(DamageSource damagesource, float amount) {
		return super.attackEntityFrom(damagesource, amount);
	}

	public boolean attackEntityAsMob(Entity entityIn) {
		this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
		for (LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(1.5f, 0, 1.5f))) {
			if (!this.isOnSameTeam(livingentity)) {
				livingentity.addPotionEffect(new EffectInstance(CursedDiversionPotionEffect.potion, (int) 200, (int) 0, (true), (false)));
				livingentity.attackEntityFrom(DamageSource.causeMobDamage(this), this.getAttackDamage() / 3);
				this.applyEnchantments(this, livingentity);
				livingentity.hurtResistantTime = 0;
			}
			if (livingentity != this)
				for (int i = 0; i < 3; i++)
					this.constructKnockBackVector(livingentity);
		}
		return super.attackEntityAsMob(entityIn);
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.UNDEFINED;
	}

	@Override
	public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
		return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lotmorestevesremake:hostile_steve_hurt"));
	}

	@Override
	public net.minecraft.util.SoundEvent getDeathSound() {
		return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lotmorestevesremake:bouncy_steve_death"));
	}
}
