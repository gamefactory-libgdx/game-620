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

public class DifficultySelectScreen implements Screen {

    private final MainGame game;
    private final int mode;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private BitmapFont headerFont;
    private BitmapFont buttonFont;

    public DifficultySelectScreen(MainGame game, int mode) {
        this.game = game;
        this.mode = mode;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    game.setScreen(new ModeSelectScreen(game));
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
        Color cyan    = new Color(0f, 0.85f, 1f, 1f);
        Color magenta = new Color(1f, 0f, 0.43f, 1f);

        Label.LabelStyle headerStyle = new Label.LabelStyle(headerFont, cyan);
        Label.LabelStyle subStyle    = new Label.LabelStyle(buttonFont, magenta);

        String modeLabel = mode == Constants.MODE_CLASSIC ? "CLASSIC" : "WRAP";
        Label headerLabel = new Label("SELECT DIFFICULTY", headerStyle);
        Label modeSubLabel = new Label(modeLabel + " MODE", subStyle);

        // Small (16x16)
        TextButton smallBtn = new TextButton("SMALL\n16x16",
                makeStyle("sprites/button_green.png", "sprites/button_green_pressed.png"));

        // Medium (24x24)
        TextButton medBtn = new TextButton("MEDIUM\n24x24",
                makeStyle("sprites/button_blue.png", "sprites/button_blue_pressed.png"));

        // Large (32x32)
        TextButton largeBtn = new TextButton("LARGE\n32x32",
                makeStyle("sprites/button_yellow.png", "sprites/button_yellow_pressed.png"));

        TextButton backBtn = new TextButton("BACK",
                makeStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));

        smallBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_CLICK, Sound.class).play(1.0f);
                launchGame(Constants.DIFF_SMALL);
            }
        });

        medBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_CLICK, Sound.class).play(1.0f);
                launchGame(Constants.DIFF_MEDIUM);
            }
        });

        largeBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_CLICK, Sound.class).play(1.0f);
                launchGame(Constants.DIFF_LARGE);
            }
        });

        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_BACK, Sound.class).play(1.0f);
                game.setScreen(new ModeSelectScreen(game));
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(80f);

        table.add(headerLabel).padBottom(8f).row();
        table.add(modeSubLabel).padBottom(40f).row();

        table.add(smallBtn).size(Constants.DIFF_CARD_WIDE, Constants.DIFF_CARD_TALL)
                .padBottom(Constants.DIFF_CARD_GAP).row();
        table.add(medBtn).size(Constants.DIFF_CARD_WIDE, Constants.DIFF_CARD_TALL)
                .padBottom(Constants.DIFF_CARD_GAP).row();
        table.add(largeBtn).size(Constants.DIFF_CARD_WIDE, Constants.DIFF_CARD_TALL)
                .padBottom(40f).row();

        table.add(backBtn).size(Constants.BTN_SEC_WIDTH, Constants.BTN_SEC_HEIGHT).row();

        stage.addActor(table);
    }

    private void launchGame(int difficulty) {
        Screen screen;
        if (mode == Constants.MODE_CLASSIC) {
            switch (difficulty) {
                case Constants.DIFF_SMALL:  screen = new ClassicSmallScreen(game);  break;
                case Constants.DIFF_MEDIUM: screen = new ClassicMediumScreen(game); break;
                default:                    screen = new ClassicLargeScreen(game);  break;
            }
        } else {
            switch (difficulty) {
                case Constants.DIFF_SMALL:  screen = new WrapSmallScreen(game);  break;
                case Constants.DIFF_MEDIUM: screen = new WrapMediumScreen(game); break;
                default:                    screen = new WrapLargeScreen(game);  break;
            }
        }
        game.setScreen(screen);
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
