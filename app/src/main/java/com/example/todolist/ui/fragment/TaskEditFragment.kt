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
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.savedstate.SavedStateRegistryOwner
import com.example.todolist.R
import com.example.todolist.TodoListApplication
import com.example.todolist.databinding.FragmentEditTaskBinding
import com.example.todolist.model.Task
import com.example.todolist.viewmodels.TaskViewModel
import com.example.todolist.viewmodels.TaskViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.text.SimpleDateFormat
import java.util.*

/**
 * A [Fragment] for task editing.
 * @see <a href="https://www.youtube.com/watch?v=51fX94dU7Og&ab_channel=Stevdza-San">Easy Permissions</a>
 */
class TaskEditFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private val viewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory(
            activity?.application as TodoListApplication,
            owner = activity as SavedStateRegistryOwner
        )
    }
    private var task: Task? = null
    private lateinit var locationText: TextInputEditText
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var _binding: FragmentEditTaskBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "TaskEditFragment"

        /**
         * Create a new instance of [TaskEditFragment].
         * @param task the task to be edited
         * @return a new instance of [TaskEditFragment]
         */
        fun newInstance(task: Task): TaskEditFragment {
            val fragment = TaskEditFragment()
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
        view.background.alpha = 231

        // show old task data if user want to edit
        task?.apply {
            binding.let {
                it.titleInput.setText(title, TextView.BufferType.SPANNABLE)
                it.descriptionInput.setText(description, TextView.BufferType.SPANNABLE)
                it.startsDateButton.text = startDate
                it.endsDateButton.setText(dueDate, TextView.BufferType.SPANNABLE)
                // TODO: set start time and due time (12-hour format/24-hour format)
                it.locationInput.setText(location, TextView.BufferType.SPANNABLE)
            }
        }

        // variables for date and time
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val isSystem24Hour = is24HourFormat(this.requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        // set default start date for new task
        val startsDateButton = view.findViewById<Button>(R.id.starts_date_button)
        val startDateText: String = task?.startDate ?: dateFormat.format(Date())
        if (task == null) startsDateButton.text = startDateText
        // show date picker when user click on start date button
        startsDateButton.setOnClickListener {
            val date: Long = dateFormat.parse(startDateText)!!.time
            val oneDayInMillis: Long = 24 * 60 * 60 * 1000L // 24 hours in milliseconds

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(date ?: MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText("Select a Start Date").build()

            datePicker.addOnPositiveButtonClickListener {
                startsDateButton.text = dateFormat.format(Date(it))
            }
            datePicker.show(parentFragmentManager, "datePicker")
        }

        // set default start time for new task
//        val startsTimeButton = view.findViewById<Button>(R.id.starts_time_button)
//        val startTimeText: String?
//        if (task == null) {
//            startTimeText = if (isSystem24Hour)
//                SimpleDateFormat("HH:mm", Locale.getDefault()).format(today) else
//                SimpleDateFormat("hh:mm", Locale.getDefault()).format(today)
//            startsTimeButton.text = startTimeText
//        }
        // show time picker when user click on start time button
//        startsTimeButton.setOnClickListener {
//            val timePicker = MaterialTimePicker.Builder().setTimeFormat(clockFormat)
//                .setHour(startTimeText.substring(0, 1).toInt())
//                .setMinute(startTimeText.substring(3, 4).toInt())
//                .setTitleText("Select a Start Time").build()
//
//            timePicker.addOnPositiveButtonClickListener {
//                var hour = timePicker.hour
//                val minute = timePicker.minute
//                var midday: String? = null
//
//                // convert 24-hour format to 12-hour format if system is 12-hour format
//                if (!isSystem24Hour) {
//                    midday = if (hour < 12) "AM" else "PM"
//                    if (hour > 12) hour -= 12
//                }
//                // add 0 to hour and minute if they are units digit
//                val hourStr = if (timePicker.hour < 10) "0$hour" else hour.toString()
//                val minuteStr = if (minute < 10) "0${minute}" else minute.toString()
//
//                midday.let {
//                    startTimeText = if (it != null) {
//                        "$hourStr:$minuteStr $midday"
//                    } else {
//                        "$hourStr:$minuteStr"
//                    }
//                }
//            }
//            timePicker.show(parentFragmentManager, "timePicker")
//        }

        // set default end date for new task

        val endsDateButton = view.findViewById<Button>(R.id.ends_date_button)
        val endDateText: String = task?.dueDate ?: run {
            // read https://kotlinlang.org/docs/scope-functions.html#run
            val c = Calendar.getInstance()
            c.time = Date()
            c.add(Calendar.DATE, 1)
            dateFormat.format(c.time)
        }
        if (task == null) endsDateButton.text = endDateText
        // show date picker when user click on end date button
        endsDateButton.setOnClickListener {
            val date: Long = dateFormat.parse(endDateText)!!.time


            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(date ).setTitleText("Select a Due Date").build()

            datePicker.addOnPositiveButtonClickListener {
                endsDateButton.text = dateFormat.format(Date(it))
            }
            datePicker.show(parentFragmentManager, "datePicker")
        }

//        locationText.setOnFocusChangeListener { view, hasFocus ->
//            if (hasFocus)
//                requestLocationPermission()
//        }

        view.findViewById<MaterialButton>(R.id.save_button).setOnClickListener {
            if (binding.titleInput.text!!.isNotBlank()) {
                saveTask()
                activity?.onBackPressedDispatcher?.onBackPressed()
            } else {
                Toast.makeText(requireContext(), "Title MUST not be blank", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        view.findViewById<MaterialButton>(R.id.cancel_button)
            .setOnClickListener { activity?.onBackPressedDispatcher?.onBackPressed() }
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
                        startDate = startsDateButton.text.toString(),
                        dueDate = endsDateButton.text.toString(),
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
                        startDate = startsDateButton.text.toString(),
                        dueDate = endsDateButton.text.toString(),
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