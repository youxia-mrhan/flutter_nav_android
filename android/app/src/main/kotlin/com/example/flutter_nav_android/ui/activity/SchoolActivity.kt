package com.example.flutter_nav_android.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flutter_nav_android.databinding.ActivitySchoolBinding
import com.example.flutter_nav_android.util.FlutterRouterManager
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.plugin.common.MethodChannel


class SchoolActivity : AppCompatActivity() {

    private lateinit var bind: ActivitySchoolBinding
    private lateinit var bookFragment: FlutterFragment
    private lateinit var studentFragment: FlutterFragment

    private val METHOD_CHANNEL_BOOK = "com.example.flutter_nav_android/book/method"
    private val METHOD_CHANNEL_STUDENT = "com.example.flutter_nav_android/student/method"

    private val NAV_FLUTTER_BOOK_NOTICE = "navFlutterBookNotice"
    private val NAV_FLUTTER_STUDENT_NOTICE = "navFlutterStudentNotice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySchoolBinding.inflate(layoutInflater)
        setContentView(bind.root)
        initView()
        initChannel()
    }

    /**
     * 建立通信
     */
    private fun initChannel() {
        val bookEngine = FlutterRouterManager.getEngineCacheInstance("book_engine")
        val bookChannel = MethodChannel(bookEngine!!.dartExecutor,METHOD_CHANNEL_BOOK)
        val map: MutableMap<String, String> = mutableMapOf<String, String>()
        map["title"] = "Book"
        bookChannel.invokeMethod(NAV_FLUTTER_BOOK_NOTICE,map)

        val studentEngine = FlutterRouterManager.getEngineCacheInstance("student_engine")
        val studentChannel = MethodChannel(studentEngine!!.dartExecutor,METHOD_CHANNEL_STUDENT)
        val map2: MutableMap<String, String> = mutableMapOf<String, String>()
        map2["title"] = "Student"
        studentChannel.invokeMethod(NAV_FLUTTER_STUDENT_NOTICE,map2)
    }

    /**
     * 初始化页面
     */
    private fun initView() {
        bookFragment = FlutterFragment.withCachedEngine("book_engine")
            .transparencyMode(TransparencyMode.transparent) // 背景透明，避免切换页面，出现闪烁
            .shouldAttachEngineToActivity(false) // 是否让Flutter控制Activity，true：可以 false：不可以，默认值 true
            .build()

        supportFragmentManager
            .beginTransaction()
            .add(bind.bookFragment.id, bookFragment)
            .commit()

        studentFragment = FlutterFragment.withCachedEngine("student_engine")
            .transparencyMode(TransparencyMode.transparent) // 背景透明，避免切换页面，出现闪烁
            .shouldAttachEngineToActivity(false) // 是否让Flutter控制Activity，true：可以 false：不可以，默认值 true
            .build()

        supportFragmentManager
            .beginTransaction()
            .add(bind.studentFragment.id, studentFragment)
            .commit()
    }

    // ================================ 这些是固定写法，Flutter需要这些回调 ================================

    override fun onPostResume() {
        super.onPostResume()
        bookFragment.onPostResume()
        studentFragment.onPostResume()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        bookFragment.onNewIntent(intent)
        studentFragment.onNewIntent(intent)
    }

    override fun onBackPressed() {
        bookFragment.onBackPressed()
        studentFragment.onBackPressed()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        bookFragment.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        studentFragment.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onUserLeaveHint() {
        bookFragment.onUserLeaveHint()
        studentFragment.onUserLeaveHint()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        bookFragment.onTrimMemory(level)
        studentFragment.onTrimMemory(level)
    }

}