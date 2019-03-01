import 'package:shared_preferences/shared_preferences.dart';

class App {
  static SharedPreferences localStorage;
  static Future init() async {
    localStorage = await SharedPreferences.getInstance();
  }
}

class Util {
  static String formatDateTime(DateTime dateTime) {
    return dateTime.toIso8601String().substring(0, 10);
  }

  static String buildCounterKey(String counterTitle, { String dateString }) {
    return Strings.counterCountKey
        + "_"
        + counterTitle
        + "_"
        + (dateString ?? formatDateTime(DateTime.now()));
  }
}

class Strings {
  static String appTitle = "WhatCountsForYou";

  static String counterDataKey = "counter_data";
  static String counterCountKey = "counter_count";
}

class Counters {
  static final Counters _singleton = Counters._internal();

  List<String> list;

  factory Counters() {
    return _singleton;
  }

  Counters._internal() {
    list = App.localStorage.getStringList(Strings.counterDataKey) ?? List();
  }
}
