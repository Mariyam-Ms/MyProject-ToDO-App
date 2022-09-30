package com.project1.todo.utilsRecycle;

public class DataClass {
    String todo;
    String taskid;
    String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public DataClass() {
        this.todo = todo;
        this.taskid = taskid;
        this.imageUrl=imageUrl;
    }
    public DataClass(String imageUrl){
        this.imageUrl=imageUrl;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
}
