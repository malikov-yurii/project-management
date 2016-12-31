package ua.com.malikov.dao.jpa;

import org.slf4j.Logger;
import ua.com.malikov.dao.DeveloperDAO;
import ua.com.malikov.model.Developer;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class JpaDeveloperDAOImpl implements DeveloperDAO {

    private static final Logger LOG = getLogger(JpaDeveloperDAOImpl.class);

    private JpaUtils jpaUtils;

    public void setJpaUtils(JpaUtils jpaUtils) {
        this.jpaUtils = jpaUtils;
    }

    @Override
    public Developer save(Developer developer) {
        return jpaUtils.save(developer, LOG);
    }

    @Override
    public void saveAll(List<Developer> developers) {
        jpaUtils.saveAll(developers, LOG);
    }

    @Override
    public Developer load(int id) {
        return jpaUtils.loadById(Developer.class, id, LOG);
    }

    public Developer loadByLastName(String lastName) {
        return jpaUtils.loadByName(lastName, Developer.LOAD_BY_LAST_NAME, LOG);
    }

    @Override
    public List<Developer> findAll() {
        return jpaUtils.findAll(Developer.LOAD_ALL, LOG);
    }

    @Override
    public void deleteById(int id) {
        jpaUtils.deleteById(id, Developer.DELETE, LOG);
    }

    @Override
    public void deleteAll() {
        jpaUtils.deleteAll(Developer.DELETE_ALL, LOG);
    }
}
