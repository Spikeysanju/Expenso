package dev.spikeysanju.expensetracker.services.exportcsv

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class CreateCsvContract : ActivityResultContract<String, Uri?>() {

    override fun createIntent(context: Context, input: String): Intent {
        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/csv"
            putExtra(Intent.EXTRA_TITLE, "$input.csv")
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.data
    }
}

class OpenCsvContract : ActivityResultContract<Uri, Unit>() {

    override fun createIntent(context: Context, input: Uri): Intent {
        val title = "Open with"
        val csvPreviewIntent = Intent(Intent.ACTION_OPEN_DOCUMENT, input).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        return Intent.createChooser(csvPreviewIntent, title)
    }

    override fun parseResult(resultCode: Int, intent: Intent?) {
    }
}
