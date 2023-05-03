package com.example.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.database.task.Task
import com.example.todolist.databinding.FragmentSecondBinding
import com.example.viewmodels.TodoListViewModel
import com.example.viewmodels.TodoListViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : DialogFragment() {
    private val viewModel: TodoListViewModel by activityViewModels {
        TodoListViewModelFactory((activity?.application as TodoListApplication).database.taskDao())
    }
    private lateinit var task: Task
    private lateinit var createDateText: TextInputEditText
    private lateinit var dueDateText: TextInputEditText
    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createDateText = view.findViewById(R.id.create_date_input)
        createDateText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTitleText("Select Create Date").build()
                datePicker.addOnPositiveButtonClickListener {
                    val date = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(it))
                    createDateText.setText(date)
                }
                datePicker.show(parentFragmentManager, "CreateDatePicker")
            }
        }

        dueDateText = view.findViewById(R.id.due_date_input)
        dueDateText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTitleText("Select Due Date").build()
                datePicker.addOnPositiveButtonClickListener {
                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
                    dueDateText.setText(date)
                }
                datePicker.show(parentFragmentManager, "DueDatePicker")
            }
        }

        val selectedId = viewModel.selectedId.value
        if (viewModel.selectedId.value != null)
            viewModel.getTaskById(selectedId!!).observe(this.viewLifecycleOwner) {
                task = it
                binding.apply {
                    titleInput.setText(task.title, TextView.BufferType.SPANNABLE)
                    descriptionInput.setText(task.description, TextView.BufferType.SPANNABLE)
                    createDateInput.setText(task.createDate, TextView.BufferType.SPANNABLE)
                    dueDateInput.setText(task.dueDate, TextView.BufferType.SPANNABLE)
                    locationInput.setText(task.location, TextView.BufferType.SPANNABLE)
                }
            }

        view.findViewById<MaterialButton>(R.id.save_button).setOnClickListener {
            if (binding.titleInput.text!!.isNotBlank()) {
                saveTask()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Title MUST not be blank", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        view.findViewById<MaterialButton>(R.id.cancel_button).setOnClickListener { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setSelectedId(null)
        _binding = null
    }

    private fun saveTask() {
        if (viewModel.selectedId.value != null) {
            Toast.makeText(
                requireContext(), "successfully update ${binding.titleInput.text}",
                Toast.LENGTH_SHORT
            ).show()
            // TODO: update
        } else {
            Toast.makeText(
                requireContext(), "successfully add new task: ${binding.titleInput.text}",
                Toast.LENGTH_SHORT
            ).show()
            // TODO: add
        }
    }
}