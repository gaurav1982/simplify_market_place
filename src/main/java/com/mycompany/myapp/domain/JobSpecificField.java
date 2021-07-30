package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A JobSpecificField.
 */
@Entity
@Table(name = "job_specific_field")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "jobspecificfield")
public class JobSpecificField implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JsonIgnoreProperties(value = { "jobspecificfields", "worker", "category" }, allowSetters = true)
    private JobPreference jobpreference;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobSpecificField id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public JobSpecificField value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public JobPreference getJobpreference() {
        return this.jobpreference;
    }

    public JobSpecificField jobpreference(JobPreference jobPreference) {
        this.setJobpreference(jobPreference);
        return this;
    }

    public void setJobpreference(JobPreference jobPreference) {
        this.jobpreference = jobPreference;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobSpecificField)) {
            return false;
        }
        return id != null && id.equals(((JobSpecificField) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobSpecificField{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
