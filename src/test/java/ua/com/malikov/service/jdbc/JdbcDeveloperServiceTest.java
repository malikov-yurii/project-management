package ua.com.malikov.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ua.com.malikov.Profiles;
import ua.com.malikov.service.AbstractDeveloperServiceTest;

@ActiveProfiles(Profiles.JDBC)
public class JdbcDeveloperServiceTest extends AbstractDeveloperServiceTest {
}
