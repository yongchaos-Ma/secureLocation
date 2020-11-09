package com.example.myapplication.power_individual_demo;

public class BleCmd06_getData extends BaseBleMessage {

	public static byte mTheCmd = 0x06;
	
	public byte[] dealBleResponse(byte[] notifyData, int dataLen) {
		return null;
	}
	//6820  0800 00 00 00 00 55 72 6e bf 8416
	// 6820 0400 55 72 6f ac 6e16
	
	public byte[] onHR() {
		byte[] data = new byte[1];
		
		data[0] = 0x01;
		
    	return setMessageByteData(mTheCmd, data, data.length);
	}
	
	public byte[] offHR() {
		byte[] data = new byte[1];
		
		data[0] = 0x02;
		
    	return setMessageByteData(mTheCmd, data, data.length);
	}
	
}
