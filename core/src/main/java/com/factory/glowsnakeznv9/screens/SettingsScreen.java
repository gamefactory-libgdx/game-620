package com.factory.glowsnakeznv9.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.factory.glowsnakeznv9.Constants;
import com.factory.glowsnakeznv9.MainGame;

public class SettingsScreen implements Screen {

    private final MainGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private BitmapFont headerFont;
    private BitmapFont labelFont;
    private BitmapFont buttonFont;

    public SettingsScreen(MainGame game) {
        this.game = game;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        }));

        // Load saved preferences
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        game.musicEnabled = prefs.getBoolean(Constants.PREF_MUSIC, true);
        game.sfxEnabled   = prefs.getBoolean(Constants.PREF_SFX,   true);

        buildFonts();
        buildUI();

        game.playMusic(Constants.MUSIC_MENU);
    }

    private void buildFonts() {
        FreeTypeFontGenerator headerGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Orbitron-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter headerParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        headerParam.size  = Constants.FONT_SIZE_HEADER;
        headerParam.color = Color.WHITE;
        headerFont = headerGen.generateFont(headerParam);
        headerGen.dispose();

        FreeTypeFontGenerator labelGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter labelParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        labelParam.size  = 16;
        labelParam.color = Color.WHITE;
        labelFont = labelGen.generateFont(labelParam);
        labelGen.dispose();

        FreeTypeFontGenerator btnGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter btnParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        btnParam.size  = Constants.FONT_SIZE_BODY;
        btnParam.color = Color.WHITE;
        buttonFont = btnGen.generateFont(btnParam);
        btnGen.dispose();
    }

    private TextButton.TextButtonStyle makeButtonStyle(String upFile, String downFile) {
        TextButton.TextButtonStyle s = new TextButton.TextButtonStyle();
        s.font      = buttonFont;
        s.up        = new TextureRegionDrawable(game.manager.get(upFile,   Texture.class));
        s.down      = new TextureRegionDrawable(game.manager.get(downFile, Texture.class));
        s.fontColor = Color.WHITE;
        return s;
    }

    private void buildUI() {
        final Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        Color cyan = new Color(0f, 0.85f, 1f, 1f);

        Label.LabelStyle headerStyle = new Label.LabelStyle(headerFont, cyan);
        Label.LabelStyle rowStyle    = new Label.LabelStyle(labelFont, Color.WHITE);

        Label headerLabel = new Label("SETTINGS", headerStyle);

        // Music toggle
        final ImageButton musicBtn = new ImageButton(
                new TextureRegionDrawable(game.manager.get("sprites/icon_music_on.png",  Texture.class)),
                new TextureRegionDrawable(game.manager.get("sprites/icon_music_off.png", Texture.class)));
        musicBtn.setChecked(!game.musicEnabled);

        // SFX toggle
        final ImageButton sfxBtn = new ImageButton(
                new TextureRegionDrawable(game.manager.get("sprites/icon_sfx_on.png",  Texture.class)),
                new TextureRegionDrawable(game.manager.get("sprites/icon_sfx_off.png", Texture.class)));
        sfxBtn.setChecked(!game.sfxEnabled);

        musicBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.musicEnabled = !game.musicEnabled;
                prefs.putBoolean(Constants.PREF_MUSIC, game.musicEnabled);
                prefs.flush();
                if (game.currentMusic != null) {
                    if (game.musicEnabled) game.currentMusic.play();
                    else                   game.currentMusic.pause();
                }
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_TOGGLE, Sound.class).play(0.5f);
            }
        });

        sfxBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.sfxEnabled = !game.sfxEnabled;
                prefs.putBoolean(Constants.PREF_SFX, game.sfxEnabled);
                prefs.flush();
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_TOGGLE, Sound.class).play(0.5f);
            }
        });

        TextButton menuBtn = new TextButton("MAIN MENU",
                makeButtonStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));
        menuBtn.setSize(Constants.BTN_SEC_WIDTH, Constants.BTN_SEC_HEIGHT);
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_BACK, Sound.class).play(1.0f);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(80f);

        table.add(headerLabel).colspan(2).padBottom(60f).row();

        table.add(new Label("MUSIC", rowStyle)).left().padRight(40f);
        table.add(musicBtn).size(Constants.BTN_ROUND_SIZE, Constants.BTN_ROUND_SIZE).row();
        table.padBottom(20f);

        table.add(new Label("SOUND", rowStyle)).left().padRight(40f).padTop(20f);
        table.add(sfxBtn).size(Constants.BTN_ROUND_SIZE, Constants.BTN_ROUND_SIZE).padTop(20f).row();

        table.add(menuBtn)
                .colspan(2)
                .size(Constants.BTN_SEC_WIDTH, Constants.BTN_SEC_HEIGHT)
                .padTop(60f)
                .row();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.039f, 0.055f, 0.153f, 1f);

        game.batch.begin();
        game.batch.draw(
                game.manager.get(Constants.BG_MAIN, Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void show()   {}
    @Override public void hide()   {}
    @Override public void pause()  {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        headerFont.dispose();
        labelFont.dispose();
        buttonFont.dispose();
    }
}
