package com.hexagram2021.dyeable_redstone_signal;

import com.hexagram2021.dyeable_redstone_signal.client.ClientProxy;
import com.hexagram2021.dyeable_redstone_signal.common.DRSContent;
import com.hexagram2021.dyeable_redstone_signal.common.world.Villages;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@Mod(DyeableRedstoneSignal.MODID)
public class DyeableRedstoneSignal {
    public static final String MODID = "dyeable_redstone_signal";
    public static final String MODNAME = "Dyeable Redstone Signal";
    public static final String VERSION = "${version}";

    public static <T>
    Supplier<T> bootstrapErrorToXCPInDev(Supplier<T> in) {
        if(FMLLoader.isProduction())
            return in;
        return () -> {
            try {
                return in.get();
            } catch(BootstrapMethodError e) {
                throw new RuntimeException(e);
            }
        };
    }

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public DyeableRedstoneSignal() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        DeferredWorkQueue queue = DeferredWorkQueue.lookup(Optional.of(ModLoadingStage.CONSTRUCT)).orElseThrow();
        Consumer<Runnable> runLater = job -> queue.enqueueWork(
                ModLoadingContext.get().getActiveContainer(), job
        );
        DRSContent.modConstruction(bus, runLater);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, bootstrapErrorToXCPInDev(() -> ClientProxy::modConstruction));

        MinecraftForge.EVENT_BUS.register(this);
    }

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
