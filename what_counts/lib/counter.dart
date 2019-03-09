import 'package:flutter/material.dart';
import 'package:numberpicker/numberpicker.dart';

import 'globals.dart';

class Counter extends StatefulWidget {
  Counter({ Key key, this.title, this.dateString, this.deleteCounterCallback }) : super(key: key);

  final String title;
  final String dateString;
  final Function deleteCounterCallback;

  @override
  _CounterState createState() => _CounterState(title, dateString, deleteCounterCallback);
}

class _CounterState extends State<Counter> {
  _CounterState(this._title, this._dateString, this._deleteCounterCallback);

  final String _title;
  final String _dateString;
  final Function _deleteCounterCallback;

  int _currentValue = 0;

  void _setCounterState(int newValue) {
    setState(() {
      _currentValue = newValue;
    });
  }

  void _saveCounterState(int newValue) async {
    _setCounterState(newValue);

    App.localStorage.setInt(Util.buildCounterKey(_title, dateString: _dateString), newValue);
    build(context);
  }

  @override
  Widget build(BuildContext context) {
    _setCounterState(App.localStorage.getInt(Util.buildCounterKey(_title, dateString: _dateString)) ?? 0);

    return Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget> [
              Padding(
                padding: EdgeInsets.symmetric(vertical: 10.0),
                child: Text(_title, style: Theme.of(context).textTheme.headline)
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
            onChanged: (newValue) => _saveCounterState(newValue),
          ),
        ]
    );
  }
}
