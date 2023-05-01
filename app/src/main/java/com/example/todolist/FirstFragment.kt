package com.example.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentFirstBinding
import com.example.viewmodels.TodoListViewModel
import com.example.viewmodels.TodoListViewModelFactory

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private val viewModel: TodoListViewModel by activityViewModels {
        TodoListViewModelFactory((activity?.application as TodoListApplication).database.taskDao())
    }
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskListAdapter {
            Toast.makeText(requireContext(), "Navigate to Editing Layout", Toast.LENGTH_SHORT)
                .show()
            // TODO: navigate to editing layout
        }
        binding.todoList.adapter = adapter

        viewModel.allTasks.observe(this.viewLifecycleOwner) { tasks ->
            tasks.let {
                adapter.submitList(it)
            }
        }

        binding.todoList.layoutManager = LinearLayoutManager(this.context)
        binding.todoList.addItemDecoration(
            DividerItemDecoration(this.context, 1)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}