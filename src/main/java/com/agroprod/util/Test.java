package com.agroprod.util;

import org.springframework.beans.factory.annotation.Autowired;

public class Test {
//	@Autowired
//	private static SendMail sendMail;

	public static void main(String[] args) {

		String s = "Dear " + "Rakesh" + System.getProperty("line.separator") + System.getProperty("line.separator")
				+ "Payment against the product has been received successfully. PFA the invoice for the product. Feel free to contact seller if you have any further questions. For better experience please visit our AgroProduct App again in the future."
				+ System.getProperty("line.separator") + System.getProperty("line.separator") + "Thank You"
				+ System.getProperty("line.separator") + "AgroProduct Team ";

		System.out.println(s);
	}
}
