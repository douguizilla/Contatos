package com.odougle.contatos.util

import android.content.ContentProviderOperation
import android.content.Context
import android.content.OperationApplicationException
import android.graphics.Bitmap
import android.os.RemoteException
import android.provider.ContactsContract
import java.io.ByteArrayOutputStream

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

        //adiciona o endereço ao contato a partir do id
        operation.add(
            ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, backRefIndex)
                .withValue(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue( ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, address)
                .build()
        )

        //adiciona um phoneNumber ao contato a partir do tipo "Home"
        operation.add(
            ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, backRefIndex)
                .withValue(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue( ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                .withValue( ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build()
        )

        //adiciona imagem ao contato
        val stream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.JPEG, 75, stream)
        operation.add(
            ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, backRefIndex)
                .withValue(ContactsContract.Data.IS_SUPER_PRIMARY,1)
                .withValue(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                .withValue( ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray())
                .build()
        )

        try {
            context.contentResolver.applyBatch(
                ContactsContract.AUTHORITY, operation
            )
        }catch (e: RemoteException){
            e.printStackTrace()
        }catch (e: OperationApplicationException){
            e.printStackTrace()
        }

    }
}