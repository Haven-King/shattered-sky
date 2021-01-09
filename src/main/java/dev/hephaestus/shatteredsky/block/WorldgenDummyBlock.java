package dev.hephaestus.shatteredsky.block;

import dev.hephaestus.shatteredsky.ShatteredSky;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public class WorldgenDummyBlock extends Block {
    public static final EnumProperty<Type> TYPE = EnumProperty.of("type", Type.class);

    public WorldgenDummyBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    public enum Type implements StringIdentifiable {
        STONE(ShatteredSky.Blocks.SKY_STONE.getDefaultState()),
        SUBSTRATE(Blocks.DIRT.getDefaultState()),
        TOP_MATERIAL(Blocks.GRASS_BLOCK.getDefaultState());

        private final BlockState state;

        Type(BlockState state) {
            this.state = state;
        }

        public BlockState getState() {
            return this.state;
        }

        @Override
        public String asString() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
