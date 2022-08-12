package dev.lazurite.corduroy.impl.mixin;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import dev.lazurite.corduroy.api.ViewStack;
import dev.lazurite.corduroy.impl.util.QuaternionUtil;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
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
    @Unique private Vec3 prevPosition = new Vec3(0, 0, 0);
    @Unique private Quaternion orientation = new Quaternion(Quaternion.ONE);
    @Unique private Quaternion prevOrientation = new Quaternion(Quaternion.ONE);

    @Shadow @Final private Vector3f forwards;

    @Shadow @Final private Vector3f up;

    @Shadow @Final private Vector3f left;

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
        this.prevPosition = Vec3.ZERO;
        this.prevOrientation = new Quaternion(Quaternion.ONE);
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
            this.position = new Vec3(x, y, z);

            this.orientation = new Quaternion(QuaternionUtil.slerp(prevOrientation, view.getRotation(), f));

            this.forwards.set(0.0F, 0.0F, 1.0F);
            this.forwards.transform(this.orientation);
            this.up.set(0.0F, 1.0F, 0.0F);
            this.up.transform(this.orientation);
            this.left.set(1.0F, 0.0F, 0.0F);
            this.left.transform(this.orientation);
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

    @Inject(method = "reset", at = @At("TAIL"))
    public void reset(CallbackInfo info) {
        this.prevOrientation = new Quaternion(this.orientation);
        this.prevPosition = new Vec3(this.position.x, this.position.y, this.position.z);
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
