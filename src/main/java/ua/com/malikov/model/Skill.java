package ua.com.malikov.model;

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
