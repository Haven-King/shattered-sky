package dev.hephaestus.shatteredsky.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.hephaestus.shatteredsky.WorldGen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;

public class AtmosphereRenderer {
	static int COLOR;

	public static float getRed(float red) {
		COLOR = MinecraftClient.getInstance().world.getColor(MinecraftClient.getInstance().player.getBlockPos(), WorldGen.SKY_COLOR);

		return (COLOR >> 16)/255F;
	}

	public static float getGreen(float green) {
		return ((COLOR >> 8) & 255)/255F;
	}

	public static float getBlue(float blue) {
		return (COLOR & 255)/255F;
	}

	public static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog) {
		double y = camera.getPos().y;

		float d = 0;

		float top = 70;
		float bot = 10;
		float range = 20;

//		d = (float) Math.sin(Math.PI * (camera.getPos().y - bot) / (top - bot)) / 100;

		if (y > top - range && y <= top) {
			d += (1.0 - (y - (top - range)) / range) / 20;
		} else if (y < bot + range && y > bot) {
			d += ((y - bot) / range) / 20;
		} else if ( y <= top - range && y >= bot) {
			d = 0.05F;
		}

		RenderSystem.fogDensity(0.001F);
		RenderSystem.fogStart(0);
		RenderSystem.fogEnd(viewDistance * 0.85F);
		RenderSystem.setupNvFogDistance();
	}
}
