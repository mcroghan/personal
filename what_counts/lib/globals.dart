import 'package:shared_preferences/shared_preferences.dart';

class App {
  static SharedPreferences localStorage;
  static Future init() async {
    localStorage = await SharedPreferences.getInstance();
  }
}

class Util {
  static String buildCounterKey(String counterTitle) {
    return Strings.counterCountKey + "_" + counterTitle + "_" + new DateTime.now().toIso8601String().substring(0,9);
  }
}

class Strings {
  static String appTitle = "WhatCountsForYou";

  static String counterDataKey = "counter_data";
  static String counterCountKey = "counter_count";
}
