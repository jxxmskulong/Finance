/*
 * @Author Irakli Nadareishvili
 * CVS-ID: $Id: DashBoard.java,v 1.1 2004/11/30 22:47:42 idumali Exp $
 *
 * Copyright (c) 2004 Development Gateway Foundation, Inc. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0 which accompanies this
 * distribution, and is available at:
 * http://www.opensource.org/licenses/cpl.php
 *
 *****************************************************************************/

package com.loris.base.web.util;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * One of our goals in load-test-crawling a web-application is to be able to
 * figure-out which URLs are the slowest, so we can address then and see how
 * (if at all) we can make them quicker and thus improve overall application
 * performance.
 *
 * This is a class that tracks URL processing speed and provides encapsulated
 * way of tracking/managing this information.
 */
public class DashBoard
{

	public static final int SIZE = 20;
	public static final Object watch = new String("w");

	public static SortedSet<UrlRecord> urlList = new TreeSet<UrlRecord>();

	/**
	 * Add a new URL and its time to the dashboard. It will only get into the
	 * dashboard if it is slow-enough to deserve, such an honor, of course :)
	 * 
	 * @param url
	 *            String
	 * @param time
	 *            int
	 */
	public static void add(String url, long time)
	{

		UrlRecord newSlowUrl = new UrlRecord(url, time);

		if (urlList.size() < DashBoard.SIZE)
		{
			urlList.add(newSlowUrl);
		}
		else
		{
			UrlRecord minElement = (UrlRecord) urlList.first();
			long minTime = minElement.processTime;
			if (time < minTime)
				return;

			synchronized (DashBoard.watch)
			{
				urlList.remove(minElement);
				urlList.add(newSlowUrl);
			}
		}
	}

	/**
	 * Return the string containing the list of record URLs
	 */
	public static String print()
	{
		String ret = "";

		synchronized (DashBoard.watch)
		{
			if (urlList != null)
			{
				Iterator<UrlRecord> it = DashBoard.urlList.iterator();
				while (it.hasNext())
				{
					UrlRecord u = (UrlRecord) it.next();
					String url = u.url;
					long time = u.processTime;
					ret += (" Time: " + time + " URL: " + url + "\r\n");
				}
			}
			else
			{
				ret += (" List of the slowest URLs is empty ");
			}
		}
		return ret;
	}
}

class UrlRecord implements Comparable<UrlRecord>
{
	public String url;
	public long processTime;

	public UrlRecord(String url, long processTime)
	{
		this.url = url;
		this.processTime = processTime;
	}

	public int compareTo(UrlRecord o) throws ClassCastException
	{
		long k = (int) this.processTime - ((UrlRecord) o).processTime;

		if (k > 0)
		{
			return 1;
		}
		else if (k < 0)
		{
			return -1;
		}
		else
			return 0;

	}

	public boolean equals(Object o)
	{
		return (processTime == ((UrlRecord) o).processTime);
	}
}
