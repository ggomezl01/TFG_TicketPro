plugins {
    id("com.android.application")
}

android {
    namespace = "com.gonzalogomez.ticketpro"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gonzalogomez.ticketpro"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.recyclerview:recyclerview:1.3.2") //RecyclerView
    implementation("androidx.cardview:cardview:1.0.0") //Elementos del recycler
    implementation("com.cloudinary:cloudinary-android:2.7.1") //Cloudinary - servidor imágenes
    implementation("com.github.bumptech.glide:glide:4.16.0") //Glide - cargado de imágenes
    implementation("androidx.appcompat:appcompat:1.6.1") //Base de la aplicación
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") //ConstraintLayout en las actividades
    implementation("org.postgresql:postgresql:42.2.9") //Postgresql - Base de datos
    implementation("commons-codec:commons-codec:1.16.1") //Commons codec - Encriptado de contraseñas
    implementation("com.journeyapps:zxing-android-embedded:4.1.0") //Embedded - Códigos QR de entradas
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}