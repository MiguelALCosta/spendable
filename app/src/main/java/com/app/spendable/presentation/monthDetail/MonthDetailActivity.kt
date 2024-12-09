package com.app.spendable.presentation.monthDetail

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.spendable.databinding.ActivityMonthDetailBinding
import com.app.spendable.presentation.common.BaseSpendableActivity
import com.app.spendable.presentation.common.CloseableView
import com.app.spendable.presentation.common.ExtraConstants
import com.app.spendable.presentation.subscriptionDetail.SubscriptionDetailActivity
import com.app.spendable.presentation.transactionDetail.TransactionDetailActivity
import com.app.spendable.presentation.wallet.SubscriptionListItemModel
import com.app.spendable.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import javax.inject.Inject

interface IMonthDetailView {
    fun updateView(models: List<MonthDetailAdapterModel>)
}

@AndroidEntryPoint
class MonthDetailActivity : BaseSpendableActivity(), CloseableView, IMonthDetailView {

    private lateinit var binding: ActivityMonthDetailBinding

    @Inject
    lateinit var presenter: IMonthDetailPresenter

    private var adapter: MonthDetailAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMonthDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.bind(this)

        val yearMonth = intent.extras?.getSerializable(ExtraConstants.MONTH) as? YearMonth ?: run {
            finish()
            return
        }

        setupTopBar(yearMonth)
        setupDetail(yearMonth)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbind()
    }

    override fun close() {
        finish()
    }

    private fun setupTopBar(yearMonth: YearMonth) {
        binding.topBar.icon.setOnClickListener {
            finish()
        }
        binding.topBar.title.text =
            DateUtils.Format.toFullMonthYear(yearMonth).replaceFirstChar(Char::uppercase)
    }

    private fun setupDetail(yearMonth: YearMonth) {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this@MonthDetailActivity.adapter =
                MonthDetailAdapter(context, emptyList(), ::onItemClick)
            adapter = this@MonthDetailActivity.adapter
        }
        presenter.getMonthDetail(yearMonth)
    }

    private fun onItemClick(model: Any) {
        when (model) {
            is MonthDetailAdapterModel.Transaction -> showTransactionDetail(model.id)
            is SubscriptionListItemModel -> showSubscriptionDetail(model.id)
            else -> {
                // do nothing
            }
        }
    }

    override fun updateView(models: List<MonthDetailAdapterModel>) {
        adapter?.updateModels(models)
    }

    private fun showTransactionDetail(id: Int) {
        val intent = Intent(this, TransactionDetailActivity::class.java)
        intent.putExtra(ExtraConstants.ID, id)
        startActivity(intent)
    }

    private fun showSubscriptionDetail(id: Int) {
        val intent = Intent(this, SubscriptionDetailActivity::class.java)
        intent.putExtra(ExtraConstants.ID, id)
        startActivity(intent)
    }

}
