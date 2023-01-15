package dev.lazurite.corduroy.api;

import dev.lazurite.corduroy.impl.util.QuaternionUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

/**
 * A basic {@link View} object can be pushed onto the {@link ViewStack}.
 * What this allows you to do is apply custom behavior to the camera
 * which creates a much more flexible way to control what the player sees.
 * @since 1.0.0
 */
public abstract class View {

    protected Vec3 position;
    protected Quaternionf rotation;

    private Vec3 prevPosition;
    private Quaternionf prevRotation;

    public View() {
        var camPos = this.getCamera().getPosition();
        var camRot = this.getCamera().rotation();
        this.position = new Vec3(camPos.x, camPos.y, camPos.z);
        this.prevPosition = new Vec3(camPos.x, camPos.y, camPos.z);
        this.rotation = new Quaternionf(camRot);
        this.prevRotation = new Quaternionf(camRot);
    }

    /**
     * Gets the position of the camera with tick delta linear interpolation.
     */
    public Vec3 getPosition(float tickDelta) {
        return this.prevPosition.lerp(this.position, tickDelta);
    }

    /**
     * Gets the rotation of the camera with tick delta spherical interpolation.
     */
    public Quaternionf getRotation(float tickDelta) {
        return QuaternionUtil.slerp(this.prevRotation, this.rotation, tickDelta);
    }

    /**
     * Called each time the game is rendered. Similar to {@link Camera#setup}.
     */
    public void onRender() {

    }

    public Camera getCamera() {
        return Minecraft.getInstance().gameRenderer.getMainCamera();
    }

    public void updatePrevious() {
        this.prevPosition = new Vec3(this.position.x, this.position.y, this.position.z);
        this.prevRotation = new Quaternionf(this.rotation);
    }

    /**
     * Controls whether the "Camera Entity" should be rendered.
     */
    public boolean shouldRenderTarget() {
        return false;
    }

    /**
     * Controls whether the player's hand should be rendered.
     */
    public boolean shouldRenderHand() {
        return false;
    }

    /**
     * Controls whether the player's HUD (hotbar + healthbar) should be rendered.
     */
    public boolean shouldRenderHud() {
        return false;
    }

    /**
     * Controls whether the player's view should bob when they move.
     */
    public boolean shouldBobView() {
        return false;
    }

    /**
     * Controls whether the player should have keyboard/mouse input control.
     */
    public boolean shouldPlayerControl() {
        return false;
    }

    /**
     * Controls whether the player's FOV can change via movement.
     */
    public boolean shouldFOVChangeOnMovement() {
        return false;
    }

    /**
     * Any {@link View} that implements this interface
     * will be ticked every world tick.
     * @since 1.0.0
     */
    public interface Ticking {

        /**
         * Called each world tick.
         */
        void tick();

    }

    /**
     * When a locked view is pushed onto the {@link ViewStack}, the stack is
     * locked until {@link ViewStack#unlock()} or {@link Locked#unlock()} is called.
     * @since 1.0.0
     */
    public interface Locked {

        /**
         * Convenience method which calls {@link ViewStack#unlock()}.
         */
        default void unlock() {
            ViewStack.getInstance().unlock();
        }

    }

    /**
     * This allows a {@link View} to only temporarily exist on the {@link ViewStack}.
     * When its age has execeeded the given duration, it is automatically discarded from
     * the {@link ViewStack}.
     * @since 1.0.0
     */
    public interface Temporary {

        /**
         * Executes when this view has reached its duration.
         */
        default void onExit() {
            ViewStack.getInstance().pop();
        }

        /**
         * Returns the max duration this view should be applied for in ticks.
         */
        int getDuration();

        /**
         * Used by the {@link ViewStack} to increment this view's age.
         */
        void age();

        /**
         * Returns the age of this view in ticks.
         */
        int getAge();

    }

}
