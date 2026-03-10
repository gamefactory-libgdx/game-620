package com.factory.glowsnakeznv9.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

public class MainMenuScreen implements Screen {

    private final MainGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private BitmapFont titleFont;
    private BitmapFont subtitleFont;
    private BitmapFont buttonFont;

    public MainMenuScreen(MainGame game) {
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

        buildFonts();
        buildUI();

        game.playMusic(Constants.MUSIC_MENU);
    }

    private void buildFonts() {
        FreeTypeFontGenerator titleGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Orbitron-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter titleParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParam.size  = Constants.FONT_SIZE_TITLE;
        titleParam.color = Color.WHITE;
        titleFont = titleGen.generateFont(titleParam);
        titleGen.dispose();

        FreeTypeFontGenerator subtitleGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Orbitron-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter subtitleParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        subtitleParam.size  = Constants.FONT_SIZE_SUBTITLE;
        subtitleParam.color = Color.WHITE;
        subtitleFont = subtitleGen.generateFont(subtitleParam);
        subtitleGen.dispose();

        FreeTypeFontGenerator btnGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter btnParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        btnParam.size  = Constants.FONT_SIZE_BODY;
        btnParam.color = Color.WHITE;
        buttonFont = btnGen.generateFont(btnParam);
        btnGen.dispose();
    }

    private TextButton.TextButtonStyle makeStyle(String upFile, String downFile) {
        TextButton.TextButtonStyle s = new TextButton.TextButtonStyle();
        s.font  = buttonFont;
        s.up    = new TextureRegionDrawable(game.manager.get(upFile,   Texture.class));
        s.down  = new TextureRegionDrawable(game.manager.get(downFile, Texture.class));
        s.fontColor = Color.WHITE;
        return s;
    }

    private void buildUI() {
        // Neon colors
        Color cyan      = new Color(0f, 0.85f, 1f, 1f);
        Color limeGreen = new Color(0.22f, 1f, 0.08f, 1f);

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, cyan);
        Label.LabelStyle subStyle   = new Label.LabelStyle(subtitleFont, limeGreen);

        Label titleLabel    = new Label("GLOW SNAKE", titleStyle);
        Label subtitleLabel = new Label("ARCADE",     subStyle);

        TextButton playBtn   = new TextButton("PLAY",
                makeStyle("sprites/button_blue.png", "sprites/button_blue_pressed.png"));
        TextButton lbBtn     = new TextButton("LEADERBOARD",
                makeStyle("sprites/button_yellow.png", "sprites/button_yellow_pressed.png"));
        TextButton settingsBtn = new TextButton("SETTINGS",
                makeStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));

        playBtn.setSize(Constants.BTN_MAIN_WIDTH, Constants.BTN_MAIN_HEIGHT);
        lbBtn.setSize(Constants.BTN_MAIN_WIDTH, Constants.BTN_MAIN_HEIGHT);
        settingsBtn.setSize(Constants.BTN_MAIN_WIDTH, Constants.BTN_MAIN_HEIGHT);

        playBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_CLICK, com.badlogic.gdx.audio.Sound.class).play(1.0f);
                game.setScreen(new ModeSelectScreen(game));
            }
        });

        lbBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_CLICK, com.badlogic.gdx.audio.Sound.class).play(1.0f);
                game.setScreen(new LeaderboardScreen(game));
            }
        });

        settingsBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_CLICK, com.badlogic.gdx.audio.Sound.class).play(1.0f);
                game.setScreen(new SettingsScreen(game));
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(80f);

        table.add(titleLabel).padBottom(8f).row();
        table.add(subtitleLabel).padBottom(60f).row();
        table.add(playBtn).size(Constants.BTN_MAIN_WIDTH, Constants.BTN_MAIN_HEIGHT)
                .padBottom(Constants.BTN_MAIN_SPACING).row();
        table.add(lbBtn).size(Constants.BTN_MAIN_WIDTH, Constants.BTN_MAIN_HEIGHT)
                .padBottom(Constants.BTN_MAIN_SPACING).row();
        table.add(settingsBtn).size(Constants.BTN_MAIN_WIDTH, Constants.BTN_MAIN_HEIGHT).row();

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
        titleFont.dispose();
        subtitleFont.dispose();
        buttonFont.dispose();
    }
}
