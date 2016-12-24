package ua.com.malikov.dao.hibernate;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import ua.com.malikov.dao.SkillDAO;
import ua.com.malikov.model.Skill;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HSkillDAOImpl implements SkillDAO {

    private SessionFactory sessionFactory;

    private static final Logger LOG = getLogger(HProjectDAOImpl.class);

    @Override
    public Skill save(Skill skill) {
        return HUtils.save(skill, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public void saveAll(List<Skill> skills) {
        HUtils.saveAll(skills, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public Skill load(int id) {
        return HUtils.loadById(Skill.class, id, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public List<Skill> findAll() {
        return HUtils.findAll("from Skill", sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public void delete(Skill skill) {
        HUtils.delete(skill, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public void deleteById(int id) {
        HUtils.deleteById(id, "delete Skill where id=:id",
                sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public void deleteAll() {
        HUtils.deleteAll("delete Skill", sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public Skill getByName(String name) {
        return HUtils.loadByName(name, "from Skill where name like :name", sessionFactory.getCurrentSession(), LOG);
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
