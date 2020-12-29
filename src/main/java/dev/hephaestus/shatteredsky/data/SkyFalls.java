package dev.hephaestus.shatteredsky.data;

import dev.hephaestus.shatteredsky.util.SyncedChunkData;
import dev.hephaestus.shatteredsky.util.TickableChunkData;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.WorldChunk;

public class SkyFalls extends SyncedChunkData implements TickableChunkData {
	private final Chunk chunk;
	private final Fall[] falls;
	private final boolean[] tickedLastTick;

	public SkyFalls(Chunk chunk) {
		this.chunk = chunk;

		falls = new Fall[256];
		tickedLastTick = new boolean[256];
	}

	@Override
	public void copy(ProtoChunk from, WorldChunk to) {

	}

	public Fall getFall(BlockPos pos) {
		int i = Math.floorMod(pos.getX(), 16) + 16 * Math.floorMod(pos.getZ(), 16);
		return falls[i];
	}

	@Override
	public CompoundTag serialize(ServerWorld world, Chunk chunk, CompoundTag compoundTag) {
		ListTag list = new ListTag();

		for (Fall fall : falls) {
			if (fall != null) {
				CompoundTag tag = new CompoundTag();
				tag.putString("fluid", Registry.FLUID.getId(fall.fluid).toString());
				tag.putInt("start", fall.start);
				tag.putInt("end", fall.end);

				list.add(tag);
			}
		}

		compoundTag.put("SkyFalls", list);

		return compoundTag;
	}

	@Override
	public CompoundTag deserialize(World world, Chunk chunk, CompoundTag compoundTag) {
		ListTag list = compoundTag.getList("SkyFalls", NbtType.COMPOUND);

		for (int i = 0; i < list.size(); ++i) {
			CompoundTag tag = list.getCompound(i);
			Fluid fluid = Registry.FLUID.get(new Identifier(tag.getString("fluid")));

			if (fluid != Fluids.EMPTY) {
				falls[i] = new Fall(fluid);
				falls[i].start = tag.getInt("start");
				falls[i].end = tag.getInt("end");
			}
		}

		return compoundTag;
	}

	public void tick(BlockPos pos, Fluid fluid) {
		tick(Math.floorMod(pos.getX(), 16), Math.floorMod(pos.getZ(), 16), fluid);
	}

	public void tick(int x, int z, Fluid fluid) {
		int i = x + 16 * z;

		if (falls[i] == null) {
			falls[i] = new Fall(fluid);
			falls[i].start = 0;
			falls[i].end = -1;
		}

		tickedLastTick[i] = true;

		this.markDirty();
	}

	@Override
	public void tick(ServerWorld world) {
		for (int i = 0; i < falls.length; ++i) {
			if (falls[i] != null && world.getTime() % falls[i].fluid.getTickRate(world) == 0) {
				falls[i].end -= 1;

				if (!tickedLastTick[i]) {
					falls[i].start -= 1;
				}

				if (falls[i].start <= -256) {
					falls[i] = null;
				}

				this.markDirty();
			}
		}
	}

	public static final class Fall {
		private final Fluid fluid;
		private int start;
		private int end;

		private Fall(Fluid fluid) {
			this.fluid = fluid;
			this.start = 0;
			this.end = -1;
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return end;
		}

		public Fluid getFluid() {
			return fluid;
		}
	}
}
