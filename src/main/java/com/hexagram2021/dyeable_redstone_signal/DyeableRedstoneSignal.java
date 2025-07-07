package com.hexagram2021.dyeable_redstone_signal;

import com.hexagram2021.dyeable_redstone_signal.common.DRSContent;
import com.hexagram2021.dyeable_redstone_signal.common.world.Villages;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.DeferredWorkQueue;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.ModLoadingStage;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Consumer;

@Mod(DyeableRedstoneSignal.MODID)
public class DyeableRedstoneSignal {
    public static final String MODID = "dyeable_redstone_signal";

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public DyeableRedstoneSignal() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);

        DeferredWorkQueue queue = DeferredWorkQueue.lookup(Optional.of(ModLoadingStage.CONSTRUCT)).orElseThrow();
        Consumer<Runnable> runLater = job -> queue.enqueueWork(
                ModLoadingContext.get().getActiveContainer(), job
        );
        DRSContent.modConstruction(bus, runLater);

        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void tagsUpdated(TagsUpdatedEvent event) {
        if(event.getUpdateCause() != TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) {
            return;
        }

        Villages.addAllStructuresToPool(event.getRegistryAccess());
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Let's make redstone more colorful!");
        event.enqueueWork(DRSContent::init);
    }
}
