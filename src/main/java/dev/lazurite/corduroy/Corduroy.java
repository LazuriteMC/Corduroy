package dev.lazurite.corduroy;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.camera.CameraBuilder;
import dev.lazurite.corduroy.api.event.CameraEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Corduroy implements ClientModInitializer {
    public static final String MODID = "corduroy";

    private static KeyBinding testKey;
    public static Camera firstCamera;
    private static Camera testCamera;

    @Override
    public void onInitializeClient() {
        CameraEvents.END_CAMERA_SETUP.register(camera -> Corduroy.firstCamera = camera);
        testKeybindRegister();

        testCamera = CameraBuilder
                .createStationaryCamera()
                .setPosition(0, 10, 0)
                .build();
    }

    public static void testKeybindCallback(MinecraftClient client) {
        if (testKey.wasPressed()) {
            if (ViewStack.INSTANCE.peek().equals(testCamera)) {
                ViewStack.INSTANCE.pop();
                ViewStack.INSTANCE.push(firstCamera);
                System.out.println("FIRST CAMERA");
            } else {
                ViewStack.INSTANCE.pop();
                ViewStack.INSTANCE.push(testCamera);
                System.out.println("TEST CAMERA");
            }
        }
    }

    public static void testKeybindRegister() {
        testKey = new KeyBinding(
                "key." + MODID + ".test",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "category." + MODID + ".keys"
        );

        KeyBindingHelper.registerKeyBinding(testKey);
        ClientTickEvents.END_CLIENT_TICK.register(Corduroy::testKeybindCallback);
    }
}
