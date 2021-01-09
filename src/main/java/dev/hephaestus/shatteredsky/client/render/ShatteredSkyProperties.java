package dev.hephaestus.shatteredsky.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.source.BiomeAccess;

@Environment(EnvType.CLIENT)
public class ShatteredSkyProperties extends SkyProperties {
	public ShatteredSkyProperties() {
		super(-256, true, SkyType.NORMAL, false, false);
	}

	@Override
	public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
		double r = 0, g = 0, b = 0;

		int radius = 10;

		BiomeAccess biomeAccess = MinecraftClient.getInstance().world.getBiomeAccess();
		BlockPos pos = MinecraftClient.getInstance().cameraEntity.getBlockPos();
		for (BlockPos blockPos : BlockPos.iterate(pos.add(-radius, 0, -radius), pos.add(radius, 0, radius))) {
			int here = biomeAccess.getBiome(blockPos).getFogColor();
			r += (here >> 16) & 0xFF;
			g += (here >> 8) & 0xFF;
			b += here & 0xFF;
		}

		int total = (radius * 2 + 1) * (radius * 2 + 1);
		r /= total;
		g /= total;
		b /= total;

		return new Vec3d(r / 255, g / 255, b / 255).multiply(
				sunHeight * 0.94F + 0.06F,
				sunHeight * 0.94F + 0.06F,
				sunHeight * 0.91F + 0.09F
		);
	}

	@Override
	public boolean useThickFog(int camX, int camY) {
		return false;
	}
}
