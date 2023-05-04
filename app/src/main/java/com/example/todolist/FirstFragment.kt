package com.example.todolist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentFirstBinding
import com.example.viewmodels.TodoListViewModel
import com.example.viewmodels.TodoListViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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

        viewModel.getQuote(10, 50)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Daily Quote")
            .setMessage(viewModel.quote.value)
            .setPositiveButton("REGENERATE") { dialog, _ ->
                viewModel.getQuote(10, 50)
                (dialog as AlertDialog).setMessage(viewModel.quote.value)
            }.show()

        val adapter = TaskListAdapter(
            {
                viewModel.setSelectedId(it.id)
                (activity as MainActivity).showDialog()
            }, TaskCompleteListener {
                viewModel.completeTask(it)
//                val toastText = if (it.isCompleted) "${it.title} is completed!"
//                else "${it.title} is not completed!"
//                Toast.makeText(requireContext(), "${it.id}: $toastText", Toast.LENGTH_SHORT)
//                    .show()
            }, {
                Toast.makeText(requireContext(), "delete \"${it.title}\"", Toast.LENGTH_SHORT)
                    .show()
                viewModel.delete(it)
            }
        )
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

        val taskTouchHelper = ItemTouchHelper(TaskTouchSimpleCallBack {
            // show confirm dialog
            AlertDialog.Builder(requireContext()).setMessage(
                "You are about to permanently delete the task"
            ).setPositiveButton("Confirm") { _, _ ->
                adapter.deleteTask(it)
            }.setNegativeButton("Cancel") { _, _ ->
                adapter.notifyItemChanged(it)
            }.create().show()
        })
        taskTouchHelper.attachToRecyclerView(binding.todoList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}