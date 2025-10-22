package com.geopolitics.view;

import com.geopolitics.model.GameState;
import com.geopolitics.service.SaveService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.Route;

import java.nio.file.Path;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Route(value = "continue", layout = MainLayout.class)
@CssImport(value = "styles/continue.css")
public class ContinueView extends Div {
    public record SaveEntry(Path path, SaveService.SaveData data) {}

    private final SaveService saveService = new SaveService();

    public ContinueView() {
        H2 title = new H2(getTranslation("continue.title"));
        Grid<SaveEntry> grid = new Grid<>(SaveEntry.class, false);
        grid.addColumn(se -> se.data().country().name()).setHeader("Country");
        grid.addColumn(se -> se.data().inGameDate().toString()).setHeader("In-game date");
        grid.addColumn(se -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault()).format(se.data().savedAt())).setHeader("Saved at");
        grid.addColumn(se -> se.data().type()).setHeader("Type");

        grid.setItems(saveService.listSaves());

        Button load = new Button(getTranslation("menu.continue"));
        load.addClickListener(e -> {
            SaveEntry entry = grid.asSingleSelect().getValue();
            if (entry != null) {
                UI ui = getUI().orElseThrow();
                GameState state = saveService.load(entry.data());
                ui.getSession().setAttribute(GameState.class, state);
                ui.navigate(GameView.class);
            }
        });

        add(title, grid, load);
    }
}
