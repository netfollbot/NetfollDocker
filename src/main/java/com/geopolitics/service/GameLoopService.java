package com.geopolitics.service;

import com.geopolitics.model.GameState;
import com.vaadin.flow.component.UI;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class GameLoopService {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "game-loop");
        t.setDaemon(true);
        return t;
    });
    private ScheduledFuture<?> future;

    private final UI ui;
    private final GameState gameState;
    private final Consumer<GameState> onTick;
    private final Runnable onAutosave;

    public GameLoopService(UI ui, GameState gameState, Consumer<GameState> onTick, Runnable onAutosave) {
        this.ui = Objects.requireNonNull(ui);
        this.gameState = Objects.requireNonNull(gameState);
        this.onTick = Objects.requireNonNull(onTick);
        this.onAutosave = Objects.requireNonNull(onAutosave);
    }

    public void start() {
        stop();
        future = executor.scheduleAtFixedRate(this::tick, 1, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        if (future != null) {
            future.cancel(false);
            future = null;
        }
    }

    public void shutdown() {
        stop();
        executor.shutdownNow();
    }

    private void tick() {
        double daysPerSecond;
        switch (gameState.getSpeed()) {
            case DOUBLE -> daysPerSecond = 1.0; // 1 day per second
            case TRIPLE -> daysPerSecond = 2.0; // 2 days per second
            case NORMAL -> default -> daysPerSecond = 1.0 / 3.0; // 1 day per 3 seconds
        }

        double acc = gameState.getDayAccumulator() + daysPerSecond;
        int wholeDays = (int) Math.floor(acc);
        double leftover = acc - wholeDays;

        if (wholeDays > 0) {
            LocalDate before = gameState.getCurrentDate();
            LocalDate newDate = before.plusDays(wholeDays);
            gameState.setCurrentDate(newDate);
            gameState.applyGrowth(wholeDays);
            gameState.setDayAccumulator(leftover);

            // Autosave if crossed into a new month and day is 1
            if (newDate.getDayOfMonth() == 1) {
                onAutosave.run();
            }
            // push UI updates on UI thread
            ui.access(() -> onTick.accept(gameState));
        } else {
            gameState.setDayAccumulator(leftover);
        }
    }
}
