package com.agomezlucena.youcandoit.task_managament.ui


import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.agomezlucena.youcandoit.R
import com.agomezlucena.youcandoit.databinding.ActivityMainBinding
import com.agomezlucena.youcandoit.getEmergencyContact
import com.agomezlucena.youcandoit.launchCoroutine
import com.agomezlucena.youcandoit.task_managament.ScoreData
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.MainActivityViewModel
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task.ListTaskViewPagerFragmentViewModel
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task.ListTaskViewPagerFragmentViewModel.Position
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainActivityViewModel by viewModels<MainActivityViewModel>()
    private val listTaskViewPagerFragmentViewModel by viewModels<ListTaskViewPagerFragmentViewModel>()

    private lateinit var scoreLiveData: LiveData<ScoreData>
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.actionFab.setOnClickListener {
            navController.navigate(R.id.action_listTasksFragment_to_createTaskFragment)
        }

        navController = findNavController(R.id.navHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment, R.id.listStatistics, R.id.updateEmergencyContactFragment -> {
                    binding.cardViewTitle.visibility = View.GONE
                    binding.actionFab.hide()
                }
                R.id.createTaskFragment -> {
                    binding.cardViewTitle.visibility = View.GONE
                    binding.actionFab.hide()
                    binding.bottomAppBar.performHide()
                }
                R.id.configurationFragment -> {
                    binding.cardViewTitle.visibility = View.GONE
                    binding.actionFab.hide()
                    binding.actionFab.setImageResource(R.drawable.ic_baseline_call_24)
                    binding.actionFab.background?.setTint(getColor(R.color.chart_red))
                    binding.actionFab.setOnClickListener {
                        makeCall()
                    }
                    launchCoroutine(Dispatchers.IO){
                        delay(300)
                        launchCoroutine(Dispatchers.Main){
                            binding.actionFab.show()
                        }
                    }
                }
                else -> {
                    binding.cardViewTitle.visibility = View.VISIBLE
                    binding.actionFab.setImageResource(R.drawable.ic_baseline_note_add_24)
                    binding.actionFab.background?.setTint(getColor(R.color.blue_700))
                    binding.actionFab.setOnClickListener {
                        navController.navigate(R.id.action_listTasksFragment_to_createTaskFragment)
                    }
                    binding.actionFab.show()
                    binding.bottomAppBar.performShow()
                }
            }
        }

        listTaskViewPagerFragmentViewModel.position().observe(this) {
            mainActivityViewModel.updatePosition(it)
        }
        mainActivityViewModel.position.observe(this) {
            onChangePosition(it)
        }
    }

    @AfterPermissionGranted(101)
    private fun makeCall() {
        val contact = getEmergencyContact(this)
        if(!contact.isValid()){
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.contact_warning)
                .show()
        } else {
            if(EasyPermissions.hasPermissions(this,Manifest.permission.CALL_PHONE)){
                Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse("tel: ${contact.phoneNumber}")
                    startActivity(this)
                }
            } else{
                EasyPermissions.requestPermissions(this,"we need permission to make call in a emergency case",101,Manifest.permission.CALL_PHONE)
            }
        }
    }

    private fun onChangePosition(position: Position) {
        if (this::scoreLiveData.isInitialized) {
            scoreLiveData.removeObservers(this)
        }

        animateTitleCard(position)

        scoreLiveData = mainActivityViewModel.getScore(position)

        scoreLiveData.observe(this) {
            Timber.d(it.toString())
            binding.cardViewTitleTv.text = getTitleCardText(position.newPosition, it)
        }

    }

    private fun getTitleCardText(newPosition: Int, scoreData: ScoreData) =
            when (newPosition) {
                0 -> getString(R.string.today_score, scoreData.accomplishedScore, scoreData.totalScore)
                1 -> getString(R.string.this_week_score, scoreData.accomplishedScore, scoreData.totalScore)
                else -> getString(R.string.month_score, scoreData.accomplishedScore, scoreData.totalScore)
            }


    private fun animateTitleCard(position: Position) {
        val animationToShow = if (position.fromLeft()) {
            AnimationUtils.loadAnimation(this, R.anim.from_right)
        } else {
            AnimationUtils.loadAnimation(this, R.anim.from_left)
        }

        val fadeoutDuration = resources.getInteger(R.integer.fadeOutDuration).toLong()
        binding.cardViewTitle.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadeout))

        binding.cardViewTitleIv.visibility = View.GONE
        binding.cardViewTitleTv.visibility = View.GONE

        binding.actionFab.hide()

        launchCoroutine(Dispatchers.IO) {
            delay(fadeoutDuration)
            launchCoroutine(Dispatchers.Main) {
                binding.cardViewTitle.startAnimation(animationToShow)
                binding.cardViewTitleIv.visibility = View.VISIBLE
                binding.cardViewTitleTv.visibility = View.VISIBLE
                binding.actionFab.show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }
}