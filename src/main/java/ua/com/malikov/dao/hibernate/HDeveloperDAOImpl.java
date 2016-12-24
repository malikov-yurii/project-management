package ua.com.malikov.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
        sessionFactory.getCurrentSession().save(developer);
        return developer;
    }

    @Override
    public void saveAll(List<Developer> list) {

    }

    @Override
    public Developer load(int id) {
        Developer developer = sessionFactory.getCurrentSession().load(Developer.class, id);
        if (developer == null){
            LOG.error("Cannot find developer by id ID: " + id);
        }
        return developer;
    }

    public Developer loadByLastName(String lastName){
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select d from Developer d where d.lastName like :lastName");
        query.setParameter("lastName", lastName);
        return (Developer) query.uniqueResult();
    }

    @Override
    public List<Developer> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select d from Developer d").list();
    }

    @Override
    public void delete(Developer developer) {
        sessionFactory.getCurrentSession().delete(developer);
    }

    @Override
    public void deleteById(int id) {


    }

    @Override
    public void deleteAll() {
        sessionFactory.getCurrentSession().createQuery("delete from Developer").executeUpdate();

    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
