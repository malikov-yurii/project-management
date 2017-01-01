package ua.com.malikov;

import ua.com.malikov.matcher.ModelMatcher;
import ua.com.malikov.model.Company;

import java.util.Objects;

public class CompanyTestData {

    public static final Company COMPANY_CIKLUM       = new Company(1, "Ciklum");
    public static final Company COMPANY_EPAM         = new Company(2, "EPAM");
    public static final Company COMPANY_GLOBAL_LOGIC = new Company(3, "GlobalLogic");
    public static final Company COMPANY_LUXOFT       = new Company(4, "Luxoft");
    public static final Company COMPANY_SOFTSERVE    = new Company(5, "SoftServe");

    public static final ModelMatcher<Company> MATCHER = ModelMatcher.of(Company.class,
            (expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getId(), actual.getId())
                            && Objects.equals(expected.getName(), actual.getName())
                    )
    );
}
