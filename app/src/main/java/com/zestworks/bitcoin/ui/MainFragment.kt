package com.zestworks.bitcoin.ui


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.snackbar.Snackbar
import com.zestworks.bitcoin.R
import com.zestworks.bitcoin.data.BitValue
import com.zestworks.bitcoin.di.BitCoinInjector
import com.zestworks.bitcoin.ui.model.*
import com.zestworks.utils.exhaustive
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

class MainFragment : Fragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: BitCoinViewModel

    init {
        BitCoinInjector.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChartView()
        setupObservers()

        durationToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            viewModel.onDurationChecked(checkedId, isChecked)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel =
            ViewModelProviders.of(activity!!, viewModelFactory)[BitCoinViewModel::class.java]
    }

    private fun setupObservers() {
        observeMarketPrice()
        observeDuration()
    }

    private fun setupChartView() {
        lineChart.run {

            xAxis.apply {
                this.setDrawGridLines(false)
                setLabelCount(5, false)
                position = XAxis.XAxisPosition.BOTTOM

                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val date = Date(value.toLong() * 1000)
                        return SimpleDateFormat("dd MMM YY", Locale.getDefault()).format(
                            date
                        )
                    }
                }
                textColor = Color.BLACK
            }

            axisRight.apply {
                this.isEnabled = false
                this.setDrawGridLines(false)
            }

            axisLeft.apply {
                setDrawGridLines(false)
                setDrawAxisLine(false)
            }

            legend.apply {
                isEnabled = false
            }

            description.isEnabled = false
        }
    }

    private fun observeDuration() {
        viewModel.duration.observe(viewLifecycleOwner, Observer {

            val checkedId = when (it) {
                OneWeek -> R.id.oneWeek
                OneMonth -> R.id.oneMonth
                SixMonths -> R.id.sixMonths
                OneYear -> R.id.oneYear
            }.exhaustive

            durationToggleGroup.check(checkedId)
        })
    }

    private fun observeMarketPrice() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            when (it) {
                Loading -> {

                }
                is Content -> {
                    updateContent(it.priceList)
                }
                is Error -> Snackbar.make(rootView, it.errorMessage, Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY") {
                        viewModel.retry()
                    }
                    .show()
            }.exhaustive
        })
    }

    private fun updateContent(priceList: List<BitValue>) {
        val entryList = priceList.map { bitValue ->
            Entry(
                bitValue.x.toFloat(),
                bitValue.y.toFloat()
            )
        }

        lineChart.run {
            val dataSet = LineDataSet(entryList, "").apply {
                setDrawCircles(false)
                lineWidth = 2f
                color = Color.RED
                setDrawValues(false)
                setDrawFilled(true)
                fillColor = Color.RED
            }
            data = LineData(dataSet)
            invalidate()
            animateX(500)
        }

        val currentPriceText = SpannableStringBuilder(
            getString(
                R.string.current_price_text,
                priceList.last().y.toFloat()
            )
        ).apply {
            setSpan(AbsoluteSizeSpan(32, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        currentPrice.text = currentPriceText

        val diffAmount =
            priceList[priceList.size - 1].y.minus(priceList[priceList.size - 2].y)
                .toFloat()

        val stringBuilder = SpannableStringBuilder("$${abs(diffAmount)}")
        if (diffAmount < 0f) {
            stringBuilder.replace(0, 0, "- ")
            stringBuilder.setSpan(
                ForegroundColorSpan(Color.RED),
                0,
                stringBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            stringBuilder.setSpan(
                ForegroundColorSpan(Color.GREEN),
                0,
                stringBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        differenceAmount.text = stringBuilder
    }

}
