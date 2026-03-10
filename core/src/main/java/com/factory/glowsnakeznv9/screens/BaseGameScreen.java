package com.factory.glowsnakeznv9.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.factory.glowsnakeznv9.Constants;
import com.factory.glowsnakeznv9.MainGame;

public class BaseGameScreen implements Screen {

    // -----------------------------------------------------------------------
    // Constants
    // -----------------------------------------------------------------------
    private static final int   POWER_TYPE_SPEED = 0;
    private static final int   POWER_TYPE_GHOST = 1;
    private static final float SWIPE_THRESHOLD  = 40f; // screen pixels

    // -----------------------------------------------------------------------
    // Dependencies
    // -----------------------------------------------------------------------
    protected final MainGame game;
    protected final int mode;     // MODE_CLASSIC or MODE_WRAP
    protected final int gridSize; // GRID_SIZE_SMALL / MEDIUM / LARGE

    // -----------------------------------------------------------------------
    // Grid geometry (computed in constructor)
    // -----------------------------------------------------------------------
    private final float gridPixels; // rendered size of the grid in world units
    private final float cellSize;   // size of one cell in world units
    private final float gridX;      // world-unit X of grid left edge
    private final float gridY;      // world-unit Y of grid bottom edge

    // -----------------------------------------------------------------------
    // Viewport / Stage
    // -----------------------------------------------------------------------
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage hudStage;
    private ShapeRenderer shapeRenderer;

    // -----------------------------------------------------------------------
    // Fonts
    // -----------------------------------------------------------------------
    private BitmapFont hudFont;
    private BitmapFont smallFont;
    private BitmapFont buttonFont;

    // -----------------------------------------------------------------------
    // HUD labels (updated each frame)
    // -----------------------------------------------------------------------
    private Label scoreLabel;
    private Label livesLabel;
    private Label timerLabel;

    // -----------------------------------------------------------------------
    // Snake state
    // -----------------------------------------------------------------------
    private Array<GridPoint2> snake;
    private int dirX;
    private int dirY;
    private int pendingDirX;
    private int pendingDirY;

    // -----------------------------------------------------------------------
    // Food
    // -----------------------------------------------------------------------
    private GridPoint2 food;

    // -----------------------------------------------------------------------
    // Power-up on grid
    // -----------------------------------------------------------------------
    private GridPoint2 powerUpPos;
    private int        powerUpType;
    private boolean    powerUpVisible;
    private float      powerUpLifeTimer;
    private float      powerUpSpawnTimer;

    // -----------------------------------------------------------------------
    // Active power-up effects
    // -----------------------------------------------------------------------
    private boolean speedActive;
    private float   speedTimer;
    private boolean ghostActive;
    private float   ghostTimer;

    // -----------------------------------------------------------------------
    // Game state
    // -----------------------------------------------------------------------
    private int   score;
    private int   foodEaten;
    private int   lives;
    private float survivalTime;
    private float stepInterval;
    private float stepTimer;
    private boolean gameOverHandled;

    // -----------------------------------------------------------------------
    // Input adapter (stored as field for re-registration in show())
    // -----------------------------------------------------------------------
    private InputAdapter touchInputAdapter;
    private float touchStartX;
    private float touchStartY;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    public BaseGameScreen(MainGame game, int mode, int gridSize) {
        this.game     = game;
        this.mode     = mode;
        this.gridSize = gridSize;

        // Grid geometry
        switch (gridSize) {
            case Constants.GRID_SIZE_SMALL:
                gridPixels = Constants.GRID_PIXELS_SMALL;
                break;
            case Constants.GRID_SIZE_MEDIUM:
                gridPixels = Constants.GRID_PIXELS_MEDIUM;
                break;
            default:
                gridPixels = Constants.GRID_PIXELS_LARGE;
                break;
        }
        cellSize = gridPixels / gridSize;
        gridX    = (Constants.WORLD_WIDTH  - gridPixels) / 2f;
        gridY    = (Constants.WORLD_HEIGHT - gridPixels) / 2f;

        // Camera / viewport / stage
        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        hudStage = new Stage(viewport, game.batch);

        shapeRenderer = new ShapeRenderer();

        buildFonts();
        buildHUD();
        buildTouchAdapter();

        // Game logic
        initGame();

        game.playMusic(Constants.MUSIC_GAMEPLAY);
    }

