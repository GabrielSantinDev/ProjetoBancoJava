package com.projetodb.projetobancojava.control;

import com.projetodb.projetobancojava.model.Funcionario;

public class UserControl {

    private static Funcionario user;

    public static Funcionario getUser() {
        return user;
    }

    public static void setUser(Funcionario user) {
        UserControl.user = user;
    }
}
