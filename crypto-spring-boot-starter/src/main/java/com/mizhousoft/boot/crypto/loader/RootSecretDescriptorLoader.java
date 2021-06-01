package com.mizhousoft.boot.crypto.loader;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.mizhousoft.boot.crypto.descriptor.RootSecretDescriptor;
import com.mizhousoft.commons.crypto.CryptoException;
import com.mizhousoft.commons.lang.CharEncoding;

/**
 * 根密钥描述符加载器
 *
 * @version
 */
public class RootSecretDescriptorLoader
{
	public static RootSecretDescriptor load(File rootDir) throws CryptoException
	{
		File dogDirectory = new File(rootDir, "dog");
		File catDirectory = new File(rootDir, "cat");
		File pigDirectory = new File(rootDir, "pig");
		File birdDirectory = new File(rootDir, "bird");

		File dogFile = new File(dogDirectory, "dog.txt");
		File catFile = new File(catDirectory, "cat.txt");
		File pigFile = new File(pigDirectory, "pig.txt");
		File birdFile = new File(birdDirectory, "bird.txt");

		File[] files = { dogFile, catFile, pigFile, birdFile };
		for (File file : files)
		{
			if (!file.exists())
			{
				throw new CryptoException(file.getName() + " not found.");
			}
		}

		String dog = readFileToString(dogFile);
		String cat = readFileToString(catFile);
		String pig = readFileToString(pigFile);
		String bird = readFileToString(birdFile);

		if (dog.length() != cat.length() || cat.length() != pig.length() || pig.length() != bird.length())
		{
			throw new CryptoException("Secret length is invalid.");
		}

		RootSecretDescriptor descriptor = new RootSecretDescriptor();
		descriptor.setDog(dog.trim());
		descriptor.setCat(cat.trim());
		descriptor.setPig(pig.trim());
		descriptor.setBird(bird.trim());

		return descriptor;
	}

	private static String readFileToString(File file) throws CryptoException
	{
		try
		{
			String value = FileUtils.readFileToString(file, CharEncoding.UTF8);
			if (StringUtils.isBlank(value))
			{
				throw new CryptoException(file.getName() + " secret not found.");
			}

			return value;
		}
		catch (IOException e)
		{
			throw new CryptoException(file.getName() + " secret read failed.");
		}
	}
}
