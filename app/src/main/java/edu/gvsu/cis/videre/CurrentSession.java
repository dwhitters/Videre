package edu.gvsu.cis.videre;

import com.google.firebase.auth.FirebaseUser;

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
    // Singleton object of the user.
    private static CurrentSession single_instance = null;

    public static CurrentSession getInstance()
    {
        if (single_instance == null)
            single_instance = new CurrentSession();

        return single_instance;
    }
}
