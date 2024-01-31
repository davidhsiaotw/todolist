package com.example.todolist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.Manifest
import android.annotation.SuppressLint
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.todolist.R
import com.example.todolist.model.Task
import com.example.todolist.databinding.FragmentSecondBinding
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
 * @see <a href="https://www.youtube.com/watch?v=51fX94dU7Og&ab_channel=Stevdza-San"></a>
 */
class TaskEditDialogFragment : DialogFragment(), EasyPermissions.PermissionCallbacks {
    private val viewModel: TodoListViewModel by activityViewModels { TodoListViewModel.Factory }
    private lateinit var task: Task
    private lateinit var createDateText: TextInputEditText
    private lateinit var dueDateText: TextInputEditText
    private lateinit var locationText: TextInputEditText
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createDateText = view.findViewById(R.id.create_date_input)
        val today = Date()
        createDateText.setText(SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(today))
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
        val c = Calendar.getInstance()
        c.time = today
        c.add(Calendar.DATE, 1)
        val tomorrow = c.time
        dueDateText.setText(SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(tomorrow))
        dueDateText.setOnFocusChangeListener { view, hasFocus ->
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

        val selectedId = viewModel.selectedId.value
        locationText = view.findViewById(R.id.location_input)
        if (viewModel.selectedId.value != null)
            viewModel.getTaskById(selectedId!!).observe(this.viewLifecycleOwner) {
                task = it
                binding.apply {
                    titleInput.setText(task.title, TextView.BufferType.SPANNABLE)
                    descriptionInput.setText(task.description, TextView.BufferType.SPANNABLE)
                    createDateInput.setText(task.createDate, TextView.BufferType.SPANNABLE)
                    dueDateInput.setText(task.dueDate, TextView.BufferType.SPANNABLE)
                    if (task.location.isBlank()) {
                        if (hasLocationPermission()) {
                            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                                val location = task.result
                                if (location != null) {
                                    val ll = "${location.latitude}, ${location.longitude}"
                                    locationInput.setText(
                                        ll, TextView.BufferType.SPANNABLE
                                    )
                                } else {
                                    Toast.makeText(
                                        requireContext(), "NULL location", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        locationInput.setText("")
                    } else {
                        locationInput.setText(task.location, TextView.BufferType.SPANNABLE)
                    }
                }
            }
        else {
            // set default location for new task
            if (hasLocationPermission()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location != null) {
                        val ll = "${location.latitude}, ${location.longitude}"
                        locationText.setText(
                            ll, TextView.BufferType.SPANNABLE
                        )
                    } else {
                        Toast.makeText(
                            requireContext(), "NULL location", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        locationText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus)
                requestLocationPermission()
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
            binding.apply {
                viewModel.updateTask(
                    viewModel.selectedId.value!!, titleInput.text.toString(),
                    descriptionInput.text.toString(), createDateInput.text.toString(),
                    dueDateInput.text.toString(), locationInput.text.toString()
                )
            }

            Toast.makeText(
                requireContext(),
                "successfully update ${binding.titleInput.text}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            binding.apply {
                viewModel.addNewTask(
                    titleInput.text.toString(), descriptionInput.text.toString(),
                    createDateInput.text.toString(), dueDateInput.text.toString(),
                    locationInput.text.toString()
                )
            }

            Toast.makeText(
                requireContext(), "successfully add new task: ${binding.titleInput.text}",
                Toast.LENGTH_SHORT
            ).show()
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