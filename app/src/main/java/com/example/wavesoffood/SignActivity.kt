package com.example.wavesoffood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.wavesoffood.SharedPreference.ProfileSavedPreferences
import com.example.wavesoffood.databinding.ActivitySignBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignActivity : AppCompatActivity() {
    private val binding : ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }

    lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    private lateinit var etname: EditText
    private lateinit var btnSignUp: Button
    private lateinit var googlesignupbtn :Button
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
//    lateinit var tvRedirectLogin: TextView

    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)

        btnSignUp = binding.createactbtn
        etEmail = binding.etemail
        etPass  = binding.etpassword
        etname = binding.etname
        googlesignupbtn = binding.googlesignupbtn
        // Initialising auth object
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

       handleBtnClick()



    }
    private fun handleBtnClick()
    {

        btnSignUp.setOnClickListener {
            signUpUser()
        }
        googlesignupbtn.setOnClickListener {
            Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
            signInGoogle()
        }

        binding.alreadyhavebutton.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun signUpUser() {
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()
        val name = etname.text.toString()

        Log.d("amrit", "Email: $email, Password: $pass, Name: $name")

        // check if fields are blank
        if (email.isBlank() || pass.isBlank() || name.isBlank()) {
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Successfully Signed Up", Toast.LENGTH_SHORT).show()
                 saveDetailsToSharedPreference(name,email)
                startActivity(Intent(this@SignActivity, MainActivity::class.java))
            } else {
                val errorMessage = task.exception?.message ?: "Unknown error occurred"
                Toast.makeText(this, "Sign Up Failed: $errorMessage", Toast.LENGTH_SHORT).show()
                Log.e("amrit", "Sign Up Failed: $errorMessage")
            }
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // this is where we update the UI after Google signin takes place
    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                saveDetailsToSharedPreference(account.displayName.toString(),account.email.toString())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(
                Intent(
                    this, MainActivity
                    ::class.java
                )
            )
            finish()
        }
        if (auth.currentUser != null)
        {
            startActivity(
                Intent(
                    this, MainActivity
                    ::class.java
                ))

        }
    }

    private fun saveDetailsToSharedPreference(name : String , email : String)
    {
        ProfileSavedPreferences.setName(this, name)
        ProfileSavedPreferences.setEmail(this, email)
    }
}