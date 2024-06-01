package com.example.myapplication.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.data.dto.EventHeader
import com.example.myapplication.R
import com.example.myapplication.databinding.FrgmentEventDescriptionBinding
import com.example.myapplication.viewmodel.EventViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventDescriptionFragment : Fragment() {
    private var _binding: FrgmentEventDescriptionBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventViewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrgmentEventDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventViewModel = ViewModelProvider(requireActivity()).get(EventViewModel::class.java)

        val event = arguments?.getSerializable("selectedEvent") as? EventHeader

        event?.let { selectedEvent ->
            Log.d("DescriptionFragment", "Selected Event: ${selectedEvent.title}")
            binding.textView1.text = selectedEvent.streetAddress
            binding.textView2.text = selectedEvent.title
            binding.textView3.text = selectedEvent.startAt
            binding.textView4.text = selectedEvent.endAt
            binding.textView5.text = selectedEvent.managerName
            binding.textView7.text = selectedEvent.content
            binding.textView8.text = selectedEvent.detailAddress
            binding.textView9.text = selectedEvent.zipCode

            // 이미지 로딩
            Glide.with(requireContext())
                .load(selectedEvent.imageUrl)
                .into(binding.imageView2)

            // Set up the apply button to navigate with the event data
            binding.applybtn.setOnClickListener {
                val bundle = Bundle().apply {
                    putSerializable("selectedEvent", selectedEvent)
                }
                findNavController().navigate(R.id.action_eventDescriptionFragment_to_eventApplicationFragment, bundle)
            }
        } ?: run {
            Log.d("DescriptionFragment", "No selected event")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
