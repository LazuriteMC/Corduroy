package dev.lazurite.rayon.examplemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ExampleMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ChangeViewKeybinding.register();
    }
}
