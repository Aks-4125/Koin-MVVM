package com.aks4125.omdb.ui.list


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aks4125.omdb.R
import com.aks4125.omdb.adapter.MovieListAdapter
import com.aks4125.omdb.network.NetworkBound
import com.aks4125.omdb.ui.MainActivity
import com.aks4125.omdb.ui.list.MovieListFragmentArgs.Companion.fromBundle
import com.aks4125.omdb.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movie_list.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Movie Listing fragment
 */
class MovieListFragment : Fragment() {

    private val movieAdapter = MovieListAdapter(arrayListOf())
    private var queryText: String? = null
    private val listViewModel by viewModel<MovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        queryText = arguments?.let { fromBundle(it).movieName }
        initUI()
        Log.d(MainActivity.LOGGER, "view model hash-> " + listViewModel.hashCode())
        listViewModel.response.observe(this, Observer { resource ->

            resource?.let {
                when (it) {
                    is NetworkBound.Success -> {
                        progressBar.visibility = GONE
                        movieAdapter.updateUsers(it.data)
                        Log.d(MainActivity.LOGGER, "Api Success ")
                    }
                    is NetworkBound.Error -> {
                        Log.d(MainActivity.LOGGER, "Api Error ${it.error}")
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                        progressBar.visibility = GONE
                    }
                    is NetworkBound.Loading -> {
                        Log.d(MainActivity.LOGGER, "Api Loading ")
                        progressBar.visibility = VISIBLE
                    }
                }
            }
        })
        queryText?.let { listViewModel.fetchMovies(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listViewModel.response.removeObservers(this)
    }

    private fun initUI() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar?.title = queryText
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowHomeEnabled(true)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { (activity as AppCompatActivity).onBackPressed() }

        //recyclerView initialize
        rvMovies.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter
            movieAdapter.onItemClick = { movie, view ->
                val action =
                    MovieListFragmentDirections.actionMovieListFragmentToDetailFragment(movieId = movie.imdbID)
                view.findNavController().navigate(action)
            }
        }
    }
}
