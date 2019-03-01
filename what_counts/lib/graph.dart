import 'package:flutter/material.dart';

import 'globals.dart';

class Graph extends StatefulWidget {
  Graph({ Key key, this.title }) : super(key: key);

  final String title;

  @override
  _GraphState createState() => _GraphState(title);
}

class _GraphState extends State<Graph> {
  _GraphState(this._title);

  final String _title;
  final Counters _counters = Counters();

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        Padding(padding: EdgeInsets.symmetric(vertical: 10.0),
            child: Text(_title, style: Theme.of(context).textTheme.headline)
        ),
        ListView(
          shrinkWrap: true,
          children: List.generate(_counters.list.length, (index) {
            String counterName = _counters.list[index];
            int counterValue = App.localStorage.getInt(Util.buildCounterKey(counterName, dateString: _title)) ?? 0;
            return Text("$counterName: $counterValue",
              textAlign: TextAlign.center,
            );
          })
        )
      ],
    );
  }
}
