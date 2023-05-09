package com.ttlock.bl.sdk.callback;

/**
 * Created on  2019/4/2 0002 15:37
 *
 * @author theodre
 */
public class OperationType {

    public final static int UNKNOWN_TYPE = -1;

    public final static int INIT_LOCK = 2;
    public final static int RESET_LOCK = 3;
    public final static int CONTROL_LOCK = 4;
    public final static int RESET_KEY = 5;
    public final static int GET_MUTE_MODE_STATE  = 6;
    public final static int SET_MUTE_MODE_STATE = 7;
    public final static int GET_REMOTE_UNLOCK_STATE = 8;
    public final static int SET_REMOTE_UNLOCK_STATE = 9;
    public final static int GET_PASSCODE_VISIBLE_STATE = 10;
    public final static int SET_PASSCODE_VISIBLE_STATE = 11;
    public final static int SET_PASSAGE_MODE = 12;
    public final static int DELETE_PASSAGE_MODE = 13;
    public final static int CLEAR_PASSAGE_MODE = 14;
    public final static int GET_PASSAGE_MODE = 15;
    public final static int SET_LOCK_TIME = 16;
    public final static int GET_LOCK_TIME = 17;
    public final static int GET_OPERATION_LOG = 18;
    public final static int GET_ELECTRIC_QUALITY = 19;
    public final static int GET_LOCK_VERSION = 20;
    public final static int GET_SPECIAL_VALUE = 21;
    public final static int RECOVERY_DATA = 22;
    public final static int GET_SYSTEM_INFO = 23;
    public final static int CREATE_CUSTOM_PASSCODE = 24;
    public final static int GET_LOCK_STATUS = 25;
    public final static int SET_AUTO_LOCK_PERIOD = 26;
    public final static int MODIFY_PASSCODE = 27;
    public final static int DELETE_PASSCODE = 28;
    public final static int RESET_PASSCODE = 29;
    public final static int GET_ALL_VALID_PASSCODES = 30;
    public final static int GET_PASSCODE_INFO = 31;
    public final static int MODIFY_ADMIN_PASSCODE = 32;
    public final static int GET_ADMIN_PASSCODE = 33;
    public final static int ADD_IC_CARD = 34;
    public final static int MODIFY_IC_CARD_PERIOD = 35;
    public final static int ADD_FINGERPRINT = 36;
    public final static int MODIFY_FINGEPRINT_PERIOD = 37;
    public final static int GET_ALL_IC_CARDS = 38;
    public final static int DELETE_IC_CARD = 39;
    public final static int CLEAR_ALL_IC_CARD = 40;
    public final static int GET_ALL_FINGERPRINTS = 41;
    public final static int DELETE_FINGERPRINT = 42;
    public final static int CLEAR_ALL_FINGERPRINTS = 43;
    public final static int WRITE_FINGERPRINT_DATA = 44;
    public final static int ENTER_DFU_MODE = 45;
    public final static int SET_NB_SERVER = 46;
    public final static int INIT_KEYPAD = 47;
    public final static int GET_LOCK_FREEZE_STATE = 48;
    public final static int SET_LOCK_FREEZE_STATE = 49;
    public final static int GET_LIGHT_TIME = 50;
    public final static int SET_LIGHT_TIME = 51;
    public final static int SET_HOTEL_CARD_SECTION = 52;
    public final static int CONNECT_LOCK = 53;
    public final static int SET_LOCK_CONFIG = 54;
    public final static int GET_LOCK_CONFIG = 55;
    public final static int SET_HOTEL_DATA = 56;
    public final static int SET_ELEVATOR_CONTROLABLE_FLOORS = 57;
    public final static int SET_ELEVATOR_WORK_MODE = 58;
    public final static int GET_AUTO_LOCK_PERIOD = 59;

    public final static int ADD_CYCLIC_IC_CARD = 60;
    public final static int ADD_CYCLIC_FINGERPRINT = 61;

    public final static int SET_NB_ACTIVATE_CONFIG = 62;
    public final static int GET_NB_ACTIVATE_CONFIG = 63;
    public final static int SET_NB_ACTIVATE_MODE = 64;
    public final static int GET_NB_ACITATE_MODE = 65;

    public final static int GET_HOTEL_DATA = 66;
    public final static int LOSS_CARD = 67;

    public final static int ACTIVATE_LIFT_FLOORS = 68;

    public static final int SET_UNLOCK_DIRECTION = 69;

    public static final int GET_UNLOCK_DIRECTION = 70;

    public static final int GET_ACCESSORY_BATTERY = 71;

    public static final int ADD_KEY_FOB = 72;

    public static final int DELETE_KEY_FOB = 73;

    public static final int CLEAR_KEY_FOB = 74;

    public static final int UPDATE_KEY_FOB_VALIDITY = 75;

    public static final int SCAN_WIFI = 76;

    public static final int CONFIGURE_WIFI_AP = 77;

    public static final int CONFIGURE_SERVER = 78;

    public static final int CONFIGURE_STATIC_IP = 79;

    public static final int GET_WIFI_INFO = 80;

    public static final int CONFIGURE_WIFI_LOCK_STATIC_IP = 81;

    public static final int SET_LOCK_SOUND_VOLUME = 82;

    public static final int GET_LOCK_SOUND_VOLUME = 83;

    public static final int ADD_DOOR_SENSOR = 84;

    public static final int DELETE_DOOR_SENSOR = 85;

}
