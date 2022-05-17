package ru.dictionary;

import org.springframework.lang.NonNull;
import org.springframework.test.context.support.DefaultActiveProfilesResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//http://stackoverflow.com/questions/23871255/spring-profiles-simple-example-of-activeprofilesresolver
public class ActiveDbProfileResolver extends DefaultActiveProfilesResolver {
    public static final String DATAJPA = "datajpa";
    public static final String JPA = "jpa";
    @Override
    public @NonNull
    String[] resolve(@NonNull Class<?> aClass) {
        List<String> profiles = new ArrayList<>(Arrays.asList(super.resolve(aClass)));
        String activeRepoImpl = System.getProperty("spring.profiles.active");
        if (DATAJPA.equalsIgnoreCase(activeRepoImpl))
            profiles.add(DATAJPA);
        else if (JPA.equalsIgnoreCase(activeRepoImpl))
            profiles.add(JPA);

        return profiles.toArray(String[]::new);
    }
}
