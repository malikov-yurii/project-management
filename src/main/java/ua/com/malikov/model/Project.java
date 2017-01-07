package ua.com.malikov.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = Project.DELETE_BY_ID, query = "DELETE FROM Project WHERE id=:id"),
        @NamedQuery(name = Project.DELETE_ALL, query = "DELETE FROM Project"),
        @NamedQuery(name = Project.LOAD_BY_NAME, query = "FROM Project p WHERE p.name=:name"),
        @NamedQuery(name = Project.LOAD_ALL, query = "FROM Project p ORDER BY p.id")
})
@Entity
@Table(name = "projects")
@AttributeOverride(name = "name", column = @Column(name = Project.NAME))
public class Project extends NamedEntity {

    public static final String COMPANY_ID = "company_id";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String NAME = "project_name";
    public static final String COST = "cost";

    public static final String LOAD_BY_NAME = "Project.byName";
    public static final String LOAD_ALL = "Project.loadAll";
    public static final String DELETE_BY_ID = "Project.deleteById";
    public static final String DELETE_ALL = "Project.deleteAll";

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = Project.COMPANY_ID)
    private Company company;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = Project.CUSTOMER_ID)
    private Customer customer;

    @Column(name = Project.COST)
    private float cost;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "projects_developers",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "developer_id")
    )
    private Set<Developer> developers;

    public Project(String name, Company company, Customer customer, Set<Developer> developers) {
        super(name);
        this.company = company;
        this.customer = customer;
        this.developers = developers;
    }

    public Project(Integer id, String name, Company company, Customer customer, Set<Developer> developers) {
        this(name, company, customer, developers);
        this.id = id;
    }

    public Project(Integer id, String name, Company company, Customer customer, Set<Developer> developers, Float cost) {
        this(id, name, company, customer, developers);
        this.cost = cost;
    }

    public Project(String name, Company company, Customer customer, Set<Developer> developers, Float cost) {
        this(name, company, customer, developers);
        this.cost = cost;
    }

    public Project() {
    }

    public Project(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Project(Project project) {
        this(project.getId(), project.getName(), new Company(project.getCompany()),
                new Customer(project.getCustomer()),
                        new HashSet<>(project.getDevelopers()),
                        project.getCost());
    }

    @Override
    public String toString() {
        return "Project{" +
                "ID=" + getId() +
                ", company=" + company.getName() +
                ", customer=" + customer.getName() +
                ", project_name='" + getName() + '\'' +
                ", company_id=" + company.getId() +
                ", customer_id=" + customer.getId() +
                ", cost=" + cost +
                '}';
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Set<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(Set<Developer> developers) {
        this.developers = developers;
    }
}
