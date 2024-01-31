import 'package:flutter/material.dart';
import 'package:flutter_nav_android/book.dart';
import 'package:flutter_nav_android/student.dart';

import 'home.dart';
import 'login.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
            seedColor: Colors.deepPurple,
         ),
        useMaterial3: true,
      ),
      routes: {
        '/home': (context) => const Home(),
        '/login': (context) => const Login(),
        '/book': (context) => const Book(),
        '/student': (context) => const Student(),
      },
      initialRoute: '/home',
    );
  }
}
