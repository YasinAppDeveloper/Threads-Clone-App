data class UserModel(
    val id:String,
    val name:String,
    val userName:String,
    val bio:String,
    val email:String,
    val password:String,
    val imageProfile:String?=null
){
    constructor() : this ("","","","","","","")
}
