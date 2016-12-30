package ua.com.malikov.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = Company.DELETE_BY_ID, query = "DELETE FROM Company c WHERE c.id=:id"),
        @NamedQuery(name = Company.DELETE_ALL, query = "DELETE FROM Company"),
        @NamedQuery(name = Company.LOAD_BY_NAME, query = "SELECT c FROM Company c WHERE c.name=:name"),
        @NamedQuery(name = Company.LOAD_ALL, query = "SELECT c FROM Company c"),
})
@Entity
@Table(name = "pms.companies")
@AttributeOverride(name = "name", column = @Column(name = Company.NAME))
public class Company extends NamedEntity {

    public static final String NAME = "company_name";

    public static final String LOAD_BY_NAME = "Company.byName";
    public static final String LOAD_ALL = "Company.loadAll";
    public static final String DELETE_BY_ID = "Company.deleteById";
    public static final String DELETE_ALL = "Company.deleteAll";

    @OneToMany
    @JoinColumn(name = "company_id")
    @Fetch(FetchMode.JOIN)
    private Set<Developer> developers;

    public Company() {}

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
