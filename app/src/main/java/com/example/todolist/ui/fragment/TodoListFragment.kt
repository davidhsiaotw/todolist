package com.example.todolist.ui.fragmentimport android.os.Bundleimport android.util.Logimport android.view.*import androidx.fragment.app.Fragmentimport android.widget.Toastimport androidx.appcompat.app.AlertDialogimport androidx.fragment.app.FragmentTransactionimport androidx.fragment.app.activityViewModelsimport androidx.recyclerview.widget.DividerItemDecorationimport androidx.recyclerview.widget.ItemTouchHelperimport androidx.recyclerview.widget.LinearLayoutManagerimport androidx.savedstate.SavedStateRegistryOwnerimport com.example.todolist.TodoListApplicationimport com.example.todolist.ui.adapter.TaskListAdapterimport com.example.todolist.databinding.FragmentTodolistBindingimport com.example.todolist.model.Taskimport com.example.todolist.util.TaskTouchSimpleCallBackimport com.example.todolist.viewmodels.TaskViewModelimport com.example.todolist.viewmodels.TaskViewModelFactory/** * A [Fragment] for displaying a list of tasks. */class TodoListFragment : Fragment(), CallBackFunctions {    private val viewModel: TaskViewModel by activityViewModels {        TaskViewModelFactory(            activity?.application as TodoListApplication,            // reference: cast Activity to SavedStateRegistryOwner            // https://github.com/yatw/saveStateHandleDemo/blob/e9ae751346039c82ab454a46bcc65ee70791739c/app/src/main/java/com/example/savestatehandledemo/MyViewModel.kt#L14            owner = activity as SavedStateRegistryOwner        )    }    private var _binding: FragmentTodolistBinding? = null    // This property is only valid between onCreateView and onDestroyView.    private val binding get() = _binding!!    override fun onCreateView(        inflater: LayoutInflater, container: ViewGroup?,        savedInstanceState: Bundle?    ): View {        _binding = FragmentTodolistBinding.inflate(inflater, container, false)        return binding.root    }    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {        super.onViewCreated(view, savedInstanceState)        val adapter = TaskListAdapter(            onItemClicked = {                // Remove any currently showing dialog                val fragmentManager = requireActivity().supportFragmentManager                val transaction = fragmentManager.beginTransaction()                val prev = fragmentManager.findFragmentByTag("dialog")                if (prev != null) transaction.remove(prev)                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)                val dialog = TaskEditDialogFragment.newInstance(it)                transaction.addToBackStack(null).add(android.R.id.content, dialog).commit()            }, onItemChecked = {                viewModel.updateTask(it)            }, onItemSwiped = {                Toast.makeText(requireContext(), "delete \"${it.title}\"", Toast.LENGTH_SHORT)                    .show()                viewModel.delete(it)            }        )        binding.todoList.adapter = adapter        viewModel.allTasks.observe(viewLifecycleOwner) { tasks ->            tasks.let {                adapter.submitList(it)            }        }//        viewModel.getQuote(10, 50)//        MaterialAlertDialogBuilder(requireContext())//            .setTitle("Daily Quote")//            .setMessage(viewModel.quote.value)//            .setPositiveButton("REGENERATE") { dialog, _ ->//                viewModel.getQuote(10, 50)//                (dialog as AlertDialog).setMessage(viewModel.quote.value)//            }.show()        binding.todoList.layoutManager = LinearLayoutManager(this.context)        binding.todoList.addItemDecoration(            DividerItemDecoration(this.context, 1)        )        val taskTouchHelper = ItemTouchHelper(TaskTouchSimpleCallBack {            // show confirm dialog            AlertDialog.Builder(requireContext()).setMessage(                "You are about to permanently delete the task"            ).setPositiveButton("Confirm") { _, _ ->                adapter.deleteTask(it)            }.setNegativeButton("Cancel") { _, _ ->                adapter.notifyItemChanged(it)            }.create().show()        })        taskTouchHelper.attachToRecyclerView(binding.todoList)    }    override fun onDestroyView() {        super.onDestroyView()        _binding = null    }}