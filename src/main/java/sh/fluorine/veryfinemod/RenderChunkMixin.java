package sh.fluorine.veryfinemod;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.world.RenderChunk;
import net.minecraft.client.render.vertex.Tesselator;
import org.lwjgl.opengl.GL11;

@Mixin(RenderChunk.class)
public class RenderChunkMixin {

    @Shadow public int sizeX;
    @Shadow public int sizeZ;

	@Inject(
        method = "setOrigin(III)V",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/world/RenderChunk;renderOffsetZ:I", shift = At.Shift.AFTER)
    )
    private void destroyChunks(int x, int y, int z, CallbackInfo ci) {
        RenderChunk instance = (RenderChunk) (Object) this;
        instance.renderOffsetX = x;
        instance.renderOffsetZ = z;
    }
    
	@Redirect(
    method = "compile()V",
    	at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/vertex/Tesselator;offset(DDD)V")
	)
     private void destroyBlocks(Tesselator tesselator, double x, double y, double z) {
     	float correctedX = (float)x - ((this.sizeX - this.sizeZ) / 2.0F);
        GL11.glTranslatef((float)correctedX, (float)y, (float)z);
    }
}
