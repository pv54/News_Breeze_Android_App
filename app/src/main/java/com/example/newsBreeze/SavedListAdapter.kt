import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsBreeze.Article
import com.example.newsBreeze.R
import com.squareup.picasso.Picasso

class ImageViewHolder(v:View):RecyclerView.ViewHolder(v){
    var thumbnail=v.findViewById<ImageView>(R.id.savedimage)
    var tittle=v.findViewById<TextView>(R.id.savedtitile)
    var date=v.findViewById<TextView>(R.id.saveddate)

}
class SavedListAdapter(private var newslist:List<Article>):RecyclerView.Adapter<ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.savebrowswlist,parent,false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val cdata=newslist[position]
        if(cdata.urlToImage==null){cdata.urlToImage="Invalid"}
        if(cdata.title==null){cdata.title="Title Not Available"}
        if(cdata.publishedAt==null){cdata.publishedAt="Date Not Available"}

        Picasso.get().load(cdata.urlToImage)
            .error(R.drawable.baseline_image_black_48)
            .placeholder(R.drawable.baseline_image_black_48).into(holder.thumbnail)
        holder.tittle.text=cdata.title
        holder.date.text=cdata.publishedAt
    }

    override fun getItemCount(): Int {
        return if(newslist.isNotEmpty()) newslist.size else 0
    }

    fun onChange( newlist:List<Article>){
        newslist=newlist
        notifyDataSetChanged()
    }

    fun getNews(position: Int): Article?{
        return if(newslist.isNotEmpty()) newslist[position] else null
    }
}