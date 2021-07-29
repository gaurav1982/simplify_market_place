package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OTPTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OTP.class);
        OTP oTP1 = new OTP();
        oTP1.setId(1L);
        OTP oTP2 = new OTP();
        oTP2.setId(oTP1.getId());
        assertThat(oTP1).isEqualTo(oTP2);
        oTP2.setId(2L);
        assertThat(oTP1).isNotEqualTo(oTP2);
        oTP1.setId(null);
        assertThat(oTP1).isNotEqualTo(oTP2);
    }
}
