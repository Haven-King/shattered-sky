package dev.hephaestus.shatteredsky.mixin.client.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(SkyProperties.class)
public interface SkyPropertiesAccessor {
	@Accessor("BY_IDENTIFIER")
	static Object2ObjectMap<Identifier, SkyProperties> getIdMap() {
		throw new UnsupportedOperationException();
	}
}
