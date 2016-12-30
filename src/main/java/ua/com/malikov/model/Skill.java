package ua.com.malikov.model;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = Skill.DELETE_BY_ID, query = "DELETE Skill WHERE s.id=:id"),
        @NamedQuery(name = Skill.DELETE_ALL, query = "DELETE Skill"),
        @NamedQuery(name = Skill.LOAD_BY_NAME, query = "SELECT FROM Skill s WHERE s.name=:name"),
        @NamedQuery(name = Skill.LOAD_ALL, query = "SELECT  Skill s")
})
@Entity
@Table(name = "pms.skills")
@AttributeOverride(name = "name", column = @Column(name = Skill.NAME))
public class Skill extends NamedEntity {

    public static final String LOAD_BY_NAME = "Skill.byName";
    public static final String LOAD_ALL = "Skill.loadAll";
    public static final String DELETE_BY_ID = "Skill.deleteById";
    public static final String DELETE_ALL = "Skill.deleteAll";

    public static final String NAME = "skill_name";

    public Skill() {
    }

    public Skill(String name) {
        super(name);
    }

    public Skill(Integer id, String name) {
        super(id, name);
    }
}
