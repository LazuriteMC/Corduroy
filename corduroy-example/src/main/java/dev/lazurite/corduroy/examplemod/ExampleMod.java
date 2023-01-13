package dev.lazurite.corduroy.examplemod;

import com.mojang.blaze3d.platform.InputConstants;
import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.examplemod.views.GlideInView;
import dev.lazurite.corduroy.examplemod.views.PointView;
import dev.lazurite.corduroy.examplemod.views.TopDownView;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ExampleMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        final var m_key = new KeyMapping(
                "key.corduroy.change_view",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "key.categories.gameplay"
        );

        final var n_key = new KeyMapping(
                "key.corduroy.change_view_2",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "key.categories.gameplay"
        );

        final var b_key = new KeyMapping(
                "key.corduroy.change_view_3",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "key.categories.gameplay"
        );

        KeyBindingHelper.registerKeyBinding(m_key);
        KeyBindingHelper.registerKeyBinding(n_key);
        KeyBindingHelper.registerKeyBinding(b_key);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (m_key.consumeClick()) {
                ViewStack.getInstance().peek().ifPresentOrElse(
                        view -> ViewStack.getInstance().pop(),
                        () -> ViewStack.getInstance().push(new PointView(10)));
            }

            if (n_key.consumeClick()) {
                ViewStack.getInstance().peek().ifPresentOrElse(
                        view -> ViewStack.getInstance().pop(),
                        () -> ViewStack.getInstance().push(new TopDownView(5)));
            }

            if (b_key.consumeClick()) {
                ViewStack.getInstance().peek().ifPresentOrElse(
                        view -> ViewStack.getInstance().pop(),
                        () -> ViewStack.getInstance().push(new GlideInView(5, 5, 100)));
            }
        });
    }
}
