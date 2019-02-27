import 'package:flutter/material.dart';

class Graph extends StatefulWidget {
  Graph({ Key key, this.title }) : super(key: key);

  final String title;

  @override
  _GraphState createState() => _GraphState(title);
}

class _GraphState extends State<Graph> {
  _GraphState(this._title);

  final String _title;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        Padding(padding: EdgeInsets.symmetric(vertical: 10.0),
            child: Text(_title, style: Theme.of(context).textTheme.headline)
        ),
        Icon(Icons.insert_chart)
      ],
    );
  }
}
