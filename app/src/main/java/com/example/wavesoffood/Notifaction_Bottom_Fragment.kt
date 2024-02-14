package com.example.wavesoffood

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wavesoffood.Fragment.HistoryFragment
import com.example.wavesoffood.Modelclass.OrderDetails
import com.example.wavesoffood.SharedPreference.ProfileSavedPreferences
import com.example.wavesoffood.adaptar.NotificationAdapter
import com.example.wavesoffood.databinding.FragmentNotifactionBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class Notifaction_Bottom_Fragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotifactionBottomBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var userId : String
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotifactionBottomBinding.inflate(layoutInflater, container, false)
        mAuth = FirebaseAuth.getInstance()

        checkCurrentOrderStatus()

        return binding.root
    }
    companion object {
    }

    private fun checkCurrentOrderStatus()
    {

        userId = mAuth.currentUser?.uid ?: ""
        database = FirebaseDatabase.getInstance()
        var currentitempushkey = ProfileSavedPreferences.getItemPushKey(requireContext())

        var recentitemref = currentitempushkey?.let {
                database.reference.child("Users").child(userId).child("BuyHistory").child(
                    it
                )
            }
      recentitemref?.addValueEventListener(object : ValueEventListener{
          override fun onDataChange(snapshot: DataSnapshot) {
             if (snapshot.exists())
             {
                 var orderacceptstaus = snapshot.child("orderisAccepted").getValue()

                 if (orderacceptstaus is Boolean ) {
                     setDataInNotfication(orderacceptstaus)
                 }
             }
          }

          override fun onCancelled(error: DatabaseError) {

          }

      })



    }

    private fun setDataInNotfication(orderstatus: Boolean) {
        if (orderstatus == false)
        {
            binding.apply {
                notificationImageView.setImageResource(R.drawable.congrats)
                notificationTextView.text = " Congrats your order  is placed"
            }
        }

        else if(orderstatus == true)
        {
            binding.apply {
                notificationImageView.setImageResource(R.drawable.truck)
                notificationTextView.text = "Your Order is On the Way"
            }
        }
    }

}