package dev.lazurite.corduroy.examplemod;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.impl.views.free.LineView;
import dev.lazurite.corduroy.impl.views.subject.OrbitView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.Vector3f;
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
//                ViewStack.getInstance().push(new LineView(
//                        new Vec3d(0, 5, 0),
//                        new Vec3d(0, 5, 20),
//                        Vector3f.NEGATIVE_Z.getDegreesQuaternion(0),
//                        Vector3f.NEGATIVE_Z.getDegreesQuaternion(90),
//                        400));

                ViewStack.getInstance().push(new OrbitView(client.player));
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
