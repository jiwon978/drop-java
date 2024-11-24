package io.com.saehee.mygame.physics;

import java.util.ArrayList;
import java.util.List;

public class SimplePhysicsEngine {
    private List<Body> bodies = new ArrayList<>();
    private float deltaTime = 1 / 60f;

    public void addBody(Body body) {
        bodies.add(body);
    }

    public void update() {
        // 모든 공의 위치 업데이트
        for (Body body : bodies) {
            body.update(deltaTime);

            // 바닥과의 충돌 처리
            float groundLevel = 0; // 바닥의 y 위치
            if (body.position.y < groundLevel) {
                body.position.y = groundLevel;
                body.velocity.y = -body.velocity.y * 0.5f; // 바닥과의 충돌 반발력을 높임
            }
        }

        // 공끼리의 충돌 처리
        for (int i = 0; i < bodies.size(); i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
                Body a = bodies.get(i);
                Body b = bodies.get(j);

                // 두 공 사이의 거리 계산
                float distance = (float) Math.sqrt(Math.pow(b.position.x - a.position.x, 2) + Math.pow(b.position.y - a.position.y, 2));
                float radiusSum = a.mass + b.mass; // 두 공의 반지름 합(간단히 질량을 반지름으로 사용)

                // 충돌 감지 및 처리
                if (distance < radiusSum) {
                    resolveCollision(a, b);
                }
            }
        }
    }

    public void resolveCollision(Body a, Body b) {
        Vector2 relativeVelocity = new Vector2(b.velocity.x - a.velocity.x, b.velocity.y - a.velocity.y);
        float elasticity = 0.9f; // 반발 계수 설정으로 충돌 후 반발 효과를 줄임

        // 충돌 법선 벡터 계산 (두 공의 중심을 잇는 벡터)
        Vector2 normal = new Vector2(b.position.x - a.position.x, b.position.y - a.position.y).normalize();
        float velAlongNormal = relativeVelocity.dot(normal);

        // 두 공이 서로 멀어지고 있다면 충돌 처리하지 않음
        if (velAlongNormal > 0) return;

        // 충돌 후 반발력 계산 (법선 방향에만 반작용 적용)
        float j = -(1 + elasticity) * velAlongNormal / (1 / a.mass + 1 / b.mass);
        Vector2 impulse = normal.scale(j);

        // 반사 벡터를 정확히 반대 방향으로 적용
        a.velocity = a.velocity.sub(impulse.scale(1 / a.mass)); // a의 속도를 반대 방향으로 이동
        b.velocity = b.velocity.add(impulse.scale(1 / b.mass)); // b의 속도를 반대 방향으로 이동

        // 각 공의 속도 제한하여 과도한 반발 방지
        limitVelocity(a);
        limitVelocity(b);
    }

    // 속도를 제한하여 과도한 반발 방지
    private void limitVelocity(Body body) {
        float maxSpeed = 150f; // 최대 속도 제한
        if (body.velocity.x > maxSpeed) body.velocity.x = maxSpeed;
        if (body.velocity.y > maxSpeed) body.velocity.y = maxSpeed;
        if (body.velocity.x < -maxSpeed) body.velocity.x = -maxSpeed;
        if (body.velocity.y < -maxSpeed) body.velocity.y = -maxSpeed;
    }

}
