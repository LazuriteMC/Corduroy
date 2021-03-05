package dev.lazurite.corduroy.examplemod;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.view.View;
import dev.lazurite.corduroy.impl.util.math.QuaternionHelper;
import dev.lazurite.corduroy.impl.views.free.LineView;
import dev.lazurite.corduroy.impl.views.subject.TopDownView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ChangeViewKeybinding {
    private static KeyBinding key;

    public static void callback(MinecraftClient client) {
        if (key.wasPressed()) {
            if (ViewStack.getInstance().peek() instanceof LineView) {
                ViewStack.getInstance().pop();
            } else {

                for (int i = 0; i < 3; ++i) {
                    ViewStack.getInstance().push(new LineView(
                            new Vec3d(0, 5, 0),
                            new Vec3d(0, 5, 20),
                            new Quaternion(Quaternion.IDENTITY),
                            QuaternionHelper.rotateY(QuaternionHelper.rotateX(new Quaternion(Quaternion.IDENTITY), -20), 180),
                            100));
                }

//                ViewStack.getInstance().push(new TopDownView(client.player));
            }
        }
    }

    public static void register() {
        key = new KeyBinding(
                "key.corduroy.change_view",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "key.categories.gameplay"
        );

        KeyBindingHelper.registerKeyBinding(key);
        ClientTickEvents.END_CLIENT_TICK.register(ChangeViewKeybinding::callback);
    }
}
