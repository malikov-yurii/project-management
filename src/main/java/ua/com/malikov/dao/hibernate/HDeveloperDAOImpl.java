package ua.com.malikov.dao.hibernate;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import ua.com.malikov.dao.DeveloperDAO;
import ua.com.malikov.model.Developer;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HDeveloperDAOImpl implements DeveloperDAO {

    private static final Logger LOG = getLogger(HDeveloperDAOImpl.class);

    SessionFactory sessionFactory;

    @Override
    @Transactional
    public Developer save(Developer developer) {
        return HUtils.save(developer, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public void saveAll(List<Developer> developers) {
        HUtils.saveAll(developers, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public Developer load(int id) {
        return HUtils.loadById(Developer.class, id, sessionFactory.getCurrentSession(), LOG);
    }

    public Developer loadByLastName(String lastName) {
        return HUtils.loadByName(lastName, "from Developer where lastName like :lastName",
                sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public List<Developer> findAll() {
        return HUtils.findAll("from Developer", sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public void delete(Developer developer) {
        HUtils.delete(developer, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public void deleteById(int id) {
        HUtils.deleteById(id, "delete Developer where id=:id",
                sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public void deleteAll() {
        HUtils.deleteAll("delete Developer", sessionFactory.getCurrentSession(), LOG);
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
