package com.alfredobejarano.endorsements

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.alfredobejarano.endorsements.databinding.ActivityEndorsementsBinding
import com.alfredobejarano.endorsements.viewmodel.ProfileViewModel
import com.alfredobejarano.endorsements.viewmodel.StorageViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_endorsements.*

/**
 * Activity for the app that will serve as the lifecycle
 * owner for the fragments CareerProfileViewModel.
 *
 * @author Alfredo Bejarano
 * @since Jul 26th, 2018 - 02:05 PM
 */
class EndorsementsActivity : AppCompatActivity() {
    companion object {
        const val FRAGMENT_TAG = "navigationFragment"
    }

    private lateinit var mStorageViewModel: StorageViewModel
    private lateinit var mProfileViewModel: ProfileViewModel
    private lateinit var mBinding: ActivityEndorsementsBinding


    /**
     * Attaches this activity to the application object
     * and assigns a ViewModel to this activity DataBinding.
     * @param savedInstanceState Parameters saved for restoring data after onDestroy().
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Attach this activity to the application object.
        super.onCreate(savedInstanceState)
        // Initialize Fresco
        Fresco.initialize(this)
        // Initialize the DataBinding for this view.
        setupDataBinding()
        // Retrieve the local stored session
        mStorageViewModel.getStoredProfile()
    }

    /**
     * Initializes the mBinding property for this activity and sets
     * the variables for its binding and the contents for this activity
     */
    private fun setupDataBinding() {
        // Retrieve the DataBinding object for this activity layout.
        mBinding = ActivityEndorsementsBinding.inflate(layoutInflater)
        // Set observables for the ViewModels.
        setupViewModels()
        // Assign a ViewModel for the layout binding.
        mBinding.viewModel = mProfileViewModel
        // Set this activity as the LifecycleOwner for the binding.
        mBinding.setLifecycleOwner(this)
        // Set this activity content view as the binding root.
        setContentView(mBinding.root)
    }

    /**
     * Initializes the value for this activity ViewModel properties and assigns Observables to them.
     */
    private fun setupViewModels() {
        // Retrieve the CareerProfile ViewModel for this activity.
        mProfileViewModel = ViewModelProviders.of(this)[ProfileViewModel::class.java]
        // Display a Toast when an error happens.
        mProfileViewModel.status.observe(this, Observer {
            when (it) {
                // Display that the player profile page was not found.
                ProfileViewModel.Status.STATUS_PROFILE_NOT_FOUND ->
                    displayMessage(R.string.profile_not_found)
                // Display that the overwatch page is down.
                ProfileViewModel.Status.STATUS_BLIZZARD_DOWN ->
                    displayMessage(R.string.cannot_connect_to_blizzard_servers)
                // If the profile request passes, display the career fragment.
                else -> setFragment(CareerFragment())
            }
        })

        // Get the local session ViewModel.
        mStorageViewModel = ViewModelProviders.of(this)[StorageViewModel::class.java]
        // Listen to changes in the session property in the ViewModel.
        mStorageViewModel.session.observe(this, Observer { playerProfile ->
            playerProfile?.let {
                // Get the local session data if there is one stored (not null).
                mProfileViewModel.getProfileData(it.platform, it.userName, false)
            } ?: run {
                // Set he fragment that will ask for a username.
                setFragment(BattletagFragment())
                // Hide the loading view if there isn't any session stored (null).
                loading?.visibility = View.GONE
            }
        })
    }

    /**
     * Replaces a fragment in the container with a given
     * fragment if it has not been already added.
     */
    private fun setFragment(fragment: Fragment) {
        // Get the current fragment in the Fragment container,
        val currentFragment: Fragment? = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        /**
         * Check if the fragment can be added, if there is no fragment in the container or
         * if the current fragment displayed is not from the same class as the current fragment.
         */
        if (currentFragment == null || currentFragment::class != fragment::class) {
            // Perform the transaction.
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.container, fragment, FRAGMENT_TAG)
                    .commit()
        }
    }

    /**
     * Displays a Snackbar showing a String from the resources as the message.
     */
    private fun displayMessage(@StringRes message: Int) = Unit
}