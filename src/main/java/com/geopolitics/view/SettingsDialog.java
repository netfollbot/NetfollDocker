package com.geopolitics.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.Locale;

public class SettingsDialog extends Dialog {
    public SettingsDialog(UI ui, Runnable onSave, Runnable onExit, Runnable onToMenu, boolean musicEnabled, java.util.function.Consumer<Boolean> onMusicToggle) {
        setHeaderTitle(getTranslation("settings.title"));
        setModal(true);
        setDraggable(true);
        setResizable(false);

        ComboBox<Locale> languages = new ComboBox<>(getTranslation("settings.language"));
        languages.setItems(new Locale("ru"), Locale.ENGLISH);
        languages.setItemLabelGenerator(loc -> loc.getDisplayLanguage(loc));
        languages.addValueChangeListener(ev -> {
            if (ev.getValue() != null) {
                ui.setLocale(ev.getValue());
                ui.getPage().reload();
            }
        });

        Button toggleMusic = new Button(musicEnabled ? getTranslation("settings.music.off") : getTranslation("settings.music.on"), new Icon(VaadinIcon.MUSIC));
        toggleMusic.addClickListener(e -> {
            boolean newVal = !musicEnabled;
            onMusicToggle.accept(newVal);
            close();
        });

        Button save = new Button(getTranslation("settings.save"), new Icon(VaadinIcon.CLOUD_UPLOAD));
        save.addClickListener(e -> { onSave.run(); close(); });
        Button toMenu = new Button(getTranslation("settings.toMenu"), new Icon(VaadinIcon.MENU));
        toMenu.addClickListener(e -> { onToMenu.run(); close(); });
        Button exit = new Button(getTranslation("settings.exit"), new Icon(VaadinIcon.EXIT));
        exit.addClickListener(e -> { onExit.run(); close(); });

        Div content = new Div(languages, new Span(getTranslation("settings.music")), toggleMusic);
        content.getStyle().set("display", "flex");
        content.getStyle().set("flexDirection", "column");
        content.getStyle().set("gap", "0.5rem");
        add(content);
        getFooter().add(save, toMenu, exit);
    }

    public static void openCentered(UI ui) {
        SettingsDialog dialog = new SettingsDialog(ui, () -> {}, () -> ui.getPage().executeJs("window.close()"), () -> ui.navigate(MenuView.class), true, ignored -> {});
        dialog.open();
    }
}
