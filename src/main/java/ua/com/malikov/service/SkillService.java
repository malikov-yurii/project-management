package ua.com.malikov.service;

import org.springframework.transaction.annotation.Transactional;
import ua.com.malikov.dao.SkillDAO;
import ua.com.malikov.model.Skill;

import java.util.List;

public class SkillService extends AbstractService<Skill> {

    private SkillDAO skillDAO;

    @Override
    @Transactional
    public Skill add(Skill skill){
        if (isNullThanPrintAndLogErrorMessageFor(skill)) return null;
        return skillDAO.save(skill);
    }

    @Override
    @Transactional
    public Skill get(int id){return skillDAO.load(id);}

    @Transactional
    public Skill getByName(String name){
        return skillDAO.getByName(name);
    }

    @Override
    @Transactional
    public List<Skill> getAll(){return skillDAO.findAll();}

    @Override
    @Transactional
    public void update(Skill skill){
        if (isNullThanPrintAndLogErrorMessageFor(skill)) return;
        skillDAO.save(skill);
    }

    @Override
    @Transactional
    public void delete(int id){skillDAO.deleteById(id);}

    @Override
    @Transactional
    public void deleteAll(){skillDAO.deleteAll();}

    public void setSkillDAO(SkillDAO skillDAO) {
        this.skillDAO = skillDAO;
    }
}
