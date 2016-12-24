package ua.com.malikov.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "companies")
@AttributeOverride(name = "name", column = @Column(name = Company.NAME))
public class Company extends NamedEntity {

    public static final String NAME = "company_name";

    @OneToMany
    @JoinColumn(name = "company_id")
    @Fetch(FetchMode.JOIN)
    private Set<Developer> developers;

    public Company(String name) {
        super(name);
    }

    public Company(Integer id, String name) {
        super(id, name);
    }

    public void addDeveloper(Developer developer){
        this.developers.add(developer);
    }

    public Set<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(Set<Developer> developers) {
        this.developers = developers;
    }
}
