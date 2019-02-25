import 'package:flutter/material.dart';
import 'package:numberpicker/numberpicker.dart';

import 'resources.dart';
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

  @override
  Widget build(BuildContext context) {
    return Column();
  }
}
