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

public class ModeSelectScreen implements Screen {

    private final MainGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private BitmapFont headerFont;
    private BitmapFont buttonFont;

    public ModeSelectScreen(MainGame game) {
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

        Label.LabelStyle headerStyle = new Label.LabelStyle(headerFont, cyan);
        Label headerLabel = new Label("SELECT MODE", headerStyle);

        TextButton classicBtn = new TextButton("CLASSIC\nWalls Kill",
                makeStyle("sprites/button_blue.png", "sprites/button_blue_pressed.png"));

        TextButton wrapBtn = new TextButton("WRAP\nWalls Loop",
                makeStyle("sprites/button_yellow.png", "sprites/button_yellow_pressed.png"));

        TextButton howBtn = new TextButton("HOW TO PLAY",
                makeStyle("sprites/button_green.png", "sprites/button_green_pressed.png"));

        TextButton backBtn = new TextButton("BACK",
                makeStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));

        classicBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_CLICK, Sound.class).play(1.0f);
                game.setScreen(new DifficultySelectScreen(game, Constants.MODE_CLASSIC));
            }
        });

        wrapBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_CLICK, Sound.class).play(1.0f);
                game.setScreen(new DifficultySelectScreen(game, Constants.MODE_WRAP));
            }
        });

        howBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_CLICK, Sound.class).play(1.0f);
                game.setScreen(new HowToPlayScreen(game));
            }
        });

        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_BACK, Sound.class).play(1.0f);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(80f);

        table.add(headerLabel).colspan(2).padBottom(50f).row();

        table.add(classicBtn)
                .size(Constants.CARD_WIDE, Constants.CARD_TALL)
                .padRight(Constants.CARD_GAP / 2f);
        table.add(wrapBtn)
                .size(Constants.CARD_WIDE, Constants.CARD_TALL)
                .padLeft(Constants.CARD_GAP / 2f)
                .row();

        table.add(howBtn)
                .colspan(2)
                .size(Constants.BTN_MAIN_WIDTH, Constants.BTN_MAIN_HEIGHT)
                .padTop(40f)
                .padBottom(16f)
                .row();

        table.add(backBtn)
                .colspan(2)
                .size(Constants.BTN_SEC_WIDTH, Constants.BTN_SEC_HEIGHT)
                .row();

        stage.addActor(table);
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
