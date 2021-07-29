package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.DegreeType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Education.
 */
@Entity
@Table(name = "education")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "education")
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "degree_name")
    private String degreeName;

    @Column(name = "institute")
    private String institute;

    @Column(name = "year_of_passing")
    private Integer yearOfPassing;

    @Column(name = "marks")
    private Float marks;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "degree_type")
    private DegreeType degreeType;

    @Column(name = "description")
    private String description;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

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

    public Education id(Long id) {
        this.id = id;
        return this;
    }

    public String getDegreeName() {
        return this.degreeName;
    }

    public Education degreeName(String degreeName) {
        this.degreeName = degreeName;
        return this;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public String getInstitute() {
        return this.institute;
    }

    public Education institute(String institute) {
        this.institute = institute;
        return this;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public Integer getYearOfPassing() {
        return this.yearOfPassing;
    }

    public Education yearOfPassing(Integer yearOfPassing) {
        this.yearOfPassing = yearOfPassing;
        return this;
    }

    public void setYearOfPassing(Integer yearOfPassing) {
        this.yearOfPassing = yearOfPassing;
    }

    public Float getMarks() {
        return this.marks;
    }

    public Education marks(Float marks) {
        this.marks = marks;
        return this;
    }

    public void setMarks(Float marks) {
        this.marks = marks;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Education startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Education endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public DegreeType getDegreeType() {
        return this.degreeType;
    }

    public Education degreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
        return this;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public String getDescription() {
        return this.description;
    }

    public Education description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Education createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Education createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Education updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public Education updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Worker getId() {
        return this.id;
    }

    public Education id(Worker worker) {
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
        if (!(o instanceof Education)) {
            return false;
        }
        return id != null && id.equals(((Education) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Education{" +
            "id=" + getId() +
            ", degreeName='" + getDegreeName() + "'" +
            ", institute='" + getInstitute() + "'" +
            ", yearOfPassing=" + getYearOfPassing() +
            ", marks=" + getMarks() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", degreeType='" + getDegreeType() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
