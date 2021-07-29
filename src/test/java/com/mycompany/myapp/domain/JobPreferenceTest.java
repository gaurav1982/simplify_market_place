package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobPreferenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobPreference.class);
        JobPreference jobPreference1 = new JobPreference();
        jobPreference1.setId(1L);
        JobPreference jobPreference2 = new JobPreference();
        jobPreference2.setId(jobPreference1.getId());
        assertThat(jobPreference1).isEqualTo(jobPreference2);
        jobPreference2.setId(2L);
        assertThat(jobPreference1).isNotEqualTo(jobPreference2);
        jobPreference1.setId(null);
        assertThat(jobPreference1).isNotEqualTo(jobPreference2);
    }
}
