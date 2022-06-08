package com.kapilguru.trainer

enum class UserRole {

    STUDENT {
        override fun roleId(): Int = 3 // Student
    },

    TRAINER {
        override fun roleId(): Int = 2 //Trainer
    };
    abstract fun roleId(): Int
}