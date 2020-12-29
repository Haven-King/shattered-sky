package dev.hephaestus.shatteredsky.client.render;

import dev.hephaestus.shatteredsky.ShatteredSky;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Lazy;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;

//import grondag.frex.api.event.WorldRenderContext;
//import grondag.frex.api.event.WorldRenderEvents;

public class RenderFluidLayer /*implements WorldRenderEvents.AfterEntities*/ {
	public static final RenderFluidLayer INSTANCE = new RenderFluidLayer();
	private static final Lazy<Sprite> SPRITE = new Lazy<>(ShatteredSky.Fluids.ATMOSPHERE_STILL::getSprite);

//	@Override
//	public void afterEntities(WorldRenderContext.MainContext context) {
//		if (RegistryUtil.dimensionMatches(MinecraftClient.getInstance().world, ShatteredSky.DIMENSION_TYPE)) {
//			MatrixStack matrixStack = context.matrixStack();
//			Vec3d camPos = context.camera().getPos();
//
//			matrixStack.push();
//
//			matrixStack.translate(0, -camPos.y,0);
//
//			VertexConsumer consumer = context.consumers().getBuffer(TexturedRenderLayers.getItemEntityTranslucentCull());
//
//			float u0 = 0;
//			float v0 = 0;
//			float u1 = 1;
//			float v1 = 1;
//
//			float r = 127 / 255F;
//			float g = 170 / 255F;
//			float b = 1F;
//			float a = 1F;
//
//			float[] levels = new float[] {10, 68.80013F};
//
//			for (float y : levels) {
//				consumer.vertex(matrixStack.peek().getModel(), 1000, y, 1000).color(r, g, b, a).texture(u1, v1).light(-1).normal(0, 1, 0).next();
//				consumer.vertex(matrixStack.peek().getModel(), 1000, y, -1000).color(r, g, b, a).texture(u1, v0).light(-1).normal(0, 1, 0).next();
//				consumer.vertex(matrixStack.peek().getModel(), -1000, y, -1000).color(r, g, b, a).texture(u0, v0).light(-1).normal(0, 1, 0).next();
//				consumer.vertex(matrixStack.peek().getModel(), -1000, y, 1000).color(r, g, b, a).texture(u0, v1).light(-1).normal(0, 1, 0).next();
//
//				consumer.vertex(matrixStack.peek().getModel(), -1000, y, -1000).color(r, g, b, a).texture(u0, v0).light(-1).normal(0, -1, 0).next();
//				consumer.vertex(matrixStack.peek().getModel(), 1000, y, -1000).color(r, g, b, a).texture(u1, v0).light(-1).normal(0, -1, 0).next();
//				consumer.vertex(matrixStack.peek().getModel(), 1000, y, 1000).color(r, g, b, a).texture(u1, v1).light(-1).normal(0, -1, 0).next();
//				consumer.vertex(matrixStack.peek().getModel(), -1000, y, 1000).color(r, g, b, a).texture(u0, v1).light(-1).normal(0, -1, 0).next();
//			}
//
//			matrixStack.pop();
//		}
//	}

	public static void render(MatrixStack matrixStack, BlockPos chunkSectionOrigin, BufferBuilder builder) {
		int x = chunkSectionOrigin.getX(), z = chunkSectionOrigin.getZ(), y = chunkSectionOrigin.getY();

		if ((y == 0 || y == 64)) {
			matrixStack.push();
//			matrixStack.translate(-x, 0, -z);

			float r = 127 / 255F;
			float g = 170 / 255F;
			float b = 1F;
			float a = 1F;

			float u0 = SPRITE.get().getMinU();
			float v0 = SPRITE.get().getMinV();
			float u1 = SPRITE.get().getMaxU();
			float v1 = SPRITE.get().getMaxV();

			Matrix4f matrix = matrixStack.peek().getModel();
			for (int dX = 0; dX < 16; ++dX) {
				for (int dZ = 0; dZ < 16; ++dZ) {
					builder.vertex(matrix, dX, y + 10, dZ).color(r, g, b, a).texture(u1, v1).light(-1).normal(0, 1, 0).next();
					builder.vertex(matrix, dX, y + 10, dZ + 1).color(r, g, b, a).texture(u1, v0).light(-1).normal(0, 1, 0).next();
					builder.vertex(matrix, dX + 1, y + 10, dZ + 1).color(r, g, b, a).texture(u0, v0).light(-1).normal(0, 1, 0).next();
					builder.vertex(matrix, dX + 1, y + 10, dZ).color(r, g, b, a).texture(u0, v1).light(-1).normal(0, 1, 0).next();
				}
			}

			matrixStack.pop();
		}
	}
}
