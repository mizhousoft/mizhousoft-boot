package com.mizhousoft.boot.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mizhousoft.geo.GEOCoder;
import com.mizhousoft.geo.GEOException;
import com.mizhousoft.geo.model.Address;

/**
 * GEOCoder Test
 *
 * @version
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoApplication.class)
public class GEOCoderTest
{
	@Autowired
	private GEOCoder geoCoder;

	@Test
	public void test()
	{
		try
		{
			Address address = geoCoder.regeo(113.6561111111f, 26.8130555556f);

			Assertions.assertEquals(address.getAddrComponent().getProvince(), "湖南省");
		}
		catch (GEOException e)
		{
			Assertions.fail(e);
		}
	}
}
