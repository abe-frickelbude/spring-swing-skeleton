package de.fb.jvips_playground.view.ansi;

/**
 * ANSI terminal escape sequences.
 *
 * <p>
 * Rationale for not using an enum here: most of the processing algorithms work via string matching, so using a
 * friggin' enum here would only cause unnecessary overhead!
 * </p>
 *
 * @author Ibragim Kuliev
 *
 */
final class AnsiConstants {

    // attribute modifiers (partial list!)
    public static final String RESET = "0";
    public static final String BRIGHT = "1";
    public static final String FAINT = "2";
    public static final String ITALIC = "3";
    public static final String UNDERLINE = "4";
    public static final String REVERSE = "7";

    // Original ANSI spec ("named") foreground colors 30 - 37
    public static final String BLACK = "30";
    public static final String RED = "31";
    public static final String GREEN = "32";
    public static final String YELLOW = "33";
    public static final String BLUE = "34";
    public static final String MAGENTA = "35";
    public static final String CYAN = "36";
    public static final String WHITE = "37";

    // Extended "bright" foreground colors 90-97
    public static final String BRIGHT_BLACK = "90";
    public static final String BRIGHT_RED = "91";
    public static final String BRIGHT_GREEN = "92";
    public static final String BRIGHT_YELLOW = "93";
    public static final String BRIGHT_BLUE = "94";
    public static final String BRIGHT_MAGENTA = "95";
    public static final String BRIGHT_CYAN = "96";
    public static final String BRIGHT_WHITE = "97";

    // foreground commands
    public static final String SET_FOREGROUND = "38";
    public static final String DEFAULT_FOREGROUND = "39";

    // Original ANSI spec ("named") background codes 40 - 47
    public static final String BLACK_BACKGROUND = "40";
    public static final String RED_BACKGROUND = "41";
    public static final String GREEN_BACKGROUND = "42";
    public static final String YELLOW_BACKGROUND = "43";
    public static final String BLUE_BACKGROUND = "44";
    public static final String MAGENTA_BACKGROUND = "45";
    public static final String CYAN_BACKGROUND = "46";
    public static final String WHITE_BACKGROUND = "47";

    // Extended "bright" background colors 100-107
    public static final String BRIGHT_BLACK_BACKGROUND = "100";
    public static final String BRIGHT_RED_BACKGROUND = "101";
    public static final String BRIGHT_GREEN_BACKGROUND = "102";
    public static final String BRIGHT_YELLOW_BACKGROUND = "103";
    public static final String BRIGHT_BLUE_BACKGROUND = "104";
    public static final String BRIGHT_MAGENTA_BACKGROUND = "105";
    public static final String BRIGHT_CYAN_BACKGROUND = "106";
    public static final String BRIGHT_WHITE_BACKGROUND = "107";

    // background commands
    public static final String SET_BACKGROUND = "48";
    public static final String DEFAULT_BACKGROUND = "49";

    private AnsiConstants() {

    }
}
