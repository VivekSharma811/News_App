package com.doubtnut.news.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

import com.doubtnut.news.R
import com.doubtnut.news.databinding.FragmentDetailsBinding
import com.doubtnut.news.model.data.Article
import com.doubtnut.news.util.getProgressDrawable
import com.doubtnut.news.util.loadImage
import com.doubtnut.news.viewmodel.details.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private var newsUuid = 0
    private lateinit var viewModel : DetailsViewModel
    private lateinit var dataBinding : FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            newsUuid = DetailsFragmentArgs.fromBundle(it).newsUuid
        }

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        viewModel.refresh(newsUuid)

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.article.observe(viewLifecycleOwner, Observer { article->
            article?.let {
                dataBinding.article = article
            }
        })
    }
}