package com.example.vender;

import com.example.vender.Model.User;

public class GlobalUser {
    private static GlobalUser instance;

    private User user;

    private GlobalUser(){};

    public User getUser(){
        return  this.user;
    }

//    public void setUser(String nama, String email, String password, String foto, int id, String istalent){
//        user = new User(nama, email, password, foto, id, istalent);
//    }

    public void setUser(User user){
        this.user = user;
    }

    public void setIstalent(String istalent){
        this.user.setIstalent(istalent);
    }

    public static synchronized GlobalUser getInstance(){
        if(instance==null){
            instance=new GlobalUser();
        }
        return instance;
    }

}
