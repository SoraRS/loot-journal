package dev.obscuria.lootjournal.client.renderer.layout;

import dev.obscuria.lootjournal.client.renderer.layout.tokens.*;

import java.util.ArrayList;

public final class LayoutParser {

    public static PickupLayout parse(String input) {
        var normalized = normalize(input);
        var parts = normalized.split("\\s+");
        var tokens = new ArrayList<LayoutToken>();

        for (var part : parts) {
            tokens.add(part.matches("\\d+")
                    ? new GapToken(Integer.parseInt(part))
                    : createElement(part));
        }

        return new PickupLayout(tokens);
    }

    private static String normalize(String input) {
        return input
                .replace("+", " ")
                .replace("-", " ")
                .replace("px", "");
    }

    private static LayoutToken createElement(String id) {
        return switch (id.toUpperCase()) {
            case "ICON" -> IconToken.SHARED;
            case "NAME" -> NameToken.SHARED;
            case "COUNT" -> CountToken.SHARED;
            case "TOTAL" -> TotalToken.SHARED;
            default -> throw new IllegalArgumentException("Unknown token: " + id);
        };
    }
}
