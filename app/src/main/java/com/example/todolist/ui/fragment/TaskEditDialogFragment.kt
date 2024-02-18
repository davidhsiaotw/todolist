package com.example.todolist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.todolist.R
import com.example.todolist.databinding.FragmentEditTaskBinding
import com.example.todolist.model.Task
import com.example.todolist.viewmodels.TodoListViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [DialogFragment] subclass as a task edit UI.
 * @see <a href="https://www.youtube.com/watch?v=51fX94dU7Og&ab_channel=Stevdza-San">Easy Permissions</a>
 */
class TaskEditDialogFragment : DialogFragment(), EasyPermissions.PermissionCallbacks {
    private val viewModel: TodoListViewModel by activityViewModels { TodoListViewModel.Factory }
    private var task: Task? = null
    private lateinit var createDateText: TextInputEditText
    private lateinit var dueDateText: TextInputEditText
    private lateinit var locationText: TextInputEditText
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var _binding: FragmentEditTaskBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance(task: Task): TaskEditDialogFragment {
            val fragment = TaskEditDialogFragment()
            val bundle = Bundle()
            bundle.putParcelable("task", task)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            task = arguments?.getParcelable("task", Task::class.java)
        } else {
            arguments?.classLoader = Task::class.java.classLoader
            @Suppress("DEPRECATION")
            task = arguments?.getParcelable("task")
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // do not set background color in xml, because it makes animation not completed
        view.setBackgroundColor(resources.getColor(R.color.md_theme_surface, null))
        view.background.alpha = 204

        // set default create date for new task
        createDateText = view.findViewById(R.id.create_date_input)
        val today = Date()
        createDateText.setText(SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(today))
        // show date picker when user click on create date input
        createDateText.setOnFocusChangeListener { _, hasFocus ->
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

        // set default due date for new task
        dueDateText = view.findViewById(R.id.due_date_input)
        val c = Calendar.getInstance()
        c.time = today
        c.add(Calendar.DATE, 1)
        val tomorrow = c.time
        dueDateText.setText(SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(tomorrow))
        // show date picker when user click on due date input
        dueDateText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTitleText("Select Due Date").build()
                datePicker.addOnPositiveButtonClickListener {
                    val date = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(it))
                    dueDateText.setText(date)
                }
                datePicker.show(parentFragmentManager, "DueDatePicker")
            }
        }

        // show old task data if user want to edit
        task?.apply {
            binding.let {
                it.titleInput.setText(title, TextView.BufferType.SPANNABLE)
                it.descriptionInput.setText(description, TextView.BufferType.SPANNABLE)
                it.createDateInput.setText(createDate, TextView.BufferType.SPANNABLE)
                it.dueDateInput.setText(dueDate, TextView.BufferType.SPANNABLE)
                it.locationInput.setText(location, TextView.BufferType.SPANNABLE)
            }
        }

//        locationText.setOnFocusChangeListener { view, hasFocus ->
//            if (hasFocus)
//                requestLocationPermission()
//        }

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
        _binding = null
    }

    /**
     * Save task to database
     */
    private fun saveTask() {
        // if task is not null, update task; otherwise, add new task
        if (task != null) {
            binding.apply {
                viewModel.updateTask(
                    task!!.copy(
                        title = titleInput.text.toString(),
                        description = descriptionInput.text.toString(),
                        createDate = createDateInput.text.toString(),
                        dueDate = dueDateInput.text.toString(),
                        location = locationInput.text.toString()
                    )
                )
            }
            Toast.makeText(
                requireContext(),
                "successfully update ${task!!.title}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            binding.apply {
                viewModel.addNewTask(
                    Task(
                        title = titleInput.text.toString(),
                        description = descriptionInput.text.toString(),
                        createDate = createDateInput.text.toString(),
                        dueDate = dueDateInput.text.toString(),
                        location = locationInput.text.toString()
                    )
                )
                Toast.makeText(
                    requireContext(), "successfully add new task: ${titleInput.text}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun hasLocationPermission() = EasyPermissions.hasPermissions(
        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
    ) || EasyPermissions.hasPermissions(
        requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
    }

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this, "This application cannot work normally without Location Permission.",
            0, Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}