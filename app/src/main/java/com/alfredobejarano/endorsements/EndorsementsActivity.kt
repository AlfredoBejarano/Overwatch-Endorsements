package com.alfredobejarano.endorsements

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
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
        // Retrieve the DataBinding object for this activity layout.
        val binding = ActivityEndorsementsBinding.inflate(layoutInflater)
        // Retrieve the CareerProfile ViewModel for this activity.
        val careerVM = ViewModelProviders.of(this)[ProfileViewModel::class.java]
        // Assign a ViewModel for the layout binding.
        binding.viewModel = careerVM
        // Set this activity as the LifecycleOwner for the binding.
        binding.setLifecycleOwner(this)
        // Set this activity content view as the binding root.
        setContentView(binding.root)
        // Get the local session ViewModel.
        val storageVM = ViewModelProviders.of(this)[StorageViewModel::class.java]
        // Listen to changes in the session property in the ViewModel.
        storageVM.session.observe(this, Observer {
            it?.let {
                // Get the local session data if there is one stored (not null).
                careerVM.getProfileData(it.platform, it.userName)
            } ?: run {
                // Set he fragment that will ask for a username.
                setFragment(CareerFragment())
                // Hide the loading view if there isn't any session stored (null).
                loading?.visibility = View.GONE
            }
        })
        // Retrieve the local stored session
        storageVM.getStoredProfile()
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
}
