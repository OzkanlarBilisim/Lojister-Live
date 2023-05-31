package com.lojister.core.converter;

import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.core.i18n.Translator;
import com.lojister.model.enums.Language;
import retrofit2.Converter;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Locale;

public class StringToLanguageConverter implements Converter<String, Language> {
    @Nullable
    @Override
    public Language convert(String value) throws IOException {
        try {
            return Language.valueOf(value.toUpperCase());
        }
        catch (Exception e)
        {
            throw new EntityNotFoundException(Translator.toLocale("lojister.common.EntityNotFound.exception"));
        }
    }
}
