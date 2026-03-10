package com.factory.glowsnakeznv9;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.factory.glowsnakeznv9.screens.MainMenuScreen;

public class MainGame extends Game {

    public SpriteBatch batch;
    public AssetManager manager;

    public boolean musicEnabled = true;
    public boolean sfxEnabled   = true;
    public Music   currentMusic = null;

    @Override
    public void create() {
        batch   = new SpriteBatch();
        manager = new AssetManager();
        loadAllAssets();
        manager.finishLoading();
        setScreen(new MainMenuScreen(this));
    }

    private void loadAllAssets() {
        // Backgrounds
        manager.load(Constants.BG_MAIN, Texture.class);
        manager.load(Constants.BG_GRID, Texture.class);

        // UI power-up icons
        manager.load(Constants.ICON_SPEED_BOOST, Texture.class);
        manager.load(Constants.ICON_GHOST_PASS,  Texture.class);

        // Sprites — buttons
        manager.load("sprites/button_blue.png",         Texture.class);
        manager.load("sprites/button_blue_pressed.png", Texture.class);
        manager.load("sprites/button_green.png",        Texture.class);
        manager.load("sprites/button_green_pressed.png",Texture.class);
        manager.load("sprites/button_grey.png",         Texture.class);
        manager.load("sprites/button_grey_pressed.png", Texture.class);
        manager.load("sprites/button_red.png",          Texture.class);
        manager.load("sprites/button_red_pressed.png",  Texture.class);
        manager.load("sprites/button_yellow.png",       Texture.class);
        manager.load("sprites/button_yellow_pressed.png", Texture.class);

        // Sprites — HUD icons
        manager.load("sprites/icon_heart.png",       Texture.class);
        manager.load("sprites/icon_heart_empty.png", Texture.class);
        manager.load("sprites/icon_timer.png",       Texture.class);
        manager.load("sprites/icon_settings.png",    Texture.class);
        manager.load("sprites/icon_leaderboard.png", Texture.class);
        manager.load("sprites/icon_music_on.png",    Texture.class);
        manager.load("sprites/icon_music_off.png",   Texture.class);
        manager.load("sprites/icon_sfx_on.png",      Texture.class);
        manager.load("sprites/icon_sfx_off.png",     Texture.class);
        manager.load("sprites/icon_pause.png",       Texture.class);
        manager.load("sprites/icon_play.png",        Texture.class);
        manager.load("sprites/icon_home.png",        Texture.class);
        manager.load("sprites/icon_close.png",       Texture.class);
        manager.load("sprites/icon_trophy.png",      Texture.class);
        manager.load("sprites/coin_gold.png",        Texture.class);
        manager.load("sprites/gem_green.png",        Texture.class);

        // Music
        manager.load(Constants.MUSIC_MENU,      Music.class);
        manager.load(Constants.MUSIC_GAMEPLAY,  Music.class);
        manager.load(Constants.MUSIC_GAME_OVER, Music.class);

        // SFX
        manager.load(Constants.SFX_CLICK,          Sound.class);
        manager.load(Constants.SFX_BACK,           Sound.class);
        manager.load(Constants.SFX_TOGGLE,         Sound.class);
        manager.load(Constants.SFX_COIN,           Sound.class);
        manager.load(Constants.SFX_HIT,            Sound.class);
        manager.load(Constants.SFX_GAME_OVER,      Sound.class);
        manager.load(Constants.SFX_LEVEL_COMPLETE, Sound.class);
        manager.load(Constants.SFX_POWER_UP,       Sound.class);
        manager.load(Constants.SFX_ERROR,          Sound.class);
    }

    public void playMusic(String path) {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.stop();
        }
        currentMusic = manager.get(path, Music.class);
        currentMusic.setLooping(true);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) {
            currentMusic.play();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        manager.dispose();
    }
}
