package dev.lazurite.corduroy.impl.mixin;

import com.mojang.math.Quaternion;
import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.impl.util.QuaternionUtil;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Unique private Vec3 prevPosition = new Vec3(0, 0, 0);
    @Unique private Quaternion orientation = new Quaternion(Quaternion.ONE);
    @Unique private Quaternion prevOrientation = new Quaternion(Quaternion.ONE);

    @Shadow protected abstract void setPosition(Vec3 vec3);
    @Shadow public abstract Vec3 getPosition();
    @Shadow public abstract Quaternion rotation();

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick_HEAD(CallbackInfo info) {
        ViewStack.getInstance().peek().ifPresent(view -> {
            this.prevOrientation = new Quaternion(view.getRotation());

            final var pos = view.getPosition();
            this.prevPosition = new Vec3(pos.x, pos.y, pos.z);
        });
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(CallbackInfo info) {
        final var pos = getPosition();
        this.prevPosition = new Vec3(pos.x, pos.y, pos.z);
        this.prevOrientation = new Quaternion(rotation());
    }

    @Inject(method = "isDetached", at = @At("HEAD"), cancellable = true)
    public void isDetached_HEAD(CallbackInfoReturnable<Boolean> info) {
        ViewStack.getInstance().peek().ifPresent(view -> info.setReturnValue(view.shouldRenderTarget()));
    }

    @Inject(
            method = "setup",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Camera;entity:Lnet/minecraft/world/entity/Entity;"
            ),
            cancellable = true
    )
    public void setup_ENTITY(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo info) {
        ViewStack.getInstance().peek().ifPresent(view -> {
            final var pos = view.getPosition();
            final var x = Mth.lerp(f, prevPosition.x, pos.x);
            final var y = Mth.lerp(f, prevPosition.y, pos.y);
            final var z = Mth.lerp(f, prevPosition.z, pos.z);
            this.setPosition(new Vec3(x, y, z));

            this.orientation = new Quaternion(QuaternionUtil.slerp(prevOrientation, view.getRotation(), f));
            info.cancel();
        });
    }

    @Inject(method = "getXRot", at = @At("RETURN"), cancellable = true)
    public void getXRot_RETURN(CallbackInfoReturnable<Float> info) {
        ViewStack.getInstance().peek().ifPresent(view ->
                info.setReturnValue(QuaternionUtil.getYaw(this.orientation))
        );
    }

    @Inject(method = "getYRot", at = @At("RETURN"), cancellable = true)
    public void getYRot_RETURN(CallbackInfoReturnable<Float> info) {
        if (ViewStack.getInstance().peek().isPresent()) {
            info.setReturnValue(QuaternionUtil.getPitch(this.orientation));
        }
    }

    @Inject(method = "rotation", at = @At("RETURN"), cancellable = true)
    public void rotation_RETURN(CallbackInfoReturnable<Quaternion> info) {
        if (ViewStack.getInstance().peek().isPresent()) {
            info.setReturnValue(this.orientation);
        }
    }
}
