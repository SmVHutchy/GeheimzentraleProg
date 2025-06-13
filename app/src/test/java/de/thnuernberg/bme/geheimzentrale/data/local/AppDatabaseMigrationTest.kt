import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.room.testing.MigrationTestHelper
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseMigrationTest {
    private val TEST_DB = "migration-test"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    private fun migration(name: String): Migration {
        val field = AppDatabase::class.java.getDeclaredField(name)
        field.isAccessible = true
        return field.get(null) as Migration
    }

    @Test
    fun migrateAll() {
        helper.createDatabase(TEST_DB, 1).apply {
            execSQL("CREATE TABLE IF NOT EXISTS `playlists` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `createdAt` INTEGER NOT NULL)")
            execSQL("CREATE TABLE IF NOT EXISTS `playlist_episodes` (`playlistId` INTEGER NOT NULL, `episodeId` INTEGER NOT NULL, `position` INTEGER NOT NULL, `addedAt` INTEGER NOT NULL, PRIMARY KEY(`playlistId`, `episodeId`))")
            execSQL("CREATE TABLE IF NOT EXISTS `episode_status` (`episodeId` INTEGER PRIMARY KEY NOT NULL, `isListened` INTEGER NOT NULL, `isInProgress` INTEGER NOT NULL, `progress` INTEGER NOT NULL, `lastUpdated` INTEGER NOT NULL)")
            close()
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.databaseBuilder(context, AppDatabase::class.java, TEST_DB)
            .addMigrations(
                migration("MIGRATION_1_2"),
                migration("MIGRATION_2_3"),
                migration("MIGRATION_3_4")
            )
            .build()

        db.query("SELECT * FROM episode_status", emptyArray()).close()
        db.query("PRAGMA table_info(`episode_status`)", emptyArray()).use { cursor ->
            var found = false
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndexOrThrow("name")) == "isFavorite") {
                    found = true
                }
            }
            assertTrue(found)
        }
        db.close()
    }
}
