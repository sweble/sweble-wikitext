/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sweble.wikitext.articlecruncher.utils;

import de.fau.cs.osr.utils.BinaryPrefix;

public class ConsoleWriter
		extends
			WorkerBase
{
	private final Object progressLock = new Object();
	
	private final SpeedMeter compressedSpeed = new SpeedMeter(0.1f, 1.0f, 30.0f);
	
	private final SpeedMeter decompressedSpeed = new SpeedMeter(0.1f, 1.0f, 30.0f);
	
	private final int inTrayCapacity;
	
	private final int completedJobsCapacity;
	
	private final int outTrayCapacity;
	
	private boolean progressHasUpdate = false;
	
	private long lastUpdate;
	
	private int outTrayFill;
	
	private int inTrayFill;
	
	private int completedJobsFill;
	
	private long parsedCount;
	
	private long compressedBytesRead;
	
	private long compressedLength;
	
	// =========================================================================
	
	public ConsoleWriter(
			AbortHandler abortHandler,
			int inTrayCapacity,
			int completedJobsCapacity,
			int outTrayCapacity)
	{
		super(ConsoleWriter.class.getSimpleName(), abortHandler);
		this.inTrayCapacity = inTrayCapacity;
		this.completedJobsCapacity = completedJobsCapacity;
		this.outTrayCapacity = outTrayCapacity;
	}
	
	// =========================================================================
	
	@Override
	protected void work() throws Throwable
	{
		synchronized (progressLock)
		{
			lastUpdate = System.currentTimeMillis();
			
			while (true)
			{
				if (progressHasUpdate)
				{
					printProgress();
					progressHasUpdate = false;
					progressLock.notify();
				}
				
				progressLock.wait();
			}
		}
	}
	
	// =========================================================================
	
	public void writeProgress(
			long parsedCount,
			long decompressedBytesRead,
			long compressedBytesRead,
			long compressedLength)
	{
		synchronized (progressLock)
		{
			this.parsedCount = parsedCount;
			this.compressedBytesRead = compressedBytesRead;
			this.compressedLength = compressedLength;
			
			compressedSpeed.update(compressedBytesRead, compressedLength);
			decompressedSpeed.update(decompressedBytesRead, -1);
			
			update();
		}
	}
	
	public void updateOutTray(int size)
	{
		synchronized (progressLock)
		{
			this.outTrayFill = size;
			update();
		}
	}
	
	public void updateInTray(int size)
	{
		synchronized (progressLock)
		{
			this.inTrayFill = size;
			update();
		}
	}
	
	public void updateCompletedJobs(int size)
	{
		synchronized (progressLock)
		{
			this.completedJobsFill = size;
			update();
		}
	}
	
	public void finish()
	{
		synchronized (progressLock)
		{
			progressHasUpdate = true;
			progressLock.notify();
			lastUpdate = System.currentTimeMillis();
			
			try
			{
				while (progressHasUpdate)
					progressLock.wait();
			}
			catch (InterruptedException e)
			{
			}
			finally
			{
				System.out.println();
				System.out.println();
				System.out.flush();
			}
		}
	}
	
	// =========================================================================
	
	private void update()
	{
		progressHasUpdate = true;
		
		long now = System.currentTimeMillis();
		if (now - lastUpdate > 250)
		{
			progressLock.notify();
			lastUpdate = now;
		}
	}
	
	private void printProgress()
	{
		StringBuilder out = new StringBuilder("\r");
		// --------
		{
			out.append(String.format(
					"I:%2d/%d - C:%2d/%d - O:%2d/%d",
					inTrayFill,
					inTrayCapacity,
					completedJobsFill,
					completedJobsCapacity,
					outTrayFill,
					outTrayCapacity));
		}
		// --------
		{
			out.append(", Page count: ");
			out.append(String.format("%10d", this.parsedCount));
		}
		// --------
		{
			BinaryPrefix read = new BinaryPrefix(this.compressedBytesRead);
			BinaryPrefix length = new BinaryPrefix(this.compressedLength);
			
			out.append(String.format(
					", Progress: %5.2f%%, %4d %s/%4d %s",
					this.compressedSpeed.getCurrentProgress(),
					read.getValue(),
					read.makePaddedUnit("B"),
					length.getValue(),
					length.makePaddedUnit("B")));
		}
		// --------
		{
			float speed = this.decompressedSpeed.getCurrentSpeed();
			
			out.append(", ");
			out.append("Speed: ");
			if (Float.isNaN(speed))
			{
				out.append(" ------   B/s");
			}
			else
			{
				BinaryPrefix p = new BinaryPrefix((long) speed);
				out.append(String.format("%7.2f", speed / p.getFactor()));
				out.append(" ");
				out.append(p.makePaddedUnit("B/s"));
			}
		}
		// --------
		{
			float eta = this.compressedSpeed.getEta();
			
			out.append(", ");
			out.append("ETA: ");
			if (Float.isInfinite(eta))
			{
				out.append("  ---- min");
			}
			else
			{
				out.append(String.format("%6.2f", eta));
				out.append(" min");
			}
		}
		// --------
		{
			BinaryPrefix u = new BinaryPrefix(Runtime.getRuntime().totalMemory());
			BinaryPrefix t = new BinaryPrefix(Runtime.getRuntime().maxMemory());
			
			out.append(String.format(
					", Heap: %4d %s/%4d %s",
					u.getValue(),
					u.makePaddedUnit("B"),
					t.getValue(),
					t.makePaddedUnit("B")));
		}
		// --------
		System.out.print(out.toString());
		System.out.flush();
	}
}
