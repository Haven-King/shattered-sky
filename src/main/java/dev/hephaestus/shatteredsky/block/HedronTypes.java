package dev.hephaestus.shatteredsky.block;

import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HedronTypes {
    private static final Map<Identifier, HedronType> TYPES = new HashMap<>();

    public static void register(Identifier id, HedronType type) {
        TYPES.put(id, type);
    }

    public static void register(Identifier id, SpriteIdentifier face, SpriteIdentifier inner, SpriteIdentifier base) {
        register(id, new HedronType(id, face, inner, base));
    }

    public static void register(Identifier id, SpriteIdentifier sprite) {
        register(id, sprite, sprite, sprite);
    }

    public static Iterator<HedronType> getTypes() {
        return TYPES.values().iterator();
    }
}
