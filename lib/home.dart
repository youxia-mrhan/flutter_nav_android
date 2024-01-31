import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class Home extends StatefulWidget {
  const Home({super.key});

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  late MethodChannel homeMethodChannel;

  String name = '';
  String age = '';

  static const String METHOD_CHANNEL_HOME = 'com.example.flutter_nav_android/home/method';
  static const String NAV_ANDROID_PERSONAL_NOTICE = 'navAndroidPersonalNotice';
  static const String NAV_FLUTTER_HOME_NOTICE = 'navFlutterHomeNotice';
  static const String POP_NOTICE = 'popNotice';
  static const String PERSONAL_POP_NOTICE = 'personalPopNotice';

  @override
  void initState() {
    super.initState();
    homeMethodChannel = const MethodChannel(METHOD_CHANNEL_HOME);
    homeMethodChannel.setMethodCallHandler(methodHandler);
  }

  /// 监听来自 Android端 的消息通道
  /// Android端调用了函数，这个handler函数就会被触发
  Future<dynamic> methodHandler(MethodCall call) async {
    final String methodName = call.method;
    switch (methodName) {
      case NAV_FLUTTER_HOME_NOTICE: // 进入当前页面
        {
          name = call.arguments['name'];
          setState(() {});
          return 0;
        }
      case PERSONAL_POP_NOTICE: // Android Personal 页面 销毁了
        {
          age = '${call.arguments['age']}';
          setState(() {});
          return 0;
        }
      default:
        {
          return PlatformException(
              code: '-1',
              message: '未找到Flutter端具体实现函数',
              details: '具体描述'); // 返回给Android端
        }
    }
  }

  /// 销毁当前页面
  popPage() {
    if (Navigator.canPop(context)) { // 检查Flutter路由栈中，是否还有其他路由
      Navigator.pop(context);
    } else {
      Map<String, int> map = {'age': 12};
      homeMethodChannel.invokeMethod(POP_NOTICE, map);
    }
  }

  /// 前往 Android Personal 页面
  navAndroidPersonal() {
    Map<String, String> map = {'name': '李四'};
    homeMethodChannel.invokeMethod(NAV_ANDROID_PERSONAL_NOTICE, map);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
          backgroundColor: Colors.blue,
          title: const Text(
            'Flutter Home',
            style: TextStyle(
                fontWeight: FontWeight.w500,
                fontSize: 26,
                color: Colors.yellow),
          )),
      body: SizedBox(
        width: MediaQuery.of(context).size.width,
        height: MediaQuery.of(context).size.height,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Padding(
              padding: const EdgeInsets.only(bottom: 16),
              child: RichText(
                  text: TextSpan(
                      text: '接收初始化参数：',
                      style: const TextStyle(color: Colors.black, fontSize: 20),
                      children: [
                    TextSpan(
                      text: name,
                      style: const TextStyle(color: Colors.red,fontWeight: FontWeight.bold),
                    )
                  ])),
            ),
            Padding(
              padding: const EdgeInsets.only(bottom: 16),
              child: RichText(
                  text: TextSpan(
                      text: '接收上一页返回参数：',
                      style: const TextStyle(color: Colors.black, fontSize: 20),
                      children: [
                    TextSpan(
                      text: age,
                      style: const TextStyle(color: Colors.red,fontWeight: FontWeight.bold),
                    )
                  ])),
            ),
            Padding(
              padding: const EdgeInsets.only(bottom: 8),
              child: ElevatedButton(
                onPressed: navAndroidPersonal,
                child: const Text(
                  '前往 Android Personal',
                  style: TextStyle(fontSize: 20),
                ),
              ),
            ),
            ElevatedButton(
              onPressed: popPage,
              child: const Text(
                '返回 上一页',
                style: TextStyle(fontSize: 20),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
