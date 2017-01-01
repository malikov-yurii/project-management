package ua.com.malikov.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ua.com.malikov.Profiles;
import ua.com.malikov.service.AbstractCustomerServiceTest;

@ActiveProfiles(Profiles.JDBC)
public class JdbcCustomerServiceTest extends AbstractCustomerServiceTest {
}
