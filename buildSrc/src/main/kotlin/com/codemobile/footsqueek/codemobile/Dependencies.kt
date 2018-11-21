package com.codemobile.footsqueek.codemobile


object Dependencies {

    object Versions {

        const val Kotlin = "1.3.0"

        const val AndroidPlugin = "3.2.1"
        const val AndroidSupportLibs = "27.1.1"
        const val ConstraintLayout = "1.0.2"

        const val Picasso = "2.5.2"

        const val Crashlytics = "2.9.4"

        const val MultiDex = "2.0.0"

        const val Realm = "2.3.2"
        const val Fabric = "1.26.1"

        const val JUnit = "4.12"
        const val Elmyr = "0.8.1"
        const val Mockito = "2.19.0"
        const val MockitoKotlin = "1.6.0"
        const val AssertJ = "3.11.0"

    }

    object Libraries {

        @JvmField val AndroidSupport = arrayOf(
                "com.android.support:appcompat-v7:${Versions.AndroidSupportLibs}",
                "com.android.support:design:${Versions.AndroidSupportLibs}",
                "com.android.support:cardview-v7:${Versions.AndroidSupportLibs}",
                "com.android.support:recyclerview-v7:${Versions.AndroidSupportLibs}",
                "com.android.support.constraint:constraint-layout:${Versions.ConstraintLayout}"
        )

        const val MultiDex = "androidx.multidex:multidex:${Versions.MultiDex}"

        @JvmField
        val Kotlin = arrayOf(
                "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.Kotlin}",
                "org.jetbrains.kotlin:kotlin-reflect:${Versions.Kotlin}"
        )

        const val Picasso = "com.squareup.picasso:picasso:${Versions.Picasso}"


        const val Crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.Crashlytics}@aar"

        @JvmField
        val Testing = arrayOf(
                "junit:junit:${Versions.JUnit}",
                "org.mockito:mockito-core:${Versions.Mockito}",
                "com.nhaarman:mockito-kotlin:${Versions.MockitoKotlin}",
                "com.github.xgouchet:Elmyr:${Versions.Elmyr}",
                "org.assertj:assertj-core:${Versions.AssertJ}"
        )
    }

    object ClassPaths {
        const val AndroidPlugin = "com.android.tools.build:gradle:${Versions.AndroidPlugin}"
        const val KotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin}"
        const val RealmPlugin = "io.realm:realm-gradle-plugin:${Versions.Realm}"
        const val FabricPlugin = "io.fabric.tools:gradle:${Versions.Fabric}"
    }

    object Repositories {
        const val Fabric = "https://maven.fabric.io/public"
        const val Jitpack = "https://jitpack.io"
        const val Gradle = "https://plugins.gradle.org/m2/"
        const val Google = "https://maven.google.com"
    }

}
