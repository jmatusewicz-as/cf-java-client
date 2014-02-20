package org.cloudfoundry.gradle.tasks

import org.cloudfoundry.client.lib.domain.CloudApplication
import org.gradle.api.tasks.TaskAction

/**
 * @author: Jan Matusewicz
 * @since: 19.02.14 17:42
 */

@Mixin(DeployCloudFoundryHelper)
class UnmapTestUrlCloudFoundryTask extends AbstractMapCloudFoundryTask{

    public static final String NAME = "cf_unmap_testuri"

    UnmapTestUrlCloudFoundryTask() {
        super()
        description = 'Unmaps the canonical test-uri from the inactive app-variation'
    }

    @TaskAction
    void unmapTestUri() {
        withCloudFoundryClient {
            validateVariantsForDeploy()

            List<CloudApplication> apps = client.applications

            List<String> unmappedAppVariants = findUnmappedVariants(application, apps)

            if (unmappedAppVariants) {
                log "Unmapping URIs ${testUri} for ${unmappedAppVariants}"
            }

            withApplication {
                unmappedAppVariants.each { appName ->
                    project.cloudfoundry.application = appName
                    unmapTestUriFromApplication()
                }

            }
        }
    }

}
