package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.CompType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comp_name")
    private String compName;

    @Column(name = "comp_address")
    private String compAddress;

    @Column(name = "comp_website")
    private String compWebsite;

    @Enumerated(EnumType.STRING)
    @Column(name = "comp_type")
    private CompType compType;

    @Column(name = "comp_contact_no")
    private String compContactNo;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client", "employments" }, allowSetters = true)
    private Set<Location> locations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client id(Long id) {
        this.id = id;
        return this;
    }

    public String getCompName() {
        return this.compName;
    }

    public Client compName(String compName) {
        this.compName = compName;
        return this;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompAddress() {
        return this.compAddress;
    }

    public Client compAddress(String compAddress) {
        this.compAddress = compAddress;
        return this;
    }

    public void setCompAddress(String compAddress) {
        this.compAddress = compAddress;
    }

    public String getCompWebsite() {
        return this.compWebsite;
    }

    public Client compWebsite(String compWebsite) {
        this.compWebsite = compWebsite;
        return this;
    }

    public void setCompWebsite(String compWebsite) {
        this.compWebsite = compWebsite;
    }

    public CompType getCompType() {
        return this.compType;
    }

    public Client compType(CompType compType) {
        this.compType = compType;
        return this;
    }

    public void setCompType(CompType compType) {
        this.compType = compType;
    }

    public String getCompContactNo() {
        return this.compContactNo;
    }

    public Client compContactNo(String compContactNo) {
        this.compContactNo = compContactNo;
        return this;
    }

    public void setCompContactNo(String compContactNo) {
        this.compContactNo = compContactNo;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Client createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Client createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Client updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public Client updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Location> getLocations() {
        return this.locations;
    }

    public Client locations(Set<Location> locations) {
        this.setLocations(locations);
        return this;
    }

    public Client addLocation(Location location) {
        this.locations.add(location);
        location.setClient(this);
        return this;
    }

    public Client removeLocation(Location location) {
        this.locations.remove(location);
        location.setClient(null);
        return this;
    }

    public void setLocations(Set<Location> locations) {
        if (this.locations != null) {
            this.locations.forEach(i -> i.setClient(null));
        }
        if (locations != null) {
            locations.forEach(i -> i.setClient(this));
        }
        this.locations = locations;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", compName='" + getCompName() + "'" +
            ", compAddress='" + getCompAddress() + "'" +
            ", compWebsite='" + getCompWebsite() + "'" +
            ", compType='" + getCompType() + "'" +
            ", compContactNo='" + getCompContactNo() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
