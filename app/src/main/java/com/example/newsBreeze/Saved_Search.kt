package com.example.newsBreeze

import SavedListAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList


@Suppress("UNCHECKED_CAST")
class Saved_Search : AppCompatActivity(), RecyclerItemClickListner.OnRecyclerClickListner {
    val copynewslist = ArrayList<Article>()
    var originalnewslist = ArrayList<Article>()
    var savedListAdapter = SavedListAdapter(ArrayList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_search)
        val toolbar=findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        toolbar.setNavigationIcon(R.drawable.back_black)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val savedrecycleview = findViewById<RecyclerView>(R.id.savedrecycleview)

        savedrecycleview.layoutManager = LinearLayoutManager(this)
        savedrecycleview.addOnItemTouchListener(
            RecyclerItemClickListner(
                this,
                savedrecycleview,
                this
            )
        )
        savedrecycleview.adapter = savedListAdapter

        val savedlist = intent.getSerializableExtra("SAVED_LIST") as ArrayList<Article>

        originalnewslist.addAll(savedlist)
        copynewslist.addAll(originalnewslist)
        savedListAdapter.onChange(copynewslist)

        search(findViewById(R.id.Search_bar_saved))
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun search(searchbar: SearchView) {
        searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    copynewslist.clear()
                    val searchText = newText.lowercase(Locale.getDefault())
                    originalnewslist.forEach {
                        if (it.title?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true
                        ) {
                            copynewslist.add(it)
                        }
                    }
                    savedListAdapter.onChange(copynewslist)
                } else {
                    savedListAdapter.onChange(originalnewslist)
                }
                return true
            }
        })
    }

    override fun onclick(v: View, position: Int) {
        Toast.makeText(this, "Click $position", Toast.LENGTH_SHORT).show()
        val news = savedListAdapter.getNews(position)
       
        if (news != null) {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("PHOTO_TRANSFER", news)
            startActivity(intent)
        }
    }

    override fun onLongClick(v: View, position: Int) {

    }
}
