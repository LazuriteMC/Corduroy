package dev.lazurite.corduroy;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.camera.Temporary;
import dev.lazurite.corduroy.cameras.PointCamera;
import dev.lazurite.corduroy.cameras.base.BaseCamera;
import dev.lazurite.corduroy.cameras.base.FreeCamera;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Corduroy implements ClientModInitializer {
    public static final String MODID = "corduroy";

    private static KeyBinding testKey;
    private static Camera camera;

    @Override
    public void onInitializeClient() {
        testKeybindRegister();
        camera = new PointCamera(new Vec3d(0, 20, 0), Quaternion.IDENTITY);
        ((PointCamera) camera).setRoll(45);

        ClientTickEvents.END_WORLD_TICK.register(world -> {
            if (ViewStack.getInstance().peek() instanceof BaseCamera) {
                BaseCamera base = (BaseCamera) ViewStack.getInstance().peek();

                base.tick();

                if (base instanceof Temporary) {
                    Temporary temp = (Temporary) base;
                    temp.setAge(temp.getAge() + 1);

                    if (temp.getAge() > temp.getLifeSpan()) {
                        temp.finish();
                    }
                }
            }

        });
    }

    public static void testKeybindCallback(MinecraftClient client) {
        if (testKey.wasPressed()) {
            if (ViewStack.getInstance().peek() instanceof FreeCamera) {
                ViewStack.getInstance().pop();
            } else {
                ViewStack.getInstance().push(camera);
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
