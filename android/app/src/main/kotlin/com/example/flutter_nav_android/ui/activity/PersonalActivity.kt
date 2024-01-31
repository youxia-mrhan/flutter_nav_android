package com.example.flutter_nav_android.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flutter_nav_android.databinding.ActivityPersonalBinding
import com.example.flutter_nav_android.util.FlutterRouterManager
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class PersonalActivity : AppCompatActivity(), MethodChannel.MethodCallHandler, View.OnClickListener {

    private lateinit var bind: ActivityPersonalBinding

    private lateinit var homeFlutterEngine: FlutterEngine
    private lateinit var loginRouterManager: FlutterRouterManager

    private lateinit var loginMethodChannel: MethodChannel
    private lateinit var homeMethodChannel: MethodChannel

    private val METHOD_CHANNEL_LOGIN = "com.example.flutter_nav_android/login/method"
    private val METHOD_CHANNEL_HOME = "com.example.flutter_nav_android/home/method"
    private val NAV_FLUTTER_LOGIN_NOTICE = "navFlutterLoginNotice"
    private val POP_NOTICE = "popNotice"
    private val PERSONAL_POP_NOTICE = "personalPopNotice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityPersonalBinding.inflate(layoutInflater)
        setContentView(bind.root)
        initView()

        loginRouterManager = FlutterRouterManager("/login", "login_engine", this)

        // 两端建立通信
        loginMethodChannel = MethodChannel(loginRouterManager.mEngine!!.dartExecutor, METHOD_CHANNEL_LOGIN)
        loginMethodChannel.setMethodCallHandler(this)

        // 获取 Flutter Home页面的引擎，并且建立通信
        homeFlutterEngine = FlutterRouterManager.getEngineCacheInstance("home_engine")!!
        homeMethodChannel = MethodChannel(homeFlutterEngine.dartExecutor,METHOD_CHANNEL_HOME)
    }

    /**
     * 监听来自 Flutter端 的消息通道
     *
     * call： Android端 接收到 Flutter端 发来的 数据对象
     * result：Android端 给 Flutter端 执行回调的接口对象
     */
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val methodName: String = call.method
        when (methodName) { // 销毁 Flutter Login 页面
            POP_NOTICE -> {
                val age = call.argument<Int>("age")
                getResult(age.toString())
                loginRouterManager.mEngine!!.navigationChannel.popRoute()
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            bind.toFlutter -> { // 前往 Flutter Login 页面
                val map: MutableMap<String, String> = mutableMapOf<String, String>()
                map["name"] = "老王"
                loginMethodChannel.invokeMethod(NAV_FLUTTER_LOGIN_NOTICE,map)
                loginRouterManager.push()
            }
            bind.pop -> { // 销毁 Android Personal 页面
                val map: MutableMap<String, Int> = mutableMapOf<String, Int>()
                map["age"] = 18
                homeMethodChannel.invokeMethod(PERSONAL_POP_NOTICE,map)
                finish()
            }
        }
    }

    /**
     * 初始化页面
     */
    private fun initView() {
        bind.toFlutter.setOnClickListener(this)
        bind.pop.setOnClickListener(this)

        var name = intent.getStringExtra("name")
        val title = "接收初始化参数："
        val msg = title + name

        val ss = SpannableString(msg)
        ss.setSpan(
            ForegroundColorSpan(Color.RED),
            title.length,
            msg.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        bind.initV.text = ss
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
        loginRouterManager.destroy()
    }

}