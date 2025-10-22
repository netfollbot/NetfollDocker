package com.geopolitics.i18n;

import com.vaadin.flow.i18n.I18NProvider;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;

@Component
public class AppI18NProvider implements I18NProvider {
    public static final String BUNDLE_PREFIX = "i18n/messages";

    private final List<Locale> providedLocales = List.of(new Locale("ru"), Locale.ENGLISH);

    @Override
    public List<Locale> getProvidedLocales() {
        return providedLocales;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            return "";
        }
        final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale == null ? Locale.ENGLISH : locale);
        String value;
        try {
            value = bundle.getString(key);
        } catch (MissingResourceException ex) {
            return key;
        }
        if (params != null && params.length > 0) {
            value = MessageFormat.format(value, params);
        }
        return value;
    }
}
