package ua.com.malikov.dao.hibernate;

import org.slf4j.Logger;
import ua.com.malikov.dao.DeveloperDAO;
import ua.com.malikov.model.Developer;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HDeveloperDAOImpl implements DeveloperDAO {

    private static final Logger LOG = getLogger(HDeveloperDAOImpl.class);

    private HUtils hUtils;

    public void sethUtils(HUtils hUtils) {
        this.hUtils = hUtils;
    }

    @Override
    public Developer save(Developer developer) {
        return hUtils.save(developer, LOG);
    }

    @Override
    public void saveAll(List<Developer> developers) {
        hUtils.saveAll(developers, LOG);
    }

    @Override
    public Developer load(int id) {
        return hUtils.loadById(Developer.class, id, LOG);
    }

    public Developer loadByLastName(String lastName) {
        return hUtils.loadByName(lastName, Developer.LOAD_BY_LAST_NAME, LOG);
    }

    @Override
    public List<Developer> findAll() {
        return hUtils.findAll(Developer.LOAD_ALL, LOG);
    }

    @Override
    public void deleteById(int id) {
        hUtils.deleteById(id, Developer.DELETE, LOG);
    }

    @Override
    public void deleteAll() {
        hUtils.deleteAll(Developer.DELETE_ALL, LOG);
    }
}
