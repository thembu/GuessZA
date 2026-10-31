package com.thembu.guessza.clientConfig;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MapsPropertiesTest {

    // --- plain unit tests: no Spring needed, the check lives in the constructor ---

    @Test
    void nullKeyIsRejected() {
        assertThatThrownBy(() -> new MapsProperties(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void blankKeyIsRejected() {
        assertThatThrownBy(() -> new MapsProperties(" "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void toStringDoesNotLeakKey() {
        MapsProperties properties = new MapsProperties("secret-key");

        assertThat(properties.toString()).doesNotContain("secret-key");
    }

    // --- context tests: prove Spring refuses to start without a key ---

    //minimal context containing only MapsProperties (no web layer, no database, no .env)
    @EnableConfigurationProperties(MapsProperties.class)
    static class TestConfig {
    }

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner().withUserConfiguration(TestConfig.class);

    @Test
    void contextFailsWhenKeyIsMissing() {
        contextRunner.run(context -> assertThat(context).hasFailed());
    }

    @Test
    void contextFailsWhenKeyIsBlank() {
        contextRunner
                .withPropertyValues("guessza.maps.api-key=")
                .run(context -> assertThat(context).hasFailed());
    }

    @Test
    void contextStartsAndBindsKeyWhenSet() {
        contextRunner
                .withPropertyValues("guessza.maps.api-key=test-key")
                .run(context -> {
                    assertThat(context).hasNotFailed();
                    assertThat(context.getBean(MapsProperties.class).apikey()).isEqualTo("test-key");
                });
    }
}
