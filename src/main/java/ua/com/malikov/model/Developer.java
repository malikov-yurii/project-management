package ua.com.malikov.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@NamedQueries({
        @NamedQuery(name = Developer.DELETE, query = "DELETE FROM Developer d WHERE d.id=:id"),
        @NamedQuery(name = Developer.DELETE_ALL, query = "DELETE FROM Developer"),
//      ":name" below is not mistake - it is for generic method
        @NamedQuery(name = Developer.LOAD_BY_LAST_NAME, query = "SELECT d FROM Developer d WHERE d.lastName=:name"),
        @NamedQuery(name = Developer.LOAD_ALL, query = "SELECT d FROM Developer d ORDER BY d.id"),
})
@Entity
@Table(name = "pms.developers")
@AttributeOverride(name = "name", column = @Column(name = Developer.FIRST_NAME))
public class Developer extends NamedEntity {

    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String COMPANY_ID = "company_id";

    public static final String DELETE = "Developer.delete";
    public static final String DELETE_ALL = "Developer.deleteAll";
    public static final String LOAD_BY_LAST_NAME = "Developer.loadByLastName";
    public static final String LOAD_ALL = "Developer.loadAll";

    @Column(name = Developer.LAST_NAME)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = Developer.COMPANY_ID)
    private Company company;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "pms.developers_skills",
            joinColumns = @JoinColumn(name = "developer_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills;


    public Developer(String name, String lastName,Company company,Set<Skill>skills)
    {
        super(name);
        this.lastName = lastName;
        this.company = company;
        this.skills = skills;
    }

    public Developer(Integer id, String name, String lastName, Company company, Set<Skill> skills) {
        this(name, lastName, company, skills);
        this.id = id;
    }

    public Developer() {
    }

    public Developer(Developer developer) {
        this(developer.getId(), developer.getName(), developer.getLastName(), new Company(developer.getCompany()), new HashSet<>(developer.getSkills()));
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Developer{" +
                "id='" + getId() + '\'' +
                " Name='" + getName() + '\'' +
                " lastName='" + lastName + '\'' +
                ", company=" + company.getName() +
                ", skills=" + skills +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Developer)) return false;

        Developer developer = (Developer) o;

        if (name != null ? !name.equals(developer.name) : developer.name != null) return false;
        if (lastName != null ? !lastName.equals(developer.lastName) : developer.lastName != null) return false;
        if (company != null ? !company.equals(developer.company) : developer.company != null) return false;
        return skills != null ? skills.equals(developer.skills) : developer.skills == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (skills != null ? skills.hashCode() : 0);
        return result;
    }
}
