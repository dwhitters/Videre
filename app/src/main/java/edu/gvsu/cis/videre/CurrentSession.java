package edu.gvsu.cis.videre;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * This is a singleton class that contains the current user's info.
 */
public class CurrentSession {

    public DatabaseReference getDatabaseRef() { return currentRef;}
    public FirebaseUser getUser() {
        return currentUser;
    }

    public void setUser(FirebaseUser currentUser) {
        this.currentUser = currentUser;
    }
    public void setDatabaseRef(DatabaseReference currentRef) { this.currentRef = currentRef;}

    // The user that is currently logged in.
    private FirebaseUser currentUser;
    private DatabaseReference currentRef;
    // Singleton object of the user.
    private static CurrentSession single_instance = null;

    public static CurrentSession getInstance()
    {
        if (single_instance == null)
            single_instance = new CurrentSession();

        return single_instance;
    }
}