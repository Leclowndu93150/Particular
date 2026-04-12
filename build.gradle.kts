plugins {
    id("dev.prism")
}

group = "com.leclowndu93150"
version = "1.4.1"

prism {
    curseMaven()
    modrinthMaven()
    maven("Terraformers", "https://maven.terraformersmc.com/")
    maven("Nucleoid", "https://maven.nucleoid.xyz/")
    maven("Fuzs Mod Resources", "https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
    maven("BaguetteLib", "https://maven.leclowndu93150.dev/releases")

    metadata {
        modId = "particular"
        name = "Particular Reforged"
        description = "Particular is a mod that enhances Minecraft's ambience with many hand-crafted visual effects."
        license = "LGPL-3.0"
        author("Leclowndu93150")
        credit("Leclowndu93150, and Chai the goat for the original Particular mod")
    }

    version("26.1") {
        minecraftVersions("26.1", "26.1.1", "26.1.2")

        common {
            compileOnly("curse.maven:irisshaders-455508:7867943")
            compileOnly("fuzs.forgeconfigapiport:forgeconfigapiport-common-neoforgeapi:26.1.3")
            compileOnly("com.leclowndu93150.baguettelib:baguettelib-26.1-common:2.0.6")
        }

        fabric {
            loaderVersion = "0.19.1"
            fabricApi("0.145.4+26.1.2")

            dependencies {
                implementation("com.electronwill.night-config:core:3.8.3")
                implementation("com.electronwill.night-config:toml:3.8.3")
                implementation("com.electronwill.night-config:json:3.8.3")
                implementation("com.electronwill.night-config:hocon:3.8.3")
                implementation("com.electronwill.night-config:yaml:3.8.3")
                implementation("com.leclowndu93150.baguettelib:baguettelib-26.1-fabric:2.0.6")
                implementation("curse.maven:forge-config-api-port-547434:7861252")
                compileOnly("curse.maven:irisshaders-455508:7867943")
                compileOnly("curse.maven:sodium-394468:7867826")
                implementation("com.terraformersmc:modmenu:18.0.0-alpha.8")
            }

            publishingDependencies {
                requires("fabric-api")
                requires("forge-config-api-port")
                requires("modmenu")
                requires("baguettelib")
                optional("iris")
            }
        }

        neoforge {
            loaderVersion = "26.1.2.7-beta"
            loaderVersionRange = "[4,)"

            dependencies {
                implementation("com.leclowndu93150.baguettelib:baguettelib-26.1-neoforge:2.0.6")
                compileOnly("curse.maven:irisshaders-455508:7867946")
                compileOnly("curse.maven:sodium-394468:7867828")
            }

            publishingDependencies {
                requires("baguettelib")
                optional("iris")
            }
        }
    }

    publishing {
        type = BETA
        curseforge {
            accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
            projectId = "1219053"
        }
        modrinth {
            accessToken = providers.environmentVariable("MODRINTH_TOKEN")
            projectId = "pYFUU6cq"
        }
    }
}
