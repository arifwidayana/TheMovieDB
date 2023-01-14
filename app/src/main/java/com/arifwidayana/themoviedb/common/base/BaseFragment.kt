package com.arifwidayana.themoviedb.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.IllegalArgumentException
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>(
    private val bindingFactory: (LayoutInflater) -> VB
) : Fragment(), BaseContract {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    @Inject
    protected lateinit var viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingFactory.invoke(inflater)
        when (_binding) {
            null -> throw IllegalArgumentException("Binding cannot be null")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun initView()
    abstract fun observeData()

    override fun showContent(isVisible: Boolean) {}
    override fun showEmptyContent(isVisible: Boolean) {}
    override fun showErrorContent(isVisible: Boolean) {}
    override fun showLoading(isVisible: Boolean) {}

    override fun showMessageToast(isEnabled: Boolean, message: String?) {
        when {
            isEnabled -> {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun showMessageSnackBar(isEnabled: Boolean, message: String?) {
        when {
            isEnabled -> {
                Snackbar.make(binding.root, message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun moveNav() {
        findNavController().popBackStack()
    }

    override fun moveNav(navUp: Int) {
        findNavController().navigate(navUp)
    }

    override fun moveNav(direction: NavDirections) {
        findNavController().navigate(direction)
    }
}