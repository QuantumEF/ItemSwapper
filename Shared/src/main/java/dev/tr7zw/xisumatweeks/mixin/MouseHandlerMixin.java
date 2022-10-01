package dev.tr7zw.xisumatweeks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.Blaze3D;

import dev.tr7zw.xisumatweeks.SwitchItemOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Overlay;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Shadow
    private Minecraft minecraft;
    @Shadow
    private double lastMouseEventTime = Double.MIN_VALUE;
    @Shadow
    private double accumulatedDX;
    @Shadow
    private double accumulatedDY;
    
    @Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
    public void turnPlayer(CallbackInfo ci) {
        Overlay overlay = Minecraft.getInstance().getOverlay();
        if(overlay instanceof SwitchItemOverlay switcher) {
            double d0 = Blaze3D.getTime();
            this.lastMouseEventTime = d0;
            if (this.isMouseGrabbed() && this.minecraft.isWindowActive()) {
                double d4 = this.minecraft.options.sensitivity().get() * 0.6000000238418579D
                        + 0.20000000298023224D;
                double d5 = d4 * d4 * d4;
                double d6 = d5 * 8.0D;
                double d2 = this.accumulatedDX * d6;
                double d3 = this.accumulatedDY * d6;

                this.accumulatedDX = 0.0D;
                this.accumulatedDY = 0.0D;

                if (this.minecraft.player != null) {
                    switcher.handleInput(d2, d3);
                }
            } else {
                this.accumulatedDX = 0.0D;
                this.accumulatedDY = 0.0D;
            }
            ci.cancel();
        }
    }
    
    @Shadow
    public boolean isMouseGrabbed() {
        return false;
    }
    
}