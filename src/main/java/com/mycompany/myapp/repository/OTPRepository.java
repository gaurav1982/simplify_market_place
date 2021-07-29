package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.OTP;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OTP entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {}
