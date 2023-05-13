package com.ttlock.bl.sdk.callback

/**
 * Created on  2019/4/2 0002 15:37
 *
 * @author theodre
 */
object OperationType {
    const val UNKNOWN_TYPE = -1
    const val INIT_LOCK = 2
    const val RESET_LOCK = 3
    const val CONTROL_LOCK = 4
    const val RESET_KEY = 5
    const val GET_MUTE_MODE_STATE = 6
    const val SET_MUTE_MODE_STATE = 7
    const val GET_REMOTE_UNLOCK_STATE = 8
    const val SET_REMOTE_UNLOCK_STATE = 9
    const val GET_PASSCODE_VISIBLE_STATE = 10
    const val SET_PASSCODE_VISIBLE_STATE = 11
    const val SET_PASSAGE_MODE = 12
    const val DELETE_PASSAGE_MODE = 13
    const val CLEAR_PASSAGE_MODE = 14
    const val GET_PASSAGE_MODE = 15
    const val SET_LOCK_TIME = 16
    const val GET_LOCK_TIME = 17
    const val GET_OPERATION_LOG = 18
    const val GET_ELECTRIC_QUALITY = 19
    const val GET_LOCK_VERSION = 20
    const val GET_SPECIAL_VALUE = 21
    const val RECOVERY_DATA = 22
    const val GET_SYSTEM_INFO = 23
    const val CREATE_CUSTOM_PASSCODE = 24
    const val GET_LOCK_STATUS = 25
    const val SET_AUTO_LOCK_PERIOD = 26
    const val MODIFY_PASSCODE = 27
    const val DELETE_PASSCODE = 28
    const val RESET_PASSCODE = 29
    const val GET_ALL_VALID_PASSCODES = 30
    const val GET_PASSCODE_INFO = 31
    const val MODIFY_ADMIN_PASSCODE = 32
    const val GET_ADMIN_PASSCODE = 33
    const val ADD_IC_CARD = 34
    const val MODIFY_IC_CARD_PERIOD = 35
    const val ADD_FINGERPRINT = 36
    const val MODIFY_FINGEPRINT_PERIOD = 37
    const val GET_ALL_IC_CARDS = 38
    const val DELETE_IC_CARD = 39
    const val CLEAR_ALL_IC_CARD = 40
    const val GET_ALL_FINGERPRINTS = 41
    const val DELETE_FINGERPRINT = 42
    const val CLEAR_ALL_FINGERPRINTS = 43
    const val WRITE_FINGERPRINT_DATA = 44
    const val ENTER_DFU_MODE = 45
    const val SET_NB_SERVER = 46
    const val INIT_KEYPAD = 47
    const val GET_LOCK_FREEZE_STATE = 48
    const val SET_LOCK_FREEZE_STATE = 49
    const val GET_LIGHT_TIME = 50
    const val SET_LIGHT_TIME = 51
    const val SET_HOTEL_CARD_SECTION = 52
    const val CONNECT_LOCK = 53
    const val SET_LOCK_CONFIG = 54
    const val GET_LOCK_CONFIG = 55
    const val SET_HOTEL_DATA = 56
    const val SET_ELEVATOR_CONTROLABLE_FLOORS = 57
    const val SET_ELEVATOR_WORK_MODE = 58
    const val GET_AUTO_LOCK_PERIOD = 59
    const val ADD_CYCLIC_IC_CARD = 60
    const val ADD_CYCLIC_FINGERPRINT = 61
    const val SET_NB_ACTIVATE_CONFIG = 62
    const val GET_NB_ACTIVATE_CONFIG = 63
    const val SET_NB_ACTIVATE_MODE = 64
    const val GET_NB_ACITATE_MODE = 65
    const val GET_HOTEL_DATA = 66
    const val LOSS_CARD = 67
    const val ACTIVATE_LIFT_FLOORS = 68
    const val SET_UNLOCK_DIRECTION = 69
    const val GET_UNLOCK_DIRECTION = 70
    const val GET_ACCESSORY_BATTERY = 71
    const val ADD_KEY_FOB = 72
    const val DELETE_KEY_FOB = 73
    const val CLEAR_KEY_FOB = 74
    const val UPDATE_KEY_FOB_VALIDITY = 75
    const val SCAN_WIFI = 76
    const val CONFIGURE_WIFI_AP = 77
    const val CONFIGURE_SERVER = 78
    const val CONFIGURE_STATIC_IP = 79
    const val GET_WIFI_INFO = 80
    const val CONFIGURE_WIFI_LOCK_STATIC_IP = 81
    const val SET_LOCK_SOUND_VOLUME = 82
    const val GET_LOCK_SOUND_VOLUME = 83
    const val ADD_DOOR_SENSOR = 84
    const val DELETE_DOOR_SENSOR = 85
}
