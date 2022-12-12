package com.example.cbcnewsandsports

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cbcnewsandsports.databinding.ActivityMainBinding
import com.example.cbcnewsandsports.model.NewsFeedModel
import com.example.cbcnewsandsports.network.NewsApi
import com.example.cbcnewsandsports.network.RetrofitHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum


class MainActivity : AppCompatActivity() {
    private var adapter: NewsFeedsAdapter? = null
    private var binding: ActivityMainBinding? = null
    var list = MutableLiveData<ArrayList<NewsFeedModel>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        noInternetConnection()

        adapter = NewsFeedsAdapter()
        binding?.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.adapter = adapter
            txtResetFilter.setOnClickListener {
                edSpinner.text = "Select News Type"
                if (adapter != null) {
                    adapter?.filter?.filter("")
                }
            }

            edSpinner.setOnClickListener {
                spinner.performClick()
            }
        }


        list.observe(this) {
            binding?.progressbar?.visibility = View.GONE
            adapter?.refreshList(it)
            adapter?.notifyDataSetChanged()
            setSpinner(it)
        }

    }

    fun setSpinner(list: ArrayList<NewsFeedModel>) {
        val ad: ArrayAdapter<NewsFeedModel> = ArrayAdapter<NewsFeedModel>(
            this,
            android.R.layout.simple_spinner_item,
            list
        )
        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        binding?.spinner?.adapter = ad
        binding?.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding?.edSpinner?.text = list[position].type
                adapter?.filter?.filter(list[position].type)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }


    fun noInternetConnection() {
        NoInternetDialogPendulum.Builder(
            this,
            lifecycle
        ).apply {
            dialogProperties.apply {
                connectionCallback = object : ConnectionCallback { // Optional
                    override fun hasActiveConnection(hasActiveConnection: Boolean) {

                        if (hasActiveConnection) {
                            val newsApi = RetrofitHelper.getInstance().create(NewsApi::class.java)
                            // launching a new coroutine
                            GlobalScope.launch {
                                val result = newsApi.getNewsFeedList()
                                result.body()?.let {
                                    list.postValue(it)
                                }

                            }
                        }
                    }
                }

                cancelable = false
                noInternetConnectionTitle = getString(R.string.InternetConnectionTitle)
                noInternetConnectionMessage =
                    getString(R.string.InternetConnectionMessage)
                showInternetOnButtons = true
                pleaseTurnOnText = getString(R.string.TurnOnText)
                wifiOnButtonText = getString(R.string.WifiButtonText)
                mobileDataOnButtonText = getString(R.string.MobileDataText)

                onAirplaneModeTitle = getString(R.string.AirplaneModeText)
                onAirplaneModeMessage = getString(R.string.AirplaneModeMessage)
                pleaseTurnOffText = getString(R.string.TurnOffText)
                airplaneModeOffButtonText = getString(R.string.AirplaneMode)
                showAirplaneModeOffButtons = true
            }
        }.build()
    }

}