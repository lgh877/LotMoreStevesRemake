
package net.mcreator.lotmorestevesremake.entity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.DungeonHooks;

import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.potion.EffectInstance;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.Pose;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.AreaEffectCloudEntity;

import net.mcreator.lotmorestevesremake.potion.CursedDiversionPotionEffect;
import net.mcreator.lotmorestevesremake.particle.SteveFaceParticleParticle;
import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.SpinningSteveRenderer;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import java.util.Collection;

@LotmorestevesremakeModElements.ModElement.Tag
public class SpinningSteveEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("spinning_steve").setRegistryName("spinning_steve");

	public SpinningSteveEntity(LotmorestevesremakeModElements instance) {
		super(instance, 26);
		FMLJavaModLoadingContext.get().getModEventBus().register(new SpinningSteveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16724788, -10843649, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("spinning_steve_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 1, 1, 1));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				AggressiveSteveEntity::customSpawningConditionInLight);
		DungeonHooks.addDungeonMob(entity, 180);
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 45);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AggressiveSteveEntity {
		public boolean mustExplode;

		public void readAdditional(CompoundNBT compound) {
			super.readAdditional(compound);
			this.mustExplode = compound.getBoolean("mustExplode");
		}

		public void writeAdditional(CompoundNBT compound) {
			super.writeAdditional(compound);
			compound.putBoolean("mustExplode", this.mustExplode);
		}

		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 50;
			stepHeight = 10;
			jumpMovementFactor = 0.5f;
			setNoAI(false);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2, false));
			this.goalSelector.addGoal(5, new RandomWalkingGoal(this, 1, 1));
			this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(8, new SwimGoal(this));
		}

		private void defaultExplode() {
			if (!this.world.isRemote) {
				Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)
						? Explosion.Mode.DESTROY
						: Explosion.Mode.NONE;
				this.dead = true;
				this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 4, explosion$mode);
				this.remove();
				this.spawnLingeringCloud(4);
			}
		}

		protected void collideWithEntity(Entity entityIn) {
			super.collideWithEntity(entityIn);
			if (this.mustExplode && !EntityTypeTags.getCollection().getTagByID(new ResourceLocation("lotmorestevesremake:we_are_steves"))
					.contains(entityIn.getType()) && entityIn instanceof LivingEntity) {
				this.defaultExplode();
			}
		}

		public void livingTick() {
			super.livingTick();
			if (!this.onGround)
				this.setPose(Pose.FALL_FLYING);
			else
				this.setPose(Pose.STANDING);
			if (this.onGround && rand.nextInt(25) == 0)
				this.setMotion(this.getMotion().add(this.getLookVec().x, 1.5, this.getLookVec().z));
			else if ((!this.onGround && rand.nextInt(60) == 0 && this.getMotion().y > 0) || this.isInWater() || this.isInLava()) {
				this.setMotion(this.getMotion().add(this.getLookVec().x * 0.5f, 1, this.getLookVec().y * 0.5f));
				if ((this.isInWater() || this.isInLava()) && this.rand.nextInt(6) == 0) {
					this.defaultExplode();
				}
			}
		}

		public boolean isOnGround() {
			return this.onGround;
		}

		public boolean isPushedByWater() {
			return false;
		}

		public boolean onLivingFall(float distance, float damageMultiplier) {
			if (rand.nextInt(6) == 0 && distance > 3 || this.mustExplode) {
				if (!this.world.isRemote) {
					Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)
							? Explosion.Mode.DESTROY
							: Explosion.Mode.NONE;
					this.dead = true;
					this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), (float) Math.pow(distance, 0.5), explosion$mode);
					this.remove();
					this.spawnLingeringCloud((float) Math.pow(distance, 0.5));
				}
			}
			return false;
		}

		private void spawnLingeringCloud(float radius) {
			this.addPotionEffect(new EffectInstance(CursedDiversionPotionEffect.potion, (int) 200, (int) 0));
			Collection<EffectInstance> collection = this.getActivePotionEffects();
			if (!collection.isEmpty()) {
				AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ());
				areaeffectcloudentity.setRadius(radius);
				areaeffectcloudentity.setRadiusOnUse(-0.5F);
				areaeffectcloudentity.setWaitTime(10);
				areaeffectcloudentity.setParticleData(SteveFaceParticleParticle.particle);
				areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
				areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());
				for (EffectInstance effectinstance : collection) {
					areaeffectcloudentity.addEffect(new EffectInstance(effectinstance));
				}
				this.world.addEntity(areaeffectcloudentity);
			}
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
					.getValue(new ResourceLocation("lotmorestevesremake:hostile_steve_hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
					.getValue(new ResourceLocation("lotmorestevesremake:hostile_steve_hurt"));
		}
	}
}
