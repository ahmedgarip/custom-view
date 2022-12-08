package com.udacity

import DownloadItem
import DownloadProgressLiveData
import android.animation.ObjectAnimator
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.LogPrinter
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch




class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var downloadlive: DownloadProgressLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        setContentView(binding.root)


        setSupportActionBar(toolbar)
        val factory = MainViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {

            val selectedOption: Int = binding.root.radioGroup!!.checkedRadioButtonId

            when(binding.root.radioGroup!!.checkedRadioButtonId){
                binding.root.radioButton.id -> download("https://github.com/bumptech/glide" , resources.getString(R.string.glide_option))
                binding.root.radioButton2.id -> download("https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip", resources.getString(R.string.loadapp_option))
                binding.root.radioButton3.id -> download("https://github.com/square/retrofit", resources.getString(R.string.retrofit_option))
                else -> Toast.makeText(this, "Please select the file to download", Toast.LENGTH_SHORT).show()
            }


        }


        viewModel.progress.observe(this, Observer<Int> { progress ->
            // Update the UI, in this case, a TextView.
            binding.root.custom_button.progress = progress
        })
        binding.root.custom_button.wasCompleted.observe(this, {
            Log.i("notification", "now you can really push")
        })


        createChannel(
            getString(R.string.project3_notification_channel_id),
            getString(R.string.project3_notification_channel_name)
        )


    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download(url : String, file:String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
//        Log.i("notification", downloadID.toString())
        if (cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
//            Log.i("Status", "$status")
        }
        viewModel.downloadId = downloadID
        downloadlive = DownloadProgressLiveData(application, downloadID)

        downloadlive.observe(this, {
//            Log.i("notification", it.status.toString())
            Log.i("downloadstatus", (it.status.toString()))
            if (it.status == 8) {
//                push sccsess
                val extras: Bundle = Bundle()
                extras.putInt("status", it.status)
                extras.putString("file",file)
                val notificationManager = ContextCompat.getSystemService(
                    application,
                    NotificationManager::class.java
                ) as NotificationManager
                notificationManager.sendNotification(
                    application.getString(R.string.notification_title),
                    application,
                    extras
                )

            } else if (it.status == 16) {
//                push fail
            }
        })


    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,

                NotificationManager.IMPORTANCE_LOW
            )


            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Description here"

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)



        }

        }


    }

