package ua.com.malikov.dao.hibernate;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import ua.com.malikov.dao.DeveloperDAO;
import ua.com.malikov.model.Developer;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HDeveloperDAOImpl implements DeveloperDAO {

    private static final Logger LOG = getLogger(HDeveloperDAOImpl.class);

    private EntityManagerFactory emf;

    private HUtils hUtils;

    public void sethUtils(HUtils hUtils) {
        this.hUtils = hUtils;
    }

    @Override
    @Transactional
    public Developer save(Developer developer) {
        return hUtils.save(developer, LOG);
    }

    @Override
    @Transactional
    public void saveAll(List<Developer> developers) {
        hUtils.saveAll(developers, LOG);
    }

    @Override
    @Transactional
    public Developer load(int id) {
        return hUtils.loadById(Developer.class, id, LOG);
    }

    @Transactional
    public Developer loadByLastName(String lastName) {
        return hUtils.loadByName(lastName, Developer.LOAD_BY_LAST_NAME, LOG);
    }

    @Override
    @Transactional
    public List<Developer> findAll() {
        return hUtils.findAll(Developer.LOAD_ALL, LOG);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        hUtils.deleteById(id, Developer.DELETE, LOG);
    }

    @Override
    @Transactional
    public void deleteAll() {
        hUtils.deleteAll(Developer.DELETE_ALL, LOG);
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

}
