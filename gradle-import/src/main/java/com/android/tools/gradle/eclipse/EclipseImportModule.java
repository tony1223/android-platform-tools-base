/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tools.gradle.eclipse;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.repository.GradleCoordinate;

import java.io.File;
import java.util.List;

/** An imported module from Eclipse */
class EclipseImportModule extends ImportModule {
    private final EclipseProject mProject;

    public EclipseImportModule(@NonNull GradleImport importer, @NonNull EclipseProject project) {
        super(importer);
        mProject = project;
    }

    @Override
    @NonNull
    protected File resolveFile(@NonNull File file) {
        if (file.isAbsolute()) {
            return file;
        } else {
            return new File(mProject.getDir(), file.getPath());
        }
    }

    @Override
    protected void initDependencies() {
        super.initDependencies();
        for (File jar : mProject.getJarPaths()) {
            if (mImporter.isReplaceJars()) {
                GradleCoordinate dependency = guessDependency(jar);
                if (dependency != null) {
                    mDependencies.add(dependency);
                    mImporter.getSummary().replacedJar(jar, dependency);
                    continue;
                }
            }
            mJarDependencies.add(jar);
        }
    }

    public void addDependencies(@NonNull List<GradleCoordinate> dependencies) {
        for (GradleCoordinate dependency : dependencies) {
            if (!mDependencies.contains(dependency)) {
                mDependencies.addAll(dependencies);
            }
        }
    }

    @Override
    protected boolean dependsOnLibrary(@NonNull String pkg) {
        if (pkg.equals(mProject.getPackage())) {
            return true;
        }

        for (EclipseProject project : mProject.getAllLibraries()) {
            if (pkg.equals(project.getPackage())) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean isAndroidProject() {
        return mProject.isAndroidProject();
    }

    @Override
    protected boolean isLibrary() {
        return mProject.isLibrary();
    }

    @Nullable
    @Override
    protected String getPackage() {
        return mProject.getPackage();
    }

    @NonNull
    @Override
    protected String getOriginalName() {
        return mProject.getName();
    }

    @Override
    public boolean isApp() {
        return mProject.isAndroidProject() && !mProject.isLibrary();
    }

    @Override
    public boolean isAndroidLibrary() {
        return mProject.isAndroidProject() && mProject.isLibrary();
    }

    @Override
    public boolean isJavaLibrary() {
        return !mProject.isAndroidProject();
    }

    @Override
    @Nullable protected File getManifestFile() {
        return mProject.getManifestFile();
    }

    @Override
    @Nullable protected File getResourceDir() {
        return mProject.getResourceDir();
    }

    @Override
    @Nullable protected File getAssetsDir() {
        return mProject.getAssetsDir();
    }

    @Override
    @NonNull
    protected List<File> getSourcePaths() {
        return mProject.getSourcePaths();
    }

    @Override
    @NonNull
    protected List<File> getJarPaths() {
        return mProject.getJarPaths();
    }

    @Override
    @NonNull
    protected List<File> getLocalProguardFiles() {
        return mProject.getLocalProguardFiles();
    }

    @NonNull
    @Override
    protected List<File> getSdkProguardFiles() {
        return mProject.getSdkProguardFiles();
    }

    @NonNull
    @Override
    protected File getCanonicalModuleDir() {
        return mProject.getCanonicalDir();
    }

    @Nullable
    @Override
    protected File getOutputDir() {
        return mProject.getOutputDir();
    }

    @NonNull
    @Override
    protected String getLanguageLevel() {
        return mProject.getLanguageLevel();
    }

    @Override
    protected int getCompileSdkVersion() {
        return mProject.getCompileSdkVersion();
    }

    @Override
    protected int getTargetSdkVersion() {
        return mProject.getTargetSdkVersion();
    }

    @Override
    protected int getMinSdkVersion() {
        return mProject.getMinSdkVersion();
    }

    @Override
    protected boolean dependsOn(@NonNull ImportModule other) {
        return mProject.getAllLibraries().contains(((EclipseImportModule)other).mProject);
    }

    @NonNull
    public EclipseProject getProject() {
        return mProject;
    }
}
