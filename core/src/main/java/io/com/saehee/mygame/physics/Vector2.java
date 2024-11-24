package io.com.saehee.mygame.physics;

public class Vector2 {
    public float x, y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2 sub(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector2 scale(float scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    public Vector2 normalize() {
        float length = (float) Math.sqrt(x * x + y * y);
        if (length != 0) {
            this.x /= length;
            this.y /= length;
        }
        return this;
    }

    public float dot(Vector2 other) {
        return this.x * other.x + this.y * other.y;
    }
}

