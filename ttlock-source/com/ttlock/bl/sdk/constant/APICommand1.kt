package com.ttlock.bl.sdk.constant

/**
 * Created by Smartlock on 2016/5/30.
 */
object APICommand {
    const val OP_GET_LOCK_VERSION = 1
    const val OP_ADD_ADMIN = 2 // 添加管理员
    const val OP_UNLOCK_ADMIN = 3 // 管理员开门
    const val OP_UNLOCK_EKEY = 4 // 管通用户开门
    const val OP_SET_KEYBOARD_PASSWORD = 5 // 设置管理员键盘密码
    const val OP_CALIBRATE_TIME = 6
    const val OP_SET_NORMAL_USER_PASSWORD = 7 // 设置删除密码
    const val OP_READ_NORMAL_USER_PASSWORD = 8
    const val OP_CLEAR_NORMAL_USER_PASSWORD = 9
    const val OP_REMOVE_SINGLE_NORMAL_USER_PASSWORD = 10
    const val OP_RESET_KEYBOARD_PASSWORD = 11 // 重置键盘密码
    const val OP_SET_DELETE_PASSWORD = 12
    const val OP_LOCK_ADMIN = 13 // 车位锁admin关锁
    const val OP_LOCK_EKEY = 14 // 车位锁EKEY关锁
    const val OP_RESET_EKEY = 15 // set lockFlag

    /**
     * 初始化密码
     */
    const val OP_INIT_PWD = 16

    // 设置锁名称
    const val OP_SET_LOCK_NAME = 17

    // 读取门锁时间
    const val OP_GET_LOCK_TIME = 18

    // 恢复出厂设置
    const val OP_RESET_LOCK = 19

    /**
     * 添加单次密码，需要开始时间和结束时间
     */
    const val OP_ADD_ONCE_KEYBOARD_PASSWORD = 20

    /**
     * 添加永久键盘密码，需要开始时间
     */
    const val OP_ADD_PERMANENT_KEYBOARD_PASSWORD = 21

    /**
     * 添加期限密码
     */
    const val OP_ADD_PERIOD_KEYBOARD_PASSWORD = 22

    /**
     * 修改密码
     */
    const val OP_MODIFY_KEYBOARD_PASSWORD = 23

    /**
     * 删除单个密码
     */
    const val OP_REMOVE_ONE_PASSWORD = 24

    /**
     * 删除锁内所有密码
     */
    const val OP_REMOVE_ALL_KEYBOARD_PASSWORD = 25

    /**
     * 获取操作日志
     */
    const val OP_GET_OPERATE_LOG = 26

    /**
     * 查询设备特征
     */
    const val OP_SEARCH_DEVICE_FEATURE = 27

    /**
     * 查询IC卡号
     */
    const val OP_SEARCH_IC_CARD_NO = 28

    /**
     * 添加IC卡
     */
    const val OP_ADD_IC = 29

    /**
     * 修改IC卡有效期
     */
    const val OP_MODIFY_IC_PERIOD = 30

    /**
     * 删除IC卡
     */
    const val OP_DELETE_IC = 31

    /**
     * 清空IC卡
     */
    const val OP_CLEAR_IC = 32

    /**
     * 设置手环KEY
     */
    const val OP_SET_WRIST_KEY = 33

    /**
     * 添加指纹
     */
    const val OP_ADD_FR = 34

    /**
     * 修改指纹有效期
     */
    const val OP_MODIFY_FR_PERIOD = 35

    /**
     * 删除指纹
     */
    const val OP_DELETE_FR = 36

    /**
     * 清空指纹
     */
    const val OP_CLEAR_FR = 37

    /**
     * 查询闭锁最短最长时间
     */
    const val OP_SEARCH_AUTO_LOCK_PERIOD = 38

    /**
     * 设置闭锁时间
     */
    const val OP_SET_AUTO_LOCK_TIME = 39

    /**
     * 进入升级模式
     */
    const val OP_ENTER_DFU_MODE = 40

