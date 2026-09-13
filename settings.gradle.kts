pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://xposed.info") } // <--- Добавьте эту строку
    }
}

rootProject.name = "DpiChanger"
include(":app")
include(":xposed-module")
