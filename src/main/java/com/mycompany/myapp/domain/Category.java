package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.CategoryType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_name")
    private CategoryType categoryName;

    @Column(name = "is_parent")
    private Boolean isParent;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "categories", "jobprefrences", "parent", "fields" }, allowSetters = true)
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jobspecificfields", "worker", "category" }, allowSetters = true)
    private Set<JobPreference> jobprefrences = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "categories", "jobprefrences", "parent", "fields" }, allowSetters = true)
    private Category parent;

    @ManyToMany(mappedBy = "categories")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "categories" }, allowSetters = true)
    private Set<Field> fields = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category id(Long id) {
        this.id = id;
        return this;
    }

    public CategoryType getCategoryName() {
        return this.categoryName;
    }

    public Category categoryName(CategoryType categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public void setCategoryName(CategoryType categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getIsParent() {
        return this.isParent;
    }

    public Category isParent(Boolean isParent) {
        this.isParent = isParent;
        return this;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Category isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Category createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Category createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Category updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public Category updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public Category categories(Set<Category> categories) {
        this.setCategories(categories);
        return this;
    }

    public Category addCategory(Category category) {
        this.categories.add(category);
        category.setParent(this);
        return this;
    }

    public Category removeCategory(Category category) {
        this.categories.remove(category);
        category.setParent(null);
        return this;
    }

    public void setCategories(Set<Category> categories) {
        if (this.categories != null) {
            this.categories.forEach(i -> i.setParent(null));
        }
        if (categories != null) {
            categories.forEach(i -> i.setParent(this));
        }
        this.categories = categories;
    }

    public Set<JobPreference> getJobprefrences() {
        return this.jobprefrences;
    }

    public Category jobprefrences(Set<JobPreference> jobPreferences) {
        this.setJobprefrences(jobPreferences);
        return this;
    }

    public Category addJobprefrence(JobPreference jobPreference) {
        this.jobprefrences.add(jobPreference);
        jobPreference.setCategory(this);
        return this;
    }

    public Category removeJobprefrence(JobPreference jobPreference) {
        this.jobprefrences.remove(jobPreference);
        jobPreference.setCategory(null);
        return this;
    }

    public void setJobprefrences(Set<JobPreference> jobPreferences) {
        if (this.jobprefrences != null) {
            this.jobprefrences.forEach(i -> i.setCategory(null));
        }
        if (jobPreferences != null) {
            jobPreferences.forEach(i -> i.setCategory(this));
        }
        this.jobprefrences = jobPreferences;
    }

    public Category getParent() {
        return this.parent;
    }

    public Category parent(Category category) {
        this.setParent(category);
        return this;
    }

    public void setParent(Category category) {
        this.parent = category;
    }

    public Set<Field> getFields() {
        return this.fields;
    }

    public Category fields(Set<Field> fields) {
        this.setFields(fields);
        return this;
    }

    public Category addField(Field field) {
        this.fields.add(field);
        field.getCategories().add(this);
        return this;
    }

    public Category removeField(Field field) {
        this.fields.remove(field);
        field.getCategories().remove(this);
        return this;
    }

    public void setFields(Set<Field> fields) {
        if (this.fields != null) {
            this.fields.forEach(i -> i.removeCategory(this));
        }
        if (fields != null) {
            fields.forEach(i -> i.addCategory(this));
        }
        this.fields = fields;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", categoryName='" + getCategoryName() + "'" +
            ", isParent='" + getIsParent() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
