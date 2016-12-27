package ua.com.malikov.dao.hibernate;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import ua.com.malikov.dao.SkillDAO;
import ua.com.malikov.model.Skill;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HSkillDAOImpl implements SkillDAO {

    private EntityManagerFactory emf;

    private static final Logger LOG = getLogger(HSkillDAOImpl.class);

    @Override
    @Transactional
    public Skill save(Skill skill) {
        return HUtils.save(skill, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public void saveAll(List<Skill> skills) {
        HUtils.saveAll(skills, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public Skill load(int id) {
        return HUtils.loadById(Skill.class, id, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public List<Skill> findAll() {
        return HUtils.findAll("from Skill", sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public void delete(Skill skill) {
        HUtils.delete(skill, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        HUtils.deleteById(id, "delete Skill where id=:id",
                sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public void deleteAll() {
        HUtils.deleteAll("delete Skill", sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public Skill getByName(String name) {
        return HUtils.loadByName(name, "from Skill where name like :name", sessionFactory.getCurrentSession(), LOG);
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
