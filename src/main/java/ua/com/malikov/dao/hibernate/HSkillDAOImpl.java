package ua.com.malikov.dao.hibernate;

import org.slf4j.Logger;
import ua.com.malikov.dao.SkillDAO;
import ua.com.malikov.model.Skill;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HSkillDAOImpl implements SkillDAO {

    private static final Logger LOG = getLogger(HSkillDAOImpl.class);

    private HUtils hUtils;

    public void sethUtils(HUtils hUtils) {
        this.hUtils = hUtils;
    }

    @Override
    public Skill save(Skill skill) {
        return hUtils.save(skill, LOG);
    }

    @Override
    public void saveAll(List<Skill> skills) {
        hUtils.saveAll(skills, LOG);
    }

    @Override
    public Skill load(int id) {
        return hUtils.loadById(Skill.class, id, LOG);
    }

    @Override
    public List<Skill> findAll() {
        return hUtils.findAll(Skill.LOAD_ALL, LOG);
    }

    @Override
    public void deleteById(int id) {
        hUtils.deleteById(id, Skill.DELETE_BY_ID, LOG);
    }

    @Override
    public void deleteAll() {
        hUtils.deleteAll(Skill.DELETE_ALL, LOG);
    }

    @Override
    public Skill getByName(String name) {
        return hUtils.loadByName(name, Skill.LOAD_BY_NAME, LOG);
    }
}
