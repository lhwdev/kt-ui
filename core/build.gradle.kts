import com.lhwdev.build.*


plugins {
	kotlin("multiplatform")
	id("com.android.application")
	
	id("common-plugin")
}


kotlin {
	setupCommon()
	setupAndroid(project)
}

android {
	compileSdkVersion(AndroidVariants.compileSdk)
	
	defaultConfig {
		minSdkVersion(AndroidVariants.minSdk)
		targetSdkVersion(AndroidVariants.targetSdk)
		versionCode = AndroidVariants.versionCode
		versionName = AndroidVariants.versionName
	}
}
