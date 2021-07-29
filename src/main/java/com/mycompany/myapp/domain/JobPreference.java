package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.EngagementType;
import com.mycompany.myapp.domain.enumeration.LocationType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A JobPreference.
 */
@Entity
@Table(name = "job_preference")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "jobpreference")
public class JobPreference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hourly_rate")
    private Integer hourlyRate;

    @Column(name = "daily_rate")
    private Integer dailyRate;

    @Column(name = "monthly_rate")
    private Integer monthlyRate;

    @Column(name = "hour_per_day")
    private Integer hourPerDay;

    @Column(name = "hour_per_week")
    private Integer hourPerWeek;

    @Enumerated(EnumType.STRING)
    @Column(name = "engagement_type")
    private EngagementType engagementType;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type")
    private LocationType locationType;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "id" }, allowSetters = true)
    private Set<JobSpecificField> jobSpecificFields = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "resumes", "jobPreferences", "locationPrefrences", "educations", "employments" }, allowSetters = true)
    private Worker id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "categories", "jobPreferences", "parent", "fields" }, allowSetters = true)
    private Category id;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobPreference id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getHourlyRate() {
        return this.hourlyRate;
    }

    public JobPreference hourlyRate(Integer hourlyRate) {
        this.hourlyRate = hourlyRate;
        return this;
    }

    public void setHourlyRate(Integer hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Integer getDailyRate() {
        return this.dailyRate;
    }

    public JobPreference dailyRate(Integer dailyRate) {
        this.dailyRate = dailyRate;
        return this;
    }

    public void setDailyRate(Integer dailyRate) {
        this.dailyRate = dailyRate;
    }

    public Integer getMonthlyRate() {
        return this.monthlyRate;
    }

    public JobPreference monthlyRate(Integer monthlyRate) {
        this.monthlyRate = monthlyRate;
        return this;
    }

    public void setMonthlyRate(Integer monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public Integer getHourPerDay() {
        return this.hourPerDay;
    }

    public JobPreference hourPerDay(Integer hourPerDay) {
        this.hourPerDay = hourPerDay;
        return this;
    }

    public void setHourPerDay(Integer hourPerDay) {
        this.hourPerDay = hourPerDay;
    }

    public Integer getHourPerWeek() {
        return this.hourPerWeek;
    }

    public JobPreference hourPerWeek(Integer hourPerWeek) {
        this.hourPerWeek = hourPerWeek;
        return this;
    }

    public void setHourPerWeek(Integer hourPerWeek) {
        this.hourPerWeek = hourPerWeek;
    }

    public EngagementType getEngagementType() {
        return this.engagementType;
    }

    public JobPreference engagementType(EngagementType engagementType) {
        this.engagementType = engagementType;
        return this;
    }

    public void setEngagementType(EngagementType engagementType) {
        this.engagementType = engagementType;
    }

    public LocationType getLocationType() {
        return this.locationType;
    }

    public JobPreference locationType(LocationType locationType) {
        this.locationType = locationType;
        return this;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public JobPreference createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public JobPreference createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public JobPreference updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public JobPreference updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<JobSpecificField> getJobSpecificFields() {
        return this.jobSpecificFields;
    }

    public JobPreference jobSpecificFields(Set<JobSpecificField> jobSpecificFields) {
        this.setJobSpecificFields(jobSpecificFields);
        return this;
    }

    public JobPreference addJobSpecificField(JobSpecificField jobSpecificField) {
        this.jobSpecificFields.add(jobSpecificField);
        jobSpecificField.setId(this);
        return this;
    }

    public JobPreference removeJobSpecificField(JobSpecificField jobSpecificField) {
        this.jobSpecificFields.remove(jobSpecificField);
        jobSpecificField.setId(null);
        return this;
    }

    public void setJobSpecificFields(Set<JobSpecificField> jobSpecificFields) {
        if (this.jobSpecificFields != null) {
            this.jobSpecificFields.forEach(i -> i.setId(null));
        }
        if (jobSpecificFields != null) {
            jobSpecificFields.forEach(i -> i.setId(this));
        }
        this.jobSpecificFields = jobSpecificFields;
    }

    public Worker getId() {
        return this.id;
    }

    public JobPreference id(Worker worker) {
        this.setId(worker);
        return this;
    }

    public void setId(Worker worker) {
        this.id = worker;
    }

    public Category getId() {
        return this.id;
    }

    public JobPreference id(Category category) {
        this.setId(category);
        return this;
    }

    public void setId(Category category) {
        this.id = category;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobPreference)) {
            return false;
        }
        return id != null && id.equals(((JobPreference) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobPreference{" +
            "id=" + getId() +
            ", hourlyRate=" + getHourlyRate() +
            ", dailyRate=" + getDailyRate() +
            ", monthlyRate=" + getMonthlyRate() +
            ", hourPerDay=" + getHourPerDay() +
            ", hourPerWeek=" + getHourPerWeek() +
            ", engagementType='" + getEngagementType() + "'" +
            ", locationType='" + getLocationType() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
