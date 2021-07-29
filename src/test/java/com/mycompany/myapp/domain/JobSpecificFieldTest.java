package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobSpecificFieldTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobSpecificField.class);
        JobSpecificField jobSpecificField1 = new JobSpecificField();
        jobSpecificField1.setId(1L);
        JobSpecificField jobSpecificField2 = new JobSpecificField();
        jobSpecificField2.setId(jobSpecificField1.getId());
        assertThat(jobSpecificField1).isEqualTo(jobSpecificField2);
        jobSpecificField2.setId(2L);
        assertThat(jobSpecificField1).isNotEqualTo(jobSpecificField2);
        jobSpecificField1.setId(null);
        assertThat(jobSpecificField1).isNotEqualTo(jobSpecificField2);
    }
}
