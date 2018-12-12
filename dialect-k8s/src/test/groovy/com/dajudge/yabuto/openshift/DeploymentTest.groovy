package com.dajudge.yabuto.openshift

import com.dajudge.yabuto.testbase.AbstractBaseRootObjectBuilderTest
import com.dajudge.ymlgen.api.Entrypoint
import org.junit.Test

import static com.dajudge.yabuto.openshift.KubernetesDialect.ENTRYPOINT_DEPLOYMENT
import static com.dajudge.yabuto.testbase.DialectAsserts.assertKind
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

class DeploymentTest extends AbstractBaseRootObjectBuilderTest {

    @Override
    Entrypoint entrypoint() {
        new KubernetesDialect().getEntrypoints().get(ENTRYPOINT_DEPLOYMENT)
    }

    @Test
    void has_correct_kind() {
        def result = underTest {}

        assertKind(OBJECT_NAME, "apps/v1", "Deployment", result)
    }

    @Test
    void adds_match_labels() {
        def result = underTest {
            matchLabel("app", "appName")
        }

        assertEquals(result.spec?.matchLabels?.app, "appName")
    }

    @Test
    void adds_template() {
        def result = underTest {
            template {
                label("app", "appName")
            }
        }

        def metadata = result.spec?.template?.metadata
        assertNotNull(metadata)
        assertEquals("appName", metadata.labels?.app)
    }

    @Test
    void adds_containers() {
        def result = underTest {
            template {
                container("myContainer") {}
            }
        }
        List containers = result.spec?.template?.containers
        assertNotNull(containers)
        assertEquals(1, containers.size())
        assertEquals("myContainer", containers[0].name)
    }

    @Test
    void adds_imagePullSecrets() {
        def result = underTest {
            template {
                imagePullSecret "lolcats"
            }
        }

        List secrets = result.spec?.template?.imagePullSecrets
        assertNotNull(secrets)
        assertEquals(1, secrets.size())
        assertEquals("lolcats", secrets[0].name)
    }
}