package com.example.newsBreeze

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

private var flag = 0
class MainActivity : AppCompatActivity() {

    var recyclerviewadapter = RecyclerViewAdapter(ArrayList(), this)
    private var cachelistnews: ArrayList<Article>? = null
    val copynewslist = ArrayList<Article>()
    var originalnewslist = ArrayList<Article>()
    private lateinit var popupMenu:PopupMenu
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("oncreate", "$flag")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        toolbar.setNavigationIcon(R.drawable.back_black)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val searchbar = findViewById<SearchView>(R.id.Search_bar_saved)
        val recycleview = findViewById<RecyclerView>(R.id.savedrecycleview)
        val savelistbutton = findViewById<ImageButton>(R.id.indicator)

        recycleview.layoutManager = LinearLayoutManager(this)
        recycleview.adapter = recyclerviewadapter

        getNews(NewsService.newsinstance.getHeadlines("in", 1))
        search(searchbar)

        savelistbutton.setOnClickListener {
            val intent = Intent(this@MainActivity, Saved_Search::class.java)
            intent.putExtra("SAVED_LIST", recyclerviewadapter.getSavedlist())
            startActivity(intent)
        }
        val filter = findViewById<ImageButton>(R.id.filter)
        popupMenu = PopupMenu(this, filter)
        popupMenu.inflate(R.menu.menu_main)
        filter.setOnClickListener {
            menuclicklistner(popupMenu)
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("key", recyclerviewadapter.getSavedlist())
        outState.putSerializable("serial", recyclerviewadapter.getNewsList())
        outState.putSerializable("untouch", recyclerviewadapter.getUnTouchList())
        outState.putStringArray("checklist", recyclerviewadapter.getCheckList())
        if(popupMenu.menu.findItem(R.id.IN).isChecked)outState.putInt("checker",1)
        else outState.putInt("checker",2)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState.getInt("checker")==1){popupMenu.menu.findItem(R.id.IN).isChecked=true} else popupMenu.menu.findItem(R.id.US).isChecked=true

        val savedlist = savedInstanceState.getSerializable("key") as ArrayList<Article>
        recyclerviewadapter.savedList = savedlist
        cachelistnews = savedInstanceState.getSerializable("serial") as ArrayList<Article>
        originalnewslist = savedInstanceState.getSerializable("untouch") as ArrayList<Article>
        val checklist = savedInstanceState.getStringArray("checklist") as Array<String>
        recyclerviewadapter.beforesearch = originalnewslist
        recyclerviewadapter.onChange(cachelistnews!!, checklist, 0)
        flag = 1

    }


    @Suppress("UNCHECKED_CAST")
    fun getNews(news:Call<News>) {
        if (flag == 0) {
            flag = 1
            news.enqueue(object : Callback<News> {
                override fun onResponse(call: Call<News>, response: Response<News>) {
                    val newsA = response.body()
                    if (newsA != null) {
                        newsA.articles = NullCheck.check(newsA.articles as ArrayList<Article>)
                        originalnewslist = newsA.articles as ArrayList<Article>
                        val savedlist = recyclerviewadapter.getSavedlist()
                        val array = makeBooleanarray(newsA.articles.size)
                        recyclerviewadapter.onChange(newsA.articles, array, 1)
                        recyclerviewadapter.savedList = savedlist
                    }

                    Toast.makeText(this@MainActivity, "completed", Toast.LENGTH_SHORT).show()

                }

                override fun onFailure(call: Call<News>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "failed", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun makeBooleanarray(size: Int): Array<String> {
        val array = arrayOfNulls<String>(size)
        for (i in array.indices) {
            array[i] = "true"
        }
        return array as Array<String>
    }


    private fun search(searchbar: SearchView) {
        searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    copynewslist.clear()
                    val searchText = newText.lowercase(Locale.getDefault())
                    originalnewslist.forEach {
                        if ((it.title?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true) || (it.description?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true)
                        ) {
                            copynewslist.add(it)
                        }
                    }
                    recyclerviewadapter.onChange2(copynewslist)
                } else {
                    recyclerviewadapter.onChange2(
                        originalnewslist
                    )
                }
                return true
            }
        })
    }


    private fun menuclicklistner(popupMenu: PopupMenu) {
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.IN,R.id.US -> {
                        if (!item.isChecked) {
                            flag=0
                            var country ="in"
                            when(item.itemId) {
                              R.id.IN-> country="in"
                              R.id.US->country="us"
                                        }
                            val news: Call<News> = NewsService.newsinstance.getHeadlines( country, 1)
                            getNews(news)
                            item.isChecked = true
                            flag=1

                        }
                        return true
                    }
                }
                return false
            }
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupMenu.gravity = Gravity.END
        }
        popupMenu.show()

    }

}