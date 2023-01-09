package com.appsinventiv.verifype.Utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants {
    public static boolean ACCEPTED=false;
    public static boolean REQUEST_RECEIVED=false;
    public static boolean OPTION_CLICKED=false;
    public static String VERIFY_OR_REPORT="verify";
    public static DatabaseReference M_DATABASE= FirebaseDatabase.getInstance("https://verifipe-default-rtdb.firebaseio.com/").getReference();

}
