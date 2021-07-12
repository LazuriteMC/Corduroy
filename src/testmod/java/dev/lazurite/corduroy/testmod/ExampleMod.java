package dev.lazurite.corduroy.testmod;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.testmod.views.subject.GlideInView;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ExampleMod implements ClientModInitializer {
    private static KeyBinding key;

    @Override
    public void onInitializeClient() {
        key = new KeyBinding(
                "key.corduroy.change_view",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "key.categories.gameplay"
        );

        KeyBindingHelper.registerKeyBinding(key);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (key.wasPressed()) {
                ViewStack.getInstance().peek().ifPresentOrElse(view -> {
                    ViewStack.getInstance().pop();
                }, () -> {
                    ViewStack.getInstance().push(new GlideInView(client.player, 10, 5, 60));
                });
            }
        });
    }
}
