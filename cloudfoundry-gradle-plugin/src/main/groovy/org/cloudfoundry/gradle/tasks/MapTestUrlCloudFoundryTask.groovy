package org.cloudfoundry.gradle.tasks

import org.cloudfoundry.client.lib.domain.CloudApplication
import org.gradle.api.tasks.TaskAction

/**
 * @author: Jan Matusewicz
 * @since: 19.02.14 11:33
 */

@Mixin(DeployCloudFoundryHelper)
class MapTestUrlCloudFoundryTask extends AbstractMapCloudFoundryTask {

    public static final String NAME = "cf_map_testuri"

    MapTestUrlCloudFoundryTask() {
        super()
        description = 'Maps a canonical test-uri to the inactive app-variation'
    }

    @TaskAction
    void mapTestUri() {
        withCloudFoundryClient {
            validateVariantsForDeploy()

            List<CloudApplication> apps = client.applications

            List<String> unmappedAppVariants = findUnmappedVariants(application, apps)

            if (unmappedAppVariants) {
                log "Mapping URIs ${testUri} for ${unmappedAppVariants}"
            }

            withApplication {
                unmappedAppVariants.each { appName ->
                    project.cloudfoundry.application = appName
                    mapTestUriToApplication()
                }

            }
        }
    }

}
