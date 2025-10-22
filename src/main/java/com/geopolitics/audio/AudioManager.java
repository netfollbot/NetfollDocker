package com.geopolitics.audio;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@JsModule("/src/audio-player.js")
public class AudioManager {
    private final UI ui;

    public AudioManager(UI ui) {
        this.ui = ui;
    }

    public void ensureScriptLoaded() {
        Page page = ui.getPage();
        page.addJavaScript("window._audioPlayerLoaded || (window._audioPlayerLoaded = true)");
    }

    public void play(String trackUrl, boolean loop) {
        ui.getPage().executeJs("window.audioPlayer && window.audioPlayer.play($0, $1)", trackUrl, loop);
    }

    public void stop() {
        ui.getPage().executeJs("window.audioPlayer && window.audioPlayer.stop()");
    }
}
