package com.alfredobejarano.endorsements

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alfredobejarano.endorsements.databinding.ActivityEndorsementsBinding
import com.alfredobejarano.endorsements.viewmodel.ProfileViewModel

/**
 * Activity for the app that will serve as the lifecycle
 * owner for the fragments CareerProfileViewModel.
 *
 * @author Alfredo Bejarano
 * @since Jul 26th, 2018 - 02:05 PM
 */
class EndorsementsActivity : AppCompatActivity() {
    /**
     * Attaches this activity to the application object
     * and assigns a ViewModel to this activity DataBinding.
     * @param savedInstanceState Parameters saved for restoring data after onDestroy().
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Attach this activity to the application object.
        super.onCreate(savedInstanceState)
        // Retrieve the DataBinding object for this activity layout.
        val binding = ActivityEndorsementsBinding.inflate(layoutInflater)
        // Assign a ViewModel for the layout binding.
        binding.viewModel = ViewModelProviders.of(this)[ProfileViewModel::class.java]
        // Set this activity as the LifecycleOwner for the binding.
        binding.setLifecycleOwner(this)
        // Set this activity contentt view as the binding root.
        setContentView(binding.root)
    }
}
