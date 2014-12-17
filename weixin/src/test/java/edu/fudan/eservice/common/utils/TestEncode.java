package edu.fudan.eservice.common.utils;

import java.security.GeneralSecurityException;
import static edu.fudan.eservice.common.utils.EncodeHelper.*;
import org.junit.Assert;
import org.junit.Test;

public class TestEncode {
	@Test
		public void testDES() throws GeneralSecurityException {
			String key = "f@!_2*-6";
			String source = "f2afd6a30adec9a1d469bb8739629cea14ab3107634393302497788501#123456789";
			//String keymatrix = "FF4A336BF243DD17";
			String res = bytes2hex(encrypt("DES", source.getBytes(), key.getBytes(), null));
			System.out.println(res);
			String de = new String(dencrypt("DES", hex2bytes(res), key.getBytes(), null));
			System.out.println(de);
			Assert.assertEquals(source, de);
		}

		@Test
		public void testEncode() {
			//System.out.println(encode("example:123456", "BASE64"));
			System.out.println(digest(randpass(20),"SHA"));
		}

}
