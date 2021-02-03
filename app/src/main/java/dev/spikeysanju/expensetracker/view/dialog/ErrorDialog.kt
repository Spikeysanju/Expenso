package dev.spikeysanju.expensetracker.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.spikeysanju.expensetracker.databinding.ErrorDialogLayoutBinding

class ErrorDialog : BottomSheetDialogFragment() {
    private var _binding: ErrorDialogLayoutBinding? = null
    private val binding get() = _binding!!
    private val args: ErrorDialogArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ErrorDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            dialogTitle.text = args.title
            dialogMessage.text = args.message
            dialogButtonOk.setOnClickListener { dialog?.dismiss() }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
