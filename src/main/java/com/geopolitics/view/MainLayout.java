package com.geopolitics.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.PageTitle;

@PageTitle("Caucasus Strategy")
public class MainLayout extends AppLayout {
    public MainLayout() {
        setPrimarySection(Section.DRAWER);
    }
}
