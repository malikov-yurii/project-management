package ua.com.malikov;

import ua.com.malikov.matcher.ModelMatcher;
import ua.com.malikov.model.Skill;

import java.util.Objects;

public class SkillTestData {
    public static final Skill JAVA      = new Skill(1, "Java");
    public static final Skill SQL        = new Skill(2, "SQL");
    public static final Skill SPRING = new Skill(3, "Spring");
    public static final Skill JUNIT = new Skill(4, "Junit");
    public static final Skill MAVEN = new Skill(5, "Maven");

    public static final ModelMatcher<Skill> MATCHER = ModelMatcher.of(Skill.class,
            (expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getId(), actual.getId())
                            && Objects.equals(expected.getName(), actual.getName())
                    )
    );
}
