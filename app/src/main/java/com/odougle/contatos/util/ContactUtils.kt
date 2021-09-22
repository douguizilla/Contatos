package com.odougle.contatos.util

import android.content.ContentProviderOperation
import android.content.Context
import android.graphics.Bitmap
import android.provider.ContactsContract

object ContactUtils {
    fun insetContact(context: Context, name: String, phoneNumber: String,
    address: String, photo: Bitmap){
        //lista de operações que serão realizadas em batch
        val operation = ArrayList<ContentProviderOperation>()

        //Armazenara o id interno do contato e servira p inserir os detalhes
        val backRefIndex = 0

        //Associa o contato a conta-padrao do phoneNumber
        operation.add(
            ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .build()
        )

        //adiciona o nome do contato e alimenta o id
        operation.add(
            ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, backRefIndex)
                .withValue(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue( ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build()
        )


    }
}