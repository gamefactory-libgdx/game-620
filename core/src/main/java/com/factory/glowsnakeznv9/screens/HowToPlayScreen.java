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

public class HowToPlayScreen implements Screen {

    private final MainGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private BitmapFont headerFont;
    private BitmapFont sectionFont;
    private BitmapFont bodyFont;
    private BitmapFont buttonFont;

    public HowToPlayScreen(MainGame game) {
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

        FreeTypeFontGenerator secGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Orbitron-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter sp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        sp.size  = 18;
        sp.color = Color.WHITE;
        sectionFont = secGen.generateFont(sp);
        secGen.dispose();

        FreeTypeFontGenerator bodyGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter bp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        bp.size  = Constants.FONT_SIZE_BODY;
        bp.color = Color.WHITE;
        bodyFont = bodyGen.generateFont(bp);
        bodyGen.dispose();

        FreeTypeFontGenerator btnGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter btp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        btp.size  = Constants.FONT_SIZE_BODY;
        btp.color = Color.WHITE;
        buttonFont = btnGen.generateFont(btp);
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
        Color cyan      = new Color(0f, 0.85f, 1f, 1f);
        Color magenta   = new Color(1f, 0f, 0.43f, 1f);
        Color limeGreen = new Color(0.22f, 1f, 0.08f, 1f);
        Color orange    = new Color(1f, 0.549f, 0.259f, 1f);

        Label.LabelStyle headerStyle  = new Label.LabelStyle(headerFont,  cyan);
        Label.LabelStyle secMagenta   = new Label.LabelStyle(sectionFont, magenta);
        Label.LabelStyle secGreen     = new Label.LabelStyle(sectionFont, limeGreen);
        Label.LabelStyle secCyan      = new Label.LabelStyle(sectionFont, cyan);
        Label.LabelStyle secOrange    = new Label.LabelStyle(sectionFont, orange);
        Label.LabelStyle bodyStyle    = new Label.LabelStyle(bodyFont,    Color.WHITE);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(60f);

        table.add(new Label("HOW TO PLAY", headerStyle)).padBottom(30f).row();

        // Section 1: Swipe to move
        addSection(table,
                new Label("SWIPE TO MOVE", secMagenta),
                new Label("Swipe in any direction to steer\nthe snake across the grid.", bodyStyle));

        // Section 2: Eat food
        addSection(table,
                new Label("EAT FOOD", secGreen),
                new Label("Eat the lime pellets to grow longer\nand earn 10 points each.", bodyStyle));

        // Section 3: Avoid collisions
        addSection(table,
                new Label("AVOID COLLISIONS", secCyan),
                new Label("CLASSIC: hitting a wall or your body\nlosses a life (3 lives total).\n" +
                        "WRAP: walls loop you to the other side.", bodyStyle));

        // Section 4: Power-ups
        addSection(table,
                new Label("POWER-UPS", secOrange),
                new Label("Speed Boost: moves faster for 5s.\n" +
                        "Ghost Pass: pass through your body for 5s.\n" +
                        "Each power-up earns 25 bonus points.", bodyStyle));

        TextButton backBtn = new TextButton("BACK",
                makeStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_BACK, Sound.class).play(1.0f);
                game.setScreen(new ModeSelectScreen(game));
            }
        });

        TextButton menuBtn = new TextButton("MAIN MENU",
                makeStyle("sprites/button_blue.png", "sprites/button_blue_pressed.png"));
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_BACK, Sound.class).play(1.0f);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        Table btnRow = new Table();
        btnRow.add(backBtn).size(Constants.BTN_SEC_WIDTH, Constants.BTN_SEC_HEIGHT).padRight(16f);
        btnRow.add(menuBtn).size(Constants.BTN_SEC_WIDTH, Constants.BTN_SEC_HEIGHT);
        table.add(btnRow).padTop(20f).row();

        stage.addActor(table);
    }

    private void addSection(Table table, Label title, Label body) {
        Table section = new Table();
        section.pad(12f);
        section.add(title).left().row();
        section.add(body).left().padTop(6f).row();

        table.add(section)
                .width(360f)
                .left()
                .padBottom(12f)
                .row();
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
        sectionFont.dispose();
        bodyFont.dispose();
        buttonFont.dispose();
    }
}
