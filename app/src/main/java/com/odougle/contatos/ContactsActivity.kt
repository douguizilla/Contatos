package com.odougle.contatos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.odougle.contatos.databinding.ActivityContactsBinding

class ContactsActivity : AppCompatActivity() {
    private val binding: ActivityContactsBinding by lazy{
        ActivityContactsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}