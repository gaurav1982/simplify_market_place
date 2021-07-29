package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationPrefrenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationPrefrence.class);
        LocationPrefrence locationPrefrence1 = new LocationPrefrence();
        locationPrefrence1.setId(1L);
        LocationPrefrence locationPrefrence2 = new LocationPrefrence();
        locationPrefrence2.setId(locationPrefrence1.getId());
        assertThat(locationPrefrence1).isEqualTo(locationPrefrence2);
        locationPrefrence2.setId(2L);
        assertThat(locationPrefrence1).isNotEqualTo(locationPrefrence2);
        locationPrefrence1.setId(null);
        assertThat(locationPrefrence1).isNotEqualTo(locationPrefrence2);
    }
}
