//package com.example.wavesoffood.Fragment
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.SearchView
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.wavesoffood.Modelclass.MenuItem
//import com.example.wavesoffood.R
//import com.example.wavesoffood.adaptar.MenuAdapter
//import com.example.wavesoffood.databinding.FragmentSearchBinding
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//
//
//class SearchFragment : Fragment() {
//    private lateinit var binding: FragmentSearchBinding
//    private lateinit var adapter: MenuAdapter
//    private val foodNames = mutableListOf<String>()
//    private val foodPrices = mutableListOf<String>()
//    private val foodDescriptions = mutableListOf<String>()
//    private val imageuris = mutableListOf<String>()
//    private val ingredients = mutableListOf<String>()
//    private lateinit var database: FirebaseDatabase
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//    private val filteredMenuFoodName = mutableListOf<String>()
//    private val filteredMenuItemPrice = mutableListOf<String>()
//    private val filteredMenuImage = mutableListOf<String>()
//    private val filteredMenuDescription = mutableListOf<String>()
//    private val filteredMenuIngedients = mutableListOf<String>()
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentSearchBinding.inflate(inflater, container, false)
//
//        retreriveMenuItems()
//
//        return binding.root
//    }
//
//
//    private fun setRecyclerViewtest() {
//        adapter = MenuAdapter(
//            foodNames,
//            foodPrices,
//            imageuris,
//            foodDescriptions,
//            ingredients,
//            requireContext()
//        )
//
//        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.menuRecyclerView.adapter = adapter
//    }
//
//    private fun  retreriveMenuItems()
//    {
//
//
//        // Get reference to the Firebase database
//        database = FirebaseDatabase.getInstance()
//
//        val foodRef: DatabaseReference = database.reference.child("Vendor").child("Menu")
//
//        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Loop through each food item
//                for (foodSnapshot in dataSnapshot.children) {
//                    // Get the FoodItem object from the child node
//                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
//
//                    // Add the foodname to the foodNames list
//                    menuItem?.foodname?.let {
//                        foodNames.add(it)
//                    }
//                    menuItem?.foodprice?.let {
//                        foodPrices.add(it)
//                    }
//                    menuItem?.imageurl?.let {
//                        imageuris.add(it)
//                    }
//                    menuItem?.ingredients?.let {
//                        ingredients.add(it)
//                    }
//                    menuItem?.fooddescription?.let {
//                        foodDescriptions.add(it)
//                    }
//                }
//               setRecyclerViewtest()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle errors here if any
//                println("Error: ${databaseError.message}")
//            }
//        })
//    }
//}
package com.example.wavesoffood.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wavesoffood.Modelclass.MenuItem
import com.example.wavesoffood.R
import com.example.wavesoffood.adaptar.MenuAdapter
import com.example.wavesoffood.databinding.FragmentSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private lateinit var database :FirebaseDatabase
    private val originalMenuFoodName = mutableListOf<String>()
    private val originalMenuItemPrice = mutableListOf<String>()
    private val originalMenuImage = mutableListOf<String>()
    private val originalMenuDescription = mutableListOf<String>()
    private val originalMenuIngredients = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private val filteredMenuFoodName = mutableListOf<String>()
    private val filteredMenuItemPrice = mutableListOf<String>()
    private val filteredMenuImage = mutableListOf<String>()
    private val filteredMenuDescription = mutableListOf<String>()
    private val filteredMenuIngredients = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        retreriveMenuItems()


        return binding.root
    }
    private fun showAllMenu() {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImage.clear()
        filteredMenuFoodName.addAll(originalMenuFoodName)
        filteredMenuItemPrice.addAll(originalMenuItemPrice)
        filteredMenuImage.addAll(originalMenuImage)
        filteredMenuDescription.addAll(originalMenuDescription)
        filteredMenuIngredients.addAll(originalMenuIngredients)
        setAdapter()
        adapter.notifyDataSetChanged()
    }
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }
        })
    }
    private fun filterMenuItems(query: String) {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImage.clear()
        originalMenuFoodName.forEachIndexed { index, foodName ->
            if (foodName.contains(query, ignoreCase = true)) {
                filteredMenuFoodName.add(foodName)
                filteredMenuItemPrice.add(originalMenuItemPrice[index])
                filteredMenuImage.add(originalMenuImage[index])
            }
        }
        adapter.notifyDataSetChanged()
    }
    companion object {
    }
    private fun  retreriveMenuItems()
    {


        // Get reference to the Firebase database
        database = FirebaseDatabase.getInstance()

        val foodRef: DatabaseReference = database.reference.child("Vendor").child("Menu")
        // Fetch data from the database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Loop through each food item
                for (foodSnapshot in dataSnapshot.children) {
                    // Get the FoodItem object from the child node
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)

                    // Add the foodname to the foodNames list
                    menuItem?.foodname?.let {
                        originalMenuFoodName.add(it)
                    }
                    menuItem?.foodprice?.let {
                        originalMenuItemPrice.add(it)
                    }
                    menuItem?.imageurl?.let {
                        originalMenuImage.add(it)
                    }
                    menuItem?.ingredients?.let {
                        originalMenuIngredients.add(it)
                    }
                    menuItem?.fooddescription?.let {
                        originalMenuDescription.add(it)
                    }
                }

                showAllMenu()
                setupSearchView()



            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here if any
                println("Error: ${databaseError.message}")
            }
        })
    }
    private fun setAdapter()
    {
        adapter = MenuAdapter(
            filteredMenuFoodName,
            filteredMenuItemPrice,
            filteredMenuImage,
            filteredMenuDescription,
            filteredMenuIngredients,
            requireContext()
        )
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

    }
}