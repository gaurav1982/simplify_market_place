package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A OTPAttempt.
 */
@Entity
@Table(name = "otp_attempt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "otpattempt")
public class OTPAttempt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "otp")
    private Integer otp;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private Integer phone;

    @Column(name = "ip")
    private String ip;

    @Column(name = "coookie")
    private String coookie;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDate createdAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OTPAttempt id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getOtp() {
        return this.otp;
    }

    public OTPAttempt otp(Integer otp) {
        this.otp = otp;
        return this;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return this.email;
    }

    public OTPAttempt email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return this.phone;
    }

    public OTPAttempt phone(Integer phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getIp() {
        return this.ip;
    }

    public OTPAttempt ip(String ip) {
        this.ip = ip;
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCoookie() {
        return this.coookie;
    }

    public OTPAttempt coookie(String coookie) {
        this.coookie = coookie;
        return this;
    }

    public void setCoookie(String coookie) {
        this.coookie = coookie;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public OTPAttempt createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public OTPAttempt createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OTPAttempt)) {
            return false;
        }
        return id != null && id.equals(((OTPAttempt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OTPAttempt{" +
            "id=" + getId() +
            ", otp=" + getOtp() +
            ", email='" + getEmail() + "'" +
            ", phone=" + getPhone() +
            ", ip='" + getIp() + "'" +
            ", coookie='" + getCoookie() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
