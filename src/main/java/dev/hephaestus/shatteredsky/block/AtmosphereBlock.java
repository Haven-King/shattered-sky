package dev.hephaestus.shatteredsky.block;

import dev.hephaestus.shatteredsky.WorldGen;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AtmosphereBlock extends PsuedoFluidBlock implements BlockColorProvider {
	public AtmosphereBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected int getTickRate(World world) {
		return 3;
	}

	@Override
	public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
		if (world != null) {
			return world.getColor(pos, WorldGen.SKY_COLOR);
		}

		return 0xFF7FAAFF;
	}
}
