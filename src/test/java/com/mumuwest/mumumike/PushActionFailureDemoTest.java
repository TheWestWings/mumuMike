package com.mumuwest.mumumike;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PushActionFailureDemoTest {

    @Test
    void failsToDemonstrateGithubActionsOnBranchPush() {
        assertThat("push action failure demo")
                .as("This test is intentionally wrong so backend-code-test turns red after push.")
                .isEqualTo("all tests pass");
    }
}
