package dev.hephaestus.shatteredsky.world.gen.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.hephaestus.seedy.SeedSupplier;
import dev.hephaestus.shatteredsky.ShatteredSky;
import dev.hephaestus.shatteredsky.mixin.world.gen.chunk.ChunkGeneratorSettingsAccessor;
import dev.hephaestus.shatteredsky.util.math.noise.RidgedOctavePerlinNoiseSampler;
import dev.hephaestus.shatteredsky.world.gen.surfacebuilder.SkySurfaceBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.util.math.noise.*;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public final class NoiseChunkGenerator extends ChunkGenerator {
   public static final Codec<NoiseChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> {
      return instance.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter((noiseChunkGenerator) -> {
         return noiseChunkGenerator.populationSource;
      }), Codec.LONG.fieldOf("seed").orElseGet(SeedSupplier::getSeed).stable().forGetter((noiseChunkGenerator) -> {
         return noiseChunkGenerator.seed;
      }), ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter((noiseChunkGenerator) -> {
         return noiseChunkGenerator.settings;
      })).apply(instance, instance.stable(NoiseChunkGenerator::new));
   });
   private static final float[] NOISE_WEIGHT_TABLE = Util.make(new float[13824], (array) -> {
      for(int i = 0; i < 24; ++i) {
         for(int j = 0; j < 24; ++j) {
            for(int k = 0; k < 24; ++k) {
               array[i * 24 * 24 + j * 24 + k] = (float)calculateNoiseWeight(j - 12, k - 12, i - 12);
            }
         }
      }

   });
   private static final float[] BIOME_WEIGHT_TABLE = Util.make(new float[49], (array) -> {
      for(int i = -4; i <= 4; ++i) {
         for(int j = -4; j <= 4; ++j) {
            float f = 10.0F / MathHelper.sqrt((float)(i * i + j * j) + 0.2F);
            array[i + 4 + (j + 4) * 5] = f;
         }
      }

   });
   private static final BlockState AIR;
   private final int verticalNoiseResolution;
   private final int horizontalNoiseResolution;
   private final int noiseSizeX;
   private final int noiseSizeY;
   private final int noiseSizeZ;
   protected final ChunkRandom random;
   private final OctavePerlinNoiseSampler lowerInterpolatedNoise;
   private final OctavePerlinNoiseSampler upperInterpolatedNoise;
   private final OctavePerlinNoiseSampler interpolationNoise;
   private final RidgedOctavePerlinNoiseSampler riverNoise;
   private final NoiseSampler surfaceDepthNoise;
   private final OctavePerlinNoiseSampler densityNoise;
   @Nullable
   protected final BlockState defaultBlock;
   protected final BlockState defaultFluid;
   private final long seed;
   protected final Supplier<ChunkGeneratorSettings> settings;
   private final int worldHeight;

   public NoiseChunkGenerator(BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settings) {
      this(biomeSource, biomeSource, seed, settings);
   }

   private NoiseChunkGenerator(BiomeSource populationSource, BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settings) {
      super(populationSource, biomeSource, ((ChunkGeneratorSettings)settings.get()).getStructuresConfig(), seed);
      this.seed = seed;
      ChunkGeneratorSettings chunkGeneratorSettings = (ChunkGeneratorSettings)settings.get();
      this.settings = settings;
      GenerationShapeConfig generationShapeConfig = chunkGeneratorSettings.getGenerationShapeConfig();
      this.worldHeight = generationShapeConfig.getHeight();
      this.verticalNoiseResolution = generationShapeConfig.getSizeVertical() * 4;
      this.horizontalNoiseResolution = generationShapeConfig.getSizeHorizontal() * 4;
      this.defaultBlock = chunkGeneratorSettings.getDefaultBlock();
      this.defaultFluid = chunkGeneratorSettings.getDefaultFluid();
      this.noiseSizeX = 16 / this.horizontalNoiseResolution;
      this.noiseSizeY = generationShapeConfig.getHeight() / this.verticalNoiseResolution;
      this.noiseSizeZ = 16 / this.horizontalNoiseResolution;
      this.random = new ChunkRandom(seed);
      this.lowerInterpolatedNoise = new OctavePerlinNoiseSampler(this.random, IntStream.rangeClosed(-15, 0));
      this.upperInterpolatedNoise = new OctavePerlinNoiseSampler(this.random, IntStream.rangeClosed(-15, 0));
      this.interpolationNoise = new OctavePerlinNoiseSampler(this.random, IntStream.rangeClosed(-7, 0));
      this.riverNoise = new RidgedOctavePerlinNoiseSampler(this.random, IntStream.rangeClosed(-3, 0));
      this.surfaceDepthNoise = (NoiseSampler)(generationShapeConfig.hasSimplexSurfaceNoise() ? new OctaveSimplexNoiseSampler(this.random, IntStream.rangeClosed(-3, 0)) : new OctavePerlinNoiseSampler(this.random, IntStream.rangeClosed(-3, 0)));
      this.random.consume(2620);
      this.densityNoise = new OctavePerlinNoiseSampler(this.random, IntStream.rangeClosed(-15, 0));
   }

   protected Codec<? extends ChunkGenerator> getCodec() {
      return CODEC;
   }

   @Environment(EnvType.CLIENT)
   public ChunkGenerator withSeed(long seed) {
      return new NoiseChunkGenerator(this.populationSource.withSeed(seed), seed, this.settings);
   }

   public boolean matchesSettings(long seed, RegistryKey<ChunkGeneratorSettings> settingsKey) {
      return this.seed == seed && this.settings.get().equals(settingsKey);
   }

   private double sampleNoise(int x, int y, int z, double horizontalScale, double verticalScale, double horizontalStretch, double verticalStretch) {
      double d = 0.0D;
      double e = 0.0D;
      double f = 0.0D;
      boolean bl = true;
      double g = 1.0D;

      for(int i = 0; i < 16; ++i) {
         double h = OctavePerlinNoiseSampler.maintainPrecision((double)x * horizontalScale * g);
         double j = OctavePerlinNoiseSampler.maintainPrecision((double)y * verticalScale * g);
         double k = OctavePerlinNoiseSampler.maintainPrecision((double)z * horizontalScale * g);
         double l = verticalScale * g;
         PerlinNoiseSampler perlinNoiseSampler = this.lowerInterpolatedNoise.getOctave(i);
         if (perlinNoiseSampler != null) {
            d += perlinNoiseSampler.sample(h, j, k, l, (double)y * l) / g;
         }

         PerlinNoiseSampler perlinNoiseSampler2 = this.upperInterpolatedNoise.getOctave(i);
         if (perlinNoiseSampler2 != null) {
            e += perlinNoiseSampler2.sample(h, j, k, l, (double)y * l) / g;
         }

         if (i < 8) {
            PerlinNoiseSampler perlinNoiseSampler3 = this.interpolationNoise.getOctave(i);
            if (perlinNoiseSampler3 != null) {
               f += perlinNoiseSampler3.sample(OctavePerlinNoiseSampler.maintainPrecision((double)x * horizontalStretch * g), OctavePerlinNoiseSampler.maintainPrecision((double)y * verticalStretch * g), OctavePerlinNoiseSampler.maintainPrecision((double)z * horizontalStretch * g), verticalStretch * g, (double)y * verticalStretch * g) / g;
            }
         }

         g /= 2.0D;
      }

      return MathHelper.clampedLerp(d / 512.0D, e / 512.0D, (f / 10.0D + 1.0D) / 2.0D);
   }

   private double[] sampleNoiseColumn(int x, int z) {
      double[] ds = new double[this.noiseSizeY + 1];
      this.sampleNoiseColumn(ds, x, z);
      return ds;
   }

   private void sampleNoiseColumn(double[] buffer, int x, int z) {
      GenerationShapeConfig generationShapeConfig = this.settings.get().getGenerationShapeConfig();
      double d0;
      double d1;

      int chunksChecked = 0;
      int otherBiomeChunks = 0;

      float g = 0.0F;
      float h = 0.0F;
      float i = 0.0F;
      int seaLevel = this.getSeaLevel();
      Biome biome = this.populationSource.getBiomeForNoiseGen(x, seaLevel, z);
      float depth = biome.getDepth();

      for(int m = -4; m <= 4; ++m) {
         for (int n = -4; n <= 4; ++n) {
            Biome neighbor = this.populationSource.getBiomeForNoiseGen(x + m, seaLevel, z + n);

            ++chunksChecked;

            if (neighbor != biome) {
               ++otherBiomeChunks;
            }
         }
      }

      for(int m = -4; m <= 4; ++m) {
         for(int n = -4; n <= 4; ++n) {
            Biome neighbor = this.populationSource.getBiomeForNoiseGen(x + m, seaLevel, z + n);

            float neighborDepth = neighbor.getDepth();
            float neighborScale = neighbor.getScale();
            float modifiedDepth = neighborDepth;
            float modifiedScale = neighborScale;

            if (generationShapeConfig.isAmplified() && neighborDepth > 0.0F) {
               modifiedDepth = 1.0F + neighborDepth * 2.0F;
               modifiedScale = 1.0F + neighborScale * 4.0F;
            }

            float u = neighborDepth > depth ? 0.5F : 1.0F;
            float v = u * BIOME_WEIGHT_TABLE[m + 4 + (n + 4) * 5] / (modifiedDepth + 2.0F);
            g += modifiedScale * v;
            h += modifiedDepth * v;
            i += v;
         }
      }

      float w = h / i;
      float y = g / i;
      float c = w * 0.5F - 0.125F;
      float d = y * 0.9F + 0.1F;
      d0 = c * 0.265625D;
      d1 = 96.0D / d;

      double xzScale = 684.412D * generationShapeConfig.getSampling().getXZScale();
      double yScale = 684.412D * generationShapeConfig.getSampling().getYScale();
      double modifiedXzScale = xzScale / generationShapeConfig.getSampling().getXZFactor();
      double modifiedYScale = yScale / generationShapeConfig.getSampling().getYFactor();
      double topSlideTarget = generationShapeConfig.getTopSlide().getTarget();
      double topSlideSize = generationShapeConfig.getTopSlide().getSize();
      double topSlideOffset = generationShapeConfig.getTopSlide().getOffset();
      double bottomSlideTarget = generationShapeConfig.getBottomSlide().getTarget();
      double bottomSlideSize = generationShapeConfig.getBottomSlide().getSize();
      double bottomSlideOffset = generationShapeConfig.getBottomSlide().getOffset();
      double density = generationShapeConfig.hasRandomDensityOffset() ? this.getRandomDensityAt(x, z) : 0.0D;
      double densityFactor = generationShapeConfig.getDensityFactor();
      double densityOffset = generationShapeConfig.getDensityOffset();

      if (otherBiomeChunks > 0) {
         float f = 1 + otherBiomeChunks / (float) chunksChecked;
         topSlideSize *= f;
         bottomSlideSize *= f;
      }

      for(int j = 0; j <= this.noiseSizeY; ++j) {
         double noise = this.sampleNoise(x, j, z, xzScale, yScale, modifiedXzScale, modifiedYScale);
         double d3 = 1.0D - (double)j * 2.0D / (double)this.noiseSizeY + density;
         double d4 = d3 * densityFactor + densityOffset;
         double d5 = (d4 + d0) * d1;
         if (d5 > 0.0D) {
            noise += d5 * 4.0D;
         } else {
            noise += d5;
         }

         if (topSlideSize > 0.0D) {
            double d6 = ((double)(this.noiseSizeY - j) - topSlideOffset) / topSlideSize;
            noise = MathHelper.clampedLerp(topSlideTarget, noise, d6);
         }

         if (bottomSlideSize > 0.0D) {
            double d6 = ((double)j - bottomSlideOffset) / bottomSlideSize;
            noise = MathHelper.clampedLerp(bottomSlideTarget, noise, d6);
         }

         buffer[j] = noise;
      }

   }

   private double getRandomDensityAt(int x, int z) {
      double d = this.densityNoise.sample((double)(x * 200), 10.0D, (double)(z * 200), 1.0D, 0.0D, true);
      double f;
      if (d < 0.0D) {
         f = -d * 0.3D;
      } else {
         f = d;
      }

      double g = f * 24.575625D - 2.0D;
      return g < 0.0D ? g * 0.009486607142857142D : Math.min(g, 1.0D) * 0.006640625D;
   }

   public int getHeight(int x, int z, Heightmap.Type heightmapType) {
      return this.sampleHeightmap(x, z, null, heightmapType.getBlockPredicate());
   }

   public BlockView getColumnSample(int x, int z) {
      BlockState[] blockStates = new BlockState[this.noiseSizeY * this.verticalNoiseResolution];
      this.sampleHeightmap(x, z, blockStates, null);
      return new VerticalBlockSample(blockStates);
   }

   private int sampleHeightmap(int x, int z, @Nullable BlockState[] states, @Nullable Predicate<BlockState> predicate) {
      int i = Math.floorDiv(x, this.horizontalNoiseResolution);
      int j = Math.floorDiv(z, this.horizontalNoiseResolution);
      int k = Math.floorMod(x, this.horizontalNoiseResolution);
      int l = Math.floorMod(z, this.horizontalNoiseResolution);
      double d = (double)k / (double)this.horizontalNoiseResolution;
      double e = (double)l / (double)this.horizontalNoiseResolution;
      double[][] ds = new double[][]{this.sampleNoiseColumn(i, j), this.sampleNoiseColumn(i, j + 1), this.sampleNoiseColumn(i + 1, j), this.sampleNoiseColumn(i + 1, j + 1)};

      for(int m = this.noiseSizeY - 1; m >= 0; --m) {
         double f = ds[0][m];
         double g = ds[1][m];
         double h = ds[2][m];
         double n = ds[3][m];
         double o = ds[0][m + 1];
         double p = ds[1][m + 1];
         double q = ds[2][m + 1];
         double r = ds[3][m + 1];

         for(int s = this.verticalNoiseResolution - 1; s >= 0; --s) {
            double t = (double)s / (double)this.verticalNoiseResolution;
            double u = MathHelper.lerp3(t, d, e, f, o, h, q, g, p, n, r);
            int v = m * this.verticalNoiseResolution + s;
            BlockState blockState = this.getBlockState(u, v);
            if (states != null) {
               states[v] = blockState;
            }

            if (predicate != null && predicate.test(blockState)) {
               return v + 1;
            }
         }
      }

      return 0;
   }

   protected BlockState getBlockState(double density, int y) {
      BlockState blockState3;
      if (density > 0.0D) {
         blockState3 = this.defaultBlock;
      } else if (y < this.getSeaLevel() && y > 10) {
         blockState3 = this.defaultFluid;
      } else {
         blockState3 = AIR;
      }

      return blockState3;
   }

   public void buildSurface(ChunkRegion region, Chunk chunk) {
      ChunkPos chunkPos = chunk.getPos();
      int i = chunkPos.x;
      int j = chunkPos.z;
      ChunkRandom chunkRandom = new ChunkRandom();
      chunkRandom.setTerrainSeed(i, j);
      ChunkPos chunkPos2 = chunk.getPos();
      int k = chunkPos2.getStartX();
      int l = chunkPos2.getStartZ();
      BlockPos.Mutable mutable = new BlockPos.Mutable();

      for(int m = 0; m < 16; ++m) {
         for(int n = 0; n < 16; ++n) {
            int x = k + m;
            int z = l + n;
            int q = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, m, n) + 1;
            double surfaceDepthNoise = this.surfaceDepthNoise.sample((double)x * 0.0625D, (double)z * 0.0625D, 0.0625D, (double)m * 0.0625D) * 15.0D;
            double riverNoise = this.riverNoise.sample((double)x * 0.0625D, (double)z * 0.0625D, 0.0625D, (double)m * 0.0625D) * 15.0D;
            Biome biome = region.getBiome(mutable.set(k + m, q, l + n));
            ConfiguredSurfaceBuilder<?> configuredSurfaceBuilder = biome.getGenerationSettings().getSurfaceBuilder().get();
            configuredSurfaceBuilder.initSeed(seed);

            if (configuredSurfaceBuilder.surfaceBuilder instanceof SkySurfaceBuilder) {
               ((SkySurfaceBuilder) configuredSurfaceBuilder.surfaceBuilder).generate(random, chunk, biome, x, z, worldHeight, surfaceDepthNoise, defaultBlock, defaultFluid, this.getSeaLevel(), seed, (TernarySurfaceConfig) configuredSurfaceBuilder.config, riverNoise);
            } else {
               configuredSurfaceBuilder.generate(random, chunk, biome, x, z, worldHeight, surfaceDepthNoise, defaultBlock, defaultFluid, this.getSeaLevel(), seed);
            }
         }
      }

      this.buildBedrock(chunk, chunkRandom);
   }

   private void buildBedrock(Chunk chunk, Random random) {
      BlockPos.Mutable mutable = new BlockPos.Mutable();
      int i = chunk.getPos().getStartX();
      int j = chunk.getPos().getStartZ();
      ChunkGeneratorSettings chunkGeneratorSettings = this.settings.get();
      int k = chunkGeneratorSettings.getBedrockFloorY();
      int l = this.worldHeight - 1 - chunkGeneratorSettings.getBedrockCeilingY();
      boolean bl = l + 4 >= 0 && l < this.worldHeight;
      boolean bl2 = k + 4 >= 0 && k < this.worldHeight;
      if (bl || bl2) {
         Iterator var12 = BlockPos.iterate(i, 0, j, i + 15, 0, j + 15).iterator();

         while(true) {
            BlockPos blockPos;
            int o;
            do {
               if (!var12.hasNext()) {
                  return;
               }

               blockPos = (BlockPos)var12.next();
               if (bl) {
                  for(o = 0; o < 5; ++o) {
                     if (o <= random.nextInt(5)) {
                        chunk.setBlockState(mutable.set(blockPos.getX(), l - o, blockPos.getZ()), Blocks.BEDROCK.getDefaultState(), false);
                     }
                  }
               }
            } while(!bl2);

            for(o = 4; o >= 0; --o) {
               if (o <= random.nextInt(5)) {
                  chunk.setBlockState(mutable.set(blockPos.getX(), k + o, blockPos.getZ()), Blocks.BEDROCK.getDefaultState(), false);
               }
            }
         }
      }
   }

   private double density(double[][][] noiseData, int noiseY, int noiseZ, int pieceX, int pieceY, int pieceZ, int realX, int realY, int realZ, ObjectList<StructurePiece> structurePieces, ObjectList<JigsawJunction> jigsaws) {
      // Lower samples
      double x0z0y0 = noiseData[0][noiseZ][noiseY];
      double x0z1y0 = noiseData[0][noiseZ + 1][noiseY];
      double x1z0y0 = noiseData[1][noiseZ][noiseY];
      double x1z1y0 = noiseData[1][noiseZ + 1][noiseY];

      // Upper samples
      double x0z0y1 = noiseData[0][noiseZ][noiseY + 1];
      double x0z1y1 = noiseData[0][noiseZ + 1][noiseY + 1];
      double x1z0y1 = noiseData[1][noiseZ][noiseY + 1];
      double x1z1y1 = noiseData[1][noiseZ + 1][noiseY + 1];

      // progress withing loop
      double yLerp = (double) pieceY / (double)this.verticalNoiseResolution;

      // Interpolate noise data based on y progress
      double x0z0 = MathHelper.lerp(yLerp, x0z0y0, x0z0y1);
      double x1z0 = MathHelper.lerp(yLerp, x1z0y0, x1z0y1);
      double x0z1 = MathHelper.lerp(yLerp, x0z1y0, x0z1y1);
      double x1z1 = MathHelper.lerp(yLerp, x1z1y0, x1z1y1);

      double xLerp = (double) pieceX / (double)this.horizontalNoiseResolution;
      // Interpolate noise based on x progress
      double z0 = MathHelper.lerp(xLerp, x0z0, x1z0);
      double z1 = MathHelper.lerp(xLerp, x0z1, x1z1);

      double zLerp = (double) pieceZ / (double)this.horizontalNoiseResolution;
      // Get the real noise here by interpolating the last 2 noises together
      double rawNoise = MathHelper.lerp(zLerp, z0, z1);
      // Normalize the noise from [-256, 256] to [-1, 1]
      double density = MathHelper.clamp(rawNoise / 200.0D, -1.0D, 1.0D);

      // Iterate through structures to add density
      density = density / 2.0D - density * density * density / 24.0D;
      for (StructurePiece structurePiece : structurePieces) {
         BlockBox blockBox = structurePiece.getBoundingBox();
         int structureX = Math.max(0, Math.max(blockBox.minX - realX, realX - blockBox.maxX));
         int structureY = realY - (blockBox.minY + (structurePiece instanceof PoolStructurePiece ? ((PoolStructurePiece)structurePiece).getGroundLevelDelta() : 0));
         int structureZ = Math.max(0, Math.max(blockBox.minZ - realZ, realZ - blockBox.maxZ));

         density += getNoiseWeight(structureX, structureY, structureZ) * 0.8D;
      }

      // Iterate through jigsaws to add density
      for (JigsawJunction jigsawJunction : jigsaws) {
         int sourceX = realX - jigsawJunction.getSourceX();
         int sourceY = realY - jigsawJunction.getSourceGroundY();
         int sourceZ = realZ - jigsawJunction.getSourceZ();
         density += getNoiseWeight(sourceX, sourceY, sourceZ) * 0.4D;
      }

      return density;
   }

   public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk) {
      ObjectList<StructurePiece> structurePieces = new ObjectArrayList<>(10);
      ObjectList<JigsawJunction> jigsaws = new ObjectArrayList<>(32);
      ChunkPos pos = chunk.getPos();
      int chunkX = pos.x;
      int chunkZ = pos.z;
      int chunkStartX = chunkX << 4;
      int chunkStartZ = chunkZ << 4;

      for (StructureFeature<?> feature : StructureFeature.JIGSAW_STRUCTURES) {
         accessor.getStructuresWithChildren(ChunkSectionPos.from(pos, 0), feature).forEach(start -> {
            Iterator<StructurePiece> pieces = start.getChildren().iterator();

            while (true) {
               StructurePiece piece;
               do {
                  if (!pieces.hasNext()) {
                     return;
                  }

                  piece = pieces.next();
               } while (!piece.intersectsChunk(pos, 12));

               if (piece instanceof PoolStructurePiece) {
                  PoolStructurePiece pool = (PoolStructurePiece) piece;
                  StructurePool.Projection projection = pool.getPoolElement().getProjection();
                  if (projection == StructurePool.Projection.RIGID) {
                     structurePieces.add(pool);
                  }

                  // Add junctions that fit within the general chunk area
                  for (JigsawJunction junction : pool.getJunctions()) {
                     int sourceX = junction.getSourceX();
                     int sourceZ = junction.getSourceZ();
                     if (sourceX > chunkStartX - 12 && sourceZ > chunkStartZ - 12 && sourceX < chunkStartX + 15 + 12 && sourceZ < chunkStartZ + 15 + 12) {
                        jigsaws.add(junction);
                     }
                  }
               } else {
                  structurePieces.add(piece);
               }
            }
         });
      }

      // Holds the rolling noise data for this chunk
      // Instead of being noise[4 * 32 * 4] it's actually noise [2 * 5 * 33] to reuse noise data when moving onto the next column on the x axis.
      // This could probably be optimized but I'm a bit too lazy to figure out the best way to do so :P
      double[][][] noiseData = new double[2][this.noiseSizeZ + 1][this.noiseSizeY + 1];
      double[][][] densities = new double[16][256][16];

      // Initialize noise data on the x0 column.
      for(int noiseZ = 0; noiseZ < this.noiseSizeZ + 1; ++noiseZ) {
         noiseData[0][noiseZ] = new double[this.noiseSizeY + 1];
         this.sampleNoiseColumn(noiseData[0][noiseZ], chunkX * this.noiseSizeX, chunkZ * this.noiseSizeZ + noiseZ);
         noiseData[1][noiseZ] = new double[this.noiseSizeY + 1];
      }

      ProtoChunk protoChunk = (ProtoChunk)chunk;
      Heightmap oceanFloor = protoChunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
      Heightmap worldSurface = protoChunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
      BlockPos.Mutable mutable = new BlockPos.Mutable();

      // [0, 4] -> x noise chunks
      for(int noiseX = 0; noiseX < this.noiseSizeX; ++noiseX) {
         // Initialize noise data on the x1 column
         int noiseZ;
         for(noiseZ = 0; noiseZ < this.noiseSizeZ + 1; ++noiseZ) {
            this.sampleNoiseColumn(noiseData[1][noiseZ], chunkX * this.noiseSizeX + noiseX + 1, chunkZ * this.noiseSizeZ + noiseZ);
         }

         // [0, 4] -> z noise chunks
         for(noiseZ = 0; noiseZ < this.noiseSizeZ; ++noiseZ) {
            ChunkSection section = protoChunk.getSection(15);
            section.lock();

            // [0, 32] -> y noise chunks
            for(int noiseY = this.noiseSizeY - 1; noiseY >= 0; --noiseY) {
               // [0, 8] -> y noise pieces
               for(int pieceY = this.verticalNoiseResolution - 1; pieceY >= 0; --pieceY) {
                  int realY = noiseY * this.verticalNoiseResolution + pieceY;
                  int localY = realY & 15;
                  int sectionY = realY >> 4;
                  // Get the chunk section
                  if (section.getYOffset() >> 4 != sectionY) {
                     section.unlock();
                     section = protoChunk.getSection(sectionY);
                     section.lock();
                  }

                  // [0, 4] -> x noise pieces
                  for(int pieceX = 0; pieceX < this.horizontalNoiseResolution; ++pieceX) {
                     int realX = chunkStartX + noiseX * this.horizontalNoiseResolution + pieceX;
                     int localX = realX & 15;

                     // [0, 4] -> z noise pieces
                     for(int pieceZ = 0; pieceZ < this.horizontalNoiseResolution; ++pieceZ) {
                        int realZ = chunkStartZ + noiseZ * this.horizontalNoiseResolution + pieceZ;
                        int localZ = realZ & 15;

                        double density = density(noiseData, noiseY, noiseZ, pieceX, pieceY, pieceZ, realX, realY, realZ, structurePieces, jigsaws);
                        densities[localX][realY][localZ] = density;

                        // Get the blockstate based on the y and density
                        BlockState state = this.getBlockState(density, realY);

                        if (state != AIR) {
                           // Add light source if the state has light
                           if (state.getLuminance() != 0) {
                              mutable.set(realX, realY, realZ);
                              protoChunk.addLightSource(mutable);
                           }

                           // Place the state at the position
                           section.setBlockState(localX, localY, localZ, state, false);


                           // Track heightmap data
                           oceanFloor.trackUpdate(localX, realY, localZ, state);
                           worldSurface.trackUpdate(localX, realY, localZ, state);
                        }

                        if (realY < 254) {
                           double previous = densities[localX][realY + 1][localZ];
                           if (previous > densities[localX][realY + 2][localZ] && previous > density && previous > 0) {
                              mutable.set(localX, realY, localZ);
                              chunk.setBlockState(mutable, ShatteredSky.Blocks.WORLDGEN_DUMMY.getDefaultState(), false);
                           }
                        }
                     }
                  }
               }
            }

            section.unlock();
         }

         // Reuse noise data from the previous column for speed
         double[][] xColumn = noiseData[0];
         noiseData[0] = noiseData[1];
         noiseData[1] = xColumn;
      }

   }

   private static double getNoiseWeight(int x, int y, int z) {
      int i = x + 12;
      int j = y + 12;
      int k = z + 12;
      if (i >= 0 && i < 24) {
         if (j >= 0 && j < 24) {
            return k >= 0 && k < 24 ? (double)NOISE_WEIGHT_TABLE[k * 24 * 24 + i * 24 + j] : 0.0D;
         } else {
            return 0.0D;
         }
      } else {
         return 0.0D;
      }
   }

   private static double calculateNoiseWeight(int x, int y, int z) {
      double d = (double)(x * x + z * z);
      double e = (double)y + 0.5D;
      double f = e * e;
      double g = Math.pow(2.718281828459045D, -(f / 16.0D + d / 16.0D));
      double h = -e * MathHelper.fastInverseSqrt(f / 2.0D + d / 2.0D) / 2.0D;
      return h * g;
   }

   public int getWorldHeight() {
      return this.worldHeight;
   }

   public int getSeaLevel() {
      return ((ChunkGeneratorSettings)this.settings.get()).getSeaLevel();
   }

   public List<SpawnSettings.SpawnEntry> getEntitySpawnList(Biome biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos) {
      if (accessor.getStructureAt(pos, true, StructureFeature.SWAMP_HUT).hasChildren()) {
         if (group == SpawnGroup.MONSTER) {
            return StructureFeature.SWAMP_HUT.getMonsterSpawns();
         }

         if (group == SpawnGroup.CREATURE) {
            return StructureFeature.SWAMP_HUT.getCreatureSpawns();
         }
      }

      if (group == SpawnGroup.MONSTER) {
         if (accessor.getStructureAt(pos, false, StructureFeature.PILLAGER_OUTPOST).hasChildren()) {
            return StructureFeature.PILLAGER_OUTPOST.getMonsterSpawns();
         }

         if (accessor.getStructureAt(pos, false, StructureFeature.MONUMENT).hasChildren()) {
            return StructureFeature.MONUMENT.getMonsterSpawns();
         }

         if (accessor.getStructureAt(pos, true, StructureFeature.FORTRESS).hasChildren()) {
            return StructureFeature.FORTRESS.getMonsterSpawns();
         }
      }

      return super.getEntitySpawnList(biome, accessor, group, pos);
   }

   public void populateEntities(ChunkRegion region) {
      if (!((ChunkGeneratorSettingsAccessor) (Object) this.settings.get()).isMobGenerationDisabled()) {
         int i = region.getCenterChunkX();
         int j = region.getCenterChunkZ();
         Biome biome = region.getBiome((new ChunkPos(i, j)).getStartPos());
         ChunkRandom chunkRandom = new ChunkRandom();
         chunkRandom.setPopulationSeed(region.getSeed(), i << 4, j << 4);
         SpawnHelper.populateEntities(region, biome, i, j, chunkRandom);
      }
   }

   static {
      AIR = Blocks.AIR.getDefaultState();
   }
}
