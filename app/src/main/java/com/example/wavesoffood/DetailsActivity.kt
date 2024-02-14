package com.example.wavesoffood

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.wavesoffood.Modelclass.CartItems
import com.example.wavesoffood.databinding.ActivityDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding :ActivityDetailsBinding
    private lateinit var mAuth: FirebaseAuth

    private  var foodName :String? = null
    private  var foodImage :String? = null
    private  var foodDescription :String? = null
    private  var foodIngredients :String? = null
    private  var foodPrice :String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
         foodName = intent.getStringExtra("MenuItemName")
         foodImage = intent.getStringExtra("MenuItemImage")
         foodDescription = intent.getStringExtra("MenuItemDescription")
         foodIngredients = intent.getStringExtra("MenuItemIngredients")
         foodPrice = intent.getStringExtra("MenuItemPrice")
        binding.detailFoodName.text = foodName
        binding.DescriptionTextView .text = foodDescription
        binding.IngredientsTextView.text = foodIngredients
        val uriString = foodImage
        val uri = Uri.parse(uriString)
        val detailedFoodImageview = binding.DetailFoodImage

        Glide.with(this).load(uri).into(detailedFoodImageview)
        binding.imageButton.setOnClickListener {
            finish()
        }

        binding.addtocartbtn.setOnClickListener {
            addItemToCart()
        }

    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = mAuth.currentUser?.uid ?: ""

        val cartItem = CartItems(foodName.toString(),foodPrice.toString(),foodDescription.toString(),foodImage.toString(),1 )

        database.child("Users").child(userId).child("CartItems").push().setValue(cartItem)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Cart Item saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed to save Cart Item", Toast.LENGTH_SHORT).show()
            }
    }


    }


