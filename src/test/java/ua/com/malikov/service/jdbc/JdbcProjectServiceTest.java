package ua.com.malikov.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ua.com.malikov.Profiles;
import ua.com.malikov.service.AbstractProjectServiceTest;

@ActiveProfiles(Profiles.JDBC)
public class JdbcProjectServiceTest extends AbstractProjectServiceTest {
}
