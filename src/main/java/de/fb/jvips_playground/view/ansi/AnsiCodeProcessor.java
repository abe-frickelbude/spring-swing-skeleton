package de.fb.jvips_playground.view.ansi;

import java.awt.Color;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class does most of the heavy lifting for {@linkplain AnsiTextPane}.
 *
 * <p>
 * Idea appropriated from https://stackoverflow.com/questions/6899282/ansi-colors-in-java-swing-text-fields
 * and completely rewritten to support all dem flashy bitz!
 * </p>
 *
 * Regex explanations:
 *
 * \\u001b\[(\d+)?;(\d+)?m([^\\u001b]*)
 *
 * - first optional capture group captures the:
 * ** foreground color code if it is the only group
 * ** "modifier" code (e.g. for reset, background) if it is the first group in sequence
 *
 * - second optional group captures the foreground color code if the first group exists
 *
 * - third group captures the actual text fragment until the next \u001b
 *
 * @author Ibragim Kuliev
 *
 */
class AnsiCodeProcessor {

    private static final Logger log = LoggerFactory.getLogger(AnsiCodeProcessor.class);

    private static final Pattern CODE_PATTERN = Pattern.compile("\\u001b\\[(\\d+);?(\\d+)?m([^\\u001b]*)");

    // global lookup map
    private Map<String, Pair<Color, Color>> colorMap;
    private Pair<Color, Color> defaultColors;
    private boolean colorsEnabled;

    public AnsiCodeProcessor() {
        colorsEnabled = true;
    }

    /**
     * Append the specified string to the document, automatically coloring it using embedded ANSI escape sequences.
     *
     * @param text
     * @param document
     */
    public void appendText(final String text, final Document document) {

        final Matcher matcher = CODE_PATTERN.matcher(text);

        // greedy fast check to see if there are any ANSI-escaped sections in the input
        if (matcher.find()) {

            /*
             * any prefix in the source string that hasn't yet matched the pattern is not ANSI-escaped and
             * is prepended as-is using default colors.
             */
            append(text.substring(0, matcher.start()), defaultColors, document);
            boolean hasNextMatch = true;

            while (hasNextMatch) {
                Pair<Color, Color> colors;
                if (colorsEnabled) {
                    colors = decodeEscapeSequence(matcher);
                } else {
                    colors = defaultColors;
                }

                final String subString = matcher.group(3);
                append(subString, colors, document);
                hasNextMatch = matcher.find();
            }
        } else {
            // we don't have any color codes in the input, just append it as-is
            append(text, defaultColors, document);
        }
    }

    public void setColorsEnabled(final boolean colorsEnabled) {
        this.colorsEnabled = colorsEnabled;
    }

    public void setDefaultColors(final Color defaultForeground, final Color defaultBackground) {
        defaultColors = new ImmutablePair<>(defaultForeground, defaultBackground);
    }

    public void resetDefaultColors() {
        defaultColors = new ImmutablePair<>(
            colorMap.get(AnsiConstants.DEFAULT_FOREGROUND).getLeft(),
            colorMap.get(AnsiConstants.DEFAULT_BACKGROUND).getLeft());
    }

    /*
     * Loads the specified color scheme from classpath
     *
     */
    public void setColorScheme(final String schemeName) {
        try {
            colorMap = ColorSchemeLoader.loadScheme(schemeName);

            defaultColors = new ImmutablePair<>(
                colorMap.get(AnsiConstants.DEFAULT_FOREGROUND).getLeft(),
                colorMap.get(AnsiConstants.DEFAULT_BACKGROUND).getLeft());

        } catch (Exception ex) {
            throw ex;
        }
    }

    // null-safe
    private Pair<Color, Color> getColorOrDefault(final String key) {
        final Pair<Color, Color> colors = colorMap.get(key);
        return colors != null ? colors : defaultColors;
    }

    /*
     * Algorithm is "work in progress", currently supports only some commonly encountered scenarios.
     */
    private Pair<Color, Color> decodeEscapeSequence(final Matcher matcher) {

        Color foreground = defaultColors.getLeft();
        Color background = defaultColors.getRight();

        // group1 is modifier/background if group2 != null, otherwise it is the foreground
        final String group1 = matcher.group(1);
        if (StringUtils.isNotBlank(matcher.group(2))) {

            // check if attribute modifier or background color
            switch (group1) {

                // apply bright to foreground color
                case AnsiConstants.BRIGHT:
                    foreground = getColorOrDefault(matcher.group(2)).getRight();
                    break;

                case AnsiConstants.BLACK_BACKGROUND:
                case AnsiConstants.RED_BACKGROUND:
                case AnsiConstants.GREEN_BACKGROUND:
                case AnsiConstants.YELLOW_BACKGROUND:
                case AnsiConstants.BLUE_BACKGROUND:
                case AnsiConstants.MAGENTA_BACKGROUND:
                case AnsiConstants.CYAN_BACKGROUND:
                case AnsiConstants.WHITE_BACKGROUND:
                    // WIP no bright backgrounds here yet!
                    background = getColorOrDefault(group1).getLeft();
                    foreground = getColorOrDefault(matcher.group(2)).getLeft();
                    break;

                default:
                    // don't apply any attributes to foreground color
                    foreground = getColorOrDefault(matcher.group(2)).getLeft();
                    break;
            }
        } else {

            // check for reset
            if (AnsiConstants.RESET.equals(matcher.group(1))) {
                foreground = defaultColors.getLeft();
                background = defaultColors.getRight();
            } else {
                // we only have normal foreground color
                foreground = getColorOrDefault(group1).getLeft();
            }
        }
        return new ImmutablePair<>(foreground, background);
    }

    // append a text fragment using specified colors to the current content
    private void append(final String text, final Pair<Color, Color> colors, final Document document) {

        final StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet attrSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, colors.getLeft());
        attrSet = sc.addAttribute(attrSet, StyleConstants.Background, colors.getRight());
        int len = document.getLength();
        try {
            document.insertString(len, text, attrSet);
        } catch (BadLocationException ex) {
            log.warn(ex.getMessage());
        }
    }
}
