package edu.gvsu.cis.videre;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * This is a singleton class that contains the current user's info.
 */
public class CurrentSession {

    public FirebaseUser getUser() {
        return currentUser;
    }

    public void setUser(FirebaseUser currentUser) {
        this.currentUser = currentUser;
    }

    // The user that is currently logged in.
    private FirebaseUser currentUser;

    public DatabaseReference getDatabaseRef() {
        return databaseRef;
    }

    public void setDatabaseRef(DatabaseReference databaseRef) {
        this.databaseRef = databaseRef;
    }

    // The reference to the main database.
    private DatabaseReference databaseRef;
    // Singleton object of the user.
    private static CurrentSession single_instance = null;

    public static CurrentSession getInstance()
    {
        if (single_instance == null)
            single_instance = new CurrentSession();

        return single_instance;
    }
}
