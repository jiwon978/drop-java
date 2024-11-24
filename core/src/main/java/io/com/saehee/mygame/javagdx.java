package io.com.saehee.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

public class javagdx extends ApplicationAdapter {

	private static final float VIRTUAL_WIDTH = 720; // 가로
	private static final float VIRTUAL_HEIGHT = 960; // 세로
    private static final float SCALE = 1 / 100f; // 픽셀 -> Box2D 변환 비율

    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private World world;
    private ShapeRenderer shapeRenderer;
    private Texture ballTexture;
    private List<Body> balls;
    private boolean isGameStarted = false;
    private boolean isAboutScreen = false;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Animation<Texture> backgroundAnimation;
    private float elapsedTime = 0f; // 경과 시간

    private Body playButtonBody;
    private Body aboutButtonBody;
    private Body exitButtonBody;
    private boolean showButtonBorders = true;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("assets/ui/uiskin.json"));
        batch = new SpriteBatch();

        shapeRenderer = new ShapeRenderer();
        world = new World(new Vector2(0, -4.9f), true); // 중력 설정
        balls = new ArrayList<>();

        ballTexture = new Texture(Gdx.files.internal("assets/ball.png"));

        // 배경 애니메이션 로드
        loadBackgroundAnimation();

        // UI 버튼 생성
        setupUI();

        // 물리 세계에 공 추가
        for (int i = 0; i < 10; i++) {
            addBall();
        }
    }

    private void loadBackgroundAnimation() {
        Array<Texture> frames = new Array<>();
        for (int i = 1; i <= 100; i++) { // 1부터 100까지 파일 로드
            String fileName = "C:/Users/user/Documents/DROP-JAVA2/assets/" + i + ".jpeg"; // 절대 경로 사용
            System.out.println("Trying to load: " + fileName); // 디버깅 출력
            if (Gdx.files.absolute(fileName).exists()) {
                frames.add(new Texture(Gdx.files.absolute(fileName))); // Texture 로드
            } else {
                System.err.println("File not found: " + fileName); // 파일이 없을 경우 경고
            }
        }

        if (frames.size > 0) {
            backgroundAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP); // 각 프레임 0.1초
        } else {
            throw new RuntimeException("No valid image files found for the animation.");
        }
    }

    private void setupUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Play 버튼
        TextButton playButton = new TextButton("Play", skin);
        playButton.getLabel().setFontScale(1.5f);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                isGameStarted = true;
                showButtonBorders = false;
                stage.clear(); // 모든 버튼 제거
                startGame();
            }
        });

        // About 버튼
        TextButton aboutButton = new TextButton("About", skin);
        aboutButton.getLabel().setFontScale(1.5f);
        aboutButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                isAboutScreen = true;
                showButtonBorders = false;
                stage.clear(); // 모든 버튼 제거
                showAboutScreen();
            }
        });

        // Exit 버튼
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.getLabel().setFontScale(1.5f);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(playButton).size(300, 100).padBottom(20).row();
        table.add(aboutButton).size(300, 100).padBottom(20).row();
        table.add(exitButton).size(300, 100);
    }

    private void startGame() {
        showButtonBorders = false;
        removeButtonBodies();

        // 바닥 생성
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2(VIRTUAL_WIDTH / 2 * SCALE, 10 * SCALE));
        Body groundBody = world.createBody(groundBodyDef);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(VIRTUAL_WIDTH / 2 * SCALE, 10 * SCALE);
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();

        // 공 추가
        for (int i = 0; i < 10; i++) {
            addBall();
        }
    }

    private void showAboutScreen() {
        Table aboutTable = new Table();
        aboutTable.setFillParent(true);
        stage.addActor(aboutTable);

        TextButton fullscreenButton = new TextButton("Fullscreen", skin);
        fullscreenButton.getLabel().setFontScale(1.5f);
        fullscreenButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        });

        TextButton windowedButton = new TextButton("Windowed", skin);
        windowedButton.getLabel().setFontScale(1.5f);
        windowedButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.graphics.setWindowedMode(1280, 720);
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.getLabel().setFontScale(1.5f);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                isAboutScreen = false;
                stage.clear();
                setupUI(); // 홈 화면으로 돌아가기
            }
        });

        aboutTable.add(fullscreenButton).size(300, 100).padBottom(20).row();
        aboutTable.add(windowedButton).size(300, 100).padBottom(20).row();
        aboutTable.add(backButton).size(300, 100);
    }

    private void addBall() {
        BodyDef ballBodyDef = new BodyDef();
        ballBodyDef.type = BodyDef.BodyType.DynamicBody;
        ballBodyDef.position.set((float) Math.random() * VIRTUAL_WIDTH * SCALE, VIRTUAL_HEIGHT * SCALE);

        Body ball = world.createBody(ballBodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(20f * SCALE);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 2.0f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.8f;

        ball.createFixture(fixtureDef);
        circle.dispose();

        balls.add(ball);
    }

    private void removeButtonBodies() {
        if (playButtonBody != null) {
            world.destroyBody(playButtonBody);
        }
        if (aboutButtonBody != null) {
            world.destroyBody(aboutButtonBody);
        }
        if (exitButtonBody != null) {
            world.destroyBody(exitButtonBody);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();
        elapsedTime += deltaTime;

        // 배경 애니메이션
        if (backgroundAnimation != null) {
            Texture currentFrame = backgroundAnimation.getKeyFrame(elapsedTime);
            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            // 화면 크기에 맞게 배경 이미지 드로잉
            batch.draw(currentFrame, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

            batch.end();
        }

        // 나머지 렌더링 코드
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        ballTexture.dispose();
        if (world != null) {
            world.dispose();
        }
        for (Texture texture : backgroundAnimation.getKeyFrames()) {
            texture.dispose();
        }
    }
}
