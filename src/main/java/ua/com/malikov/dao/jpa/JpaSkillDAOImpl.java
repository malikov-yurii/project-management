package ua.com.malikov.dao.jpa;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import ua.com.malikov.dao.SkillDAO;
import ua.com.malikov.model.Skill;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class JpaSkillDAOImpl implements SkillDAO {

    private static final Logger LOG = getLogger(JpaSkillDAOImpl.class);

    private JpaUtils jpaUtils;

    public void setJpaUtils(JpaUtils jpaUtils) {
        this.jpaUtils = jpaUtils;
    }

    @Override
    @Transactional
    public Skill save(Skill skill) {
        return jpaUtils.save(skill, LOG);
    }

    @Override
    public void saveAll(List<Skill> skills) {
        jpaUtils.saveAll(skills, LOG);
    }

    @Override
    public Skill load(int id) {
        return jpaUtils.loadById(Skill.class, id, LOG);
    }

    @Override
    public List<Skill> findAll() {
        return jpaUtils.findAll(Skill.LOAD_ALL, LOG);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        jpaUtils.deleteById(id, Skill.DELETE_BY_ID, LOG);
    }

    @Override
    public void deleteAll() {
        jpaUtils.deleteAll(Skill.DELETE_ALL, LOG);
    }

    @Override
    public Skill load(String name) {
        return jpaUtils.loadByName(name, Skill.LOAD_BY_NAME, LOG);
    }
}
