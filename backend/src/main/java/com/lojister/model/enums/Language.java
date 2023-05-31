package com.lojister.model.enums;

import java.util.Locale;

public enum Language{
    TURKISH,
    ENGLISH,
    GERMAN,
    RUSSIAN,
    ARABIC,
    POLISH,
    FRENCH,
    ALBANIAN,
    SPANISH;

    /*public static Language fromLocale(String locale) {
        switch (locale) {
            case "tr":
                return TURKISH;
            case "de":
                return GERMAN;
            case "ru":
                return RUSSIAN;
            case "ar":
                return ARABIC;
            case "pl":
                return POLISH;
            case "fr":
                return FRENCH;
            case "sq":
                return ALBANIAN;
            case "es":
                return SPANISH;
            case "en":
            default:
                return ENGLISH;
        }
    }*/
    public static Locale toLocale(Language language) {
        switch (language) {
            case TURKISH:
                return Locale.forLanguageTag("tr");
            case GERMAN:
                return Locale.forLanguageTag("de");
            case RUSSIAN:
                return Locale.forLanguageTag("ru");
            case ARABIC:
                return Locale.forLanguageTag("ar");
            case POLISH:
                return Locale.forLanguageTag("pl");
            case FRENCH:
                return Locale.forLanguageTag("fr");
            case ALBANIAN:
                return Locale.forLanguageTag("sq");
            case SPANISH:
                return Locale.forLanguageTag("es");
            case ENGLISH:
                return Locale.forLanguageTag("en");
            default:
                return Locale.forLanguageTag("tr");
        }
    }
}
