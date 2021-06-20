package com.example.newsBreeze

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.util.regex.Pattern

class ImageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var thumbnail = v.findViewById<ImageView>(R.id.thumbnail)
    var tittle = v.findViewById<TextView>(R.id.tittle)
    var date = v.findViewById<TextView>(R.id.date)
    var description = v.findViewById<TextView>(R.id.description)
    val savebutton = v.findViewById<Button>(R.id.savebutton)
    val readbutton = v.findViewById<Button>(R.id.readbutton)
    val indicator = v.findViewById<ImageView>(R.id.indicator)

}

class RecyclerViewAdapter(private var newslist: List<Article>, private val context: Context) :
    RecyclerView.Adapter<ImageViewHolder>() {
    var savedList = ArrayList<Article>()
    var beforesearch=ArrayList<Article>()
   var checklist: Array<String>?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browselist, parent, false)

        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val cdata = newslist[position]
        Picasso.get().load(cdata.urlToImage)
            .error(R.drawable.baseline_image_black_48)
            .placeholder(R.drawable.baseline_image_black_48).into(holder.thumbnail)
        holder.tittle.text = cdata.title
        val fulldate = Pattern.compile("T").split(cdata.publishedAt.toString())
        holder.date.text = fulldate[0]
        holder.description.text = cdata.description


            if(checklist?.get(position) =="false"){holder.indicator.setImageResource(R.drawable.outline_bookmark_24)

                holder.indicator.setBackgroundResource(R.drawable.rounded)
                holder.savebutton.setText(R.string.saved)
            }

      holder.savebutton.setOnClickListener {

              if (checklist?.get(position) == "true") {
                  savedList.add(cdata)
                  holder.savebutton.setText(R.string.saved)
                  holder.indicator.setImageResource(R.drawable.outline_bookmark_24)
                  holder.indicator.setBackgroundResource(R.drawable.rounded)
                  checklist!![position]= "false"

              }


        }
        holder.readbutton.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("PHOTO_TRANSFER", cdata)
            intent.putExtra("position",position)
            intent.putExtra("save",holder.savebutton.text)
            Log.d("read","${holder.readbutton.text}")
            context.startActivity(intent)
            Log.d("tager","backed")

        }
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun getItemCount(): Int {
        return if (newslist.isNotEmpty()) newslist.size else 0
    }

    fun onChange(newList: List<Article>, Checklist:Array<String>, check:Int) {
        if (check==1){ beforesearch = newList as ArrayList<Article> }
        newslist = newList
        notifyDataSetChanged()
        checklist=Checklist

    }
    fun onChange2(NewList: List<Article>){
        newslist = NewList
        notifyDataSetChanged()
    }

    fun setNews(prevList:ArrayList<Article>){
       newslist=prevList
    }
    fun getUnTouchList():ArrayList<Article>{
        return beforesearch
    }
    fun getNewsList(): ArrayList<Article> {
           return newslist as ArrayList<Article>

    }

    fun getSavedlist(): ArrayList<Article> {
        return savedList

    }
    fun getCheckList():Array<String>{
        return checklist as Array<String>
    }
}