package com.factory.glowsnakeznv9;

public class Constants {

    // World dimensions
    public static final float WORLD_WIDTH  = 480f;
    public static final float WORLD_HEIGHT = 854f;

    // Grid sizes (number of cells per side)
    public static final int GRID_SIZE_SMALL  = 16;
    public static final int GRID_SIZE_MEDIUM = 24;
    public static final int GRID_SIZE_LARGE  = 32;

    // Grid rendered dimensions (pixels / world units)
    public static final float GRID_PIXELS_SMALL  = 320f;
    public static final float GRID_PIXELS_MEDIUM = 360f;
    public static final float GRID_PIXELS_LARGE  = 400f;

    // Snake movement — seconds per step (lower = faster)
    public static final float SNAKE_STEP_INTERVAL_INITIAL = 0.20f;
    public static final float SNAKE_STEP_INTERVAL_MIN     = 0.05f;
    public static final float SNAKE_SPEED_INCREASE        = 0.005f; // subtracted per food

    // Initial snake length (number of body segments)
    public static final int SNAKE_INITIAL_LENGTH = 3;

    // Lives
    public static final int INITIAL_LIVES = 3;

    // Scoring
    public static final int SCORE_PER_FOOD       = 10;
    public static final int SCORE_PER_POWER_UP   = 25;
    public static final int LEADERBOARD_MAX_SIZE = 10;

    // Power-ups
    public static final float POWER_UP_SPEED_DURATION   = 5f;   // seconds
    public static final float POWER_UP_GHOST_DURATION   = 5f;   // seconds
    public static final float POWER_UP_SPAWN_INTERVAL   = 15f;  // seconds between spawns
    public static final float POWER_UP_LIFETIME         = 10f;  // seconds before it disappears

    // Game grid rendering
    public static final float GRID_BORDER_THICKNESS = 1f;      // world units

    // HUD layout
    public static final float HUD_PADDING        = 16f;
    public static final float HUD_ICON_SIZE      = 32f;

    // Button dimensions
    public static final float BTN_MAIN_WIDTH   = 240f;
    public static final float BTN_MAIN_HEIGHT  = 56f;
    public static final float BTN_MAIN_SPACING = 20f;
    public static final float BTN_SEC_WIDTH    = 200f;
    public static final float BTN_SEC_HEIGHT   = 48f;
    public static final float BTN_ROUND_SIZE   = 60f;

    // Mode-selection card dimensions
    public static final float CARD_WIDE  = 220f;
    public static final float CARD_TALL  = 140f;
    public static final float CARD_GAP   = 24f;

    // Difficulty-selection card dimensions
    public static final float DIFF_CARD_WIDE  = 180f;
    public static final float DIFF_CARD_TALL  = 100f;
    public static final float DIFF_CARD_GAP   = 16f;

    // Leaderboard row dimensions
    public static final float LB_ROW_WIDTH  = 380f;
    public static final float LB_ROW_HEIGHT = 48f;

    // Font sizes (approximate world-unit equivalents used for BitmapFont scale)
    public static final int FONT_SIZE_TITLE    = 64;
    public static final int FONT_SIZE_HEADER   = 48;
    public static final int FONT_SIZE_SCORE    = 48;
    public static final int FONT_SIZE_HUD      = 18;
    public static final int FONT_SIZE_BODY     = 14;
    public static final int FONT_SIZE_SMALL    = 12;
    public static final int FONT_SIZE_SUBTITLE = 20;

    // SharedPreferences keys
    public static final String PREFS_NAME          = "GlowSnakePrefs";
    public static final String PREF_MUSIC          = "musicEnabled";
    public static final String PREF_SFX            = "sfxEnabled";
    public static final String PREF_VIBRATION      = "vibrationEnabled";
    public static final String PREF_HIGH_SCORE     = "highScore";
    // Per-mode/difficulty leaderboard key pattern: "lb_<mode>_<size>_<rank>"
    public static final String PREF_LB_PREFIX      = "lb_";
    public static final String PREF_COIN_BALANCE   = "coinBalance";
    public static final String PREF_SKIN           = "selectedSkin";

    // Game-mode identifiers (stored as integers in SharedPreferences)
    public static final int MODE_CLASSIC = 0;
    public static final int MODE_WRAP    = 1;

    // Difficulty identifiers
    public static final int DIFF_SMALL  = 0;
    public static final int DIFF_MEDIUM = 1;
    public static final int DIFF_LARGE  = 2;

    // Sound paths — music
    public static final String MUSIC_MENU      = "sounds/music/music_menu.ogg";
    public static final String MUSIC_GAMEPLAY  = "sounds/music/music_gameplay.ogg";
    public static final String MUSIC_GAME_OVER = "sounds/music/music_game_over.ogg";

    // Sound paths — SFX
    public static final String SFX_CLICK         = "sounds/sfx/sfx_button_click.ogg";
    public static final String SFX_BACK          = "sounds/sfx/sfx_button_back.ogg";
    public static final String SFX_TOGGLE        = "sounds/sfx/sfx_toggle.ogg";
    public static final String SFX_COIN          = "sounds/sfx/sfx_coin.ogg";
    public static final String SFX_HIT           = "sounds/sfx/sfx_hit.ogg";
    public static final String SFX_GAME_OVER     = "sounds/sfx/sfx_game_over.ogg";
    public static final String SFX_LEVEL_COMPLETE = "sounds/sfx/sfx_level_complete.ogg";
    public static final String SFX_POWER_UP      = "sounds/sfx/sfx_power_up.ogg";
    public static final String SFX_ERROR         = "sounds/sfx/sfx_error.ogg";

    // Asset paths — backgrounds
    public static final String BG_MAIN = "backgrounds/bg_main.png";
    public static final String BG_GRID = "backgrounds/bg_grid.png";

    // Asset paths — UI power-up icons
    public static final String ICON_SPEED_BOOST = "ui/power_up_speed_boost_icon.png";
    public static final String ICON_GHOST_PASS  = "ui/power_up_ghost_pass_icon.png";
}
