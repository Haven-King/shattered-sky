package dev.hephaestus.shatteredsky.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;

public class SkyStoneBlock extends Block {
    public static Property<Boolean> SHATTERED = BooleanProperty.of("shattered");

    public SkyStoneBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(SHATTERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHATTERED);
    }
}
