import 'package:flutter/material.dart';
import 'package:numberpicker/numberpicker.dart';

import 'resources.dart';
import 'globals.dart';

class Counter extends StatefulWidget {
  Counter({ Key key, this.title, this.deleteCounterCallback }) : super(key: key);

  final String title;
  final Function deleteCounterCallback;

  @override
  _CounterState createState() => _CounterState(title, deleteCounterCallback);
}

String buildCounterKey(String counterTitle) {
  return Strings.counterCountKey + "_" + counterTitle;
}

class _CounterState extends State<Counter> {
  _CounterState(this._title, this._deleteCounterCallback);

  final String _title;
  final Function _deleteCounterCallback;

  int _currentValue = 0;

  void _setCounter(int newValue, bool rebuild) async {
    setState(() => _currentValue = newValue);

    App.localStorage.setInt(buildCounterKey(_title), newValue);
    if (rebuild) build(context);
  }

  @override
  Widget build(BuildContext context) {
    _setCounter(App.localStorage.getInt(buildCounterKey(_title)) ?? 0, false);

    return Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget> [
              Padding(padding: EdgeInsets.fromLTRB(0.0, 10.0, 0.0, 10.0),
                  child: Text(_title, style: TextStyle(fontWeight: FontWeight.bold, fontSize: 20.0))
              ),
              IconButton(
                icon: Icon(Icons.delete),
                onPressed: () => _deleteCounterCallback(context, _title)
              )
            ]
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
