package com.example.studentprofilemanager.service;

import com.example.studentprofilemanager.model.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Responsible ONLY for serializing, deserializing, and deleting the
 * session.dat file that persists the logged-in user's session while
 * navigating the application. No authentication or navigation logic
 * lives here.
 */
public final class SessionManager {

    private static final String SESSION_FILE = "session.dat";

    private SessionManager() {
    }

    /** Serializes the given session to session.dat, overwriting any existing file. */
    public static void createSession(Session session) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(SESSION_FILE))) {
            out.writeObject(session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Deserializes session.dat. Returns null if it doesn't exist or is invalid/corrupted. */
    public static Session loadSession() {
        File file = new File(SESSION_FILE);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) {
            return (Session) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** True if session.dat exists and deserializes successfully. */
    public static boolean hasValidSession() {
        return loadSession() != null;
    }

    public static void clearSession() {
        File file = new File(SESSION_FILE);

        System.out.println("Deleting: " + file.getAbsolutePath());

        if (file.exists()) {
            boolean deleted = file.delete();
            System.out.println("Deleted: " + deleted);
        } else {
            System.out.println("Session file not found.");
        }
    }
}