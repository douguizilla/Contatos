package com.odougle.contatos

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.odougle.contatos.databinding.FragmentContactBinding
import com.odougle.contatos.util.ContactUtils
import java.io.FileNotFoundException

class ContactFragment : DialogFragment(), DialogInterface.OnClickListener {

    private lateinit var binding: FragmentContactBinding
    private var selectedPhoto: Bitmap? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentContactBinding.inflate(requireActivity().layoutInflater)
        binding.imgPhoto.setOnClickListener {
            selectPhotoFromGallery()
        }
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok, this)
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    private fun selectPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val photo = selectedPhoto
        binding.apply {
            if(edtName.text.isNotEmpty() && edtAddress.text.isNotEmpty() && photo != null){
                ContactUtils.insetContact(
                    requireContext(),
                    edtName.text.toString(),
                    edtPhone.text.toString(),
                    edtAddress.text.toString(),
                    photo
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && data != null && data.data != null){
            try {
                val options = BitmapFactory.Options()
                options.inSampleSize = 4
                selectedPhoto = BitmapFactory.decodeStream(
                    requireContext().contentResolver.openInputStream(data.data!!),
                    null,
                    options
                )
                binding.imgPhoto.setImageBitmap(selectedPhoto)
            }catch (e: FileNotFoundException){
                e.printStackTrace()
            }
        }
    }
}