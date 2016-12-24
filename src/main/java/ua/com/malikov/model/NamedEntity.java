package ua.com.malikov.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class NamedEntity {

    public static final String ID = "id";

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = NamedEntity.ID)
    protected Integer id;

    @Column
    protected String name;

    protected NamedEntity() {
    }

    protected NamedEntity(String name) {
        this.name = name;
    }

    protected NamedEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return "id = '" + getId() + '\'' +
                ", name = '" + name + '\'';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    public boolean isNew() {
        return (getId() == null);
    }
}
