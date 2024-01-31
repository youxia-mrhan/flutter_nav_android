package com.example.flutter_nav_android.ui.activity

import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flutter_nav_android.databinding.ActivityMainBinding
import com.example.flutter_nav_android.util.FlutterRouterManager
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel


class MainActivity : AppCompatActivity(), MethodChannel.MethodCallHandler, View.OnClickListener {

    private lateinit var bind: ActivityMainBinding

    private lateinit var homeMethodChannel: MethodChannel

    private val METHOD_CHANNEL_HOME = "com.example.flutter_nav_android/home/method"
    private val NAV_ANDROID_PERSONAL_NOTICE = "navAndroidPersonalNotice"
    private val NAV_FLUTTER_HOME_NOTICE = "navFlutterHomeNotice"
    private val POP_NOTICE = "popNotice"

    private lateinit var homeRouterManager: FlutterRouterManager
    private lateinit var bookRouterManager: FlutterRouterManager
    private lateinit var studentRouterManager: FlutterRouterManager

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.toFlutter.setOnClickListener(this)
        bind.toFlutterFragment.setOnClickListener(this)

        homeRouterManager = FlutterRouterManager("/home", "home_engine", this)

        // 两端建立通信
        homeMethodChannel = MethodChannel(homeRouterManager.mEngine!!.dartExecutor,METHOD_CHANNEL_HOME)
        homeMethodChannel.setMethodCallHandler(this)

        // 这里Fragment案例的
        bookRouterManager = FlutterRouterManager("/book", "book_engine", this)
        studentRouterManager = FlutterRouterManager("/student", "student_engine", this)
    }

    /**
     * 监听来自 Flutter端 的消息通道
     *
     * call： Android端 接收到 Flutter端 发来的 数据对象
     * result：Android端 给 Flutter端 执行回调的接口对象
     */
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val methodName: String = call.method
        when (methodName) {
            NAV_ANDROID_PERSONAL_NOTICE -> { // Flutter Home 页面 前往 Android Personal 页面
                val intent = Intent(this, PersonalActivity::class.java)
                intent.putExtra("name",call.argument<String>("name"))
                startActivity(intent)
            }
            POP_NOTICE -> { // 销毁 Flutter Home 页面
                val age = call.argument<Int>("age")
                getResult(age.toString())
                homeRouterManager.mEngine!!.navigationChannel.popRoute()
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            bind.toFlutter -> { // 前往 Flutter Home 页面
                val map: MutableMap<String, String> = mutableMapOf<String, String>()
                map["name"] = "张三"
                homeMethodChannel.invokeMethod(NAV_FLUTTER_HOME_NOTICE,map)
                homeRouterManager.push()
            }
            bind.toFlutterFragment -> {
                val intent = Intent(this, SchoolActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * 获取上一页的返回参数
     */
    private fun getResult(age: String) {
        val title = "接收上一页返回参数："
        val msg = title + age

        val ss = SpannableString(msg)
        ss.setSpan(
            ForegroundColorSpan(Color.RED),
            title.length,
            msg.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        bind.resultV.text = ss
    }

    override fun onDestroy() {
        super.onDestroy()
        homeRouterManager.destroy()
        bookRouterManager.destroy()
        studentRouterManager.destroy()
    }

}
