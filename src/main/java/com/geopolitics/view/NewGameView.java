package com.geopolitics.view;

import com.geopolitics.model.Country;
import com.geopolitics.model.GameState;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "new", layout = MainLayout.class)
@CssImport(value = "./styles/newgame.css")
public class NewGameView extends VerticalLayout {

    public NewGameView() {
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H2 title = new H2(getTranslation("newgame.title"));
        HorizontalLayout flags = new HorizontalLayout();
        flags.setSpacing(true);

        flags.add(countryButton(Country.GEORGIA));
        flags.add(countryButton(Country.ARMENIA));
        flags.add(countryButton(Country.AZERBAIJAN));

        add(title, flags);
    }

    private Div countryButton(Country c) {
        Div cell = new Div();
        cell.addClassName("country-cell");
        Div flag = new Div();
        flag.addClassName("flag");
        flag.setText(c.getFlagEmoji());
        Div name = new Div();
        name.addClassName("name");
        name.setText(getTranslation(c.getI18nKey()));
        cell.add(flag, name);
        cell.addClickListener(e -> startGame(c));
        return cell;
    }

    private void startGame(Country c) {
        GameState state = GameState.newGame(c);
        UI ui = getUI().orElseThrow();
        ui.getSession().setAttribute(GameState.class, state);
        ui.navigate(GameView.class);
    }
}
