package com.example.wavesoffood

import android.os.Bundle
import com.example.wavesoffood.Modelclass.MenuItem


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wavesoffood.adaptar.MenuAdapter
import com.example.wavesoffood.databinding.FragmentMenuBootomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuBootomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMenuBootomSheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames : MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var imageuris: MutableList<String>
    private lateinit var ingredients: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBootomSheetBinding.inflate(inflater, container, false)
        retreriveMenuItems()
        binding.buttonBack.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
    companion object {
    }

    private fun  retreriveMenuItems()
    {


        // Get reference to the Firebase database
        database = FirebaseDatabase.getInstance()

        val foodRef: DatabaseReference = database.reference.child("Vendor").child("Menu")
//        Toast.makeText(this@AllItemActivity, "${foodRef.toString()}", Toast.LENGTH_SHORT).show()

        // Array to store the details  of all menu items
        foodNames = mutableListOf()
        foodPrices= mutableListOf()
        foodDescriptions = mutableListOf()
        imageuris= mutableListOf()
        ingredients = mutableListOf()
        // Fetch data from the database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Loop through each food item

                for (foodSnapshot in dataSnapshot.children) {

                    // Get the FoodItem object from the child node
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
            // Add the foodname to the foodNames list
                    menuItem?.foodname?.let {
                        foodNames.add(it)
                    }
                    menuItem?.foodprice?.let {
                        foodPrices.add(it)
                    }

                    menuItem?.imageurl?.let {
                        imageuris.add(it)
                    }
                    menuItem?.ingredients?.let {
                        ingredients.add(it)
                    }
                    menuItem?.fooddescription?.let {
                        foodDescriptions.add(it)
                    }


                }


                setAdapter()           // calling function after all the data is reterived from database


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here if any
                println("Error: ${databaseError.message}")
            }
        })
    }

    private  fun setAdapter()
    {
        val adapter = MenuAdapter(foodNames
            ,
            foodPrices,
            imageuris,foodDescriptions,ingredients,requireContext()
        )
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

    }

}
/*
    private fun addingDataToLocalList()
    {

        // Array to store the details  of all menu items
        foodNames = mutableListOf()
        foodPrices= mutableListOf()
        foodDescriptions = mutableListOf()
        imageuris= mutableListOf()
        ingredients = mutableListOf()

        for (menuitems in listofmenuitem)
        {
            menuitems.foodname?.let { foodNames.add(it) }
            menuitems.foodprice?.let { foodPrices.add(it) }
            menuitems.fooddescription?.let {foodDescriptions .add(it) }
            menuitems.imageurl?.let { imageuris.add(it) }
            menuitems.ingredients?.let { ingredients.add(it) }
        }
    }
 */