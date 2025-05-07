package com.transition.ora

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.transition.ora.services.Reader


private const val FLAGS = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B

class MainActivity : AppCompatActivity() {
    private lateinit var reader: Reader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        reader = Reader(CardDatabase.getInstance(this).dao, this)

        this.findViewById<Button>(R.id.scanHistoryButton)
            .setOnClickListener(HistoryButtonListener(this))
    }

    override fun onPause() {
        super.onPause()
        this.disableReaderMode()
    }

    override fun onResume() {
        super.onResume()
        this.enableReaderMode()
    }

    private fun enableReaderMode() {
        val nfc = NfcAdapter.getDefaultAdapter(this)
        if (nfc == null) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.no_nfc_title)
                .setMessage(R.string.no_nfc_message)
                .setNeutralButton(R.string.accept) { _, _ -> }
            val dialog = builder.create()
            dialog.show()
            return
        }

        if (!nfc.isEnabled) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.enable_nfc_title)
                .setMessage(R.string.enable_nfc_message)
                .setNeutralButton(R.string.accept) { _, _ ->
                    val intent = Intent(Settings.ACTION_NFC_SETTINGS)
                    startActivity(intent)
                }
            val dialog = builder.create()
            dialog.show()
        } else {
            nfc.enableReaderMode(this, reader, FLAGS, null)
        }
    }

    fun disableReaderMode() {
        val nfc = NfcAdapter.getDefaultAdapter(this) ?: return
        nfc.disableReaderMode(this)
    }

    class HistoryButtonListener(
        private val activity: MainActivity,
    ) : View.OnClickListener {
        override fun onClick(view: View) {
            val intent = Intent(activity , HistoryActivity::class.java)
            activity.disableReaderMode()
            activity.startActivity(intent)
        }
    }
}