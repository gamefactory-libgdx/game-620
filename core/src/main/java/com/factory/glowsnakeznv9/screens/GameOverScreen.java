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

public class GameOverScreen implements Screen {

    private final MainGame game;
    private final int score;
    private final int extra;   // food eaten / secondary stat

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private BitmapFont headerFont;
    private BitmapFont scoreFont;
    private BitmapFont bodyFont;
    private BitmapFont buttonFont;

    public GameOverScreen(MainGame game, int score, int extra) {
        this.game  = game;
        this.score = score;
        this.extra = extra;

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

        saveHighScore();
        LeaderboardScreen.addScore(score);

        buildFonts();
        buildUI();

        game.playMusic(Constants.MUSIC_GAME_OVER);
        if (game.sfxEnabled)
            game.manager.get(Constants.SFX_GAME_OVER, Sound.class).play(1.0f);
    }

    private void saveHighScore() {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int best = prefs.getInteger(Constants.PREF_HIGH_SCORE, 0);
        if (score > best) {
            prefs.putInteger(Constants.PREF_HIGH_SCORE, score);
            prefs.flush();
        }
    }

    private void buildFonts() {
        FreeTypeFontGenerator headerGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Orbitron-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter headerParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        headerParam.size  = 56;
        headerParam.color = Color.WHITE;
        headerFont = headerGen.generateFont(headerParam);
        headerGen.dispose();

        FreeTypeFontGenerator scoreGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Orbitron-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter scoreParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        scoreParam.size  = Constants.FONT_SIZE_SCORE;
        scoreParam.color = Color.WHITE;
        scoreFont = scoreGen.generateFont(scoreParam);
        scoreGen.dispose();

        FreeTypeFontGenerator bodyGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter bodyParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        bodyParam.size  = Constants.FONT_SIZE_BODY;
        bodyParam.color = Color.WHITE;
        bodyFont = bodyGen.generateFont(bodyParam);
        bodyGen.dispose();

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
        s.font      = buttonFont;
        s.up        = new TextureRegionDrawable(game.manager.get(upFile,   Texture.class));
        s.down      = new TextureRegionDrawable(game.manager.get(downFile, Texture.class));
        s.fontColor = Color.WHITE;
        return s;
    }

    private void buildUI() {
        Color magenta   = new Color(1f, 0f, 0.43f, 1f);
        Color cyan      = new Color(0f, 0.85f, 1f, 1f);
        Color limeGreen = new Color(0.22f, 1f, 0.08f, 1f);

        Label.LabelStyle headerStyle    = new Label.LabelStyle(headerFont, magenta);
        Label.LabelStyle scoreLabelStyle= new Label.LabelStyle(bodyFont,   Color.WHITE);
        Label.LabelStyle scoreNumStyle  = new Label.LabelStyle(scoreFont,  cyan);
        Label.LabelStyle statStyle      = new Label.LabelStyle(bodyFont,   Color.WHITE);
        Label.LabelStyle bestStyle      = new Label.LabelStyle(bodyFont,   limeGreen);

        int personalBest = Gdx.app.getPreferences(Constants.PREFS_NAME)
                .getInteger(Constants.PREF_HIGH_SCORE, 0);

        Label headerLabel  = new Label("GAME OVER",  headerStyle);
        Label finalLabel   = new Label("FINAL SCORE", scoreLabelStyle);
        Label scoreLabel   = new Label(String.valueOf(score), scoreNumStyle);
        Label bestLabel    = new Label("BEST: " + personalBest, bestStyle);
        Label foodLabel    = new Label("FOOD EATEN: " + extra,  statStyle);

        TextButton retryBtn = new TextButton("RETRY",
                makeStyle("sprites/button_green.png", "sprites/button_green_pressed.png"));
        TextButton menuBtn  = new TextButton("MAIN MENU",
                makeStyle("sprites/button_grey.png",  "sprites/button_grey_pressed.png"));

        retryBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_CLICK, Sound.class).play(1.0f);
                game.setScreen(new ModeSelectScreen(game));
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
        table.top().padTop(80f);

        table.add(headerLabel).padBottom(30f).row();
        table.add(finalLabel).padBottom(4f).row();
        table.add(scoreLabel).padBottom(12f).row();
        table.add(bestLabel).padBottom(8f).row();
        table.add(foodLabel).padBottom(40f).row();

        Table btnRow = new Table();
        btnRow.add(retryBtn).size(Constants.BTN_SEC_WIDTH, Constants.BTN_SEC_HEIGHT).padRight(16f);
        btnRow.add(menuBtn).size(Constants.BTN_SEC_WIDTH, Constants.BTN_SEC_HEIGHT);
        table.add(btnRow).row();

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
        scoreFont.dispose();
        bodyFont.dispose();
        buttonFont.dispose();
    }
}
