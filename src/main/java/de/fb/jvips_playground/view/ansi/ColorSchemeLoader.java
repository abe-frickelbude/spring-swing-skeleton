package de.fb.jvips_playground.view.ansi;

import java.awt.Color;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Ibragim Kuliev
 *
 */
final class ColorSchemeLoader {

    private static final Logger log = LoggerFactory.getLogger(ColorSchemeLoader.class);

    private static final String SUFFIX = ".yml";
    private static final Map<String, Pair<String, String>> BASE_COLORS;

    /*
     * ANSI color code -> pair of (normal color, bright color)
     */
    static {
        BASE_COLORS = new HashMap<>();

        // "reset" mapping (using default foreground color)
        BASE_COLORS.put(AnsiConstants.RESET, new ImmutablePair<>("default-foreground", "default-foreground"));

        BASE_COLORS.put(AnsiConstants.DEFAULT_FOREGROUND, new ImmutablePair<>("default-foreground", "default-foreground"));
        BASE_COLORS.put(AnsiConstants.DEFAULT_BACKGROUND, new ImmutablePair<>("default-background", "default-background"));

        BASE_COLORS.put(AnsiConstants.BLACK, new ImmutablePair<>("black", "bright-black"));
        BASE_COLORS.put(AnsiConstants.RED, new ImmutablePair<>("red", "bright-red"));
        BASE_COLORS.put(AnsiConstants.GREEN, new ImmutablePair<>("green", "bright-green"));
        BASE_COLORS.put(AnsiConstants.YELLOW, new ImmutablePair<>("yellow", "bright-yellow"));
        BASE_COLORS.put(AnsiConstants.BLUE, new ImmutablePair<>("blue", "bright-blue"));
        BASE_COLORS.put(AnsiConstants.MAGENTA, new ImmutablePair<>("magenta", "bright-magenta"));
        BASE_COLORS.put(AnsiConstants.CYAN, new ImmutablePair<>("cyan", "bright-cyan"));
        BASE_COLORS.put(AnsiConstants.WHITE, new ImmutablePair<>("white", "bright-white"));

        BASE_COLORS.put(AnsiConstants.BLACK_BACKGROUND, new ImmutablePair<>("black", "bright-black"));
        BASE_COLORS.put(AnsiConstants.RED_BACKGROUND, new ImmutablePair<>("red", "bright-red"));
        BASE_COLORS.put(AnsiConstants.GREEN_BACKGROUND, new ImmutablePair<>("green", "bright-green"));
        BASE_COLORS.put(AnsiConstants.YELLOW_BACKGROUND, new ImmutablePair<>("yellow", "bright-yellow"));
        BASE_COLORS.put(AnsiConstants.BLUE_BACKGROUND, new ImmutablePair<>("blue", "bright-blue"));
        BASE_COLORS.put(AnsiConstants.MAGENTA_BACKGROUND, new ImmutablePair<>("magenta", "bright-magenta"));
        BASE_COLORS.put(AnsiConstants.CYAN_BACKGROUND, new ImmutablePair<>("cyan", "bright-cyan"));
        BASE_COLORS.put(AnsiConstants.WHITE_BACKGROUND, new ImmutablePair<>("white", "bright-white"));
    }

    private ColorSchemeLoader() {
        // prevent instantiation
    }

    /**
     * Loads an ANSI color scheme from the specified YAML file.
     *
     *
     * @param schemeName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Pair<Color, Color>> loadScheme(final String schemeName) {

        // TODO: perhaps recover instead by returning an empty scheme?
        if (StringUtils.isBlank(schemeName)) {
            throw new IllegalArgumentException("Scheme name must not be blank!");
        }

        Map<String, Pair<Color, Color>> colorMap = Collections.emptyMap();
        try (final InputStream schemeResource = ColorSchemeLoader.class.getResourceAsStream(schemeName + SUFFIX)) {

            final Yaml yamlLoader = new Yaml();
            final Map<String, String> colorScheme = Map.class.cast(yamlLoader.load(schemeResource));
            colorMap = new HashMap<>(colorScheme.size());

            // parse colors and arrange them in pairs of (normal color, bright color) per ANSI color code
            for (String code : BASE_COLORS.keySet()) {
                try {
                    final Color normalColor = Color.decode(colorScheme.get(BASE_COLORS.get(code).getLeft()));
                    final Color brightColor = Color.decode(colorScheme.get(BASE_COLORS.get(code).getRight()));
                    colorMap.put(code, new ImmutablePair<>(normalColor, brightColor));

                } catch (NumberFormatException ex) {
                    log.warn("Cannot decode values for color code {}, defaulting to WHITE!", code);
                    colorMap.put(code, new ImmutablePair<>(Color.WHITE, Color.WHITE));
                }
            }

        } catch (Exception ex) {
            log.warn("Cannot load scheme {}: {}", schemeName, ex.getMessage(), ex);
        }
        return Collections.unmodifiableMap(colorMap);
    }
}
