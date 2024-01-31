import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class Login extends StatefulWidget {
  const Login({super.key});

  @override
  State<Login> createState() => _LoginState();
}

class _LoginState extends State<Login> {
  late MethodChannel loginMethodChannel;

  String name = '';

  final String METHOD_CHANNEL_LOGIN = 'com.example.flutter_nav_android/login/method';
  static const String NAV_FLUTTER_LOGIN_NOTICE = 'navFlutterLoginNotice';
  final String POP_NOTICE = 'popNotice';

  @override
  void initState() {
    super.initState();
    loginMethodChannel = MethodChannel(METHOD_CHANNEL_LOGIN);
    loginMethodChannel.setMethodCallHandler(methodHandler);
  }

  /// 监听来自 Android端 的消息通道
  /// Android端调用了函数，这个handler函数就会被触发
  Future<dynamic> methodHandler(MethodCall call) async {
    final String methodName = call.method;
    switch (methodName) {
      case NAV_FLUTTER_LOGIN_NOTICE: // 进入当前页面
        {
          name = call.arguments['name'];
          setState(() {});
          return 0;
        }
      default:
        {
          return PlatformException(code: '-1', message: '未找到Flutter端具体实现函数', details: '具体描述'); // 返回给Android端
        }
    }
  }

  /// 销毁当前页面
  popPage() {
    if (Navigator.canPop(context)) { // 检查Flutter路由栈中，是否还有其他路由
      Navigator.pop(context);
    } else {
      Map<String, int> map = {'age': 28};
      loginMethodChannel.invokeMethod(POP_NOTICE, map);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
          backgroundColor: Colors.blue,
          title: const Text(
            'Flutter Login',
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
