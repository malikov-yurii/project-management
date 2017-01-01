package ua.com.malikov;

import ua.com.malikov.matcher.ModelMatcher;
import ua.com.malikov.model.Developer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import static ua.com.malikov.CompanyTestData.*;
import static ua.com.malikov.SkillTestData.*;

public class DeveloperTestData {
    public static final Developer KOSINSKYI = new Developer(1, "Mykhailo", "Kosinskyi",
            COMPANY_CIKLUM, new HashSet<>(Arrays.asList(JAVA, SQL, MAVEN)));

    public static final Developer LEN = new Developer(2, "Vladyslav", "Len\'",
            COMPANY_EPAM, new HashSet<>(Arrays.asList(JAVA, SPRING)));

    public static final Developer MALIKOV = new Developer(3, "Yurii", "Malikov",
            COMPANY_EPAM, new HashSet<>(Arrays.asList(JAVA, JUNIT)));

    public static final Developer SENCHUK = new Developer(4, "Mykhailo", "Senchuk",
            COMPANY_CIKLUM, new HashSet<>(Arrays.asList(JAVA, SPRING, MAVEN)));

    public static final Developer VOLKOV = new Developer(5, "Oleg", "Volkov",
            COMPANY_SOFTSERVE, new HashSet<>(Arrays.asList(JAVA, SQL, SPRING, JUNIT, MAVEN)));

    public static final ModelMatcher<Developer> MATCHER = ModelMatcher.of(Developer.class,
            (expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getId(), actual.getId())
                            && Objects.equals(expected.getName(), actual.getName())
                    )
    );
}
