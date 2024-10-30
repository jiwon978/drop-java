package io.com.saehee.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import java.util.ArrayList;
import java.util.List;

public class javagdx extends ApplicationAdapter {

    private Stage stage;
    private Skin skin;
    private int currentMapIndex = 0;
    private static final float VIRTUAL_WIDTH = 1280;
    private static final float VIRTUAL_HEIGHT = 720;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Texture ballTexture;
    private Texture backgroundTexture;  // 배경 텍스처 추가
    private List<Body> balls;

    @Override
    public void create() {
        stage = new Stage(new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        // 기본 스킨 로드
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // 스프라이트 배치 및 텍스처 로드
        batch = new SpriteBatch();
        ballTexture = new Texture(Gdx.files.absolute("C:/Users/user/Documents/DROP-JAVA2/assets/ball.png")); // 절대 경로를 사용해 텍스처 로드
        backgroundTexture = new Texture(Gdx.files.absolute("C:/Users/user/Documents/DROP-JAVA2/assets/back.jpeg")); // 배경 이미지 절대 경로로 로드

        // 테이블 생성 (UI 배치를 위해 사용)
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // 게임 제목 라벨
        Label titleLabel = new Label("DROP-JAVA", skin);
        titleLabel.setFontScale(2f);  // 제목 크기 조정

        // 실행 버튼
        TextButton playButton = new TextButton("Play", skin);
        playButton.getLabel().setFontScale(1.5f);  // 버튼 글씨 크기 조정
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // 맵 선택 화면으로 이동
                showMapSelection();
            }
        });

        // 설명 버튼
        TextButton aboutButton = new TextButton("About", skin);
        aboutButton.getLabel().setFontScale(1.5f);  // 버튼 글씨 크기 조정
        aboutButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // 설명 버튼 클릭 시 공 하나 추가
                addBall();
            }
        });

        // 종료 버튼
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.getLabel().setFontScale(1.5f);  // 버튼 글씨 크기 조정
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // 테이블에 요소 배치
        table.add(titleLabel).padBottom(40).row();  // 제목 추가 후 줄 바꿈
        table.add(playButton).padBottom(20).row();  // 실행 버튼 추가 후 줄 바꿈
        table.add(aboutButton).padBottom(20).row(); // 설명 버튼 추가 후 줄 바꿈
        table.add(exitButton);                      // 종료 버튼 추가

        // 초기 공 50개 생성
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();
        balls = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            addBall();
        }
    }

    private void showMapSelection() {
        // 맵 선택 화면을 위한 새로운 스테이지 생성
        stage.clear();
        Table mapTable = new Table();
        mapTable.setFillParent(true);
        stage.addActor(mapTable);

        // 빈 맵 화면 표시 (임시 화면)
        Label mapLabel = new Label("Select a Map (Placeholder)", skin);
        mapLabel.setFontScale(2f);
        mapTable.add(mapLabel).padBottom(40).row();

        // 이전 맵 버튼
        TextButton previousButton = new TextButton("< Previous", skin);
        previousButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                currentMapIndex = (currentMapIndex - 1 + 3) % 3; // 3개의 맵이 있다고 가정
                System.out.println("Previous Map: " + currentMapIndex);
            }
        });

        // 다음 맵 버튼
        TextButton nextButton = new TextButton("Next >", skin);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                currentMapIndex = (currentMapIndex + 1) % 3; // 3개의 맵이 있다고 가정
                System.out.println("Next Map: " + currentMapIndex);
            }
        });

        // 시작 버튼
        TextButton startButton = new TextButton("Start Game", skin);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // 게임 시작 코드 작성
                startGame();
            }
        });

        // 이전 화면으로 돌아가기 버튼
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                create(); // 이전 화면으로 돌아가기
            }
        });

        // 버튼 배치
        HorizontalGroup buttonGroup = new HorizontalGroup();
        buttonGroup.space(20f);
        buttonGroup.addActor(previousButton);
        buttonGroup.addActor(startButton);
        buttonGroup.addActor(nextButton);
        buttonGroup.addActor(backButton);
        mapTable.add(buttonGroup);
    }

    private void startGame() {
        // 핀볼 게임 바닥 생성
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2(0, -10));
        Body groundBody = world.createBody(groundBodyDef);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(VIRTUAL_WIDTH, 10.0f);
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();

        // 초기 공 10개 생성
        for (int i = 0; i < 10; i++) {
            addBall();
        }
    }

    private void addBall() {
        if (world != null) {
            // 공 생성
            BodyDef ballBodyDef = new BodyDef();
            ballBodyDef.type = BodyType.DynamicBody;
            ballBodyDef.position.set((float) (Math.random() * VIRTUAL_WIDTH), VIRTUAL_HEIGHT - 50);
            Body ball = world.createBody(ballBodyDef);

            CircleShape circle = new CircleShape();
            circle.setRadius(20f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circle;
            fixtureDef.density = 2.0f; // 공의 무게 조절
            fixtureDef.friction = 0.4f;
            fixtureDef.restitution = 0.8f; // 탄성 증가

            ball.createFixture(fixtureDef);
            circle.dispose();

            balls.add(ball);
        }
    }

    @Override
    public void render() {
        // 화면을 지우고 배경 이미지 설정
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);  // 배경 이미지 그리기
        batch.end();

        if (world != null) {
            // 물리 시뮬레이션 진행
            world.step(1 / 60f, 6, 2);

            // 디버그 렌더링 (테스트용)
            debugRenderer.render(world, stage.getCamera().combined);
        }

        // 스프라이트 배치 시작
        batch.begin();
        for (Body ball : balls) {
            Vector2 position = ball.getPosition();
            batch.draw(ballTexture, position.x - 20, position.y - 20, 40, 40); // 공의 텍스처 그리기
        }
        batch.end();

        // UI 요소들을 렌더링
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        batch.dispose();
        ballTexture.dispose();
        backgroundTexture.dispose();  // 배경 텍스처 해제
        if (world != null) {
            world.dispose();
            debugRenderer.dispose();
        }
    }
}

