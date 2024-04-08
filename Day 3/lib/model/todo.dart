class ToDo {
  int id;
  String title;
  String description;
  bool completed;

  ToDo({
    required this.id,
    required this.title,
    required this.description,
    this.completed = false,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'title': title,
      'description': description,
      'completed': completed ? 1 : 0,
    };
  }

  static ToDo fromMap(Map<String, dynamic> map) {
    return ToDo(
      id: map['id'],
      title: map['title'],
      description: map['description'],
      completed: map['completed'] == 1 ? true : false,
    );
  }

  ToDo copyWith({
    int? id,
    String? title,
    String? description,
    bool? completed,
  }) {
    return ToDo(
      id: id ?? this.id,
      title: title ?? this.title,
      description: description ?? this.description,
      completed: completed ?? this.completed,
    );
  }
}
