import 'dart:ffi';

import 'package:flutter/material.dart';
import '../database/database_helper.dart';
import '../model/todo.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  List<ToDo> todos = [];
  late DatabaseHelper databaseHelper;

  @override
  void initState() {
    super.initState();
    databaseHelper = DatabaseHelper();
    _loadTodos();
  }

  _loadTodos() async {
    List<Map<String, dynamic>> rows = await databaseHelper.getAllTodos();
    setState(() {
      todos = rows.map((row) => ToDo.fromMap(row)).toList();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('To Do App'),
      ),
      body: ListView.builder(
        itemCount: todos.length,
        itemBuilder: (context, index) {
          return ListTile(
            tileColor: Colors.black12,
            shape: const RoundedRectangleBorder(
              borderRadius: BorderRadius.all(Radius.circular(10)),
            ),
            onTap: () {
              // Handle tapping on a to-do item
              _showAddTodoBottomSheet(todos[index]);
            },
            onLongPress: () {
              // Handle long-pressing on a to-do item
              _showDeleteConfirmationDialog(index);
            },
            title: Text(todos[index].title),
            trailing: Checkbox(
              value: todos[index].completed,
              onChanged: (bool? value) {
                // Update completion status
                _updateTodoCompletion(index, value ?? false);
              },
            ),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          _showAddTodoBottomSheet(null);
        },
        child: const Icon(Icons.add),
      ),
    );
  }

  void _showAddTodoBottomSheet(ToDo? todo) {
    TextEditingController titleController = TextEditingController();
    TextEditingController descriptionController = TextEditingController();
    bool completed = false;

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      builder: (BuildContext context) {
        return SingleChildScrollView(
          child: Container(
            padding: EdgeInsets.only(
              bottom: MediaQuery.of(context).viewInsets.bottom,
            ),
            child: Padding(
              padding:
                  const EdgeInsets.symmetric(horizontal: 10.0, vertical: 5.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisSize: MainAxisSize.min,
                children: [
                  Row(
                    children: [
                      Text(
                        'Add To-Do',
                        style: Theme.of(context).textTheme.titleLarge,
                      ),
                      const Spacer(),
                      IconButton(
                        icon: const Icon(Icons.close),
                        onPressed: () {
                          Navigator.pop(context);
                        },
                      ),
                    ],
                  ),
                  const SizedBox(height: 16),
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16.0),
                    child: TextField(
                      controller: titleController,
                      decoration: InputDecoration(
                        border: const OutlineInputBorder(),
                        labelText: todo != null ? todo.title : 'Title',
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16.0),
                    child: TextField(
                      controller: descriptionController,
                      decoration: InputDecoration(
                        border: const OutlineInputBorder(),
                        labelText:
                            todo != null ? todo.description : 'Description',
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16.0),
                    child: Row(
                      children: [
                        const Text('Completed'),
                        Checkbox(
                          value: false,
                          onChanged: (bool? value) {
                            // Handle checkbox value change
                            completed = value ?? false;
                          },
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 16),
                  Align(
                    alignment: Alignment.centerRight,
                    child: ElevatedButton(
                      onPressed: () {
                        // Add new to-do item
                        if (todo != null) {
                          _updateTodo(
                            todo.id,
                            titleController.text,
                            descriptionController.text,
                            completed,
                          );
                        } else {
                          _addTodo(
                            titleController.text,
                            descriptionController.text,
                            completed,
                          );
                        }
                        Navigator.pop(context);
                      },
                      child: Text(todo != null ? 'Update' : 'Add'),
                    ),
                  ),
                ],
              ),
            ),
          ),
        );
      },
    );
  }

  void _updateTodo(
      int id, String title, String description, bool completed) async {
    // Update to-do item in the database
    await databaseHelper.update({
      'id': id,
      'title': title,
      'description': description,
      'completed': completed,
    });

    // Refresh the to-do list
    _loadTodos();
  }

  void _deleteTodo(int index) async {
    // Delete to-do item from the database
    await databaseHelper.delete(todos[index].id);

    // Refresh the to-do list
    _loadTodos();
  }

  void _addTodo(String title, String description, bool completed) async {
    // Insert new to-do item into the database
    await databaseHelper.insertTodo({
      'title': title,
      'description': description,
      'completed': completed,
    });
    // Refresh the to-do list
    _loadTodos();
  }

  void _updateTodoCompletion(int index, bool completed) async {
    // Update completion status in the database
    await databaseHelper.update({
      'id': todos[index].id,
      'title': todos[index].title,
      'description': todos[index].description,
      'completed': completed ? 1 : 0,
    });
    // Update completion status in the UI
    setState(() {
      todos[index] = todos[index].copyWith(completed: completed);
    });
  }
  void _showDeleteConfirmationDialog(int index) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Delete To-Do'),
          content: const Text('Are you sure you want to delete this to-do?'),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.pop(context);
              },
              child: const Text('Cancel'),
            ),
            TextButton(
              onPressed: () {
                _deleteTodo(index);
                Navigator.pop(context);
              },
              child: const Text('Delete'),
            ),
          ],
        );
      },
    );
  }
}


