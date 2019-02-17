import 'dart:async';

import 'package:flutter/material.dart';

import 'resources.dart';
import 'globals.dart';
import 'counter.dart';

void main() => start();

Future start() async {
  await App.init();
  runApp(WhatCounts(title: Strings.appTitle));
}

class WhatCounts extends StatelessWidget {
  WhatCounts({Key key, this.title}) : super(key: key);

  final String title;

  Widget build(BuildContext context) {
    return MaterialApp(
        title: Strings.appTitle,
        theme: ThemeData(
          primarySwatch: Colors.purple,
        ),
        debugShowCheckedModeBanner: false,
        home: Home()
    );
  }
}

class Home extends StatefulWidget {
  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {
  int _numCounters = App.localStorage.getInt(Strings.numCountersKey) ?? 0;

  String buildCounterTitle(int counterIndex) {
    return "${Strings.counterTitleKey}$counterIndex";
  }

  void _addCounter(String title) async {
    setState(() => _numCounters++);

    App.localStorage.setInt(Strings.numCountersKey, _numCounters);
    App.localStorage.setString(buildCounterTitle(_numCounters - 1), title);
    build(context);
  }

  void showAddCounterDialog({ child: Widget }) {
    showDialog<String>(
      context: context,
      builder: (context) => child
    ).then<void>((String title) => _addCounter(title));
  }

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 2,
      child: Scaffold(
        appBar: AppBar(
          bottom: TabBar(
            tabs: [
              Tab(icon: Icon(Icons.plus_one)),
              Tab(icon: Icon(Icons.show_chart)),
            ],
          ),
          title: Text(Strings.appTitle),
        ),
        body: TabBarView(
          children: <Widget>[
            GridView.count(
              crossAxisCount: 2,
              children: List.generate(_numCounters, (index) {
                return Counter(
                    title: App.localStorage.getString(buildCounterTitle(index)),
                );
              })
            ),
            Icon(Icons.show_chart),
          ],
        ),
        floatingActionButton: FloatingActionButton(
          tooltip: 'Add a new counter',
          child: Icon(Icons.add),
          onPressed: () => showAddCounterDialog(
            child: Dialog(
                child: TextField(
                  autofocus: true,
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                    hintText: Strings.addCounterHint,
                  ),
                  onSubmitted: (newValue) => Navigator.pop(context, newValue),
                )
            ),
          ),
        ),
      ),
    );
  }
}
