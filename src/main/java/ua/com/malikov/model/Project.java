package ua.com.malikov.model;

import java.util.Set;

public class Project extends NamedEntity {

    public static final String COMPANY_ID = "company_id";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String PROJECT_NAME = "project_name";
    public static final String COST = "cost";

    private Company company;

    private Customer customer;

    private float cost;

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

    public Project(Integer id, String name, Company company, Customer customer, Set<Developer> developers, Float cost){
        this(id, name, company, customer, developers);
        this.cost = cost;
    }

    public Project(String name, Company company, Customer customer, Set<Developer> developers, Float cost){
        this(name, company, customer, developers);
        this.cost = cost;
    }

    public Project() {
    }

    public Project(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Project(int id, String name, Company company, Customer customer, Set<Developer> developers, float cost) {
        this(id, name, company, customer, developers);
        this.cost = cost;
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
