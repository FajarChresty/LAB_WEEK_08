package com.example.lab_week_08

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.*
import com.example.lab_week_08.worker.FirstWorker
import com.example.lab_week_08.worker.SecondWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val workManager by lazy { WorkManager.getInstance(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // jika layout bernama activity_main.xml pastikan ada root view dengan id 'main'
        setContentView(R.layout.activity_main)

        // Optional: handle insets agar UI tidak tertutup statusbar/nav (sama seperti modul)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Constraint: require koneksi internet (sesuai modul)
        val networkConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val id = "001"

        // Create OneTimeWorkRequest untuk FirstWorker
        val firstRequest = OneTimeWorkRequestBuilder<FirstWorker>()
            .setConstraints(networkConstraints)
            .setInputData(getIdInputData(FirstWorker.INPUT_DATA_ID, id))
            .build()

        // Create OneTimeWorkRequest untuk SecondWorker
        val secondRequest = OneTimeWorkRequestBuilder<SecondWorker>()
            .setConstraints(networkConstraints)
            .setInputData(getIdInputData(SecondWorker.INPUT_DATA_ID, id))
            .build()

        // Set up sequence: first -> second
        workManager.beginWith(firstRequest)
            .then(secondRequest)
            .enqueue()

        // Observe hasil first
        workManager.getWorkInfoByIdLiveData(firstRequest.id)
            .observe(this) { info ->
                if (info.state.isFinished) {
                    showResult("First process is done")
                }
            }

        // Observe hasil second
        workManager.getWorkInfoByIdLiveData(secondRequest.id)
            .observe(this) { info ->
                if (info.state.isFinished) {
                    showResult("Second process is done")
                }
            }
    }

    private fun getIdInputData(idKey: String, idValue: String) = Data.Builder()
        .putString(idKey, idValue)
        .build()

    private fun showResult(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
