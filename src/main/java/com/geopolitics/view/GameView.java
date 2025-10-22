package com.geopolitics.view;

import com.geopolitics.audio.AudioManager;
import com.geopolitics.model.GameState;
import com.geopolitics.service.GameLoopService;
import com.geopolitics.service.SaveService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.text.NumberFormat;
import java.util.Locale;

@Route(value = "game", layout = MainLayout.class)
@CssImport(value = "./styles/game.css")
public class GameView extends VerticalLayout implements BeforeEnterObserver {
    private GameState state;
    private AudioManager audioManager;
    private GameLoopService loop;
    private final SaveService saveService = new SaveService();

    private final Span dateLabel = new Span();
    private final Span populationLabel = new Span();
    private final Span economyLabel = new Span();
    private final Span reserveLabel = new Span();

    private boolean musicEnabled = true;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UI ui = getUI().orElseThrow();
        this.state = ui.getSession().getAttribute(GameState.class);
        if (state == null) {
            event.forwardTo(MenuView.class);
            return;
        }
        audioManager = new AudioManager(ui);
        buildUI();
        loop = new GameLoopService(ui, state, s -> updateStats(), () -> saveService.save(state, true));
        loop.start();
        if (musicEnabled) {
            audioManager.play("/audio/main-theme.mp3", true);
        }
        updateStats();
    }

    private void buildUI() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Top bar
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.addClassName("top-bar");
        topBar.setWidthFull();

        Button speed1 = new Button(getTranslation("speed.normal"));
        speed1.addClickListener(e -> state.setSpeed(GameState.Speed.NORMAL));
        Button speed2 = new Button(getTranslation("speed.double"));
        speed2.addClickListener(e -> state.setSpeed(GameState.Speed.DOUBLE));
        Button speed3 = new Button(getTranslation("speed.triple"));
        speed3.addClickListener(e -> state.setSpeed(GameState.Speed.TRIPLE));

        Button settings = new Button(new Icon(VaadinIcon.COG));
        settings.addClickListener(e -> openSettings());

        Div dateBox = statBox(VaadinIcon.CALENDAR, "stat.date", dateLabel);
        Div popBox = statBox(VaadinIcon.GROUP, "stat.population", populationLabel);
        Div ecoBox = statBox(VaadinIcon.MONEY, "stat.economy", economyLabel);
        Div resBox = statBox(VaadinIcon.SHIELD, "stat.reserve", reserveLabel);

        topBar.add(speed1, speed2, speed3, dateBox, popBox, ecoBox, resBox, settings);
        topBar.expand(dateBox);

        // Map/canvas placeholder
        Div map = new Div();
        map.addClassName("map");
        map.setText("Map placeholder");

        add(topBar, map);
        setFlexGrow(1, map);
    }

    private Div statBox(VaadinIcon iconType, String i18nKey, Span value) {
        Div box = new Div();
        box.addClassName("stat-box");
        Icon icon = new Icon(iconType);
        icon.addClassName("stat-icon");
        Span label = new Span(getTranslation(i18nKey) + ": ");
        box.add(icon, label, value);
        return box;
    }

    private void updateStats() {
        NumberFormat nf = NumberFormat.getInstance(getLocale());
        dateLabel.setText(state.getCurrentDate().toString());
        populationLabel.setText(nf.format(state.getPopulation()));
        economyLabel.setText(String.format(Locale.ROOT, "%.2f B$", state.getEconomyBillions()));
        reserveLabel.setText(nf.format(state.getMilitaryReserve()));
    }

    private void openSettings() {
        UI ui = getUI().orElseThrow();
        SettingsDialog dialog = new SettingsDialog(ui,
                () -> saveService.save(state, false),
                () -> ui.getPage().executeJs("window.close()"),
                () -> { loop.stop(); ui.navigate(MenuView.class); },
                musicEnabled,
                enabled -> {
                    musicEnabled = enabled;
                    if (musicEnabled) {
                        audioManager.play("/audio/main-theme.mp3", true);
                    } else {
                        audioManager.stop();
                    }
                }
        );
        dialog.open();
    }
}
