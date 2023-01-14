package dev.lazurite.corduroy.impl.mixin;

import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.impl.util.QuaternionUtil;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Unique private Vec3 position = new Vec3(0, 0, 0);
    @Unique private Quaternionf orientation = new Quaternionf(0, 0, 0, 1);

    @Shadow @Final private Vector3f forwards;

    @Shadow @Final private Vector3f up;

    @Shadow @Final private Vector3f left;

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
            this.position = new Vec3(pos.x, pos.y, pos.z);
            this.orientation = new Quaternionf(view.getRotation());

            this.forwards.set(0.0F, 0.0F, 1.0F);
            this.forwards.rotate(this.orientation);
            this.up.set(0.0F, 1.0F, 0.0F);
            this.up.rotate(this.orientation);
            this.left.set(1.0F, 0.0F, 0.0F);
            this.left.rotate(this.orientation);
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
    public void rotation_RETURN(CallbackInfoReturnable<Quaternionf> info) {
        if (ViewStack.getInstance().peek().isPresent()) {
            info.setReturnValue(this.orientation);
        }
    }

    @Inject(method = "getBlockPosition", at = @At("HEAD"), cancellable = true)
    public void getBlockPosition(CallbackInfoReturnable<BlockPos> info) {
        if (ViewStack.getInstance().peek().isPresent()) {
            info.setReturnValue(new BlockPos(this.position));
        }
    }

    @Inject(method = "getPosition", at = @At("HEAD"), cancellable = true)
    public void getPosition(CallbackInfoReturnable<Vec3> info) {
        if (ViewStack.getInstance().peek().isPresent()) {
            info.setReturnValue(this.position);
        }
    }
}
