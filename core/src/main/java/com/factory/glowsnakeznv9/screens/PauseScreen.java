package com.factory.glowsnakeznv9.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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

public class PauseScreen implements Screen {

    private final MainGame game;
    private final BaseGameScreen gameScreen;  // the paused screen to resume
    private final int mode;
    private final int gridSize;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private BitmapFont headerFont;
    private BitmapFont buttonFont;

    public PauseScreen(MainGame game, BaseGameScreen gameScreen, int mode, int gridSize) {
        this.game       = game;
        this.gameScreen = gameScreen;
        this.mode       = mode;
        this.gridSize   = gridSize;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    resumeGame();
                    return true;
                }
                return false;
            }
        }));

        buildFonts();
        buildUI();

        // Pause the music while game is paused
        if (game.currentMusic != null && game.currentMusic.isPlaying()) {
            game.currentMusic.pause();
        }
    }

    private void buildFonts() {
        FreeTypeFontGenerator headerGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Orbitron-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter hp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        hp.size  = Constants.FONT_SIZE_HEADER;
        hp.color = Color.WHITE;
        headerFont = headerGen.generateFont(hp);
        headerGen.dispose();

        FreeTypeFontGenerator btnGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter bp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        bp.size  = Constants.FONT_SIZE_BODY;
        bp.color = Color.WHITE;
        buttonFont = btnGen.generateFont(bp);
        btnGen.dispose();
    }

    private TextButton.TextButtonStyle makeStyle(String upFile, String downFile) {
        TextButton.TextButtonStyle s = new TextButton.TextButtonStyle();
        s.font      = buttonFont;
        s.up        = new TextureRegionDrawable(game.manager.get(upFile,   Texture.class));
        s.down      = new TextureRegionDrawable(game.manager.get(downFile, Texture.class));
        s.fontColor = Color.WHITE;
        return s;
    }

    private void buildUI() {
        Color cyan = new Color(0f, 0.85f, 1f, 1f);

        Label.LabelStyle headerStyle = new Label.LabelStyle(headerFont, cyan);
        Label headerLabel = new Label("PAUSED", headerStyle);

        TextButton resumeBtn = new TextButton("RESUME",
                makeStyle("sprites/button_green.png", "sprites/button_green_pressed.png"));

        TextButton restartBtn = new TextButton("RESTART",
                makeStyle("sprites/button_blue.png", "sprites/button_blue_pressed.png"));

        TextButton menuBtn = new TextButton("MAIN MENU",
                makeStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));

        resumeBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_CLICK, Sound.class).play(1.0f);
                resumeGame();
            }
        });

        restartBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_CLICK, Sound.class).play(1.0f);
                restartGame();
            }
        });

        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_BACK, Sound.class).play(1.0f);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(headerLabel).padBottom(50f).row();
        table.add(resumeBtn).size(Constants.BTN_MAIN_WIDTH, Constants.BTN_MAIN_HEIGHT)
                .padBottom(Constants.BTN_MAIN_SPACING).row();
        table.add(restartBtn).size(Constants.BTN_MAIN_WIDTH, Constants.BTN_MAIN_HEIGHT)
                .padBottom(Constants.BTN_MAIN_SPACING).row();
        table.add(menuBtn).size(Constants.BTN_MAIN_WIDTH, Constants.BTN_MAIN_HEIGHT).row();

        stage.addActor(table);
    }

    private void resumeGame() {
        // Resume music before going back to game
        if (game.musicEnabled && game.currentMusic != null) {
            game.currentMusic.play();
        }
        game.setScreen(gameScreen);
    }

    private void restartGame() {
        // Create a fresh game screen for the same mode/difficulty
        Screen newScreen;
        if (mode == Constants.MODE_CLASSIC) {
            switch (gridSize) {
                case Constants.GRID_SIZE_SMALL:  newScreen = new ClassicSmallScreen(game);  break;
                case Constants.GRID_SIZE_MEDIUM: newScreen = new ClassicMediumScreen(game); break;
                default:                         newScreen = new ClassicLargeScreen(game);  break;
            }
        } else {
            switch (gridSize) {
                case Constants.GRID_SIZE_SMALL:  newScreen = new WrapSmallScreen(game);  break;
                case Constants.GRID_SIZE_MEDIUM: newScreen = new WrapMediumScreen(game); break;
                default:                         newScreen = new WrapLargeScreen(game);  break;
            }
        }
        game.setScreen(newScreen);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.039f, 0.055f, 0.153f, 1f);

        game.batch.begin();
        game.batch.draw(game.manager.get(Constants.BG_MAIN, Texture.class),
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
        buttonFont.dispose();
    }
}
