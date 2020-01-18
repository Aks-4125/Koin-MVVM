package com.aks4125.omdb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aks4125.omdb.R
import com.aks4125.omdb.model.Search
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_movie.view.*
import java.util.*

class MovieListAdapter(var movieList: ArrayList<Search>) :
    RecyclerView.Adapter<MovieListAdapter.MovieHolder>() {

    var onItemClick: ((item: Search, view: View) -> Unit)? = null


    fun updateUsers(newUsers: List<Search>) {
        movieList.clear()
        movieList.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MovieHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_movie, parent, false)
    )

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bind(movieList[position])
    }

    inner class MovieHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            view.movieItemView.setOnClickListener(this)
        }

        private val poster = view.movieImage
        private val name = view.movieName
        private val year = view.tvReleaseYear

        fun bind(movie: Search) {
            name.text = movie.title
            Glide.with(view)
                .load(movie.poster)
                .centerCrop()
                .placeholder(R.drawable.ic_broken_image_black_24dp)
                .into(poster)
            year.text = movie.year.toString()
        }

        override fun onClick(v: View) {
            onItemClick?.invoke(movieList[adapterPosition], v)
        }
    }

}
