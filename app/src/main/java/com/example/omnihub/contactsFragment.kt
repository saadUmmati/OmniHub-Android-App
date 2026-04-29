package com.example.omnihub

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omnihub.classes.ContactAdapter
import com.example.omnihub.classes.ContactViewModel
import com.example.omnihub.classes.ContactViewModelFactory

class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    private lateinit var viewModel: ContactViewModel
    private lateinit var adapter: ContactAdapter

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.loadContacts()
        } else {
            Toast.makeText(context, "Permission denied to read contacts", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. RecyclerView Setup
        val rvContacts = view.findViewById<RecyclerView>(R.id.rvContacts)
        adapter = ContactAdapter(emptyList())
        rvContacts.layoutManager = LinearLayoutManager(context)
        rvContacts.adapter = adapter

        // 2. MVVM Setup (Manual DI)
        val repository = ContactRepository(requireContext())
        val factory =
            ContactViewModelFactory(repository) // ViewModelFactory banani hogi wese hi jaise Weather ki banayi thi
        viewModel = ViewModelProvider(this, factory)[ContactViewModel::class.java]

        // 3. Observe Data
        viewModel.contacts.observe(viewLifecycleOwner) { list ->
            adapter.updateData(list)
        }

        // 4. Check Permission & Load
        checkPermissionAndLoad()
    }

    private fun checkPermissionAndLoad() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED) {
            viewModel.loadContacts()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }
}