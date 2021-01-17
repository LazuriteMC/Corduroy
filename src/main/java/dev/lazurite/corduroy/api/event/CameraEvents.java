package dev.lazurite.corduroy.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.Camera;

public final class CameraEvents {
    public static final Event<EndCameraSetup> END_CAMERA_SETUP = EventFactory.createArrayBacked(EndCameraSetup.class, (callbacks) -> (camera) -> {
        for (EndCameraSetup event : callbacks) {
            event.onEndCameraSetup(camera);
        }
    });

    private CameraEvents() {
    }

    @FunctionalInterface
    public interface EndCameraSetup {
        void onEndCameraSetup(Camera camera);
    }
}
