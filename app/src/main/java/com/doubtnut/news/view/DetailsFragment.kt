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
import com.doubtnut.news.viewmodel.details.DetailsViewModelFactory
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class DetailsFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory : DetailsViewModelFactory by instance()

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

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(DetailsViewModel::class.java)

        bindUi()
    }

    private fun bindUi() = launch {
        viewModel.refresh(newsUuid).observe(viewLifecycleOwner, Observer {
            dataBinding.article = it
        })
    }
}