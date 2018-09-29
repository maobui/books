package com.me.bui.books.app

import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by mao.buidinh on 7/17/2017.
 */

class Application : android.app.Application() {
    val DATABASE_VERSION = 1
    override fun onCreate() {
        super.onCreate()

        Realm.init(this.applicationContext)
        val realmConfiguration = RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(DATABASE_VERSION.toLong())
//                .deleteRealmIfMigrationNeeded()
                .migration(AppMigration())
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)

    }
}
