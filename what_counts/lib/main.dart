import 'dart:async';

import 'package:flutter/material.dart';
import 'package:numberpicker/numberpicker.dart';
import 'package:what_counts/resources.dart';
import 'globals.dart';

void main() => start();

Future start() async {
  await App.init();
  runApp(WhatCounts());
}

class WhatCounts extends StatefulWidget {
  WhatCounts({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _WhatCountsState createState() => _WhatCountsState();
}

class _WhatCountsState extends State<WhatCounts> {
  int _numCounters = App.localStorage.getInt(Strings.num_counters_key) ?? 0;

  void _addCounter() {
    setState(() {
      _numCounters++;
      App.localStorage.setInt(Strings.num_counters_key, _numCounters);
      build(context);
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: Strings.app_title,
      theme: ThemeData(
        primarySwatch: Colors.purple,
      ),
      debugShowCheckedModeBanner: false,
      home:  DefaultTabController(
        length: 2,
        child: Scaffold(
          appBar: AppBar(
            bottom: TabBar(
              tabs: [
                Tab(icon: Icon(Icons.plus_one)),
                Tab(icon: Icon(Icons.show_chart)),
              ],
            ),
            title: Text(Strings.app_title),
          ),
          body: TabBarView(
            children: <Widget>[
              GridView.count(
                crossAxisCount: 2,
                children: List.generate(_numCounters, (index) {
                  return Center(
                    child: new NumberPicker.integer(
                      initialValue: 0,
                      minValue: 0,
                      maxValue: 20,
                      onChanged: null,
                    ),
                  );
                })
              ),
              Icon(Icons.show_chart),
            ],
          ),
          floatingActionButton: FloatingActionButton(
            onPressed: _addCounter,
            tooltip: 'Add a new counter',
            child: Icon(Icons.add),
          ),
        ),
      ),
    );
  }
}
