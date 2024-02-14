package movil.siafeson.simgolp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import movil.siafeson.simgolp.app.MyApp
import movil.siafeson.simgolp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private var progressStatus: Int = 0;

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val handler: Handler = Handler()
        Thread(Runnable {
                while (progressStatus < 100){
                    progressStatus += 1
                    try {
                        Thread.sleep(10)
                    }catch (e: InterruptedException){
                        e.printStackTrace()
                    }
                    handler.post(Runnable {
                        binding.progressBarSplash.progress = progressStatus
                    })
                }
                if (progressStatus == 100){
                    if (MyApp.preferences.userName != ""){
                        startActivity(Intent(this, MainActivity::class.java))
                        finishAffinity()
                    }
                    else {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finishAffinity()
                    }
                }
        }).start()

    }
}