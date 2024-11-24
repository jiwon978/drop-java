package io.com.saehee.mygame.physics;

public class Body {
    public Vector2 position;
    public Vector2 velocity;
    public Vector2 acceleration;
    public float mass;

    public Body(float x, float y, float mass) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.acceleration = new Vector2(0, -50f); // 중력 가속도
        this.mass = mass;
    }

    public void update(float deltaTime) {
        velocity = velocity.add(acceleration.scale(deltaTime));
        position = position.add(velocity.scale(deltaTime));
        float maxSpeed = 200f;
        if (velocity.x > maxSpeed) velocity.x = maxSpeed;
        if (velocity.y > maxSpeed) velocity.y = maxSpeed;
        if (velocity.x < -maxSpeed) velocity.x = -maxSpeed;
        if (velocity.y < -maxSpeed) velocity.y = -maxSpeed;
    }
}
