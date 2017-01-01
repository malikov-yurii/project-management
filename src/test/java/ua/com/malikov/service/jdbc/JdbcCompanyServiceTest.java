package ua.com.malikov.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ua.com.malikov.Profiles;
import ua.com.malikov.service.AbstractCompanyServiceTest;

@ActiveProfiles(Profiles.JDBC)
public class JdbcCompanyServiceTest extends AbstractCompanyServiceTest{
}