    /**
     * 批量删除密码
     */
    const val OP_BATCH_DELETE_PASSWORD = 41

    /**
     * 闭锁功能
     */
    const val OP_LOCK = 42

    /**
     * 显示隐藏密码
     */
    const val OP_SHOW_PASSWORD_ON_SCREEN = 43

    /**
     * 恢复数据
     */
    const val OP_RECOVERY_DATA = 44

    /**
     * 读取密码参数
     */
    const val OP_READ_PWD_PARA = 45

    /**
     * 查询指纹列表
     */
    const val OP_SEARCH_FR = 46

    /**
     * 查询密码列表
     */
    const val OP_SEARCH_PWD = 47

    /**
     * 控制远程开锁开关
     */
    const val OP_CONTROL_REMOTE_UNLOCK = 48

    /**
     * 获取电量
     */
    const val OP_GET_POW = 49
    const val OP_AUDIO_MANAGEMENT = 50
    const val OP_REMOTE_CONTROL_DEVICE_MANAGEMENT = 51

    /**
     * 门磁操作
     */
    const val OP_DOOR_SENSOR = 52

    /**
     * 检测门磁
     */
    const val OP_DETECT_DOOR_SENSOR = 53

    /**
     * 获取锁开关状态
     */
    const val OP_GET_LOCK_SWITCH_STATE = 54

    /**
     * 读取设备信息
     */
    const val OP_GET_DEVICE_INFO = 55

    /**
     * 配置NB锁服务器地址
     */
    const val OP_CONFIGURE_NB_SERVER_ADDRESS = 56
    const val OP_GET_ADMIN_KEYBOARD_PASSWORD = 57 // 读取管理员键盘密码
    const val OP_WRITE_FR = 58 // 写指纹数据
    const val OP_QUERY_PASSAGE_MODE = 59
    const val OP_ADD_OR_MODIFY_PASSAGE_MODE = 60
    const val OP_DELETE_PASSAGE_MODE = 61
    const val OP_CLEAR_PASSAGE_MODE = 62
    const val OP_FREEZE_LOCK = 63
    const val OP_LOCK_LAMP = 64
    const val OP_SET_HOTEL_DATA = 65
    const val OP_SET_SWITCH = 66
    const val OP_GET_SWITCH = 67
    const val OP_SET_HOTEL_CARD_SECTION = 68
    const val OP_DEAD_LOCK = 69
    const val OP_SET_ELEVATOR_CONTROL_FLOORS = 70
    const val OP_SET_ELEVATOR_WORK_MODE = 71
    const val OP_SET_NB_ACTIVATE_CONFIG = 72
    const val OP_GET_NB_ACTIVATE_CONFIG = 73
    const val OP_SET_NB_ACTIVATE_MODE = 74
    const val OP_GET_NB_ACTIVATE_MODE = 75
    const val OP_GET_HOTEL_DATA = 76

    /**
     * 挂失IC卡
     */
    const val OP_LOSS_IC = 77

    /**
     * 楼层开锁指令
     */
    const val OP_ACTIVATE_FLOORS = 78
    const val OP_SET_UNLOCK_DIRECTION = 79
    const val OP_GET_UNLOCK_DIRECTION = 80
    const val OP_GET_ACCESSORY_BATTERY = 81
    const val OP_ADD_KEY_FOB = 82
    const val OP_MODIFY_KEY_FOB_PERIOD = 83
    const val OP_DELETE_KEY_FOB = 84
    const val OP_CLEAR_KEY_FOB = 85
    const val OP_SET_LOCK_SOUND = 87

    // 比v2多的一条指令
    const val OP_GET_LOCK_SOUND = 1000
    const val OP_ADD_DOOR_SENSOR = 88
    const val OP_DELETE_DOOR_SENSOR = 89
    const val OP_SCAN_WIFI = 96
    const val OP_SET_WIFI = 97
    const val OP_SET_SERVER = 98
    const val OP_SET_STATIC_IP = 99
    const val OP_GET_WIFI_INFO = 100
}
