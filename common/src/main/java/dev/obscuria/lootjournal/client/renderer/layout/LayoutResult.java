package dev.obscuria.lootjournal.client.renderer.layout;

import dev.obscuria.lootjournal.client.renderer.PickupRenderUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class LayoutResult {

    private final List<LayoutEntry> entries = new ArrayList<>();
    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;

    public void add(LayoutEntry entry) {
        entries.add(entry);
        minX = Math.min(minX, entry.x());
        maxX = Math.max(maxX, entry.x() + entry.width());
    }

    public List<LayoutEntry> getAll() {
        return entries;
    }

    public @Nullable LayoutEntry findFirst(String id) {
        for (var entry : entries) {
            if (!entry.token().id().equals(id)) continue;
            return entry;
        }
        return null;
    }

    public int width() {
        if (entries.isEmpty()) return 0;
        return (maxX - minX);
    }

    public int height() {
        return PickupRenderUtils.PICKUP_HEIGHT;
    }

    public int minX() {
        return entries.isEmpty() ? 0 : minX;
    }

    public int maxX() {
        return entries.isEmpty() ? 0 : maxX;
    }

    public int centerX() {
        return minX() + width() / 2;
    }

    public int centerY() {
        return height() / 2;
    }
}