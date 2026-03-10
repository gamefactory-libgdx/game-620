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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardScreen implements Screen {

    private static final String LB_KEY_PREFIX = "lb_global_";

    private final MainGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private BitmapFont headerFont;
    private BitmapFont rowFont;
    private BitmapFont buttonFont;

    // -----------------------------------------------------------------------
    // Static helper — saves score into global top-10 in SharedPreferences
    // -----------------------------------------------------------------------
    public static void addScore(int score) {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        List<Integer> scores = loadScores(prefs);
        scores.add(score);
        Collections.sort(scores, Collections.reverseOrder());
        while (scores.size() > Constants.LEADERBOARD_MAX_SIZE) {
            scores.remove(scores.size() - 1);
        }
        for (int i = 0; i < scores.size(); i++) {
            prefs.putInteger(LB_KEY_PREFIX + i, scores.get(i));
        }
        prefs.flush();
    }

    private static List<Integer> loadScores(Preferences prefs) {
        List<Integer> scores = new ArrayList<Integer>();
        for (int i = 0; i < Constants.LEADERBOARD_MAX_SIZE; i++) {
            int v = prefs.getInteger(LB_KEY_PREFIX + i, -1);
            if (v >= 0) scores.add(v);
        }
        return scores;
    }

    // -----------------------------------------------------------------------

    public LeaderboardScreen(MainGame game) {
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
        FreeTypeFontGenerator.FreeTypeFontParameter headerParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        headerParam.size  = Constants.FONT_SIZE_HEADER;
        headerParam.color = Color.WHITE;
        headerFont = headerGen.generateFont(headerParam);
        headerGen.dispose();

        FreeTypeFontGenerator rowGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter rowParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        rowParam.size  = 14;
        rowParam.color = Color.WHITE;
        rowFont = rowGen.generateFont(rowParam);
        rowGen.dispose();

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
        Color cyan      = new Color(0f, 0.85f, 1f, 1f);
        Color magenta   = new Color(1f, 0f, 0.43f, 1f);
        Color limeGreen = new Color(0.22f, 1f, 0.08f, 1f);
        Color gold      = new Color(1f, 0.843f, 0f, 1f);
        Color silver    = new Color(0.753f, 0.753f, 0.753f, 1f);
        Color bronze    = new Color(0.804f, 0.498f, 0.196f, 1f);

        Label.LabelStyle headerStyle = new Label.LabelStyle(headerFont, cyan);
        Label.LabelStyle rankStyle   = new Label.LabelStyle(rowFont, cyan);
        Label.LabelStyle nameStyle   = new Label.LabelStyle(rowFont, Color.WHITE);
        Label.LabelStyle scoreStyle  = new Label.LabelStyle(rowFont, limeGreen);
        Label.LabelStyle emptyStyle  = new Label.LabelStyle(rowFont, magenta);

        Label headerLabel = new Label("LEADERBOARD", headerStyle);

        Preferences prefs  = Gdx.app.getPreferences(Constants.PREFS_NAME);
        List<Integer> scores = loadScores(prefs);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(60f);
        table.add(headerLabel).colspan(3).padBottom(30f).row();

        if (scores.isEmpty()) {
            table.add(new Label("NO SCORES YET", emptyStyle)).colspan(3).padBottom(12f).row();
        } else {
            for (int i = 0; i < scores.size(); i++) {
                Color rankColor;
                if      (i == 0) rankColor = gold;
                else if (i == 1) rankColor = silver;
                else if (i == 2) rankColor = bronze;
                else             rankColor = cyan;

                Label.LabelStyle rs = new Label.LabelStyle(rowFont, rankColor);
                Label rankLabel  = new Label((i + 1) + ".", rs);
                Label nameLabel  = new Label("------",      nameStyle);
                Label scoreLabel = new Label(String.valueOf(scores.get(i)), scoreStyle);

                table.add(rankLabel).width(60f).left().padBottom(8f);
                table.add(nameLabel).expandX().left().padBottom(8f);
                table.add(scoreLabel).right().padBottom(8f).row();
            }
        }

        TextButton menuBtn = new TextButton("MAIN MENU",
                makeStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));
        menuBtn.setSize(Constants.BTN_SEC_WIDTH, Constants.BTN_SEC_HEIGHT);
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_BACK, Sound.class).play(1.0f);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.add(menuBtn)
                .colspan(3)
                .size(Constants.BTN_SEC_WIDTH, Constants.BTN_SEC_HEIGHT)
                .padTop(30f)
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
        rowFont.dispose();
        buttonFont.dispose();
    }
}
