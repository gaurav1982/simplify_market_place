package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "location")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @ManyToOne
    @JsonIgnoreProperties(value = { "locations" }, allowSetters = true)
    private Client id;

    @ManyToMany(mappedBy = "ids")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ids", "id" }, allowSetters = true)
    private Set<Employment> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location id(Long id) {
        this.id = id;
        return this;
    }

    public String getCountry() {
        return this.country;
    }

    public Location country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return this.state;
    }

    public Location state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return this.city;
    }

    public Location city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Client getId() {
        return this.id;
    }

    public Location id(Client client) {
        this.setId(client);
        return this;
    }

    public void setId(Client client) {
        this.id = client;
    }

    public Set<Employment> getIds() {
        return this.ids;
    }

    public Location ids(Set<Employment> employments) {
        this.setIds(employments);
        return this;
    }

    public Location addId(Employment employment) {
        this.ids.add(employment);
        employment.getIds().add(this);
        return this;
    }

    public Location removeId(Employment employment) {
        this.ids.remove(employment);
        employment.getIds().remove(this);
        return this;
    }

    public void setIds(Set<Employment> employments) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.removeId(this));
        }
        if (employments != null) {
            employments.forEach(i -> i.addId(this));
        }
        this.ids = employments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", country='" + getCountry() + "'" +
            ", state='" + getState() + "'" +
            ", city='" + getCity() + "'" +
            "}";
    }
}
