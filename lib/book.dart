import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class Book extends StatefulWidget {
  const Book({super.key});

  @override
  State<Book> createState() => _BookState();
}

class _BookState extends State<Book> {

  String title = '';

  static const String METHOD_CHANNEL_BOOK = 'com.example.flutter_nav_android/book/method';
  static const String NAV_FLUTTER_BOOK_NOTICE = 'navFlutterBookNotice';

  @override
  void initState() {
    super.initState();
    MethodChannel bookMethodChannel = const MethodChannel(METHOD_CHANNEL_BOOK);
    bookMethodChannel.setMethodCallHandler(methodHandler);
  }

  /// 监听来自 Android端 的消息通道
  /// Android端调用了函数，这个handler函数就会被触发
  Future<dynamic> methodHandler(MethodCall call) async {
    final String methodName = call.method;
    switch (methodName) {
      case NAV_FLUTTER_BOOK_NOTICE: // 进入当前页面
        {
          title = call.arguments['title'];
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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.amberAccent,
      body: Container(
        width: MediaQuery.of(context).size.width,
        height: MediaQuery.of(context).size.height,
        alignment: Alignment.center,
        child: Text(
          'Flutter $title',
          style: const TextStyle(
            fontWeight: FontWeight.bold,
            color: Colors.red,
            fontSize: 20,
          ),
        ),
      ),
    );
  }

}
