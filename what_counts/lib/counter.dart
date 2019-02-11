import 'package:flutter/material.dart';
import 'package:numberpicker/numberpicker.dart';

import 'resources.dart';
import 'globals.dart';

class Counter extends StatefulWidget {
  Counter({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _CounterState createState() => _CounterState(title);
}

String scrubTitle(String unscrubbed) {
  return unscrubbed.toLowerCase().replaceAll("\W+", '');
}

String buildCounterKey(String counterTitle) {
  return Strings.counter_title_key + "_" + scrubTitle(counterTitle);
}

class _CounterState extends State<Counter> {
  _CounterState(this._title);

  final String _title;

  void _setCounter(int newValue) {
    App.localStorage.setInt(buildCounterKey(_title), newValue);
    build(context);
  }

  @override
  Widget build(BuildContext context) {
    return Column(
        children: <Widget>[
          Padding(padding: EdgeInsets.fromLTRB(0.0, 10.0, 0.0, 10.0),
            child: Text(_title, style: TextStyle(fontWeight: FontWeight.bold, fontSize: 20.0))
          ),
          NumberPicker.integer(
            initialValue: App.localStorage.getInt(buildCounterKey(_title)) ?? 0,
            minValue: 0,
            maxValue: 20,
            onChanged: (newValue) => _setCounter(newValue),
          ),
        ]
    );
  }
}
