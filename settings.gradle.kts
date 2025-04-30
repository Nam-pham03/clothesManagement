pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://www.jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://storage.zego.im/maven") } // Sửa lại dấu 'url' thành 'uri'
        maven { url = uri("https://www.jitpack.io") } // Sửa lại dấu 'url' thành 'uri'
        /// chat

    }
}

rootProject.name = "Project_PRM"
include(":app")
 