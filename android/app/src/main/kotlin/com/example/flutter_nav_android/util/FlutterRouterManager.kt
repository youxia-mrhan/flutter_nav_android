package com.example.flutter_nav_android.util

import android.app.Activity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

class FlutterRouterManager(
    val targetRoute: String,
    val mEngineId: String,
    val mContext: Activity
) {

    var mEngine: FlutterEngine? = null

    init {
        createCachedEngine()
    }

    companion object {

        /**
         * 获取缓存中的 Flutter引擎
         */
        @JvmStatic
        fun getEngineCacheInstance(engineId: String): FlutterEngine? {
            return FlutterEngineCache.getInstance().get(engineId)
        }

    }

    /**
     * 1、创建Flutter引擎
     * 2、将初始命名路由，修改为目标页面路由
     * 3、缓存Flutter引擎
     */
    fun createCachedEngine(): FlutterEngine {
        val flutterEngine = FlutterEngine(mContext) // 创建Flutter引擎

        // 将初始命名路由，修改为目标页面路由
        flutterEngine.navigationChannel.setInitialRoute(targetRoute)

        // 这一步，是在执行相关Dart文件入口的 main函数，将Flutter页面渲染出结果
        // 原生端获取结果，进行最终渲染上屏
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        // 将加载好的引擎，存储起来
        FlutterEngineCache.getInstance().put(mEngineId, flutterEngine)

        mEngine = flutterEngine

        return flutterEngine
    }

    /**
     * 根据引擎ID，前往指定的Flutter页面
     */
    fun push() {
        // 创建新的引擎（了解即可）
        // mContext.startActivity(
        //    FlutterActivity
        //        .withNewEngine() // 创建引擎
        //        .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent) // 背景改为透明，不然切换页面时，会闪烁黑色
        //        .build(mContext))

        // 使用缓存好的引擎（推荐）
        mContext.startActivity(
            FlutterActivity
                .withCachedEngine(mEngineId) // 获取缓存好的引擎
                .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent) // 背景改为透明，不然切换页面时，会闪烁黑色
                .build(mContext)
        )
    }

    /**
     * 销毁当前Flutter引擎
     */
    fun destroy() {
        mEngine?.destroy()
    }

}