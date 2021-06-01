package com.mizhousoft.boot.quartz;

/**
 * Student
 *
 * @version
 */
public class Student
{
	public int id;

	public String name;

	/**
	 * 构造函数
	 *
	 * @param id
	 * @param name
	 */
	public Student(int id, String name)
	{
		super();
		this.id = id;
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("{\"id\":\"");
		builder.append(id);
		builder.append("\", \"name\":\"");
		builder.append(name);
		builder.append("\"}");
		return builder.toString();
	}

}
