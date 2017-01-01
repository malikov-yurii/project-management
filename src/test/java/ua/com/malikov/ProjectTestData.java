package ua.com.malikov;

import ua.com.malikov.matcher.ModelMatcher;
import ua.com.malikov.model.Project;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import static ua.com.malikov.CompanyTestData.*;
import static ua.com.malikov.CustomerTestData.*;
import static ua.com.malikov.DeveloperTestData.*;

public class ProjectTestData {
    public static final Project PROJECT_FINANCIAL_SOFTWARE      = new Project(
            1, "Financial Software", COMPANY_CIKLUM, CUSTOMER_CITI_BANK,
            new HashSet<>(Arrays.asList(KOSINSKYI, SENCHUK)),  100000f );

    public static final Project PROJECT_TICKETING_SOFTWARE = new Project(
            2, "Ticketing Software", COMPANY_EPAM, CUSTOMER_UKRZALIZNYTSYA,
            new HashSet<>(Arrays.asList(LEN, MALIKOV)),  200000f );

    public static final Project PROJECT_WEBSITE_PROJECT = new Project(
            3, "Website Project", COMPANY_SOFTSERVE, CUSTOMER_ROZETKA,
            new HashSet<>(Arrays.asList(VOLKOV)),  300000f );

    public static final Project PROJECT_CMS_SOFTWARE = new Project(
            4, "CMS Software", COMPANY_SOFTSERVE, CUSTOMER_UKRZALIZNYTSYA,
            new HashSet<>(Arrays.asList(VOLKOV)),  400000f );

    public static final Project PROJECT_WEBSITE_ARCHITECTURE = new Project(
            5, "Website Architecture", COMPANY_EPAM, CUSTOMER_CITI_BANK,
            new HashSet<>(Arrays.asList(MALIKOV)),  500000f );

    public static final ModelMatcher<Project> MATCHER = ModelMatcher.of(Project.class,
            (expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getId(), actual.getId())
                            && Objects.equals(expected.getName(), actual.getName())
                    )
    );
}
