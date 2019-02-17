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
  return Strings.counterTitleKey + "_" + scrubTitle(counterTitle);
}

class _CounterState extends State<Counter> {
  _CounterState(this._title);

  int _currentValue = 0;

  final String _title;

  void _setCounter(int newValue, bool rebuild) async {
    setState(() => _currentValue = newValue);

    App.localStorage.setInt(buildCounterKey(_title), newValue);
    if (rebuild) build(context);
  }

  @override
  Widget build(BuildContext context) {
    _setCounter(App.localStorage.getInt(buildCounterKey(_title)) ?? 0, false);

    return Column(
        children: <Widget>[
          Padding(padding: EdgeInsets.fromLTRB(0.0, 10.0, 0.0, 10.0),
            child: Text(_title, style: TextStyle(fontWeight: FontWeight.bold, fontSize: 20.0))
          ),
          NumberPicker.integer(
            initialValue: _currentValue,
            minValue: 0,
            maxValue: 20,
            onChanged: (newValue) => _setCounter(newValue, true),
          ),
        ]
    );
  }
}
