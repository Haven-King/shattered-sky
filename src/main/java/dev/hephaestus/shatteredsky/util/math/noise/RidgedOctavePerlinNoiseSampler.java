package dev.hephaestus.shatteredsky.util.math.noise;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.world.gen.ChunkRandom;

import java.util.List;
import java.util.stream.IntStream;

public class RidgedOctavePerlinNoiseSampler implements NoiseSampler {
    private final PerlinNoiseSampler[] octaveSamplers;
    private final DoubleList field_26445;
    private final double field_20659;
    private final double field_20660;

    private RidgedOctavePerlinNoiseSampler(ChunkRandom chunkRandom, Pair<Integer, DoubleList> pair) {
        int i = pair.getFirst();
        this.field_26445 = pair.getSecond();
        PerlinNoiseSampler perlinNoiseSampler = new PerlinNoiseSampler(chunkRandom);
        int j = this.field_26445.size();
        int k = -i;
        this.octaveSamplers = new PerlinNoiseSampler[j];
        if (k >= 0 && k < j) {
            double d = this.field_26445.getDouble(k);
            if (d != 0.0D) {
                this.octaveSamplers[k] = perlinNoiseSampler;
            }
        }

        for(int l = k - 1; l >= 0; --l) {
            if (l < j || this.field_26445.getDouble(l) == 0D) {
                this.octaveSamplers[l] = new PerlinNoiseSampler(chunkRandom);
            } else {
                chunkRandom.consume(262);
            }
        }

        if (k < j - 1) {
            long m = (long)(perlinNoiseSampler.sample(0.0D, 0.0D, 0.0D, 0.0D, 0.0D) * 9.223372036854776E18D);
            ChunkRandom chunkRandom2 = new ChunkRandom(m);

            for(int n = k + 1; n < j; ++n) {
                if (n >= 0) {
                    double f = this.field_26445.getDouble(n);
                    if (f != 0.0D) {
                        this.octaveSamplers[n] = new PerlinNoiseSampler(chunkRandom2);
                    } else {
                        chunkRandom2.consume(262);
                    }
                } else {
                    chunkRandom2.consume(262);
                }
            }
        }

        this.field_20660 = Math.pow(2.0D, -k);
        this.field_20659 = Math.pow(2.0D, j - 1) / (Math.pow(2.0D, j) - 1.0D);
    }

    public RidgedOctavePerlinNoiseSampler(ChunkRandom random, IntStream octaves) {
        this(random, octaves.boxed().collect(ImmutableList.toImmutableList()));
    }

    private RidgedOctavePerlinNoiseSampler(ChunkRandom random, IntSortedSet octaves) {
        this(random, method_30848(octaves));
    }

    public RidgedOctavePerlinNoiseSampler(ChunkRandom random, List<Integer> octaves) {
        this(random, new IntRBTreeSet(octaves));
    }

    @Override
    public double sample(double x, double y, double d, double e) {
        return this.sample(x, y, 0.0D, d, e, false);
    }

    public double sample(double x, double y, double z) {
        return this.sample(x, y, z, 0.0D, 0.0D, false);
    }

    public double sample(double x, double y, double z, double d, double e, boolean bl) {
        double f = 0.0D;
        double g = this.field_20660;
        double h = this.field_20659;

        for(int i = 0; i < this.octaveSamplers.length; ++i) {
            PerlinNoiseSampler perlinNoiseSampler = this.octaveSamplers[i];
            if (perlinNoiseSampler != null) {
                f += 1D - Math.abs(this.field_26445.getDouble(i) * perlinNoiseSampler.sample(maintainPrecision(x * g), bl ? -perlinNoiseSampler.originY : maintainPrecision(y * g), maintainPrecision(z * g), d * g, e * g) * h);
            }

            g *= 2.0D;
            h /= 2.0D;
        }

        return f;
    }

    private static Pair<Integer, DoubleList> method_30848(IntSortedSet intSortedSet) {
        if (intSortedSet.isEmpty()) {
            throw new IllegalArgumentException("Need some octaves!");
        } else {
            int i = -intSortedSet.firstInt();
            int j = intSortedSet.lastInt();
            int k = i + j + 1;
            if (k < 1) {
                throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
            } else {
                DoubleList doubleList = new DoubleArrayList(new double[k]);
                IntBidirectionalIterator intBidirectionalIterator = intSortedSet.iterator();

                while(intBidirectionalIterator.hasNext()) {
                    int l = intBidirectionalIterator.nextInt();
                    doubleList.set(l + i, 1.0D);
                }

                return Pair.of(-i, doubleList);
            }
        }
    }

    public static double maintainPrecision(double d) {
        return d - (double) MathHelper.lfloor(d / 3.3554432E7D + 0.5D) * 3.3554432E7D;
    }
}
