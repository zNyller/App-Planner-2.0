package com.nyller.android.mach4.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nyller.android.mach4.R
import com.nyller.android.mach4.application.MyApplication
import com.nyller.android.mach4.database.models.Habit
import com.nyller.android.mach4.databinding.FragmentHomeBinding
import com.nyller.android.mach4.ui.adapters.HabitAdapter
import com.nyller.android.mach4.ui.viewmodel.HabitViewModel
import java.io.Serializable

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    private val mHabitViewModel: HabitViewModel by viewModels {
        HabitViewModel.HabitViewModelFactory((activity?.application as MyApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.habitViewModel = mHabitViewModel

        binding.fabNewHabit.setOnClickListener {
            newHabit()
        }

        val adapter = HabitAdapter(HabitAdapter.HabitClickListener { habit ->
            val action = HomeFragmentDirections.actionHomeFragmentToEditHabitFragment(habit)
            findNavController().navigate(action)
        })

        binding.rvHabits.adapter = adapter

        mHabitViewModel.allHabits.observe(viewLifecycleOwner) { habits ->
            adapter.addHeaderAndSubmitList(habits)
        }

        setFragmentResultListener("request_key") { _, bundle ->
            val result = bundle.getSerializable("newHabit") as Habit
            mHabitViewModel.insert(result)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

    }

    private fun newHabit() {
        findNavController().navigate(R.id.action_homeFragment_to_newHabitFragment)
    }


}