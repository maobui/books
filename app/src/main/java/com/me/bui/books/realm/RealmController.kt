package com.me.bui.books.realm

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment

import com.me.bui.books.helper.BookKey
import com.me.bui.books.model.Book

import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by mao.buidinh on 7/17/2017.
 */

class RealmController(application: Application) {
    private val realm: Realm?

    //find all objects in the Book.class
    val books: RealmResults<Book>
        get() = realm!!.where(Book::class.java).findAll()

    init {
        realm = Realm.getDefaultInstance()
    }

    //Refresh the realm istance
    fun refresh() {

        realm!!.refresh()
    }

    //clear all objects from Book.class
    fun clearAll() {
        realm!!.beginTransaction()
        realm.delete(Book::class.java)
        realm.commitTransaction()
    }

    //query a single item with the given id
    fun getBook(id: String): Book? {
        return realm!!.where(Book::class.java).equalTo(BookKey.ID, id).findFirst()
    }

    //query a single item with the given location
    fun getBookLocation(postion: Int): Book? {
        val results = this.books
        return results[postion]
    }

    //check if Book.class is empty
    fun hasBooks(): Boolean {
        return !realm!!.where(Book::class.java).findAll().isEmpty()
    }

    // remove a book with the given location
    fun removeBookLocation(position: Int) {
        val results = this.books
        realm!!.beginTransaction()
        // remove single match
        //results.remove(position);

        //other way.
        val book = results[position]
        book!!.deleteFromRealm()

        realm.commitTransaction()
    }

    // update a book with the given location
    fun updateBookLocation(position: Int, author: String, title: String, imageUrl: String) {
        val results = this.books
        realm!!.beginTransaction()
        results[position]!!.author = author
        results[position]!!.title = title
        results[position]!!.imageUrl = imageUrl
        realm.commitTransaction()
    }

    // add a book
    fun addBook(book: Book) {
        realm!!.beginTransaction()
        realm.copyToRealm(book)
        realm.commitTransaction()
    }

    //query example
    fun queryedBooks(): RealmResults<Book> {
        return realm!!.where(Book::class.java)
                .contains(BookKey.AUTHOR, "Author 0")
                .or()
                .contains(BookKey.TITLE, "Realm")
                .findAll()

    }

    fun close() {
        realm?.close()
    }

    companion object {
        var instance: RealmController? = null
            private set

        fun with(fragment: Fragment): RealmController {

            if (instance == null) {
                instance = RealmController(fragment.activity!!.application)
            }
            return instance as RealmController
        }

        fun with(activity: Activity): RealmController {

            if (instance == null) {
                instance = RealmController(activity.application)
            }
            return instance as RealmController
        }

        fun with(application: Application): RealmController {

            if (instance == null) {
                instance = RealmController(application)
            }
            return instance as RealmController
        }
    }
}
