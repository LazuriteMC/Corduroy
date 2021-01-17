package dev.lazurite.corduroy.api.camera;

import dev.lazurite.corduroy.impl.camera.builder.StationaryCameraBuilder;

import java.util.function.Supplier;

public enum CameraType {
    STATIONARY(StationaryCameraBuilder::new);

    private final Supplier<CameraBuilder> factory;

    CameraType(Supplier<CameraBuilder> factory) {
        this.factory = factory;
    }

    public Supplier<CameraBuilder> getFactory() {
        return this.factory;
    }
}
