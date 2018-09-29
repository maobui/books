package com.me.bui.books.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.me.bui.books.adapter.BookAdapter
import com.me.bui.books.helper.PrefManager
import com.me.bui.books.model.Book
import com.me.bui.books.realm.RealmController
import com.me.bui.books.R
import io.realm.RealmResults
import java.util.*

class MainActivity : AppCompatActivity() {

    private var adapter: BookAdapter? = null
    private var mRealmController: RealmController? = null
    private var inflater: LayoutInflater? = null
    private var fab: FloatingActionButton? = null
    private var recycler: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab = findViewById<View>(R.id.fab) as FloatingActionButton
        recycler = findViewById<View>(R.id.recycler) as RecyclerView

        //get realm instance
        this.mRealmController = RealmController.with(this)

        //set toolbar
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        setupRecycler()

        if (!PrefManager.with(this).preLoad) {
            setRealmData()
        }

        // refresh the realm instance
//        RealmController.with(this).refresh()

        Toast.makeText(this, "Press card item for edit, long press to remove item", Toast.LENGTH_LONG).show()

        //add new item
        fab!!.setOnClickListener {
            inflater = this@MainActivity.layoutInflater
            val content = inflater!!.inflate(R.layout.edit_item, null)
            val editTitle = content.findViewById<View>(R.id.title) as EditText
            val editAuthor = content.findViewById<View>(R.id.author) as EditText
            val editThumbnail = content.findViewById<View>(R.id.thumbnail) as EditText

            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setView(content)
                    .setTitle("Add book")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        val book = Book()
                        //book.setId(RealmController.getInstance().getBooks().size() + 1);
                        book.id = (RealmController.instance!!.books.size + System.currentTimeMillis()).toInt()
                        book.title = editTitle.text.toString()
                        book.author = editAuthor.text.toString()
                        book.imageUrl = editThumbnail.text.toString()

                        if (editTitle.text == null || editTitle.text.toString() == "" || editTitle.text.toString() == " ") {
                            Toast.makeText(this@MainActivity, "Entry not saved, missing title", Toast.LENGTH_SHORT).show()
                        } else {
                            // Persist your data easily
                            mRealmController!!.addBook(book)

                            adapter!!.notifyDataSetChanged()

                            // scroll the recycler view to bottom
                            recycler!!.scrollToPosition(RealmController.instance!!.books.size - 1)
                        }
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler!!.setHasFixedSize(true)

        // use a linear layout manager since the cards are vertically scrollable
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler!!.layoutManager = layoutManager

        // create an empty adapter and add it to the recycler view
        adapter = BookAdapter(this, RealmController.with(this).books, true)
        recycler!!.adapter = adapter
    }

    private fun setRealmData() {

        val books = ArrayList<Book>()

        var book = Book()
        book.id = (1 + System.currentTimeMillis()).toInt()
        book.author = "Reto Meier"
        book.title = "Android 4 Application Development"
        book.imageUrl = "https://raw.githubusercontent.com/maobui/AndroidStudio/master/Realm/asset/1.png"
        books.add(book)

        book = Book()
        book.id = (2 + System.currentTimeMillis()).toInt()
        book.author = "Itzik Ben-Gan"
        book.title = "Microsoft SQL Server 2012 T-SQL Fundamentals"
        book.imageUrl = "https://raw.githubusercontent.com/maobui/AndroidStudio/master/Realm/asset/2.png"
        books.add(book)

        book = Book()
        book.id = (3 + System.currentTimeMillis()).toInt()
        book.author = "Magnus Lie Hetland"
        book.title = "Beginning Python: From Novice To Professional Paperback"
        book.imageUrl = "https://raw.githubusercontent.com/maobui/AndroidStudio/master/Realm/asset/3.png"
        books.add(book)

        book = Book()
        book.id = (4 + System.currentTimeMillis()).toInt()
        book.author = "Chad Fowler"
        book.title = "The Passionate Programmer: Creating a Remarkable Career in Software Development"
        book.imageUrl = "https://raw.githubusercontent.com/maobui/AndroidStudio/master/Realm/asset/4.png"
        books.add(book)

        book = Book()
        book.id = (5 + System.currentTimeMillis()).toInt()
        book.author = "Yashavant Kanetkar"
        book.title = "Written Test Questions In C Programming"
        book.imageUrl = "https://raw.githubusercontent.com/maobui/AndroidStudio/master/Realm/asset/5.png"
        books.add(book)


        for (b in books) {
            // Persist your data easily
            mRealmController!!.addBook(b)
        }

        PrefManager.with(this).preLoad = true

    }

    companion object {

        private val TAG = MainActivity::class.java.simpleName
    }
}
