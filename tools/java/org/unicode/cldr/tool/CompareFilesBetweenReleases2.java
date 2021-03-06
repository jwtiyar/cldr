package org.unicode.cldr.tool;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.unicode.cldr.util.CLDRPaths;

import com.google.common.collect.ImmutableSet;

public class CompareFilesBetweenReleases2 {

    //private static final String STAGING_DIRECTORY = CldrUtility.getPath(CLDRPaths.BASE_DIRECTORY, "../cldr-staging/production");
    private static final String RELEASE_DIRECTORY = CLDRPaths.ARCHIVE_DIRECTORY + "cldr-" + ToolConstants.LAST_RELEASE_VERSION + "/";

    public static void main(String[] args) throws IOException {
        Set<String> staging = getFiles(new File(CLDRPaths.BASE_DIRECTORY));
        Set<String> lastRelease = getFiles(new File(RELEASE_DIRECTORY));
        System.out.println("\nIn trunk, but not last release:\n");
        showDiff(staging, lastRelease);
        System.out.println("\nIn last release, but not trunk:\n");
        showDiff(lastRelease, staging);
    }

    private static void showDiff(Set<String> staging, Set<String> lastRelease) {
        Set<String> staging_release = new LinkedHashSet<>(staging);
        staging_release.removeAll(lastRelease);
        for (String file : staging_release) {
            System.out.println(file);
        }
    }

    private static Set<String> getFiles(File base) throws IOException {
        Set<String> result = new TreeSet<>();
        int baseLen = base.getCanonicalPath().length();
        getFiles(baseLen, base, result);
        return result;
    }

    static final Set<String> SKIP = ImmutableSet.of("seed", "exemplars", "docs", "tools");
    
    private static void getFiles(int baseLen, File subdir, Set<String> names) throws IOException {
        String name = subdir.getName();
        if (subdir.isDirectory()) {
            if (SKIP.contains(name)
                || name.startsWith(".")) {
                return;
            }
            for (File file : subdir.listFiles()) {
                getFiles(baseLen, file, names);
            }
        }
        if (name.startsWith(".")) {
            return;
        }
        String fullName = subdir.getCanonicalPath();
        names.add(fullName.substring(baseLen));
    }
}