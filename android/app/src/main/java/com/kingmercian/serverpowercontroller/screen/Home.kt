package com.kingmercian.serverpowercontroller.screen

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kingmercian.ServerPowerController.R
import com.kingmercian.serverpowercontroller.connector.Api
import com.kingmercian.serverpowercontroller.storage.Preferences
import kotlinx.android.synthetic.main.home_activity.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        setSupportActionBar(findViewById(R.id.toolbar_home))

        pcPowerToggle.setOnTouchListener(this::onTouchArrow)
        pcForcePowerToggle.setOnTouchListener(this::onTouchArrow)

        swiperefresh.setOnRefreshListener {
            sendRequest("")
        }

        pcPowerToggle.setSvgColor(R.color.colorWhite)
        pcForcePowerToggle.setSvgColor(R.color.colorWhite)

        sendRequest("")
    }

    private fun getToken(): String {
        val preferences = Preferences(this)
        val key = preferences.getUsername() + ":" + preferences.getPassword()
        return Base64.encodeToString(key.toByteArray(), Base64.NO_WRAP)
    }


    private fun sendRequest(type: String): Call<ResponseBody> {

        val preferences = Preferences(this)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://${preferences.getIpAddress()}:${preferences.getPort()}").build()
        val apiService = retrofit.create(Api::class.java)
        var typeRequest = (apiService as Api).getPower("Basic " + getToken())

        when (type) {
            "power" -> typeRequest = (apiService).togglePower("Basic " + getToken())
            "force" -> typeRequest = (apiService).forcePower("Basic " + getToken())
        }

        val request: Call<ResponseBody> = typeRequest
        request.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                val body = response.body()?.string().toString()
                val code = response.code().toString()

                Log.i("Raw", response.raw().toString())
                Log.i("Code", response.code().toString())
                Log.i("Body", response.body()?.string().toString())

                if (type == "") {
                    if ("200" in code) {
                        connection.setColorFilter(
                            ContextCompat.getColor(
                                this@Home,
                                R.color.colorGreen
                            ), PorterDuff.Mode.SRC_IN
                        )
                    } else {
                        connection.setColorFilter(
                            ContextCompat.getColor(
                                this@Home,
                                R.color.colorRed
                            ), PorterDuff.Mode.SRC_IN
                        )
                    }

                    if ("ON" in body) {
                        server.setColorFilter(
                            ContextCompat.getColor(this@Home, R.color.colorGreen),
                            PorterDuff.Mode.SRC_IN
                        )
                    } else {
                        server.setColorFilter(
                            ContextCompat.getColor(this@Home, R.color.colorRed),
                            PorterDuff.Mode.SRC_IN
                        )
                    }
                }
                else{
                    if("200" in code){
                        Toast.makeText(this@Home, "Success!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Failure", t.message.toString())
            }
        })
        swiperefresh.isRefreshing = false

        return request
    }

    private fun onTouchArrow(v: View, event: MotionEvent): Boolean {
        val isTouchDown = event.action == MotionEvent.ACTION_DOWN
        val isTouchUp = event.action == MotionEvent.ACTION_UP
        if (isTouchDown) {
            when (v.id) {
                R.id.pcPowerToggle -> {
                    sendRequest("power")
                    pcPowerToggle.setSvgColor(R.color.colorGray)
                }
                R.id.pcForcePowerToggle -> {
                    sendRequest("force")
                    pcForcePowerToggle.setSvgColor(R.color.colorGray)
                }
            }
            return true
        }
        if (isTouchUp) {
            when (v.id) {
                R.id.pcPowerToggle -> pcPowerToggle.setSvgColor(R.color.colorWhite)
                R.id.pcForcePowerToggle -> pcForcePowerToggle.setSvgColor(R.color.colorWhite)
            }
            return true
        }
        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_information -> {
                showInformationDialog()
                true
            }
            R.id.action_configuration -> {
                val intent = Intent(this, Configuration::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun finishActivity() {
        finish()
    }

    private fun showInformationDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogTheme))
        with(builder) {
            setTitle(getString(R.string.configuration_dialog_title))
            setMessage(getString(R.string.configuration_dialog_message))
            setPositiveButton(getString(R.string.ok)) { _, _ -> }
            show()
        }
    }

    private fun ImageView.setSvgColor(@ColorRes color: Int) =
        setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
}