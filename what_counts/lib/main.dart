import 'dart:async';

import 'package:flutter/material.dart';

import 'globals.dart';
import 'counter.dart';
import 'graph.dart';

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
          textTheme: TextTheme(
            headline: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 20.0
            )
          )
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

class _HomeState extends State<Home> with SingleTickerProviderStateMixin {
  static const int history_days = 14;
  RegExp _validationRegEx = RegExp(r"^[a-zA-Z0-9 ]{1,10}$");
  bool _isCounterNameValid = true;

  Counters _counters = Counters();
  TabController _tabController;
  ScrollController _graphScrollController = ScrollController();

  @override
  void initState() {
    super.initState();
    _tabController = TabController(vsync: this, length: 2);
    _tabController.addListener(() {
      if (_tabController.index == 1) {
        _graphScrollController.animateTo(_graphScrollController.position.maxScrollExtent,
          curve: Curves.ease,
          duration: Duration(seconds: 1),
        );
      }
    });
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  void deleteCounter(BuildContext context, String counterName) {
    setState(() {
      _counters.list.remove(counterName);
    });

    App.localStorage.setStringList(Strings.counterDataKey, _counters.list);
    build(context);
  }

  void addCounter(BuildContext context, String submittedCounterName) {
    setState(() {
      _isCounterNameValid =
          !_counters.list.contains(submittedCounterName)
          && _validationRegEx.hasMatch(submittedCounterName);
    });

    if (_isCounterNameValid) {
      Navigator.pop(context, submittedCounterName);

      setState(() {
        _counters.list.add(submittedCounterName);
      });

      App.localStorage.setStringList(Strings.counterDataKey, _counters.list);
      build(context);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        bottom: TabBar(
          controller: _tabController,
          tabs: [
            Tab(icon: Icon(Icons.plus_one)),
            Tab(icon: Icon(Icons.insert_chart)),
          ],
        ),
        title: Text(Strings.appTitle),
      ),
      body: TabBarView(
        controller: _tabController,
        children: <Widget>[
          GridView.count(
            crossAxisCount: 2,
            children: List.generate(_counters.list.length, (index) {
              String title = _counters.list[index];
              return Counter(
                title: title,
                key: Key(title),
                deleteCounterCallback: deleteCounter,
              );
            })
          ),
          ListView(
            controller: _graphScrollController,
            children: List.generate(history_days, (index) {
              Duration days = Duration(days: -(history_days - index - 1));
              String title = Util.formatDateTime(DateTime.now().add(days));
              return Padding(
                padding: EdgeInsets.symmetric(vertical: 10.0),
                child: Graph(
                  title: title,
                  key: Key(title),
                )
              );
            })
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        tooltip: 'Add a new counter',
        child: Icon(Icons.add),
        onPressed: () => showDialog<String>(
          context: context,
          builder: (context) => Dialog(
            child: TextField(
              autofocus: true,
              decoration: InputDecoration(
                border: OutlineInputBorder(),
                hintText: "Please enter a counter name",
                errorText: _isCounterNameValid
                    ? null
                    : "Names must be 1-10 letters/numbers/spaces, and unique",
              ),
              onSubmitted: (submittedCounterName) => addCounter(context, submittedCounterName),
            )
          ),
        ),
      ),
    );
  }
}
