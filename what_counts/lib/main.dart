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
  RegExp _validationRegEx = new RegExp(r"^[a-zA-Z0-9 ]{1,10}$");
  bool _isTitleValid = true;

  List<String> _counters = App.localStorage.getStringList(Strings.counterDataKey) ?? new List();

  void deleteCounter(BuildContext context, String counterName) {
    setState(() {
      _counters.remove(counterName);
    });

    App.localStorage.setStringList(Strings.counterDataKey, _counters);
    build(context);
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
              children: List.generate(_counters.length, (index) {
                return Counter(
                  title: _counters[index],
                  deleteCounterCallback: deleteCounter,
                );
              })
            ),
            Icon(Icons.show_chart),
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
                  errorText: _isTitleValid ? null : "Names must be 1-10 letters/numbers/spaces, and unique",
                ),
                onSubmitted: (newValue) {
                  setState(() {
                    _isTitleValid = !_counters.contains(newValue) && _validationRegEx.hasMatch(newValue);
                  });

                  if (_isTitleValid) {
                    Navigator.pop(context, newValue);
                    
                    setState(() {
                      _counters.add(newValue);
                    });

                    App.localStorage.setStringList(Strings.counterDataKey, _counters);
                    build(context);
                  }
                }
              )
            ),
          ),
        ),
      ),
    );
  }
}
