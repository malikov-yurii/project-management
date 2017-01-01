package ua.com.malikov.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ua.com.malikov.Profiles;
import ua.com.malikov.service.AbstractCustomerServiceTest;

@ActiveProfiles(Profiles.JPA)
public class JpaCustomerServiceTest extends AbstractCustomerServiceTest{
}
