-keep class com.vehicleloadcontrol.** { *; }
-keep class com.vehicleloadcontrol.domain.model.** { *; }
-keep class com.vehicleloadcontrol.data.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keep class com.google.dagger.** { *; }

# Room
-keep class androidx.room.** { *; }

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keep class com.google.gson.** { *; }

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlinx.coroutines.** { *; }

# PDFBox
-keep class com.tom_roush.pdfbox.** { *; }
-keep class com.itextpdf.** { *; }

# Apache POI
-keep class org.apache.poi.** { *; }

# Preserve line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
