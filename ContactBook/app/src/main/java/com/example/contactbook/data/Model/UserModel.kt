package com.example.contactbook.data.Model

class UserModel(id : String, name : String, lastName : String, email : String) {
    var id : String;
    var name : String;
    var lastName : String;
    var email : String;

    init {
        this.id = id
        this.name = name
        this.lastName = lastName
        this.email = email
    }

}