    // -----------------------------------------------------------------------
    // Setup helpers
    // -----------------------------------------------------------------------
    private void buildFonts() {
        FreeTypeFontGenerator hudGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Orbitron-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter hp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        hp.size  = Constants.FONT_SIZE_HUD;
        hp.color = Color.WHITE;
        hudFont  = hudGen.generateFont(hp);
        hudGen.dispose();

        FreeTypeFontGenerator smallGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter sp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        sp.size   = Constants.FONT_SIZE_SMALL;
        sp.color  = Color.WHITE;
        smallFont = smallGen.generateFont(sp);
        smallGen.dispose();

        FreeTypeFontGenerator btnGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter bp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        bp.size    = Constants.FONT_SIZE_BODY;
        bp.color   = Color.WHITE;
        buttonFont = btnGen.generateFont(bp);
        btnGen.dispose();
    }

    private void buildHUD() {
        Color cyan      = new Color(0f, 0.85f, 1f, 1f);
        Color magenta   = new Color(1f, 0f, 0.43f, 1f);
        Color limeGreen = new Color(0.22f, 1f, 0.08f, 1f);

        Label.LabelStyle hudStyle   = new Label.LabelStyle(hudFont,   cyan);
        Label.LabelStyle liveStyle  = new Label.LabelStyle(hudFont,   magenta);
        Label.LabelStyle timerStyle = new Label.LabelStyle(smallFont, limeGreen);

        scoreLabel = new Label("SCORE: 0", hudStyle);
        livesLabel = new Label("LIVES: 3", liveStyle);
        timerLabel = new Label("0s", timerStyle);

        // Pause button
        ImageButton pauseBtn = new ImageButton(
                new TextureRegionDrawable(game.manager.get("sprites/icon_pause.png", Texture.class)),
                new TextureRegionDrawable(game.manager.get("sprites/icon_play.png",  Texture.class)));

        pauseBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get(Constants.SFX_CLICK, Sound.class).play(1.0f);
                game.setScreen(new PauseScreen(game, BaseGameScreen.this, mode, gridSize));
            }
        });

        Table table = new Table();
        table.setFillParent(true);

        // Top row: score | timer | lives
        table.top().padTop(Constants.HUD_PADDING);
        table.add(scoreLabel).expandX().left().padLeft(Constants.HUD_PADDING);
        table.add(timerLabel).expandX().center();
        table.add(livesLabel).expandX().right().padRight(Constants.HUD_PADDING);
        table.row();

        // Middle spacer
        table.add().colspan(3).expandY();
        table.row();

        // Bottom row: pause button centered
        table.add(pauseBtn)
                .colspan(3)
                .size(Constants.BTN_ROUND_SIZE, Constants.BTN_ROUND_SIZE)
                .center()
                .padBottom(Constants.HUD_PADDING);

        hudStage.addActor(table);
    }

    private void buildTouchAdapter() {
        touchInputAdapter = new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int ptr, int btn) {
                if (ptr != 0) return false;
                touchStartX = x;
                touchStartY = y;
                return true;
            }

            @Override
            public boolean touchUp(int x, int y, int ptr, int btn) {
                if (ptr != 0) return false;
                float dx = x - touchStartX;
                float dy = y - touchStartY; // screen Y increases downward

                if (Math.abs(dx) < SWIPE_THRESHOLD && Math.abs(dy) < SWIPE_THRESHOLD) {
                    return false; // tap — too small to determine direction
                }

                if (Math.abs(dx) >= Math.abs(dy)) {
                    // Horizontal swipe
                    setDirection(dx > 0 ? 1 : -1, 0);
                } else {
                    // Vertical swipe — screen Y inverted vs world Y
                    setDirection(0, dy > 0 ? -1 : 1);
                }
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.DPAD_RIGHT: setDirection( 1,  0); return true;
                    case Input.Keys.DPAD_LEFT:  setDirection(-1,  0); return true;
                    case Input.Keys.DPAD_UP:    setDirection( 0,  1); return true;
                    case Input.Keys.DPAD_DOWN:  setDirection( 0, -1); return true;
                    case Input.Keys.BACK:
                        game.setScreen(new MainMenuScreen(game));
                        return true;
                }
                return false;
            }
        };
    }

    private void setDirection(int dx, int dy) {
        // Prevent 180° reversal when snake has more than 1 segment
        if (snake != null && snake.size > 1 && dx == -dirX && dy == -dirY) return;
        pendingDirX = dx;
        pendingDirY = dy;
    }

    private void initGame() {
        snake     = new Array<GridPoint2>();
        score     = 0;
        foodEaten = 0;
        lives     = Constants.INITIAL_LIVES;
        survivalTime  = 0f;
        stepInterval  = Constants.SNAKE_STEP_INTERVAL_INITIAL;
        stepTimer     = 0f;
        gameOverHandled = false;

        powerUpVisible   = false;
        powerUpSpawnTimer = 0f;
        powerUpLifeTimer  = 0f;
        speedActive = false;
        speedTimer  = 0f;
        ghostActive = false;
        ghostTimer  = 0f;

        resetSnake();
        spawnFood();
    }

    private void resetSnake() {
        snake.clear();
        int cx = gridSize / 2;
        int cy = gridSize / 2;
        snake.add(new GridPoint2(cx,     cy));
        snake.add(new GridPoint2(cx - 1, cy));
        snake.add(new GridPoint2(cx - 2, cy));
        dirX = 1; dirY = 0;
        pendingDirX = 1; pendingDirY = 0;
        stepTimer = 0f;
    }

    private void spawnFood() {
        GridPoint2 pos;
        do {
            pos = new GridPoint2(MathUtils.random(0, gridSize - 1),
                                 MathUtils.random(0, gridSize - 1));
        } while (occupiedBySnake(pos) || (powerUpVisible && pos.equals(powerUpPos)));
        food = pos;
    }

    private void spawnPowerUp() {
        GridPoint2 pos;
        int attempts = 0;
        do {
            pos = new GridPoint2(MathUtils.random(0, gridSize - 1),
                                 MathUtils.random(0, gridSize - 1));
            attempts++;
            if (attempts > 200) return; // grid is too full
        } while (occupiedBySnake(pos) || pos.equals(food));
        powerUpPos     = pos;
        powerUpType    = MathUtils.randomBoolean() ? POWER_TYPE_SPEED : POWER_TYPE_GHOST;
        powerUpVisible = true;
        powerUpLifeTimer = 0f;
    }

    private boolean occupiedBySnake(GridPoint2 p) {
        for (int i = 0; i < snake.size; i++) {
            if (snake.get(i).equals(p)) return true;
        }
        return false;
    }

    // -----------------------------------------------------------------------
    // Game update
    // -----------------------------------------------------------------------
    private void update(float delta) {
        survivalTime += delta;
        stepTimer    += delta;

        // Power-up spawn timer
        powerUpSpawnTimer += delta;
        if (!powerUpVisible && powerUpSpawnTimer >= Constants.POWER_UP_SPAWN_INTERVAL) {
            powerUpSpawnTimer = 0f;
            spawnPowerUp();
        }

        // Power-up lifetime
        if (powerUpVisible) {
            powerUpLifeTimer += delta;
            if (powerUpLifeTimer >= Constants.POWER_UP_LIFETIME) {
                powerUpVisible = false;
            }
        }

        // Active effect timers
        if (speedActive) {
            speedTimer += delta;
            if (speedTimer >= Constants.POWER_UP_SPEED_DURATION) {
                speedActive = false;
            }
        }
        if (ghostActive) {
            ghostTimer += delta;
            if (ghostTimer >= Constants.POWER_UP_GHOST_DURATION) {
                ghostActive = false;
            }
        }

        // Step
        float effectiveInterval = speedActive ? stepInterval * 0.5f : stepInterval;
        if (stepTimer >= effectiveInterval) {
            stepTimer = 0f;
            doStep();
        }

        // Update HUD
        scoreLabel.setText("SCORE: " + score);
        livesLabel.setText("LIVES: " + lives);
        timerLabel.setText((int) survivalTime + "s");
    }

    private void doStep() {
        // Commit pending direction (prevent 180° reversal)
        if (!(pendingDirX == -dirX && pendingDirY == -dirY)) {
            dirX = pendingDirX;
            dirY = pendingDirY;
        }

        int nx = snake.first().x + dirX;
        int ny = snake.first().y + dirY;

        // Wall handling
        if (mode == Constants.MODE_CLASSIC) {
            if (nx < 0 || nx >= gridSize || ny < 0 || ny >= gridSize) {
                loseLife();
                return;
            }
        } else {
            nx = ((nx % gridSize) + gridSize) % gridSize;
            ny = ((ny % gridSize) + gridSize) % gridSize;
        }

        GridPoint2 newHead = new GridPoint2(nx, ny);

        // Self-collision (skip last segment because it will be removed)
        if (!ghostActive) {
            for (int i = 0; i < snake.size - 1; i++) {
                if (snake.get(i).equals(newHead)) {
                    loseLife();
                    return;
                }
            }
        }

        boolean ateFood    = newHead.equals(food);
        boolean atePowerUp = powerUpVisible && newHead.equals(powerUpPos);

        // Advance snake
        snake.insert(0, newHead);
        if (!ateFood) {
            snake.removeIndex(snake.size - 1);
        }

        if (ateFood) {
            foodEaten++;
            score += Constants.SCORE_PER_FOOD;
            stepInterval = Math.max(Constants.SNAKE_STEP_INTERVAL_MIN,
                    stepInterval - Constants.SNAKE_SPEED_INCREASE);
            spawnFood();
            if (game.sfxEnabled)
                game.manager.get(Constants.SFX_COIN, Sound.class).play(1.0f);
        }

        if (atePowerUp) {
            score += Constants.SCORE_PER_POWER_UP;
            activatePowerUp(powerUpType);
            powerUpVisible = false;
            powerUpSpawnTimer = 0f;
            if (game.sfxEnabled)
                game.manager.get(Constants.SFX_POWER_UP, Sound.class).play(1.0f);
        }
    }

    private void activatePowerUp(int type) {
        if (type == POWER_TYPE_SPEED) {
            speedActive = true;
            speedTimer  = 0f;
        } else {
            ghostActive = true;
            ghostTimer  = 0f;
        }
    }

    private void loseLife() {
        lives--;
        if (game.sfxEnabled)
            game.manager.get(Constants.SFX_HIT, Sound.class).play(1.0f);

        if (lives <= 0) {
            triggerGameOver();
        } else {
            resetSnake();
        }
    }

    private void triggerGameOver() {
        if (gameOverHandled) return;
        gameOverHandled = true;
        // GameOverScreen constructor calls LeaderboardScreen.addScore(score)
        game.setScreen(new GameOverScreen(game, score, foodEaten));
    }

    // -----------------------------------------------------------------------
    // Rendering
    // -----------------------------------------------------------------------
    @Override
    public void render(float delta) {
        update(delta);

        // If game over was triggered during update, setScreen already ran — skip rendering this frame
        if (gameOverHandled) return;

        ScreenUtils.clear(0.039f, 0.055f, 0.153f, 1f);

        // Draw background texture
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(game.manager.get(Constants.BG_GRID, Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        // Draw grid elements with ShapeRenderer
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(camera.combined);

        // Dark overlay for the play area
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.1f, 0.1f, 0.17f, 0.75f);
        shapeRenderer.rect(gridX, gridY, gridPixels, gridPixels);
        shapeRenderer.end();

        // Grid border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0f, 0.85f, 1f, 0.5f);
        shapeRenderer.rect(gridX, gridY, gridPixels, gridPixels);
        shapeRenderer.end();

        // Grid lines (subtle)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0f, 0.85f, 1f, 0.08f);
        for (int i = 1; i < gridSize; i++) {
            float x = gridX + i * cellSize;
            float y = gridY + i * cellSize;
            shapeRenderer.line(x, gridY, x, gridY + gridPixels);
            shapeRenderer.line(gridX, y, gridX + gridPixels, y);
        }
        shapeRenderer.end();

        // Food (lime green circle)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.22f, 1f, 0.08f, 1f);
        float fx = gridX + food.x * cellSize + cellSize * 0.5f;
        float fy = gridY + food.y * cellSize + cellSize * 0.5f;
        shapeRenderer.circle(fx, fy, cellSize * 0.35f, 16);

        // Snake segments
        for (int i = 0; i < snake.size; i++) {
            GridPoint2 seg = snake.get(i);
            float sx = gridX + seg.x * cellSize + 1f;
            float sy = gridY + seg.y * cellSize + 1f;
            float sw = cellSize - 2f;
            float sh = cellSize - 2f;

            if (i == 0) {
                // Head: bright cyan
                shapeRenderer.setColor(0f, 0.85f, 1f, 1f);
            } else {
                // Body: interpolate cyan → magenta
                float t = (float) i / (float) Math.max(snake.size - 1, 1);
                float r = t;
                float g = 0.85f * (1f - t);
                float b = 1f - t * 0.57f;
                shapeRenderer.setColor(r, g, b, 1f);
            }
            shapeRenderer.rect(sx, sy, sw, sh);
        }
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Power-up icon (texture via batch)
        if (powerUpVisible) {
            String iconPath = powerUpType == POWER_TYPE_SPEED
                    ? Constants.ICON_SPEED_BOOST
                    : Constants.ICON_GHOST_PASS;
            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();
            game.batch.draw(
                    game.manager.get(iconPath, Texture.class),
                    gridX + powerUpPos.x * cellSize,
                    gridY + powerUpPos.y * cellSize,
                    cellSize, cellSize);
            game.batch.end();
        }

        // HUD stage
        hudStage.act(delta);
        hudStage.draw();
    }

    // -----------------------------------------------------------------------
    // Screen lifecycle
    // -----------------------------------------------------------------------
    @Override
    public void show() {
        // Re-register input when returning from PauseScreen
        Gdx.input.setInputProcessor(new InputMultiplexer(hudStage, touchInputAdapter));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void hide()   {}
    @Override public void pause()  {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        hudStage.dispose();
        shapeRenderer.dispose();
        hudFont.dispose();
        smallFont.dispose();
        buttonFont.dispose();
    }
}
