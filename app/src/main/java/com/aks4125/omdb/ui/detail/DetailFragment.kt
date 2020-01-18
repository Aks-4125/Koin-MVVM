package com.aks4125.omdb.ui.detail


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aks4125.omdb.R
import com.aks4125.omdb.model.Search
import com.aks4125.omdb.network.NetworkBound
import com.aks4125.omdb.ui.MainActivity.Companion.LOGGER
import com.aks4125.omdb.ui.detail.DetailFragmentArgs.Companion.fromBundle
import com.aks4125.omdb.viewmodel.MovieViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.movieName
import kotlinx.android.synthetic.main.list_item_movie.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private var movieId: String? = null
    private val viewModel: MovieViewModel by sharedViewModel(from = {
        findNavController().getViewModelStoreOwner(
            R.id.movie_graph
        )
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        movieId = arguments?.let { fromBundle(it).movieId }
        observeViewModel()

        movieId?.let { viewModel.fetchMovieDetail(it) }
    }

    /**
     * ViewModel observer
     */
    private fun observeViewModel() {
        viewModel.response.observe(this, Observer { resource ->
            resource?.let {
                when (it) {
                    is NetworkBound.Success -> {
                        progressBar.visibility = GONE
                        Log.d(LOGGER, "Api Success ")
                        updateUI(it.data[0])
                    }
                    is NetworkBound.Error -> {
                        Log.d(LOGGER, "Api Error ${it.error}")
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                        //   progressBar.visibility = GONE
                    }
                    is NetworkBound.Loading -> {
                        Log.d(LOGGER, "Api Loading ")
                        //  progressBar.visibility = VISIBLE
                    }
                }
            }
        })

    }

    private fun updateUI(movie: Search) {
        if(!movie.runtime.contains("N/A")) {
            val duration = movie.runtime.split(" ")[0].toInt()
            val hours: Int = duration / 60 //since both are ints, you get an int
            val minutes: Int = duration % 60
            tvDuration.text = "$hours h $minutes m"
        }else
            tvDuration.text = ""
        Glide.with(this).load(movie.poster).centerCrop().into(imgPoster)
        movieName.text = movie.title
        tvRatings.text = movie.ratings[0].value
        tvDirector.text = movie.director
        tvDate.text = movie.released
        tvStory.text = movie.plot
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.response.removeObservers(this)
    }

}
