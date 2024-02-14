package com.example.wavesoffood.Modelclass

class MenuItem {

    var foodname: String? = null
    var foodprice: String? = null
    var fooddescription: String? = null
    var imageurl :String?= null
    var ingredients : String? = null

    constructor()

    constructor(foodname : String? , foodprice : String? , fooddescription : String?, imageurl : String?,ingredients : String?)
    {
        this.foodname = foodname
        this.foodprice = foodprice
        this.fooddescription = fooddescription
        this.imageurl = imageurl
        this.ingredients = ingredients

    }
}