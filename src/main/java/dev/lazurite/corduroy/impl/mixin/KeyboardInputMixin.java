package dev.lazurite.corduroy.impl.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.api.View;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {

    /**
     * Prevents keyboard controls from being used.
     * @see View#shouldPlayerControl
     */
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick$HEAD(CallbackInfo ci) {
        ViewStack.getInstance().peek().filter(view -> !view.shouldPlayerControl()).ifPresent(view -> {
            this.forwardImpulse = 0.0f;
            this.leftImpulse = 0.0f;
            this.jumping = false;
            this.shiftKeyDown = false;
            this.up = false;
            this.down = false;
            this.left = false;
            this.right = false;
            ci.cancel();
        });
    }

}
