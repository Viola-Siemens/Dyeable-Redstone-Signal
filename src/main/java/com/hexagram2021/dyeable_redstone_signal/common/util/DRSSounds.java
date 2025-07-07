package com.hexagram2021.dyeable_redstone_signal.common.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;

import static com.hexagram2021.dyeable_redstone_signal.DyeableRedstoneSignal.MODID;

public class DRSSounds {
	static final Map<ResourceLocation, SoundEvent> registeredEvents = new HashMap<>();

	public static final SoundEvent VILLAGER_WORK_ENERGY_RESEARCHER = registerSound("villager.work_energy_researcher");

	@SuppressWarnings("SameParameterValue")
	private static SoundEvent registerSound(String name) {
		ResourceLocation location = new ResourceLocation(MODID, name);
		SoundEvent event = SoundEvent.createVariableRangeEvent(location);
		registeredEvents.put(location, event);
		return event;
	}

	public static void init(RegisterEvent event) {
		event.register(Registries.SOUND_EVENT,helper -> registeredEvents.forEach(helper::register));
	}
}
