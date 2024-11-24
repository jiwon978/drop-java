package io.com.saehee.mygame.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.com.saehee.mygame.javagdx;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new javagdx(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("DROP-JAVA");
        configuration.setWindowedMode(720, 960); // 16:9 비율 설정 (1280x720)
        configuration.useVsync(true);
        configuration.setForegroundFPS(60); // 60 FPS 제한
        configuration.setResizable(false); // 창 크기 고정
        return configuration;
    }
}