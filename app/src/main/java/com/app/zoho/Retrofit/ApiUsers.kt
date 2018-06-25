package com.app.zoho.Retrofit

import com.app.zoho.UsersResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sun, 08/04/2018 at 17:56.
 */

/*interface ApiUsers {

    @GET("/api/users?page=1")
    fun getUsers() : Observable<UsersResponse>

}*/
interface ApiUsers {
    @GET("api/users")
    fun getUsers(@Query("page") page:String): Observable<UsersResponse>

    /*@GET("api/users?page=2")
    fun pageb(): Observable<UsersResponse>

    @GET("api/users?page=3")
    fun pagec(): Observable<UsersResponse>

    @GET("api/users?page=4")
    fun paged(): Observable<UsersResponse>*/
}
/*
interface  ApiUsers{
fun pagea(){
    @GET("/api/users?page=1")
    fun getUsers() : Observable<UsersResponse> {
        return getUsers()
        pageb()
    }
}

fun pageb(){
    @GET("/api/users?page=2")
    fun getUsers() : Observable<UsersResponse> {
        return getUsers()
        pagec()
    }
}
fun pagec(){
    @GET("/api/users?page=3")
    fun getUsers() : Observable<UsersResponse> {
        return getUsers()
        paged()
    }
}
fun paged(){
    @GET("/api/users?page=3")
    fun getUsers() : Observable<UsersResponse> {
        return getUsers()

    }
}}
*/
