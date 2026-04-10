package dev.obscuria.lootjournal.client.renderer.layout;

import dev.obscuria.lootjournal.client.renderer.layout.tokens.LayoutToken;

public record LayoutEntry(LayoutToken token, int x, int width) {

    public int centerX() {
        return x + width / 2;
    }
}
