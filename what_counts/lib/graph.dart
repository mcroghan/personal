import 'package:flutter/material.dart';

import 'globals.dart';

class Graph extends StatefulWidget {
  Graph({ Key key, this.title, this.switchToCountersTabCallback }) : super(key: key);

  final String title;
  final Function switchToCountersTabCallback;

  @override
  _GraphState createState() => _GraphState(title, switchToCountersTabCallback);
}

class _GraphState extends State<Graph> {
  _GraphState(this._title, this._switchToCountersTabCallback);

  final String _title;
  final Function _switchToCountersTabCallback;
  final Counters _counters = Counters();

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget> [
            Padding(padding: EdgeInsets.symmetric(vertical: 10.0),
                child: Text(_title, style: Theme.of(context).textTheme.headline)
            ),
            IconButton(
                icon: Icon(Icons.edit),
                onPressed: () => _switchToCountersTabCallback(context, _title),
            )
          ]
        ),
        ListView(
          shrinkWrap: true,
          physics: NeverScrollableScrollPhysics(),
          children: List.generate(_counters.list.length, (index) {
            String counterName = _counters.list[index];
            String counterKey = Util.buildCounterKey(counterName, dateString: _title);
            int counterValue = App.localStorage.getInt(counterKey) ?? 0;
            return Text("$counterName: $counterValue",
              textAlign: TextAlign.center,
            );
          })
        )
      ],
    );
  }
}
