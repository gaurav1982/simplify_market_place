package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Worker.
 */
@Entity
@Table(name = "worker")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "worker")
public class Worker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private Integer phone;

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

    @OneToMany(mappedBy = "worker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true)
    private Set<Resume> resumes = new HashSet<>();

    @OneToMany(mappedBy = "worker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jobspecificfields", "worker", "category" }, allowSetters = true)
    private Set<JobPreference> jobprefrences = new HashSet<>();

    @OneToMany(mappedBy = "worker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true)
    private Set<LocationPrefrence> locationprefrences = new HashSet<>();

    @OneToMany(mappedBy = "worker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "worker" }, allowSetters = true)
    private Set<Education> educations = new HashSet<>();

    @OneToMany(mappedBy = "worker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "locations", "worker" }, allowSetters = true)
    private Set<Employment> employments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Worker id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Worker name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Worker email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return this.phone;
    }

    public Worker phone(Integer phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return this.description;
    }

    public Worker description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Worker createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Worker createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Worker updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public Worker updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Resume> getResumes() {
        return this.resumes;
    }

    public Worker resumes(Set<Resume> resumes) {
        this.setResumes(resumes);
        return this;
    }

    public Worker addResume(Resume resume) {
        this.resumes.add(resume);
        resume.setWorker(this);
        return this;
    }

    public Worker removeResume(Resume resume) {
        this.resumes.remove(resume);
        resume.setWorker(null);
        return this;
    }

    public void setResumes(Set<Resume> resumes) {
        if (this.resumes != null) {
            this.resumes.forEach(i -> i.setWorker(null));
        }
        if (resumes != null) {
            resumes.forEach(i -> i.setWorker(this));
        }
        this.resumes = resumes;
    }

    public Set<JobPreference> getJobprefrences() {
        return this.jobprefrences;
    }

    public Worker jobprefrences(Set<JobPreference> jobPreferences) {
        this.setJobprefrences(jobPreferences);
        return this;
    }

    public Worker addJobprefrence(JobPreference jobPreference) {
        this.jobprefrences.add(jobPreference);
        jobPreference.setWorker(this);
        return this;
    }

    public Worker removeJobprefrence(JobPreference jobPreference) {
        this.jobprefrences.remove(jobPreference);
        jobPreference.setWorker(null);
        return this;
    }

    public void setJobprefrences(Set<JobPreference> jobPreferences) {
        if (this.jobprefrences != null) {
            this.jobprefrences.forEach(i -> i.setWorker(null));
        }
        if (jobPreferences != null) {
            jobPreferences.forEach(i -> i.setWorker(this));
        }
        this.jobprefrences = jobPreferences;
    }

    public Set<LocationPrefrence> getLocationprefrences() {
        return this.locationprefrences;
    }

    public Worker locationprefrences(Set<LocationPrefrence> locationPrefrences) {
        this.setLocationprefrences(locationPrefrences);
        return this;
    }

    public Worker addLocationprefrence(LocationPrefrence locationPrefrence) {
        this.locationprefrences.add(locationPrefrence);
        locationPrefrence.setWorker(this);
        return this;
    }

    public Worker removeLocationprefrence(LocationPrefrence locationPrefrence) {
        this.locationprefrences.remove(locationPrefrence);
        locationPrefrence.setWorker(null);
        return this;
    }

    public void setLocationprefrences(Set<LocationPrefrence> locationPrefrences) {
        if (this.locationprefrences != null) {
            this.locationprefrences.forEach(i -> i.setWorker(null));
        }
        if (locationPrefrences != null) {
            locationPrefrences.forEach(i -> i.setWorker(this));
        }
        this.locationprefrences = locationPrefrences;
    }

    public Set<Education> getEducations() {
        return this.educations;
    }

    public Worker educations(Set<Education> educations) {
        this.setEducations(educations);
        return this;
    }

    public Worker addEducation(Education education) {
        this.educations.add(education);
        education.setWorker(this);
        return this;
    }

    public Worker removeEducation(Education education) {
        this.educations.remove(education);
        education.setWorker(null);
        return this;
    }

    public void setEducations(Set<Education> educations) {
        if (this.educations != null) {
            this.educations.forEach(i -> i.setWorker(null));
        }
        if (educations != null) {
            educations.forEach(i -> i.setWorker(this));
        }
        this.educations = educations;
    }

    public Set<Employment> getEmployments() {
        return this.employments;
    }

    public Worker employments(Set<Employment> employments) {
        this.setEmployments(employments);
        return this;
    }

    public Worker addEmployment(Employment employment) {
        this.employments.add(employment);
        employment.setWorker(this);
        return this;
    }

    public Worker removeEmployment(Employment employment) {
        this.employments.remove(employment);
        employment.setWorker(null);
        return this;
    }

    public void setEmployments(Set<Employment> employments) {
        if (this.employments != null) {
            this.employments.forEach(i -> i.setWorker(null));
        }
        if (employments != null) {
            employments.forEach(i -> i.setWorker(this));
        }
        this.employments = employments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Worker)) {
            return false;
        }
        return id != null && id.equals(((Worker) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Worker{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone=" + getPhone() +
            ", description='" + getDescription() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
