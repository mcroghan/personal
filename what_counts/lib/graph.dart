import 'package:flutter/material.dart';
import 'package:charts_flutter/flutter.dart' as charts;

import 'globals.dart';

class Graph extends StatefulWidget {
  Graph({ Key key, this.title, this.switchToCountersTabCallback }) : super(key: key);

  final String title;
  final Function switchToCountersTabCallback;

  @override
  _GraphState createState() => _GraphState(title, switchToCountersTabCallback);
}

class CounterValue {
  final String counterName;
  final int value;
  final int maxValue;
  final charts.Color color;

  CounterValue(this.counterName, this.value, this.maxValue, Color color)
      : this.color = new charts.Color(
      r: color.red, g: color.green, b: color.blue, a: color.alpha);
}

class _GraphState extends State<Graph> {
  _GraphState(this._title, this._switchToCountersTabCallback);

  final String _title;
  final Function _switchToCountersTabCallback;
  final Counters _counters = Counters();

  final Map<int, List<int>> _barColors = {
    1: [500],
    2: [100, 900],
    3: [100, 500, 900],
    4: [100, 300, 600, 900],
    5: [100, 200, 400, 600, 900],
    6: [100, 200, 300, 500, 700, 900],
    7: [100, 200, 300, 400, 500, 700, 900],
    8: [100, 200, 300, 400, 500, 600, 700, 900],
    9: [100, 200, 300, 400, 500, 600, 700, 800, 900],
    10: [50, 100, 200, 300, 400, 500, 600, 700, 800, 900],
  };

  @override
  Widget build(BuildContext context) {
    var data = List.generate(_counters.list.length, (index) {
      String counterName = _counters.list[index];
      String counterKey = Util.buildCounterKey(counterName, dateString: _title);
      int counterValue = App.localStorage.getInt(counterKey) ?? 0;
      return CounterValue(
          counterName,
          counterValue,
          Ints.maxCounterValue,
          Hues.primarySwatch[_barColors[_counters.list.length][index]]
      );
    });

    var series = [
      charts.Series(
        domainFn: (CounterValue counterData, _) => counterData.counterName,
        measureFn: (CounterValue counterData, _) => counterData.value,
        measureUpperBoundFn: (CounterValue counterData, _) => counterData.maxValue,
        colorFn: (CounterValue counterData, _) => counterData.color,
        id: 'Counters',
        data: data,
      ),
    ];

    return Column(
      children: <Widget>[
        Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget> [
              Text(Util.formatDateStringForHumans(_title),
                  style: Theme.of(context).textTheme.headline
              ),
              IconButton(
                icon: Icon(Icons.edit),
                onPressed: () => _switchToCountersTabCallback(context, _title),
              )
            ]
        ),
        Padding(
          padding: EdgeInsets.symmetric(horizontal: 10),
          child: SizedBox(
            height: _counters.list.length * 20.0,
            child: charts.BarChart(
              series,
              animate: true,
              vertical: false,
              primaryMeasureAxis: charts.NumericAxisSpec(
                tickProviderSpec: charts.BasicNumericTickProviderSpec(
                  dataIsInWholeNumbers: true,
                  desiredTickCount: Ints.maxCounterValue + 1,
                ),
              ),
            ),
          ),
        ),
      ]
    );
  }
}


