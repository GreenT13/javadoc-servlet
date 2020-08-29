package com.apon.javadocservlet.controllers.frontend;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class FrontendFormTest {

    @Test
    void equalsAndHashCodeAreImplemented() {
        EqualsVerifier.simple().forClass(FrontendForm.class).verify();
    }
}
