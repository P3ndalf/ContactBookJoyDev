package com.example.contactbook.data.Model

class ContactModel(id : String, contactName : String, phoneNumber : String,
                   gender : Boolean, instagram : String, ownerId : String) {

    var id : String
    var contactName : String
    var phoneNumber : String
    var gender : Boolean
    var instagram : String
    var ownerId : String

    init{
        this.id = id
        this.contactName = contactName
        this.phoneNumber = phoneNumber
        this.gender = gender
        this.instagram = instagram
        this.ownerId = ownerId
    }
}