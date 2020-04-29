rootProject.apply {
	name = "kt-ui"
}


include(":compiler-plugin", ":idea-plugin", ":test")
include(":core")

@Suppress("UnstableApiUsage")
enableFeaturePreview("GRADLE_METADATA")
