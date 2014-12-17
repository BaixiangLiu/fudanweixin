package edu.fudan.weixin;

import java.io.File;

import org.junit.Test;

import edu.fudan.weixin.utils.AccessTokenHelper;
import edu.fudan.weixin.utils.MediaIDHelper;


public class TestUploadFile {
	
	@Test
	public void Test() {
		System.out.println(AccessTokenHelper.getInstance().getToken(AccessTokenHelper.WEIXIN));
		File file = new File("D:\\testpic.jpg");
		MediaIDHelper.uploadMediaFile(file);
		System.out.println(MediaIDHelper.getMediaID("testpic.jpg"));
		
	}
	

}
