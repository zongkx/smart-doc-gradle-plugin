package com.ly.doc.gradle.util;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yu 2020/4/29
 */
public class ClassLoaderUtil {

    public static ClassLoader getRuntimeClassLoader(Project project) {
        try {
            Configuration compileConfiguration = project.getConfigurations().getByName(JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME);
            Set<File> fileSet = compileConfiguration.getFiles();
            List<URL> urls = new ArrayList<>();
            for (File file : fileSet) {
                urls.add(file.toURI().toURL());
            }

            JavaPluginExtension javaExtension = project.getExtensions().getByType(JavaPluginExtension.class);
            SourceSetContainer ssc = javaExtension.getSourceSets();

            FileCollection classesDir = ssc.getByName(SourceSet.MAIN_SOURCE_SET_NAME).getOutput().getClassesDirs();
            Set<File> fileSet1 = classesDir.getFiles();
            for (File file : fileSet1) {
                urls.add(file.toURI().toURL());
            }
            URL[] runtimeUrls = urls.toArray(new URL[0]);
            return new URLClassLoader(runtimeUrls, Thread.currentThread().getContextClassLoader());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unable to load project runtime !", e);
        }
    }
}