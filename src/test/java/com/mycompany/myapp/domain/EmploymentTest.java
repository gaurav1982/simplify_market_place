package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmploymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employment.class);
        Employment employment1 = new Employment();
        employment1.setId(1L);
        Employment employment2 = new Employment();
        employment2.setId(employment1.getId());
        assertThat(employment1).isEqualTo(employment2);
        employment2.setId(2L);
        assertThat(employment1).isNotEqualTo(employment2);
        employment1.setId(null);
        assertThat(employment1).isNotEqualTo(employment2);
    }
}
