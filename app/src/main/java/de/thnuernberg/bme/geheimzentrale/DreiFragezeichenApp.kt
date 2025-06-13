package de.thnuernberg.bme.geheimzentrale

import android.app.Application
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DreiFragezeichenApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Coil-Konfiguration
        val imageLoader = ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .crossfade(true)
            .build()
            
        coil.Coil.setImageLoader(imageLoader)
    }
} 