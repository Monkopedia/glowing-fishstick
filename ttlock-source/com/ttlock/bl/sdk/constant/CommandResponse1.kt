package com.ttlock.bl.sdk.constant

object CommandResponse {
    const val SUCCESS: Byte = 0X01
    const val FAILED: Byte = 0X00 // 	public static final byte INITIALIZED 					= 0X01;
    // 	public static final byte NOT_INITIALIZED 				= 0X00;
    //
    // 	// by wan ------ 区分F,G
    // //	public static final byte MODE_ADMIN 			= 0X00;
    // //	public static final byte MODE_UNLOCK	 		= 0X01;
    //
    // 	public static final byte ADMIN 							= 0X01;		//管理员
    // 	public static final byte USER 							= 0X00;		//普通用户
    //
    // 	public static final byte SUCCESS_GET_DYN_PASSWORD 		= 0X02;
    // 	public static final byte ERROR_NONE 					= 0X00;
    // 	public static final byte ERROR_INVALID_CRC 				= 0X01;
    // 	public static final byte ERROR_NO_PERMISSION 			= 0X02;
    // 	public static final byte ERROR_WRONG_ID_OR_PASSWORD 	= 0X03;
    // 	public static final byte ERROR_REACH_LIMIT	 			= 0X04;
    // 	public static final byte ERROR_IN_SETTING	 			= 0X05;
    // 	//	------by wan------
    // 	public static final byte ERROR_IN_SAME_USERID	 		= 0X06;		//	不能添加重名的
    // 	public static final byte ERROR_NO_ADMIN_YET				= 0X07;		//	必须先添加一个管理员
    //
    // 	public static final byte ERROR_Dyna_Password_Out_Time	= 0X08;		//	动态密码过期
    // 	public static final byte ERROR_NO_DATA					= 0X09;		//	数据为空
    //
    // 	public static final byte ERROR_LOCK_NO_POWER			= 0X0a;		//	锁没有电量了
    //
    // 	public byte command;
    // 	public byte isSuccess;
    // 	public byte errorCode;
}
