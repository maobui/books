package com.me.bui.books.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by mao.buidinh on 7/17/2017.
 */

open class Book : RealmObject() {

    @PrimaryKey
    open var id: Int = 0

    open var title: String? = null

    open var description: String? = null

    open var author: String? = null

    open var imageUrl: String? = null
}
