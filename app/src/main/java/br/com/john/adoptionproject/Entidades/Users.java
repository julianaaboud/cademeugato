package br.com.john.adoptionproject.Entidades;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import br.com.john.adoptionproject.DAO.ConfiguracaoFirebase;

/**
 * Created by John on 02/12/2017.
 */

public class Users {

    private String id,email,password,name,surname,sex;

    public Users() {
    }

    public void salvar(){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuario").child(String.valueOf(getId())).setValue(this);
    }

    @Exclude
    public Map<String, Object> toMap(){

        HashMap<String, Object> hashMapUsuario = new HashMap<>();

        hashMapUsuario.put ("id", getId());
        hashMapUsuario.put ("email", getEmail());
        hashMapUsuario.put ("password", getPassword());
        hashMapUsuario.put ("name", getName());
        hashMapUsuario.put ("surname", getSurname());
        hashMapUsuario.put ("sex", getSex());

        return hashMapUsuario;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
