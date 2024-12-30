package com.app.spendable.presentation.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.spendable.databinding.FragmentCalendarBinding
import com.app.spendable.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

interface ICalendarView {
    fun updateView(models: List<CalendarAdapterModel>)
}

@AndroidEntryPoint
class CalendarFragment : Fragment(), ICalendarView {

    @Inject
    lateinit var presenter: ICalendarPresenter

    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var adapter: CalendarAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        presenter.bind(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this@CalendarFragment.adapter = CalendarAdapter(context, emptyList(), ::onItemClick)
            adapter = this@CalendarFragment.adapter
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.refreshCalendarInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbind()
    }

    private fun onItemClick(model: CalendarAdapterModel) {
        when (model) {
            is CalendarAdapterModel.MonthCard -> (activity as? MainActivity)?.showMonthDetail(model.month)
            else -> {
                // nothing
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun updateView(models: List<CalendarAdapterModel>) {
        adapter?.updateModels(models)
    }
}