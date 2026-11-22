package id.indigym.service;

import id.indigym.model.Admin;

// Menyimpan siapa yang sedang login. Cukup satu variabel karena satu laptop = satu petugas.
public class Sesi {

    public static Admin adminLogin = null;

    public static boolean sudahLogin() {
        return adminLogin != null;
    }
}
