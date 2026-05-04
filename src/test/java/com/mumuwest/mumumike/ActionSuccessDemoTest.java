package com.mumuwest.mumumike;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActionSuccessDemoTest {

    @Test
    void passesToDemonstrateGithubActionsQualityGateSuccess() {
        assertThat("GitHub Actions success demo")
                .as("This test is intentionally correct so the quality gate can pass.")
                .isEqualTo("GitHub Actions success demo");
    }
}
