package dev.lazurite.corduroy;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.impl.camera.StationaryCamera;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Corduroy implements ClientModInitializer {
    public static final String MODID = "corduroy";

    private static KeyBinding testKey;

    @Override
    public void onInitializeClient() {
        testKeybindRegister();
    }

    public static void testKeybindCallback(MinecraftClient client) {
        if (testKey.wasPressed()) {
            System.out.println("CHANGE CAMERA");
            if (ViewStack.getInstance().peek() instanceof StationaryCamera) {
                ViewStack.getInstance().pop();
            } else {
                ViewStack.getInstance().push(new StationaryCamera(new Vec3d(0, 5, 0), Quaternion.IDENTITY));
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
