package com.infinity.childtracking.data;

import com.infinity.childtracking.model.CALL;
import com.infinity.childtracking.model.ChildLocation;
import com.infinity.childtracking.model.ChildNotification;
import com.infinity.childtracking.model.SMS;
import com.infinity.childtracking.model.Token;
import com.infinity.childtracking.model.USAGE;
import com.infinity.childtracking.model.User;

import java.util.ArrayList;

public class DataBank {
    public static ArrayList<CALL> call_log = new ArrayList<>();
    public static ArrayList<SMS> sms_log = new ArrayList<>();
    public static ArrayList<ChildNotification> notifications = new ArrayList<>();
    public static ArrayList<ChildLocation> location_log = new ArrayList<>();
    public static ArrayList<USAGE> usages_log = new ArrayList<>();

    public static User curUser;

    public static ArrayList<String> notAllowUsernames = new ArrayList<>();
    public static Token curToken;

    public static ArrayList<String> child_users = new ArrayList<>();
}
