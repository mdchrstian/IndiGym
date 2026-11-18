package id.indigym.service;

import id.indigym.model.Admin;

public class Sesi {

    public static Admin adminLogin = null;

    public static boolean sudahLogin() {
        return adminLogin != null;
    }
}