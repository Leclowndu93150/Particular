plugins {
    id("dev.prism")
}

group = "com.leclowndu93150"
version = "1.5.0"

prism {
    curseMaven()
    modrinthMaven()
    maven("Terraformers", "https://maven.terraformersmc.com/")
    maven("Nucleoid", "https://maven.nucleoid.xyz/")
    maven("Fuzs Mod Resources", "https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
    maven("BaguetteLib", "https://maven.leclowndu93150.dev/releases")
    maven("Sable Companion", "https://maven.ryanhcode.dev/releases")

    metadata {
        modId = "particular"
        name = "Particular Reforged"
        description = "Particular is a mod that enhances Minecraft's ambience with many hand-crafted visual effects."
        license = "LGPL-3.0"
        author("Leclowndu93150")
        credit("Leclowndu93150, and Chai the goat for the original Particular mod")
    }

    version("1.20.1") {
        parchmentMinecraftVersion = "1.20.1"
        parchmentMappingsVersion = "2023.09.03"
        changelogFile = "versions/1.20.1/CHANGELOG.md"

        common {
            compileOnly("curse.maven:terrafirmacraft-302973:7730077")
            modImplementation("curse.maven:forge-config-api-port-547434:7260491")
            compileOnly("io.github.llamalad7:mixinextras-common:0.4.1")
            annotationProcessor("io.github.llamalad7:mixinextras-common:0.4.1")
            compileOnly("org.ow2.asm:asm:9.6")
        }

        fabric {
            loaderVersion = "0.16.10"
            fabricApi("0.92.8+1.20.1")

            dependencies {
                modImplementation("curse.maven:forge-config-api-port-547434:7260491")
                modCompileOnly("curse.maven:irisshaders-455508:6258195")
                modCompileOnly("curse.maven:sodium-394468:6260639")
                modImplementation("curse.maven:modmenu-308702:5162837")
                compileOnly("io.github.llamalad7:mixinextras-common:0.4.1")
                annotationProcessor("io.github.llamalad7:mixinextras-common:0.4.1")
            }

            publishingDependencies {
                requires("fabric-api")
                requires("forge-config-api-port")
                optional("modmenu")
                curseforge { optional("irisshaders") }
                modrinth { optional("iris") }
            }
        }

        forge {
            loaderVersion = "47.4.18"

            dependencies {
                modCompileOnly("curse.maven:terrafirmacraft-302973:7730077")
                annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
                compileOnly("io.github.llamalad7:mixinextras-common:0.4.1")
                annotationProcessor("io.github.llamalad7:mixinextras-common:0.4.1")
                jarJar("io.github.llamalad7:mixinextras-forge:0.4.1")
            }

            publishingDependencies {
                optional("terrafirmacraft")
            }
        }
    }

    version("1.21.1") {
        parchmentMinecraftVersion = "1.21.1"
        parchmentMappingsVersion = "2024.11.17"
        changelogFile = "versions/1.21.1/CHANGELOG.md"

        version = "1.5.2"

        common {
            compileOnly("curse.maven:irisshaders-455508:6213635")
            compileOnly("fuzs.forgeconfigapiport:forgeconfigapiport-common-neoforgeapi:21.1.6")
        }

        fabric {
            loaderVersion = "0.16.10"
            fabricApi("0.110.0+1.21.1")

            dependencies {
                implementation("com.electronwill.night-config:core:3.8.1")
                implementation("com.electronwill.night-config:toml:3.8.1")
                implementation("com.electronwill.night-config:json:3.8.1")
                implementation("com.electronwill.night-config:hocon:3.8.1")
                implementation("com.electronwill.night-config:yaml:3.8.1")
                modImplementation("curse.maven:forge-config-api-port-547434:7213608")
                modCompileOnly("curse.maven:irisshaders-455508:6213635")
                modCompileOnly("curse.maven:sodium-394468:6382649")
                modImplementation("curse.maven:modmenu-308702:7808443")
            }

            publishingDependencies {
                requires("fabric-api")
                requires("forge-config-api-port")
                optional("modmenu")
                curseforge { optional("irisshaders") }
                modrinth { optional("iris") }
                optional("sable")
            }
        }

        neoforge {
            loaderVersion = "21.1.222"
            loaderVersionRange = "[4,)"

            dependencies {
                compileOnly("curse.maven:irisshaders-455508:6661598")
                compileOnly("curse.maven:sodium-394468:6382651")
                compileOnly("curse.maven:sable-1312371:8007005")
                runtimeOnly("curse.maven:sable-1312371:8007005")
                compileOnly("dev.ryanhcode.sable-companion:sable-companion-common-1.21.1:1.6.0")
                compileOnly("maven.modrinth:create-aeronautics:1.2.1+mc1.21.1")
                runtimeOnly("maven.modrinth:create-aeronautics:1.2.1+mc1.21.1")
                compileOnly("curse.maven:create-328085:7963363")
                runtimeOnly("curse.maven:create-328085:7963363")
                runtimeOnly("curse.maven:wakes-reforged-1223529:8144268")
            }

            publishingDependencies {
                curseforge { optional("irisshaders") }
                modrinth { optional("iris") }
                optional("sable")
                optional("create-aeronautics")
                optional("create")
            }
        }
    }

    version("26.1.2") {
        minecraftVersions("26.1", "26.1.1", "26.1.2")
        changelogFile = "versions/26.1.2/CHANGELOG.md"

        common {
            compileOnly("curse.maven:irisshaders-455508:7867943")
            compileOnly("fuzs.forgeconfigapiport:forgeconfigapiport-common-neoforgeapi:26.1.4")
            compileOnly("curse.maven:baguettelib-1264423:8010963")
        }

        fabric {
            loaderVersion = "0.19.2"
            fabricApi("0.145.4+26.1.2")

            dependencies {
                implementation("com.electronwill.night-config:core:3.8.3")
                implementation("com.electronwill.night-config:toml:3.8.3")
                implementation("com.electronwill.night-config:json:3.8.3")
                implementation("com.electronwill.night-config:hocon:3.8.3")
                implementation("com.electronwill.night-config:yaml:3.8.3")
                modImplementation("curse.maven:baguettelib-1264423:8010960")
                implementation("curse.maven:forge-config-api-port-547434:7986992")
                compileOnly("curse.maven:irisshaders-455508:7867943")
                compileOnly("curse.maven:sodium-394468:7867826")
                implementation("com.terraformersmc:modmenu:18.0.0-alpha.8")
            }

            publishingDependencies {
                requires("fabric-api")
                requires("forge-config-api-port")
                requires("baguettelib")
                optional("modmenu")
                curseforge { optional("irisshaders") }
                modrinth { optional("iris") }
            }
        }

        neoforge {
            loaderVersion = "26.1.2.7-beta"
            loaderVersionRange = "[4,)"

            dependencies {
                implementation("curse.maven:baguettelib-1264423:8010963")
                compileOnly("curse.maven:irisshaders-455508:7867946")
                compileOnly("curse.maven:sodium-394468:7867828")
            }

            publishingDependencies {
                requires("baguettelib")
                curseforge { optional("irisshaders") }
                modrinth { optional("iris") }
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
