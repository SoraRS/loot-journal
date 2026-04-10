package dev.obscuria.lootjournal.client.renderer;

import dev.obscuria.lootjournal.config.ConfigCache;

public enum GrowthDirection {
    NATURAL,
    UP,
    DOWN;

    public int calculate(int index, int step) {
        return index * (resolve() == UP ? -step : step);
    }

    public GrowthDirection resolve() {
        return this == NATURAL ? ConfigCache.anchor.direction() : this;
    }
}