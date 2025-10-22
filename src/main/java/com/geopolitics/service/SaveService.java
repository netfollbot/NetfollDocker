package com.geopolitics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.geopolitics.model.Country;
import com.geopolitics.model.GameState;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SaveService {
    private final Path savesDir;
    private final ObjectMapper mapper;

    public SaveService() {
        this(Paths.get("saves"));
    }

    public SaveService(Path savesDir) {
        this.savesDir = savesDir;
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            Files.createDirectories(savesDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static record SaveData(String id, Country country, LocalDate inGameDate, long population,
                                  double economyBillions, long reserve, Instant savedAt, String type) {}

    public SaveData save(GameState state, boolean autosave) {
        String type = autosave ? "autosave" : "manual";
        String id = autosave ? ("autosave-" + state.getCountry().name()) : UUID.randomUUID().toString();
        SaveData data = new SaveData(
                id,
                state.getCountry(),
                state.getCurrentDate(),
                state.getPopulation(),
                state.getEconomyBillions(),
                state.getMilitaryReserve(),
                Instant.now(),
                type
        );
        String filename = autosave ? ("autosave-" + state.getCountry().name() + ".json")
                : ("save-" + state.getCountry().name() + "-" + state.getCurrentDate() + "-" + System.currentTimeMillis() + ".json");
        Path file = savesDir.resolve(filename);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public GameState load(SaveData data) {
        GameState state = GameState.newGame(data.country());
        state.setCurrentDate(data.inGameDate());
        state.setPopulation(data.population());
        state.setEconomyBillions(data.economyBillions());
        state.setMilitaryReserve(data.reserve());
        return state;
    }

    public List<SaveListEntry> listSaves() {
        if (!Files.exists(savesDir)) {
            return List.of();
        }
        List<SaveListEntry> entries = new ArrayList<>();
        try (Stream<Path> stream = Files.list(savesDir)) {
            List<Path> files = stream.filter(p -> p.toString().endsWith(".json")).collect(Collectors.toList());
            for (Path p : files) {
                try {
                    SaveData data = mapper.readValue(p.toFile(), SaveData.class);
                    entries.add(new SaveListEntry(p, data));
                } catch (Exception ignored) {
                }
            }
        } catch (IOException e) {
            // ignore
        }
        entries.sort(Comparator.comparing((SaveListEntry s) -> s.data().savedAt()).reversed());
        return entries;
    }

    public void delete(Path path) {
        try { Files.deleteIfExists(path); } catch (IOException ignored) {}
    }

    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
