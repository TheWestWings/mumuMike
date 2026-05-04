package com.mumuwest.mumumike;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActionFailureDemoTest {

    @Test
    void intentionallyFailsToDemonstrateGithubActionsQualityGate() {
        assertThat("GitHub Actions failure demo")
                .as("This test is intentionally wrong so backend-code-test turns red.")
                .isEqualTo("all tests pass");
    }
}
