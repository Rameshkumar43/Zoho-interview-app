package com.app.zoho

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import android.content.SharedPreferences
import com.app.zoho.Model.Model
import com.app.zoho.Retrofit.ApiUsers
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var userAdapter: UsersAdapter
    var gson = Gson()
    var page: String = "1";
    val PREFERENCE_NAME = "Shareduser"
    lateinit var editor: SharedPreferences.Editor;
    private val users: MutableList<Model> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkinfo = cm.activeNetworkInfo
        val preferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        editor = preferences.edit();
        val sp = preferences.getString("userlist", null)
        if (sp != null) {
            getDatafromPreference();
        } else if (networkinfo != null && networkinfo.isConnected) {
            getUser_fromURL();

        } else {
            Toast.makeText(baseContext, "no internet", Toast.LENGTH_SHORT).show()
        }

    }

    fun getDatafromPreference() {
        userAdapter = UsersAdapter()
        users_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        users_list.adapter = userAdapter
        val preferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        var json = preferences.getString("userlist", null)
        if (json != null) {
            val data: List<Model> = gson.fromJson(json, Array<Model>::class.java).toList()
            users.addAll(data)
            userAdapter.notifyDataSetChanged()
        }
    }

    private fun getUser_fromURL() {
        userAdapter = UsersAdapter()
        users_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        users_list.adapter = userAdapter


        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://reqres.in")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val apiUsers = retrofit.create(ApiUsers::class.java)
      apiUsers.getUsers(page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe({
                    userAdapter.setUsers(it.data)
                    if (page == "1") {
                        page = "2"
                        getUser_fromURL();
                    } else if (page == "2") {
                        page = "3";
                        getUser_fromURL();
                    } else if (page == "3") {
                        page = "4";
                        getUser_fromURL();
                    } else if (page == "4") {
                        //users
                        val ff = gson.toJson(users)
                        editor.putString("userlist", ff)
                        editor.apply()

                    }
               },

                        {
                            Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                        })

    }

    inner class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            return UserViewHolder(layoutInflater.inflate(R.layout.userdata_layout, parent, false))
        }

        override fun getItemCount(): Int {
            return users.size
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            holder.bindModel(users[position])
        }

        fun setUsers(data: List<Model>) {
            users.addAll(data)
            notifyDataSetChanged()
        }

        inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val firstNameTxt: TextView = itemView.findViewById(R.id.firstName)
            val lastNameTxt: TextView = itemView.findViewById(R.id.lastName)
            val profileImage: ImageView = itemView.findViewById(R.id.profile)

            fun bindModel(model: Model) {
                firstNameTxt.text = model.first_name
                lastNameTxt.text = model.last_name
                Picasso.get().load(model.avatar).into(profileImage)
            }
        }
    }

}

