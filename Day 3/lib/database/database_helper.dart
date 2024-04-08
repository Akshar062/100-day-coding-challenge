import 'dart:async';
import 'package:sqflite/sqflite.dart';
import 'package:path_provider/path_provider.dart';
import 'package:path/path.dart';

class DatabaseHelper {
  static final DatabaseHelper _instance = DatabaseHelper.internal();

  factory DatabaseHelper() => _instance;

  static Database? _db;

  Future<Database?> get db async {
    if (_db != null) {
      return _db;
    }
    _db = await initDb();
    return _db;
  }

  DatabaseHelper.internal();

  Future<Database> initDb() async {
    final directory = await getApplicationDocumentsDirectory();
    final path = join(directory.path, 'todo.db');

    var theDb = await openDatabase(path, version: 1, onCreate: _onCreate);
    return theDb;
  }

  void _onCreate(Database db, int newVersion) async {
    await db.execute('''
      CREATE TABLE Todos(
        id INTEGER PRIMARY KEY,
        title TEXT,
        description TEXT,
        completed INTEGER
      )
    ''');
  }

  Future<int> insertTodo(Map<String, dynamic> row) async {
    Database? dbClient = await db;
    return await dbClient!.insert('Todos', row);
  }

  Future<List<Map<String, dynamic>>> getAllTodos() async {
    Database? dbClient = await db;
    return await dbClient!.query('Todos');
  }

  Future<int> update(Map<String, dynamic> row) async {
    Database? dbClient = await db;
    return await dbClient!.update('Todos', row,
        where: 'id = ?', whereArgs: [row['id']]);
  }

  Future<int> delete(int id) async {
    Database? dbClient = await db;
    return await dbClient!.delete('Todos', where: 'id = ?', whereArgs: [id]);
  }
}
