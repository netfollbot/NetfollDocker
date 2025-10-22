package com.geopolitics.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.Route;

@Route(value = "profile", layout = MainLayout.class)
public class ProfileView extends Div {
    public ProfileView() {
        add(new H2(getTranslation("profile.title")));
        add("Coming soon...");
    }
}
