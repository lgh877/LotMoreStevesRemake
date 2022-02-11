
package net.mcreator.lotmorestevesremake.entity;

import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.World;
import net.minecraft.world.BossInfo;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.ResourceLocation;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;

import net.mcreator.lotmorestevesremake.procedures.SpawnInOverworldOnlyProcedure;
import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.item.SlimeBombItem;
import net.mcreator.lotmorestevesremake.entity.renderer.GiantBouncySteveRenderer;
import net.mcreator.lotmorestevesremake.SlimySteveEntity;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;

import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;

@LotmorestevesremakeModElements.ModElement.Tag
public class GiantBouncySteveEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.CREATURE)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new).size(1.2f, 4f))
					.build("giant_bouncy_steve").setRegistryName("giant_bouncy_steve");

	public GiantBouncySteveEntity(LotmorestevesremakeModElements instance) {
		super(instance, 12);
		FMLJavaModLoadingContext.get().getModEventBus().register(new GiantBouncySteveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16751002, -16724839, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("giant_bouncy_steve_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		boolean biomeCriteria = false;
		if (new ResourceLocation("swamp").equals(event.getName()))
			biomeCriteria = true;
		if (new ResourceLocation("swamp_hills").equals(event.getName()))
			biomeCriteria = true;
		if (!biomeCriteria)
			return;
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 1, 1, 1));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> {
					int x = pos.getX();
					int y = pos.getY();
					int z = pos.getZ();
					return SpawnInOverworldOnlyProcedure.executeProcedure(Stream.of(new AbstractMap.SimpleEntry<>("world", world))
							.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
				});
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 120);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 6);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 5);
			ammma = ammma.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.4);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 2);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends SlimySteveEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 15;
			setNoAI(false);
			enablePersistence();
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
		}

		@Override
		public boolean canDespawn(double distanceToClosestPlayer) {
			return false;
		}

		@Override
		public boolean isNonBoss() {
			return false;
		}

		protected float getMovementSpeedAttribute() {
			return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
		}

		public boolean onLivingFall(float distance, float damageMultiplier) {
			if (distance > 2) {
				if (world instanceof ServerWorld) {
					for (int i = 0; i < 20; i++) {
						AbstractArrowEntity entityToSpawn = new SlimeBombItem.ArrowCustomEntity(SlimeBombItem.arrow, this, (World) world);
						entityToSpawn.setDamage(2);
						entityToSpawn.setSilent(true);
						entityToSpawn.setLocationAndAngles(this.getPosX(), this.getPosY() + 0.3, this.getPosZ(), world.getRandom().nextFloat() * 360F,
								0);
						entityToSpawn.setMotion((rand.nextFloat() - 0.5f) * 0.8f, rand.nextFloat() + 0.2f, (rand.nextFloat() - 0.5f) * 0.8f);
						world.addEntity(entityToSpawn);
					}
				}
			} else if (rand.nextInt(5) == 0) {
				if (world instanceof ServerWorld) {
					for (int i = 0; i < 4; i++) {
						AbstractArrowEntity entityToSpawn = new SlimeBombItem.ArrowCustomEntity(SlimeBombItem.arrow, this, (World) world);
						entityToSpawn.setDamage(2);
						entityToSpawn.setSilent(true);
						entityToSpawn.setLocationAndAngles(this.getPosX(), this.getPosY() + 0.3, this.getPosZ(), world.getRandom().nextFloat() * 360F,
								0);
						entityToSpawn.setMotion((rand.nextFloat() - 0.5f) * 0.8f, rand.nextFloat() / 2, (rand.nextFloat() - 0.5f) * 0.8f);
						world.addEntity(entityToSpawn);
					}
				}
			}
			return super.onLivingFall(distance * 2, damageMultiplier);
		}

		@Override
		public boolean startRiding(Entity entity, boolean force) {
			return false;
		}

		protected void jump() {
			if (world instanceof ServerWorld) {
				for (int i = 0; i < 4; i++) {
					AbstractArrowEntity entityToSpawn = new SlimeBombItem.ArrowCustomEntity(SlimeBombItem.arrow, this, (World) world);
					entityToSpawn.setDamage(2);
					entityToSpawn.setSilent(true);
					entityToSpawn.setLocationAndAngles(this.getPosX(), this.getPosY() + 0.3, this.getPosZ(), world.getRandom().nextFloat() * 360F, 0);
					entityToSpawn.setMotion((rand.nextFloat() - 0.5f) * 0.8f, rand.nextFloat() * 0.66f, (rand.nextFloat() - 0.5f) * 0.8f);
					world.addEntity(entityToSpawn);
				}
			}
			Vector3d vector3d = this.getMotion();
			this.setMotion(this.getLookVec().x * jumpStrength(), this.getJumpFactor() * 1.8f, this.getLookVec().z * jumpStrength());
			this.isAirBorne = true;
		}

		private final ServerBossInfo bossInfo = new ServerBossInfo(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS);

		@Override
		public void addTrackingPlayer(ServerPlayerEntity player) {
			super.addTrackingPlayer(player);
			this.bossInfo.addPlayer(player);
		}

		@Override
		public void removeTrackingPlayer(ServerPlayerEntity player) {
			super.removeTrackingPlayer(player);
			this.bossInfo.removePlayer(player);
		}

		@Override
		public void updateAITasks() {
			super.updateAITasks();
			this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
		}
	}
}
