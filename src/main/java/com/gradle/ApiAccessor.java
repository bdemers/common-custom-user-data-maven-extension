package com.gradle;

import com.gradle.maven.extension.api.cache.BuildCacheApi;
import com.gradle.maven.extension.api.scan.BuildScanApi;
import org.apache.maven.MavenExecutionException;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

import java.util.Optional;

import static java.util.Comparator.comparing;

final class ApiAccessor {

    private static final String BUILD_CACHE_API_PACKAGE = "com.gradle.maven.extension.api.cache";
    private static final String BUILD_SCAN_API_PACKAGE = "com.gradle.maven.extension.api.scan";

    static BuildScanApi lookupBuildScanApi(PlexusContainer container, Class<?> extensionClass) throws MavenExecutionException {
        ensureClassIsAccessible(extensionClass, BUILD_SCAN_API_PACKAGE);
        return lookupClass(BuildScanApi.class, container);
    }

    static BuildCacheApi lookupBuildCacheApi(PlexusContainer container, Class<?> extensionClass) throws MavenExecutionException {
        ensureClassIsAccessible(extensionClass, BUILD_CACHE_API_PACKAGE);
        return lookupClass(BuildCacheApi.class, container);
    }

    /**
     * Workaround for https://issues.apache.org/jira/browse/MNG-6906
     */
    private static void ensureClassIsAccessible(Class<?> extensionClass, String componentPackage) throws MavenExecutionException {
        ClassLoader classLoader = extensionClass.getClassLoader();
        if (classLoader instanceof ClassRealm) {
            ClassRealm extensionRealm = (ClassRealm) classLoader;
            if (!"maven.ext".equals(extensionRealm.getId())) {
                Optional<ClassRealm> sourceRealm = extensionRealm.getWorld().getRealms().stream()
                        .filter(realm -> realm.getId().contains("com.gradle:gradle-enterprise-maven-extension") || realm.getId().equals("maven.ext"))
                        .max(comparing((ClassRealm realm) -> realm.getId().length()));
                if (sourceRealm.isPresent()) {
                    String sourceRealmId = sourceRealm.get().getId();
                    try {
                        extensionRealm.importFrom(sourceRealmId, componentPackage);
                    } catch (Exception e) {
                        throw new MavenExecutionException("Could not import package from realm with id " + sourceRealmId, e);
                    }
                }
            }
        }
    }

    private static <T> T lookupClass(Class<T> componentClass, PlexusContainer container) throws MavenExecutionException {
        if (!container.hasComponent(componentClass)) {
            return null;
        } else {
            try {
                return container.lookup(componentClass);
            } catch (ComponentLookupException e) {
                throw new MavenExecutionException(String.format("Cannot look up object in container: %s", componentClass), e);
            }
        }
    }

    private ApiAccessor(){}

}
