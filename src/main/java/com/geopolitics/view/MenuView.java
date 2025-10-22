package com.geopolitics.view;

import com.geopolitics.i18n.AppI18NProvider;
import com.geopolitics.service.SaveService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;

@Route(value = "", layout = MainLayout.class)
@CssImport(value = "styles/menu.css")
public class MenuView extends VerticalLayout {
    private final SaveService saveService = new SaveService();

    public MenuView() {
        setSizeFull();
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1(getTranslation("app.title"));

        Button newGame = new Button(getTranslation("menu.new"), new Icon(VaadinIcon.PLAY));
        newGame.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(NewGameView.class)));

        Button cont = new Button(getTranslation("menu.continue"), new Icon(VaadinIcon.ARROW_FORWARD));
        cont.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(ContinueView.class)));

        Button settings = new Button(getTranslation("menu.settings"), new Icon(VaadinIcon.COG));
        settings.addClickListener(e -> SettingsDialog.openCentered(getUI().get()));

        Button profile = new Button(getTranslation("menu.profile"), new Icon(VaadinIcon.USER));
        profile.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(ProfileView.class)));

        Button exit = new Button(getTranslation("menu.exit"), new Icon(VaadinIcon.EXIT));
        exit.addClickListener(e -> {
            VaadinService.getCurrentRequest().getWrappedSession().invalidate();
            getUI().ifPresent(ui -> ui.getPage().executeJs("window.location.href='about:blank';"));
        });

        add(title, newGame, cont, settings, profile, exit);
        setSpacing(true);
    }
}
