package com.me.bui.books.adapter

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.me.bui.books.helper.PrefManager
import com.me.bui.books.model.Book
import com.me.bui.books.realm.RealmController
import com.me.bui.books.R
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults


/**
 * Created by mao.buidinh on 7/17/2017.
 */

class BookAdapter(val context: Context, bookResults: RealmResults<Book>?, autoUpdate: Boolean) : RealmRecyclerViewAdapter<Book, BookAdapter.CardViewHolder>(bookResults , autoUpdate) {

    private var mRealmController: RealmController? = null
    private var inflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        mRealmController = RealmController.instance

        // get the article
        val book = getItem(position)
        // cast the generic view holder to our specific one

        // set the title and the snippet
        holder.textTitle.text = book!!.title
        holder.textAuthor.text = book.author
        holder.textDescription.text = book.description

        // load the background image
        if (book.imageUrl != null) {
            Glide.with(context)
                    .load(book.imageUrl!!.replace("https", "http"))
                    .asBitmap()
                    .fitCenter()
                    .into(holder.imageBackground)
        }

        //remove single match from realm
        holder.card.setOnLongClickListener {
            // Get the book title to show it in toast message
            val b = mRealmController!!.getBookLocation(position)
            val title = b!!.title

            // remove single match
            mRealmController!!.removeBookLocation(position)

            if (!mRealmController!!.hasBooks()) {
                PrefManager.with(context).preLoad = false
            }

            notifyDataSetChanged()

            Toast.makeText(context, title!! + " is removed from Realm", Toast.LENGTH_SHORT).show()
            false
        }

        //update single match from realm
        holder.card.setOnClickListener {
            inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val content = inflater!!.inflate(R.layout.edit_item, null)
            val editTitle = content.findViewById<View>(R.id.title) as EditText
            val editAuthor = content.findViewById<View>(R.id.author) as EditText
            val editThumbnail = content.findViewById<View>(R.id.thumbnail) as EditText

            editTitle.setText(book.title)
            editAuthor.setText(book.author)
            editThumbnail.setText(book.imageUrl)

            val builder = AlertDialog.Builder(context)
            builder.setView(content)
                    .setTitle("Edit Book")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        mRealmController!!.updateBookLocation(position, editAuthor.text.toString(), editTitle.text.toString(), editThumbnail.text.toString())

                        notifyDataSetChanged()
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
            val dialog = builder.create()
            dialog.show()
        }
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var card: CardView
        var textTitle: TextView
        var textAuthor: TextView
        var textDescription: TextView
        var imageBackground: ImageView

        init {

            card = itemView.findViewById<View>(R.id.card_book) as CardView
            textTitle = itemView.findViewById<View>(R.id.text_books_title) as TextView
            textAuthor = itemView.findViewById<View>(R.id.text_books_author) as TextView
            textDescription = itemView.findViewById<View>(R.id.text_books_description) as TextView
            imageBackground = itemView.findViewById<View>(R.id.image_background) as ImageView
        }// standard view holder pattern with Butterknife view injection
    }
}
