package ua.com.malikov.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pms.skills")
@AttributeOverride(name = "name", column = @Column(name = Skill.NAME))
public class Skill extends NamedEntity {

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
