package com.doubtnut.news.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.doubtnut.news.R
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private var newsUuid = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            newsUuid = DetailsFragmentArgs.fromBundle(it).newsUuid
        }
    }

}
