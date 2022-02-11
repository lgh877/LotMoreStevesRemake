package net.mcreator.lotmorestevesremake.world;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import net.minecraft.world.GameRules;

import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;

import java.lang.reflect.Method;

@LotmorestevesremakeModElements.ModElement.Tag
public class CanCauseInfectionGameRule extends LotmorestevesremakeModElements.ModElement {
	public static final GameRules.RuleKey<GameRules.BooleanValue> gamerule = GameRules.register("canCauseInfection", GameRules.Category.MOBS,
			create(true));

	public CanCauseInfectionGameRule(LotmorestevesremakeModElements instance) {
		super(instance, 49);
	}

	public static GameRules.RuleType<GameRules.BooleanValue> create(boolean defaultValue) {
		try {
			Method createGameruleMethod = ObfuscationReflectionHelper.findMethod(GameRules.BooleanValue.class, "func_223568_b", boolean.class);
			createGameruleMethod.setAccessible(true);
			return (GameRules.RuleType<GameRules.BooleanValue>) createGameruleMethod.invoke(null, defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
