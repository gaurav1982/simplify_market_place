package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A LocationPrefrence.
 */
@Entity
@Table(name = "location_prefrence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "locationprefrence")
public class LocationPrefrence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prefrence_order")
    private Integer prefrenceOrder;

    @ManyToOne
    @JsonIgnoreProperties(value = { "resumes", "jobPreferences", "locationPrefrences", "educations", "employments" }, allowSetters = true)
    private Worker id;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocationPrefrence id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getPrefrenceOrder() {
        return this.prefrenceOrder;
    }

    public LocationPrefrence prefrenceOrder(Integer prefrenceOrder) {
        this.prefrenceOrder = prefrenceOrder;
        return this;
    }

    public void setPrefrenceOrder(Integer prefrenceOrder) {
        this.prefrenceOrder = prefrenceOrder;
    }

    public Worker getId() {
        return this.id;
    }

    public LocationPrefrence id(Worker worker) {
        this.setId(worker);
        return this;
    }

    public void setId(Worker worker) {
        this.id = worker;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationPrefrence)) {
            return false;
        }
        return id != null && id.equals(((LocationPrefrence) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationPrefrence{" +
            "id=" + getId() +
            ", prefrenceOrder=" + getPrefrenceOrder() +
            "}";
    }
}
